package com.credit.card.export.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class BillDetail {

    @ExcelProperty(value = "商户号")
    private String mrchId;
    @ExcelProperty(value = "卡号")
    private String cardNo;
    @ExcelProperty(value = "交易时间")
    private String txnDtTm;
    @ExcelProperty(value = "交易金额")
    private double txnAmt;
    @ExcelProperty(value = "持卡人")
    private String userName;
    @ExcelProperty(value = "商户名称")
    private String mrchNm;
    @ExcelIgnore
    private String tmlSeqNo;
    @ExcelIgnore
    private String srchRefrNo;
    @ExcelIgnore
    private String sysSeqNum;
   // @ExcelIgnore
    private String txnTp;
    @ExcelIgnore
    private int totalCount;

    public String getMrchId() {
        return mrchId;
    }

    public void setMrchId(String mrchId) {
        this.mrchId = mrchId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTxnDtTm() {
        return txnDtTm;
    }

    public void setTxnDtTm(String txnDtTm) {
        this.txnDtTm = txnDtTm;
    }

    public double getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(double txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getMrchNm() {
        return mrchNm;
    }

    public void setMrchNm(String mrchNm) {
        this.mrchNm = mrchNm;
    }

    public String getTmlSeqNo() {
        return tmlSeqNo;
    }

    public void setTmlSeqNo(String tmlSeqNo) {
        this.tmlSeqNo = tmlSeqNo;
    }

    public String getSrchRefrNo() {
        return srchRefrNo;
    }

    public void setSrchRefrNo(String srchRefrNo) {
        this.srchRefrNo = srchRefrNo;
    }

    public String getSysSeqNum() {
        return sysSeqNum;
    }

    public void setSysSeqNum(String sysSeqNum) {
        this.sysSeqNum = sysSeqNum;
    }

    public String getTxnTp() {
        return txnTp;
    }

    public void setTxnTp(String txnTp) {
        this.txnTp = txnTp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
