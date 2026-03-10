package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {}
    public Payment(String id, String method, String status, Map<String, String> paymentData) {}

    public String getId() { return id; }
    public String getMethod() { return method; }
    public String getStatus() { return status; }
    public Map<String, String> getPaymentData() { return paymentData; }
    public void setStatus(String status) {}
}