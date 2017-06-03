package stu.edu.cn.zing.personalbook.bmobclass;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BookItemTitle extends BmobObject {

    private String week;
    private Integer year;
    private Integer month;
    private Integer day;
    private Float totalPay = 0f;
    private Book book;

    public BookItemTitle() {

    }

    public void setWeek(String week) {
        this.week = week;
    }


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Float getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Float totalPay) {
        this.totalPay = totalPay;
    }

    public String getWeek() {
        return week;
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
}
