package stu.edu.cn.zing.personalbook.model;

import java.util.List;

import stu.edu.cn.zing.personalbook.bmobclass.BookItem;

/**
 * Created by Administrator on 2017/5/6.
 */

public class StatisticalItem implements Comparable{

    private String name;
    private List<BookItem> bookItemList;
    private float totalPayment;
    private int size;
    private float percentage;

    public StatisticalItem (List<BookItem> bookItemList) {
        this.bookItemList = bookItemList;
        init();
    }

    public StatisticalItem() {}


    private void init() {
        initSize();
        initTotalPayment();
    }

    public void init(List<BookItem> bookItemList) {
        this.bookItemList = bookItemList;
        init();
    }
    private void initSize() {
        size = bookItemList.size();
    }

    private void initTotalPayment() {
        for (int i = 0; i < bookItemList.size(); i++) {
            BookItem bookItem = bookItemList.get(i);
            totalPayment += bookItem.getPayment().getPay();
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(float totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        StatisticalItem other = (StatisticalItem) o;
        if (this.totalPayment > other.getTotalPayment()) {
            return -1;
        } else if (this.totalPayment == other.getTotalPayment()) {
            return 0;
        } else {
            return 1;
        }
    }
}
