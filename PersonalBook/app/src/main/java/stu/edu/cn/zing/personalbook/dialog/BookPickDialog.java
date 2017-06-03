package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class BookPickDialog extends BaseActivity implements View.OnClickListener, ListView.OnItemClickListener,
ListView.OnItemLongClickListener {

    private TextView tv_add;
    private ListView lv_private_book;
    private ListView lv_public_book;

    private List<Book> publicBooks;
    private List<Book> privateBooks;

    private User currentUser;

    private Querier querier;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_pick_dialog);
    }

    @Override
    public void initViews() {
        tv_add = (TextView) findViewById(R.id.tv_add);
        lv_private_book = (ListView) findViewById(R.id.lv_private_book);
        lv_public_book = (ListView) findViewById(R.id.lv_public_book);
    }

    @Override
    public void initListeners() {
        tv_add.setOnClickListener(this);
        lv_private_book.setOnItemClickListener(this);
        lv_public_book.setOnItemClickListener(this);
        lv_private_book.setOnItemLongClickListener(this);
        lv_public_book.setOnItemLongClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        querier = new Querier();
        currentUser = BmobUser.getCurrentUser(User.class);
        getPrivateBooks();
        getPublicBooks();
    }

    private void getPrivateBooks() {
        querier.queryCurrentPrivateBooks(currentUser, new FindListenerUI<Book>() {
            @Override
            public void onSucess(List<Book> list) {
                privateBooks = list;
                showPrivateBooks();
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void showPrivateBooks() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < privateBooks.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("book", privateBooks.get(i).getName());

            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.list_book_item_type,
                new String[]{"book"}, new int[]{R.id.tv_typeitem});

        lv_private_book.setAdapter(simpleAdapter);
    }


    private void getPublicBooks() {
        querier.queryCurrentPublicBooks(currentUser, new FindListenerUI<Book>() {
            @Override
            public void onSucess(List<Book> list) {
                publicBooks = list;
                showPublicBooks();
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void showPublicBooks() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < publicBooks.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("book", publicBooks.get(i).getName());

            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.list_book_item_type,
                new String[]{"book"}, new int[]{R.id.tv_typeitem});

        lv_public_book.setAdapter(simpleAdapter);
    }

    private void modifyBook(Book book) {
        Intent intent = new Intent(this, BookModifyDialog.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        intent.putExtras(bundle);
        startActivityForResult(intent, ResultRequestCode.REQUEST_BOOK_MODIFY);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) return;
        switch (requestCode) {
            case ResultRequestCode.REQUEST_BOOK_ADD:
                if (resultCode == ResultRequestCode.RESULT_BOOK_ADD) {
                    getPrivateBooks();
                    getPublicBooks();
                }
                break;

            case ResultRequestCode.REQUEST_BOOK_MODIFY:
                if (resultCode == ResultRequestCode.RESULT_BOOK_MODIFY) {
                    getPrivateBooks();
                    getPublicBooks();
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                Intent intent = new Intent(this, BookAddDialog.class);
                startActivityForResult(intent, ResultRequestCode.REQUEST_BOOK_ADD);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_private_book:
                Log.i("BOok", privateBooks.get(position).getName());
                Book.setCurrentBook(privateBooks.get(position));
                setResult(ResultRequestCode.RESULT_BOOK_PICKER);
                finish();
                break;

            case R.id.lv_public_book:
                Book.setCurrentBook(publicBooks.get(position));
                setResult(ResultRequestCode.RESULT_BOOK_PICKER);
                finish();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv_private_book:
                modifyBook(privateBooks.get(position));
                break;

            case R.id.lv_public_book:
                modifyBook(publicBooks.get(position));
                break;
        }
        return false;
    }
}
