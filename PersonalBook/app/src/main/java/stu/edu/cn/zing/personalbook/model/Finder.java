package stu.edu.cn.zing.personalbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2017/5/11.
 */

public abstract class Finder implements Serializable{

    protected List<String> nameList;
    protected List<Boolean> isCheckedList;

    public Finder() {
        nameList = new ArrayList<>();
        isCheckedList = new ArrayList<>();
    }

    public Finder(List<String> list) {
        this.nameList = list;
        isCheckedList = new ArrayList<>();
        for (int i = 0; i < nameList.size(); i++) {
            isCheckedList.add(true);
        }
    }

    public List<Boolean> getIsCheckedList() {
        return isCheckedList;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public abstract List<String> getCheckedList();

    public void addName(String s) {
        nameList.add(s);
        isCheckedList.add(true);
    }

    public void reset() {
        for (int i = 0; i < isCheckedList.size(); i++) {
            isCheckedList.set(i,true);
        }
    }

    public boolean isEnable() {
        for (int i = 0; i < isCheckedList.size(); i++) {
            if (!isCheckedList.get(i)) {
                return true;
            }
        }
        return false;
    }
}
