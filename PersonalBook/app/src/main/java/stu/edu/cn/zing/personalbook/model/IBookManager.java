package stu.edu.cn.zing.personalbook.model;

import android.widget.ListView;

import java.util.List;

import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemInfo;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookPayment;

/**
 * Created by Administrator on 2017/4/29.
 */

public interface IBookManager {
    void addBookItem(BookItemTitle itemTitle, BookPayment bookPayment, BookItemInfo itemInfo, SaveListenerUI saveListenerUI);
    void deleteBookItem(BookItem bookItem, final SaveListenerUI saveListenerUI);
    void searchBookItem(List<QueryCriteria> queryCriterias);
    void updateBookItem(BookItem bookItem, final SaveListenerUI saveListenerUI);

    void queryBookItemTitle(final BookItemTitle bookItemTitle, final FindListenerUI<BookItemTitle> findListenerUI);
    void queryBookMonthFinancial (int year, int month, Book book, FindListenerUI findListenerUI);
}
