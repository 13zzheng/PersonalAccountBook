package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
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
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;

public class BookModifyDialog extends BaseActivity implements View.OnClickListener {


    private TextView tv_enter;
    private TextView tv_delete;
    private EditText et_book_name;
    private Switch switch_public_book;

    private Book book;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_modify_dialog);
    }

    @Override
    public void initViews() {
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        et_book_name = (EditText) findViewById(R.id.et_book_name);
        switch_public_book = (Switch) findViewById(R.id.switch_public_book);

    }

    @Override
    public void initListeners() {
        tv_delete.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        Intent intent = getIntent();
        book = (Book) intent.getExtras().getSerializable("book");
        et_book_name.setText(book.getName());
        switch_public_book.setChecked(book.isPublic());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                book.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            setResult(ResultRequestCode.RESULT_BOOK_MODIFY);
                            finish();
                        }
                    }
                });
                break;

            case R.id.tv_enter:
                if (TextUtils.isEmpty(et_book_name.getText())) {
                    return;
                }
                book.setName(et_book_name.getText().toString());
                book.setPublic(switch_public_book.isChecked());
                BmobRelation relation = new BmobRelation();
                relation.add(BmobUser.getCurrentUser(User.class));
                book.setPublicUsers(relation);
                book.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            setResult(ResultRequestCode.RESULT_BOOK_MODIFY);
                            finish();
                        }
                    }
                });
                break;
        }
    }
}
