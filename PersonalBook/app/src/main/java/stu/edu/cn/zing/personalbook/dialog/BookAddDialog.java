package stu.edu.cn.zing.personalbook.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;

public class BookAddDialog extends BaseActivity implements View.OnClickListener{

    private EditText et_book_name;
    private TextView tv_add;
    private Switch switch_public_book;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_add_dialog);
    }

    @Override
    public void initViews() {
        et_book_name = (EditText) findViewById(R.id.et_book_name);
        tv_add = (TextView) findViewById(R.id.tv_add);
        switch_public_book = (Switch) findViewById(R.id.switch_public_book);
    }

    @Override
    public void initListeners() {
        tv_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        switch_public_book.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                if (TextUtils.isEmpty(et_book_name.getText())) return;
                Book book = new Book();
                book.setName(et_book_name.getText().toString());
                book.setUser(BmobUser.getCurrentUser(User.class));
                if (switch_public_book.isChecked()) {
                    book.setPublic(true);
                    BmobRelation relation = new BmobRelation();
                    relation.add(BmobUser.getCurrentUser(User.class));
                    book.setPublicUsers(relation);
                }
                book.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            setResult(ResultRequestCode.RESULT_BOOK_ADD);
                            finish();
                        }
                    }
                });
                break;
        }
    }
}
