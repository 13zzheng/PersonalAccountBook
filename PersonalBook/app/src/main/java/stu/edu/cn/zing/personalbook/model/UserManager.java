package stu.edu.cn.zing.personalbook.model;

import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;

/**
 * Created by Administrator on 2017/3/27.
 */

public class UserManager implements IUserManager {
    private User user;

    public void login(User user, final SaveListenerUI saveListenerUI) {

        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if (e == null) {
                    //登录成功
                    BmobQuery<Book> bookQuery = new BmobQuery<>();
                    final User user = BmobUser.getCurrentUser(User.class);
                    bookQuery.order("createdAt");
                    bookQuery.addWhereEqualTo("user", user);
                    bookQuery.findObjects(new FindListener<Book>() {
                        @Override
                        public void done(List<Book> list, BmobException e) {
                            if (e == null) {
                                if ( list.size() == 0) {
                                    final Book book = new Book();
                                    book.setName("生活账本");
                                    book.setUser(user);
                                    book.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null ) {
                                                Book.setCurrentBook(book);
                                                saveListenerUI.onSucessUI();
                                            }
                                        }
                                    });
                                } else {
                                    Book.setCurrentBook(list.get(0));
                                    saveListenerUI.onSucessUI();
                                }
                            }
                        }
                    });
                } else {
                    //登录失败
                    saveListenerUI.onFailUI();
                }
            }
        });
    }

    public void regist(final User user, final SaveListenerUI saveListenerUI) {


        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                if (e == null) {
                    //注册成功
                    login(user, new SaveListenerUI() {
                        @Override
                        public void onSucessUI() {
                            saveListenerUI.onSucessUI();

                        }

                        @Override
                        public void onFailUI() {
                            saveListenerUI.onFailUI();
                        }
                    });


                } else {
                    //注册失败
                    saveListenerUI.onFailUI();
                }
            }
        });
    }

    public void logout(User user) {

        //清除缓存对象
        BmobUser.logOut();
    }

    public void updateInfo(User user, final SaveListenerUI saveListenerUI) {

        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //更新信息成功
                }else {
                    //更新信息失败
                }
            }
        });

    }

}

