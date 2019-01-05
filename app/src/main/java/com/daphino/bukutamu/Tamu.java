package com.daphino.bukutamu;

public class Tamu {
    public String id;
    public String guest_name;
    public String company_name;
    public String meet;
    public String need;
    public String arrival;
    public String out;
    public String signature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getArrival() {

        return arrival;
    }

    public String getOut() {
        return out;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getMeet() {
        return meet;
    }

    public String getNeed() {
        return need;
    }

    public String getSignature() {
        return signature;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setMeet(String meet) {
        this.meet = meet;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
