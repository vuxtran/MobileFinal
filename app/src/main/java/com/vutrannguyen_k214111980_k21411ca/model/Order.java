package com.vutrannguyen_k214111980_k21411ca.model;

import java.util.List;

public class Order {
    private String orderId;
    private String accountId;
    private double totalCost;
    private List<Cart> cartList;

    public Order() {
    }

    public Order(String orderId, String accountId, double totalCost, List<Cart> cartList) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.totalCost = totalCost;
        this.cartList = cartList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
}
