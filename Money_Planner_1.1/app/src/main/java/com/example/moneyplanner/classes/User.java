package com.example.moneyplanner.classes;

public class User {
    private String id;

    private String currency;

    public User(){

    }

    public User(String id, String currency)  {
        this.id = id;
        this.currency = currency;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
