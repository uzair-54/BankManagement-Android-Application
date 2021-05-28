package com.example.metropoliscentralbank;

public class clientDetails {

    public String clientName;
    public String clientAge;
    public String clientPhoneNumber;
    public int clientBalance;
    public String clientPin;
    public String clientEmail;
    public String clientHistory;

    public clientDetails() {
    }

    public clientDetails(String name,String age, String phnNum,String email,int balance,String pin,String hist){

        this.clientName = name;
        this.clientAge = age;
        this.clientPhoneNumber = phnNum;
        this.clientEmail = email;
        this.clientBalance = balance;
        this.clientPin = pin;
        this.clientHistory = hist;
    }
}

