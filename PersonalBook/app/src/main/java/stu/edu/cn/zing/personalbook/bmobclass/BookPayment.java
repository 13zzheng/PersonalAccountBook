package stu.edu.cn.zing.personalbook.bmobclass;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BookPayment extends BmobObject {

    private Float pay = 0f;
    private Integer payType;

    public BookPayment(float pay, int payType) {
        this.pay = pay;
        this.payType = payType;
    }

    public BookPayment() {

    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public float getPay() {
        return pay;
    }

    public int getPayType() {
        return payType;
    }
}
