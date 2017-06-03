package stu.edu.cn.zing.personalbook.bmobclass;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2017/3/24.
 */

public class Book extends BmobObject implements Serializable{

    private static Book currentBook;

    private User user;
    private String name;
    private boolean isPublic = false;
    private BmobRelation publicUsers;

    public Book(User user) {
        this.user = user;
    }
    public Book(){

    }

    public BmobRelation getPublicUsers() {
        return publicUsers;
    }

    public void setPublicUsers(BmobRelation publicUsers) {
        this.publicUsers = publicUsers;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public static Book getCurrentBook() {
        return currentBook;
    }

    public static void setCurrentBook(Book book) {
        currentBook = book;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
