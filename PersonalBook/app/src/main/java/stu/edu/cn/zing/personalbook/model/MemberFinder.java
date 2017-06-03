package stu.edu.cn.zing.personalbook.model;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2017/5/12.
 */

public class MemberFinder extends Finder {

    public MemberFinder() {
        super();
    }
    public MemberFinder(List<String> list) {
        super(list);
    }

    @Override
    public List<String> getCheckedList() {
        if (!isEnable()) return null;
        List<String> checked = new ArrayList<>();
        for (int i = 0; i < isCheckedList.size(); i++) {
            if (isCheckedList.get(i)) {
                checked.add(nameList.get(i));
            }
        }
        return checked;
    }
}
