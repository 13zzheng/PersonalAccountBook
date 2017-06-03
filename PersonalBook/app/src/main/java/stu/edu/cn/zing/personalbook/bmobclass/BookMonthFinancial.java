package stu.edu.cn.zing.personalbook.bmobclass;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/5.
 */

public class BookMonthFinancial extends BmobObject implements Serializable {

    private Float monthInput = 0.00f;
    private Float monthOutput = 0.00f;
    private Integer month;
    private Integer year;
    private Float budgetValue = 0.00f;
    private Float budgetOverageValue = 0.00f;
    private Boolean isBudgetEnable = false;
    private Book book;


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Float getBudgetOverageValue() {
        return budgetOverageValue;
    }

    public void setBudgetOverageValue(Float budgetOverageValue) {
        this.budgetOverageValue = budgetOverageValue;
    }

    public Float getMonthInput() {
        return monthInput;
    }

    public void setMonthInput(Float monthInput) {
        this.monthInput = monthInput;
    }

    public Float getMonthOutput() {
        return monthOutput;
    }

    public void setMonthOutput(Float monthOutput) {
        this.monthOutput = monthOutput;
    }

    public Float getBudgetValue() {
        return budgetValue;
    }

    public void setBudgetValue(Float budgetValue) {
        this.budgetValue = budgetValue;
    }

    public Boolean getBudgetEnable() {
        return isBudgetEnable;
    }

    public void setBudgetEnable(Boolean budgetEnable) {
        isBudgetEnable = budgetEnable;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
