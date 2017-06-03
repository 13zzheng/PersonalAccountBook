package stu.edu.cn.zing.personalbook.model;

import java.io.Serializable;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookPayment;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;

/**
 * Created by Administrator on 2017/5/11.
 */

public class PaymentFinder implements Serializable{

    private boolean input = true;
    private boolean output = true;
    private boolean less = false;
    private boolean great = false;
    private float lessValue;
    private float greatValue;
    private boolean enable = false;


    public BmobQuery getQuery() {
        if (!enable) return null;
        BmobQuery<BookPayment> bookPaymentBmobQuery = new BmobQuery<>();
        if (! (input && output)) {
            if (input) {
                bookPaymentBmobQuery.addWhereEqualTo("payType", BookPaymentType.TYPE_POSITIVE);
            }
            if (output) {
                bookPaymentBmobQuery.addWhereEqualTo("payType", BookPaymentType.TYPE_NEGATIVE);
            }
        }
        if (less) {
            bookPaymentBmobQuery.addWhereLessThanOrEqualTo("pay", lessValue);
        }
        if (great) {
            bookPaymentBmobQuery.addWhereGreaterThanOrEqualTo("pay", greatValue);
        }

        return bookPaymentBmobQuery;

    }

    public void reset() {
        input = true;
        output = true;
        less = false;
        great = false;
        lessValue = 0;
        greatValue = 0;

        setEnable(false);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isInput() {
        return input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

    public boolean isOutput() {
        return output;
    }

    public void setOutput(boolean output) {
        this.output = output;
    }

    public boolean isLess() {
        return less;
    }

    public void setLess(boolean less) {
        this.less = less;
    }

    public boolean isGreat() {
        return great;
    }

    public void setGreat(boolean great) {
        this.great = great;
    }

    public float getLessValue() {
        return lessValue;
    }

    public void setLessValue(float lessValue) {
        this.lessValue = lessValue;
    }

    public float getGreatValue() {
        return greatValue;
    }

    public void setGreatValue(float greatValue) {
        this.greatValue = greatValue;
    }
}
