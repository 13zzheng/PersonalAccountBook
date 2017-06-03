package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
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
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.model.IQuerier;
import stu.edu.cn.zing.personalbook.model.Querier;
import stu.edu.cn.zing.personalbook.user_add_book.AddBookActivity;

/**
 * Created by Administrator on 2017/3/27.
 */

public class BookItemTyoeDialog extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener{

    private ListView lv_book_item_type;
    private List<BookItemType> bookItemTypes;
    private TextView tv_add;

    private IQuerier iQuerier;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_item_type);
    }

    @Override
    public void initViews() {
        lv_book_item_type = (ListView) findViewById(R.id.lv_book_item_type);
        tv_add = (TextView) findViewById(R.id.tv_add);
    }

    @Override
    public void initListeners() {
        lv_book_item_type.setOnItemClickListener(this);
        tv_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        iQuerier = new Querier();
        setResult(RESULT_OK);
        getBookItemTypeList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ResultRequestCode.REQUEST_ADD_ITEM_TYPE:
                getBookItemTypeList();
                break;
        }
    }

    private void getBookItemTypeList() {
        User user = BmobUser.getCurrentUser(User.class);
        iQuerier.queryCurrentItemType(user, new FindListenerUI<BookItemType>() {
            @Override
            public void onSucess(List<BookItemType> list) {
                bookItemTypes = list;
                showList();
            }

            @Override
            public void onFail() {
                Log.e("query_item_type","查询失败");
            }
        });
    }

    private void showList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < bookItemTypes.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("type",bookItemTypes.get(i).getType());

            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list, R.layout.list_book_item_type,
                new String[] {"type"}, new int[] {R.id.tv_typeitem});

        lv_book_item_type.setAdapter(simpleAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookItemType type = bookItemTypes.get(position);
        Log.i("item_type",type.getType());
        Intent intent = new Intent(BookItemTyoeDialog.this, AddBookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("type",type);
        intent.putExtras(bundle);
        setResult(ResultRequestCode.RESULT_ITEM_TYPE,intent);
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                Intent intent = new Intent(BookItemTyoeDialog.this, AddItemTypeDialog.class);
                startActivityForResult(intent, ResultRequestCode.REQUEST_ADD_ITEM_TYPE);
                break;
        }
    }
}
