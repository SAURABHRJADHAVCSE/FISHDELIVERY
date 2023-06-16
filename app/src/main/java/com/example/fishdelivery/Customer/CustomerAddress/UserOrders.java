package com.example.fishdelivery.Customer.CustomerAddress;

public class UserOrders {

    private String name;
    private String username;
    private String email;
    private String phoneNo;
    private String address;
    private String orderName;
    private String orderPrice;

    private String orderImageUrl;

    private String orderDate;

    private String orderTime;

    private String orderQuantity;

    private String tempPrice;

    private String longitude;

    private String latitude;

    public UserOrders() {

    }

    public UserOrders(String name, String username, String email, String phoneNo, String address, String orderName, String orderPrice, String orderImageUrl, String orderDate, String orderTime, String orderQuantity, String tempPrice, String longitude, String latitude) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderImageUrl = orderImageUrl;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.orderQuantity = orderQuantity;
        this.tempPrice = tempPrice;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderImageUrl() {
        return orderImageUrl;
    }

    public void setOrderImageUrl(String orderImageUrl) {
        this.orderImageUrl = orderImageUrl;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getTempPrice() {
        return tempPrice;
    }

    public void setTempPrice(String tempPrice) {
        this.tempPrice = tempPrice;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
