package com.example.rohit.management;

public class OrderModel {
    String id;
    String orderNo;
    String orderDate;
    String cust_name;
    String totalAmount;
    String status;
    String pendingAmount;
    String advanceAmount;
    String contactNo;
    String type;
    String imageString;

    public OrderModel(String id,String orderNo, String orderDate, String cust_name, String totalAmount, String status, String pendingAmount, String advanceAmount,String contactNo,String type,String imageString) {
        this.id = id;
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.cust_name = cust_name;
        this.totalAmount = totalAmount;
        this.status = status;
        this.pendingAmount = pendingAmount;
        this.advanceAmount = advanceAmount;
        this.contactNo = contactNo;
        this.type = type;
        this.imageString= imageString;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(String pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(String advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}
