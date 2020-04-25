package com.credit.card.export.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.credit.card.export.entity.*;
import com.credit.card.export.listener.UserCardDataListener;
import com.credit.card.export.pojo.UserCardModel;
import com.credit.card.export.util.DateUtil;
import com.google.common.collect.Lists;
import jodd.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 *
 */
@Component
public class ExcelWriteUtil {
    Logger log= LoggerFactory.getLogger(ExcelWriteUtil.class);
    private static final String baseFolderPath = "D:\\bank_data\\";
    private List<UserCard> userCards = Lists.newArrayList();
    private String cardExcelPath =baseFolderPath + "卡号.xlsx";
    private long excelLastModifiedDate;

    @PostConstruct
    private void loadUserCard() {
        //记录第一次加载卡号信息的excel文件时间，用于后期对比
        excelLastModifiedDate = FileUtils.getFile(cardExcelPath).lastModified();
        //ref: https://alibaba-easyexcel.github.io/quickstart/read.html
        // 有个很重要的点 UserCardDataListener不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        UserCardDataListener userCardDataListener = new UserCardDataListener();
        EasyExcel.read(cardExcelPath, UserCardModel.class, userCardDataListener).sheet().doRead();

        List<UserCardModel> userCardModels = userCardDataListener.getList();
        long cardCount= userCardModels.stream().map(u->u.getCardNo()).distinct().count();
        if(cardCount<userCardModels.size()){
            log.error("注意有重复卡号！！！！");
            return;
        }
        Set<String> nameSet = userCardModels.stream().map(u->u.getUserName()).collect(Collectors.toSet());
        userCards = nameSet.stream().map(name->{
            UserCard userCard = new UserCard();
            userCard.setUserName(name);
            List<String> cards = userCardModels.stream()
                    .filter(u->u.getUserName().equals(name))
                    .map(u->u.getCardNo())
                    .collect(Collectors.toList());
            userCard.setUserCards(cards);
            return userCard;
        }).collect(Collectors.toList());
    }

    public List<UserBillAmount> writeWithHead(String folderName){
        String fileName = baseFolderPath+"data\\"+folderName + ".xlsx";
        List<UserBillAmount> userBillAmounts = null;
        try  {
            File newCardExcel = new File(cardExcelPath);
            boolean isFileChanged = FileUtils.isFileNewer(newCardExcel, excelLastModifiedDate);
            if(isFileChanged){
                loadUserCard();
            }
            String jsonFilePath = baseFolderPath+folderName;
            List<BillDetail> billDetails = getBillDetails(jsonFilePath);
            userBillAmounts  = countUserBillAmount(billDetails);

            ExcelWriter excelWriter = EasyExcel.write(fileName).build();
            WriteSheet writeSheetDetail = EasyExcel.writerSheet(0, "detail").head(BillDetail.class).build();
            excelWriter.write(billDetails, writeSheetDetail);

            WriteSheet writeSheetSummarey = EasyExcel.writerSheet(1,"summary").head(UserBillAmount.class).build();
            excelWriter.write(userBillAmounts, writeSheetSummarey);
            excelWriter.finish();

        }catch (Exception se){
            se.printStackTrace();
        }
        return userBillAmounts;
    }


    private List<BillDetail> getBillDetails(String jsonFilePath) {
        List<BillDetail> billResult = new ArrayList<>();
        File files = new File(jsonFilePath);
        String[] fileList = files.list();

        Arrays.stream(fileList).forEach(f -> {
            File file = new File(jsonFilePath + "\\" + f);
            try {
                String json = FileUtils.readFileToString(file, "UTF-8");
                BillSummary billSummary = JSON.parseObject(json, BillSummary.class);
                BillData billData = billSummary.getData();
                int totalCount = Integer.valueOf(billData.getTotal());
                List<BillDetail> billDetails1 = billData.getList();
                billDetails1.stream().forEach(b->{
                    BillDetail bd = new BillDetail();
                    BeanUtils.copyProperties(b, bd);
                    String time = b.getTxnDtTm();
                    String formatTime = DateUtil.convertStrToStr(time);
                    bd.setTxnDtTm(formatTime);
                    BigDecimal bigDecimal = BigDecimal.valueOf(b.getTxnAmt());
                    double amount = bigDecimal.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    bd.setTxnAmt(amount);
                    bd.setUserName(getUserNameByCard(b.getCardNo()));
                    bd.setCardNo(formatCardNo(b.getCardNo()));
                    bd.setTotalCount(totalCount);
                    billResult.add(bd);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        int totalCountFromReq = billResult.get(0).getTotalCount();
        int totalCountFromJson = billResult.size();
//        if(totalCountFromJson!=totalCountFromReq){
//            log.error("Json文件统计的总数量:{} != 请求里的总数量:{}", totalCountFromJson, totalCountFromReq);
//            return null;
//        }
        billResult.sort(Comparator.comparing(BillDetail::getTxnDtTm));
        return billResult;
    }

    private String getUserNameByCard(String cardNo){
        String shortCardNo = formatCardNo(cardNo);
        return userCards.stream()
                .filter(u->{
                    String card = u.getUserCards().stream().filter(c->c.equals(shortCardNo)).findFirst().orElse(null);
                    if(!StringUtil.isEmpty(card)){
                        return true;
                    }else{
                        return false;
                    }
                }).findFirst().orElse(new UserCard()).getUserName();
    }

    private String formatCardNo(String originCard){
        if(StringUtils.isEmpty(originCard)){
            return null;
        }
        if(!originCard.matches("[0-9]{1,}")){
            log.info("originCard:{}", originCard);
            return originCard;
        }

        int cardNoLength = originCard.length();
        return originCard.substring(cardNoLength-8, cardNoLength);
    }

    private List<UserBillAmount> countUserBillAmount(List<BillDetail> billDetails){
        List<UserBillAmount> userBillAmounts = Lists.newArrayList();
        try{
             userBillAmounts = userCards.stream().map(u->{
                List<String> cards = u.getUserCards();
                double totalAmount = billDetails.stream().filter(b->cards.contains( formatCardNo( b.getCardNo()) )).mapToDouble(b->b.getTxnAmt()).sum();
                UserBillAmount userBillAmount = new UserBillAmount();
                userBillAmount.setUserName(u.getUserName());
                userBillAmount.setAmount(totalAmount);
                return userBillAmount;
            })
                     //.filter(u->u.getAmount()!=0)
                     .collect(Collectors.toList());

            userBillAmounts.sort(Comparator.comparing(UserBillAmount::getUserName));
        }catch (Exception se){
            se.printStackTrace();
        }
        return userBillAmounts;
    }

    public static void main(String[] args)  {
        BigDecimal bigDecimal = BigDecimal.valueOf(1000000);
        double value = bigDecimal.divide(new BigDecimal(100), 2,  BigDecimal.ROUND_HALF_UP).doubleValue();

        String originCard = "5289489790057992";
        String formatCard = originCard.substring(originCard.length()-8, originCard.length());
        System.out.println(formatCard);

        LongStream.rangeClosed(0, 10000000000L).parallel().sum();
        List<String> list = Lists.newArrayList();
    }



}
