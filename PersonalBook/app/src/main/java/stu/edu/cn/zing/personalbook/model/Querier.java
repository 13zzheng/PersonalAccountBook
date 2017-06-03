package stu.edu.cn.zing.personalbook.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemMember;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTradingWay;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.User;

/**
 * Created by Administrator on 2017/3/28.
 */

public class Querier implements IQuerier {

    @Override
    public void queryCurrentItemType(User user, final FindListenerUI findListenerUI) {
        BmobQuery<BookItemType> q1 = new BmobQuery<>();
        q1.addWhereEqualTo("user", user);
        BmobQuery<BookItemType> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("isDefault", true);
        List<BmobQuery<BookItemType>> queries = new ArrayList<>();
        queries.add(q1);
        queries.add(q2);
        BmobQuery<BookItemType> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.order("-createdAt");
        mainQuery.findObjects(new FindListener<BookItemType>() {
            @Override
            public void done(List<BookItemType> list, BmobException e) {
                if (e == null) {
                    //查询成功
                    findListenerUI.onSucess(list);
                } else {
                    //查询失败
                    findListenerUI.onFail();
                }
            }
        });
    }

    public void queryCurrentItemMember(User user, final FindListenerUI findListenerUI) {
        BmobQuery<BookItemMember> q1 = new BmobQuery<>();
        q1.addWhereEqualTo("user", user);
        BmobQuery<BookItemMember> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("isDefault", true);
        List<BmobQuery<BookItemMember>> queries = new ArrayList<>();
        queries.add(q1);
        queries.add(q2);
        BmobQuery<BookItemMember> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.order("-createdAt");
        mainQuery.findObjects(new FindListener<BookItemMember>() {
            @Override
            public void done(List<BookItemMember> list, BmobException e) {
                if (e == null) {
                    //查询成功
                    findListenerUI.onSucess(list);
                } else {
                    //查询失败
                    findListenerUI.onFail();
                }
            }
        });
    }

    public void queryCurrentItemTradingWay(User user, final FindListenerUI findListenerUI) {
        BmobQuery<BookItemTradingWay> q1 = new BmobQuery<>();
        q1.addWhereEqualTo("user", user);
        BmobQuery<BookItemTradingWay> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("isDefault", true);
        List<BmobQuery<BookItemTradingWay>> queries = new ArrayList<>();
        queries.add(q1);
        queries.add(q2);
        BmobQuery<BookItemTradingWay> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.order("-createdAt");
        mainQuery.findObjects(new FindListener<BookItemTradingWay>() {
            @Override
            public void done(List<BookItemTradingWay> list, BmobException e) {
                if (e == null) {
                    //查询成功
                    findListenerUI.onSucess(list);
                } else {
                    //查询失败
                    findListenerUI.onFail();
                }
            }
        });
    }

    public void queryCurrentPrivateBooks(User user, final FindListenerUI<Book> findListenerUI) {
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.addWhereEqualTo("user", user);
        bookBmobQuery.addWhereEqualTo("isPublic", false);
        bookBmobQuery.include("user");
        //bookBmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        bookBmobQuery.findObjects(new FindListener<Book>() {
            @Override
            public void done(List<Book> list, BmobException e) {
                if (e == null) {
                    findListenerUI.onSucess(list);
                } else {
                    findListenerUI.onFail();
                }
            }
        });
    }

    public void queryCurrentPublicBooks(final User user, final FindListenerUI<Book> findListenerUI) {
        final List<Book> publicBooks = new ArrayList<>();
        BmobQuery<Book> bookBmobQuery = new BmobQuery<>();
        bookBmobQuery.addWhereEqualTo("isPublic", true);
        bookBmobQuery.include("publicUsers");
        bookBmobQuery.include("user");
        //bookBmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        bookBmobQuery.findObjects(new FindListener<Book>() {
            @Override
            public void done(List<Book> list, BmobException e) {
                if (e == null) {
                    Log.i("PUBLIC", list.size() + "");
                    for (int i = 0; i < list.size(); i++) {
                        final Book book = list.get(i);
                        BmobQuery<User> userQuery = new BmobQuery<>();
                        userQuery.addWhereRelatedTo("publicUsers", new BmobPointer(book));
                        userQuery.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list2, BmobException e) {
                                if (e == null) {
                                    Log.i("USER", list2.size() + "");

                                    for (int j = 0; j < list2.size(); j++) {
                                        if (list2.get(j).getObjectId().equals(user.getObjectId())) {

                                            publicBooks.add(book);
                                        }
                                    }
                                    findListenerUI.onSucess(publicBooks);
                                }
                            }
                        });
                    }
                }else {
                    findListenerUI.onFail();
                }
            }
        });
    }

    public void queryCurrentPublicUsers(Book book, final FindListenerUI<User> findListenerUI) {
        BmobQuery<User> userQuery = new BmobQuery<>();
        userQuery.addWhereRelatedTo("publicUsers", new BmobPointer(book));
        userQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    findListenerUI.onSucess(list);
                }else {
                    findListenerUI.onFail();
                }
            }
        });
    }
}
