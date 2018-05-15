package com.krishnachaitanya.expensetracker.Model;

/**
 * Created by chaitanya on 12/5/18.
 */

public class addExpense {



    String date;
    String amount;
    String category;
    String note;


    public addExpense( ) {
        this.date=date;
        this.amount=amount;
        this.category=category;
        this.note=note;

    }


    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }


    public String getAmount() {

        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCategory() {

        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


  }
