package com.findclass.ajvm.findclassapp.Model;

public class Address {
    private String cep;
    private String state;
    private String city;
    private String district;
    private String address;
    private int number;
    private String complement;

    public Address() {    }

    public Address(String cep, String state, String city, String district, String address, int number) {
        this.cep = cep;
        this.state = state;
        this.city = city;
        this.district = district;
        this.address = address;
        this.number = number;
    }

    public Address(String cep, String state, String city, String district, String address, int number, String complement) {
        this.cep = cep;
        this.state = state;
        this.city = city;
        this.district = district;
        this.address = address;
        this.number = number;
        this.complement = complement;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }
}
