package com.credit.card.export.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.credit.card.export.entity.*;
import com.credit.card.export.service.ExcelWriteUtil;
import com.credit.card.export.util.MerchantMapper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

/**
 * @Author: tong, jinhu
 * @Description:
 * @Date: Created in 11:18 2019/11/15
 * @Modified By:
 */
@Controller
@RequestMapping("/card")
public class CardController {

    Logger log=LoggerFactory.getLogger(CardController.class);

    @Autowired
    private ExcelWriteUtil excelWriteUtil;

    @ResponseBody
    @RequestMapping("/getBill")
    public Object get(String path){
        String result = "";
        try {
            log.info("get path : {}", path);
            List<UserBillAmount> userBillAmounts = excelWriteUtil.writeWithHead(path);
            result = JSONObject.toJSONString(userBillAmounts);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("get error", e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/fiddler")
    public Object getFiddlerJson(String data){
        try {
            log.info("data : {}", data);
            FiddlerRequest fiddlerRequest = JSON.parseObject(data, FiddlerRequest.class);
            BillSummary billSummary = fiddlerRequest.getResponse_data();
            BillData billData = billSummary.getData();
            List<BillDetail> billDetails = billData.getList();
            if(billDetails==null || billDetails.size()==0){
                return "0";
            }
            BillDetail billDetail = billDetails.get(0);
            String pageNo = billSummary.getData().getPage_num();
            String dateTime = billDetail.getTxnDtTm();
            String time = dateTime.substring(0, 8);
            String merchantName = MerchantMapper.merchantMap.get(billDetail.getMrchId());
            String baseFolder = "D:\\bank_data\\"+merchantName+"_"+time+"\\";
            String fileName = merchantName+"_"+time+"_"+pageNo+".do";
            String json = JSONObject.toJSONString(billSummary);
            File file = new File(baseFolder+fileName);
            FileUtils.writeStringToFile(file, json, "UTF-8", false);
        } catch (Exception e) {
            log.error("get error", e);
        }
        return "success";
    }

    public static void main(String[] args) {
        String a = "20191109210213";
        System.out.println(a.substring(0, 8));
    }
}
