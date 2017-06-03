package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.model.BookManager;

public class BookItemDeleteDialog extends BaseActivity implements View.OnClickListener {


    private TextView tv_delete;

    private BookManager bookManager;
    private BookItem bookItem;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_item_delete_dialog);
    }

    @Override
    public void initViews() {
        tv_delete = (TextView) findViewById(R.id.tv_delete);
    }

    @Override
    public void initListeners() {
        tv_delete.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        bookManager = new BookManager();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bookItem = (BookItem) bundle.getSerializable("bookItem");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                bookManager.deleteBookItem(bookItem, new SaveListenerUI() {
                    @Override
                    public void onSucessUI() {
                        setResult(ResultRequestCode.RESULT_BOOK_ITEM_DELETE);
                        finish();
                    }

                    @Override
                    public void onFailUI() {

                    }
                });
                break;
        }
    }
}
