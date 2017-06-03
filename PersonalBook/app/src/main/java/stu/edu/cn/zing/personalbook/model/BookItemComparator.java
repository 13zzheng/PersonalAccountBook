package stu.edu.cn.zing.personalbook.model;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/4/30.
 */

public abstract class BookItemComparator implements Comparator {
    @Override
    public abstract int compare(Object o1, Object o2);
}
