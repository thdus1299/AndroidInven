package com.example.kangjisung.likeroom.ObjectManager;

public class ProductSellTodayListItem {
    String name;
    int year;
    int month;
    int day;
    int sell;

    public ProductSellTodayListItem(String name, int year, int month, int day, int sell) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.sell = sell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSell() {
        return sell;
    }

    public void setSell(int sell) {
        this.sell = sell;
    }
}