package stu.edu.cn.zing.personalbook;

import android.content.Intent;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import stu.edu.cn.zing.personalbook.HomeFragments.HomeActivity;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.user_login.LoginActivity;


public class MainActivity extends BaseActivity {


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {


        //BmobUser.logOut();
        User user = BmobUser.getCurrentUser(User.class);
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            Log.i("userNmae",user.getUsername() + " " + user.getObjectId());
            BmobQuery<Book> bookQuery = new BmobQuery<>();
            bookQuery.order("createdAt");
            bookQuery.addWhereEqualTo("user", user);
            bookQuery.include("user");
            bookQuery.findObjects(new FindListener<Book>() {
                @Override
                public void done(List<Book> list, BmobException e) {
                    if (e == null) {
                        if (list.size() > 0 ) {
                            Book.setCurrentBook(list.get(0));
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }
            });
        }
    }
}
