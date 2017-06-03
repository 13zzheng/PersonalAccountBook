package stu.edu.cn.zing.personalbook.finanicalreport;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/10.
 */

public class HashMapComparatorByFloat implements Comparator<Map.Entry<String, Float>> {
    @Override
    public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
        if (o1.getValue() > o2.getValue()) {
            return -1;
        } else if (o1.getValue() == o2.getValue()) {
            return 0;
        } else {
            return 1;
        }
    }
}
