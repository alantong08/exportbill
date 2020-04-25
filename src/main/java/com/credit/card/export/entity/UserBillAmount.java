package com.credit.card.export.entity;

import com.alibaba.excel.annotation.ExcelProperty;

public class UserBillAmount {

    @ExcelProperty(value = "总金额")
    private double amount;
    @ExcelProperty(value = "持卡人")
    private String userName;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
