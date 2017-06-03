package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.model.Querier;

public class PublicUsersDialog extends BaseActivity implements View.OnClickListener{

    private TextView tv_add;
    private ListView lv_public_users;
    private List<User> publicUsers;
    private Book currentBook;

    private Querier querier;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_public_users_dialog);
    }

    @Override
    public void initViews() {
        tv_add = (TextView) findViewById(R.id.tv_add);
        lv_public_users = (ListView) findViewById(R.id.lv_public_users);
    }

    @Override
    public void initListeners() {
        tv_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        querier = new Querier();
        currentBook = Book.getCurrentBook();

        if (BmobUser.getCurrentUser(User.class).getObjectId().equals(Book.getCurrentBook().getUser().getObjectId())) {
            tv_add.setVisibility(View.VISIBLE);
        } else {
            tv_add.setVisibility(View.GONE);
        }
        getPublicUsers();
    }

    private void getPublicUsers() {
        querier.queryCurrentPublicUsers(currentBook, new FindListenerUI<User>() {
            @Override
            public void onSucess(List<User> list) {
                if (list.size() > 0) {
                    publicUsers = list;
                    showPublicUsers();
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void showPublicUsers() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < publicUsers.size(); i++) {
            Map<String, String> map = new HashMap<>();
            String string = publicUsers.get(i).getName() + "(" + publicUsers.get(i).getUsername() + ")";
            if (currentBook.getUser().getObjectId().equals(publicUsers.get(i).getObjectId())) {
                string = string + "——所有者";
            }
            map.put("book", string);

            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list, R.layout.list_book_item_type,
                new String[] {"book"}, new int[] {R.id.tv_typeitem});

        lv_public_users.setAdapter(simpleAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) return;
        switch (requestCode) {
            case ResultRequestCode.REQUEST_PUBLIC_USER_ADD:
                if (resultCode == ResultRequestCode.RESULT_PUBLIC_USER_ADD) {
                    getPublicUsers();
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                Intent intent = new Intent(this, PublicUserAddDialog.class);
                startActivityForResult(intent, ResultRequestCode.REQUEST_PUBLIC_USER_ADD);
                break;
        }
    }
}
