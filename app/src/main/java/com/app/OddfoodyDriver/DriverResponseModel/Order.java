
package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("item_cost")
    @Expose
    private String itemCost;
    @SerializedName("item_unit")
    @Expose
    private Integer itemUnit;
    @SerializedName("item_offer")
    @Expose
    private Integer itemOffer;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("service_tax")
    @Expose
    private String serviceTax;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("coupon_amount")
    @Expose
    private String couponAmount;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("unit_code")
    @Expose
    private String unitCode;
    @SerializedName("order_key_formated")
    @Expose
    private String orderKeyFormated;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("invoic_pdf")
    @Expose
    private String invoicPdf;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public Integer getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(Integer itemUnit) {
        this.itemUnit = itemUnit;
    }

    public Integer getItemOffer() {
        return itemOffer;
    }

    public void setItemOffer(Integer itemOffer) {
        this.itemOffer = itemOffer;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getOrderKeyFormated() {
        return orderKeyFormated;
    }

    public void setOrderKeyFormated(String orderKeyFormated) {
        this.orderKeyFormated = orderKeyFormated;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getInvoicPdf() {
        return invoicPdf;
    }

    public void setInvoicPdf(String invoicPdf) {
        this.invoicPdf = invoicPdf;
    }

}