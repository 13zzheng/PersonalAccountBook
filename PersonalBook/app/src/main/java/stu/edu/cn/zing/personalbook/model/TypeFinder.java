package stu.edu.cn.zing.personalbook.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.BookPayment;

/**
 * Created by Administrator on 2017/5/11.
 */

public class TypeFinder extends Finder implements Serializable{



    public TypeFinder() {
        super();
    }
    public TypeFinder(List<String> list) {
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
