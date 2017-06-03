package stu.edu.cn.zing.personalbook.finanicalreport;

import java.util.Comparator;

import stu.edu.cn.zing.personalbook.BookItems;

/**
 * Created by Administrator on 2017/5/9.
 */

public class BookItemsComparatorByOutput implements Comparator<BookItems> {
    @Override
    public int compare(BookItems o1, BookItems o2) {
        if (o1.getTotalOutput() > o2.getTotalOutput()) {
            return -1;
        } else if (o1.getTotalOutput() == o2.getTotalOutput()) {
            return 0;
        } else  {
            return 1;
        }
    }
}
