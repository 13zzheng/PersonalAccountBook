package stu.edu.cn.zing.personalbook.model;

import stu.edu.cn.zing.personalbook.bmobclass.BookItem;

/**
 * Created by Administrator on 2017/4/30.
 */

public class PaymentComparator extends BookItemComparator {



    @Override
    public int compare(Object o1, Object o2) {
        BookItem bookItem1 = (BookItem) o1;
        BookItem bookItem2 = (BookItem) o2;

        if (bookItem1.getPayment().getPay() < bookItem2.getPayment().getPay()) {
            return 1;
        } else {
            return 0;
        }
    }
}
