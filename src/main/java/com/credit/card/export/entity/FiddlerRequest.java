package com.credit.card.export.entity;

public class FiddlerRequest {

    private BillSummary response_data;
    private String url;
    private String request_method;
    private String response_code;
    private String request_body;

    public BillSummary getResponse_data() {
        return response_data;
    }

    public void setResponse_data(BillSummary response_data) {
        this.response_data = response_data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequest_method() {
        return request_method;
    }

    public void setRequest_method(String request_method) {
        this.request_method = request_method;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getRequest_body() {
        return request_body;
    }

    public void setRequest_body(String request_body) {
        this.request_body = request_body;
    }
}
