package stu.edu.cn.zing.personalbook;

import java.util.List;

import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BookItems {

    private BookItemTitle title;
    private List<BookItem> items;
    private boolean isOpen = true;
    private float totalInput;
    private float totalOutput;

    public BookItems(BookItemTitle title, List<BookItem> items) {
        this.title = title;
        this.items = items;
    }

    public BookItems(){

    }

    public float getTotalInput() {
        return totalInput;
    }

    public void setTotalInput(float totalInput) {
        this.totalInput = totalInput;
    }

    public float getTotalOutput() {
        return totalOutput;
    }

    public void setTotalOutput(float totalOutput) {
        this.totalOutput = totalOutput;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setTitle(BookItemTitle title) {
        this.title = title;
    }

    public void setItems(List<BookItem> items) {
        this.items = items;
    }

    public BookItemTitle getTitle() {
        return title;
    }

    public List<BookItem> getItems() {
        return items;
    }
}
