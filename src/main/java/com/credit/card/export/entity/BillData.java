package com.credit.card.export.entity;

import java.util.List;

public class BillData {

    private String total;
    private String totalPage;
    private String page_num;
    private String page_size;
    private List<BillDetail> list;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public String getPage_size() {
        return page_size;
    }

    public void setPage_size(String page_size) {
        this.page_size = page_size;
    }

    public List<BillDetail> getList() {
        return list;
    }

    public void setList(List<BillDetail> list) {
        this.list = list;
    }
}
