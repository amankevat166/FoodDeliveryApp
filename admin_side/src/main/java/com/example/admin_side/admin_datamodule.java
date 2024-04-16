package com.example.admin_side;

import android.net.Uri;

import java.net.URI;
import java.util.Date;

public class admin_datamodule {

    public admin_datamodule() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String  date;
    public String getRef_ord() {
        return Ref_ord;
    }

    public void setRef_ord(String ref_ord) {
        Ref_ord = ref_ord;
    }

    String Ref_ord;
    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    String order_time;
    public String getName_reg() {
        return name_reg;
    }

    public void setName_reg(String name_reg) {
        this.name_reg = name_reg;
    }

    String name_reg;
    public String getGettotal_amount() {
        return gettotal_amount;
    }

    public void setGettotal_amount(String gettotal_amount) {
        this.gettotal_amount = gettotal_amount;
    }

    private String gettotal_amount;
    private String rest_name;

    public String getSub_cat() {
        return sub_cat;
    }

    public void setSub_cat(String sub_cat) {
        this.sub_cat = sub_cat;
    }

    private String sub_cat;

    public String getDec_item() {
        return dec_item;
    }

    public void setDec_item(String dec_item) {
        this.dec_item = dec_item;
    }

    private String dec_item;
    private String state;
    private String city;
    private String pincode;
    private String address;
    private String day1;
    private String day2;
    private String time;
    private String food_type;
    private String name;
    private String email;
    private String password;
    private String number;


    public String getQty_txt() {
        return qty_txt;
    }

    public void setQty_txt(String qty_txt) {
        this.qty_txt = qty_txt;
    }

    private String qty_txt;

    public String getSetReference() {
        return setReference;
    }

    public void setSetReference(String setReference) {
        this.setReference = setReference;
    }

    private String setReference;
    private String imguri;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String uid;

    public String getImguri() {
        return imguri;
    }

    public void setImguri(String imguri) {
        this.imguri = imguri;
    }

    private String cat_name,item_name,item_price,checkbox;

    public String getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(String checkbox) {
        this.checkbox = checkbox;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    private Uri image_uri;

    public Uri getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(Uri image_uri) {
        this.image_uri = image_uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }


    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    String Reference;

}
