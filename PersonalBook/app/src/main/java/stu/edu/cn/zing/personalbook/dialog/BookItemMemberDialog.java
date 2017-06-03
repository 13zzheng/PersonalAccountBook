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
import stu.edu.cn.zing.personalbook.bmobclass.BookItemMember;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.model.Querier;
import stu.edu.cn.zing.personalbook.user_add_book.AddBookActivity;

/**
 * Created by Administrator on 2017/3/31.
 */

public class BookItemMemberDialog extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ListView lv_book_item_member;
    private TextView tv_add;
    private List<BookItemMember> bookItemMembers;

    private Querier querier;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_item_member);
    }

    @Override
    public void initViews() {
        lv_book_item_member = (ListView) findViewById(R.id.lv_book_item_member);
        tv_add = (TextView) findViewById(R.id.tv_add);
    }

    @Override
    public void initListeners() {
        tv_add.setOnClickListener(this);
        lv_book_item_member.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        setResult(RESULT_OK);
        querier = new Querier();
        getBookItemMeberList();
    }

    private void getBookItemMeberList() {
        User user = BmobUser.getCurrentUser(User.class);
        querier.queryCurrentItemMember(user, new FindListenerUI<BookItemMember>() {
            @Override
            public void onSucess(List<BookItemMember> list) {
                bookItemMembers = list;
                showList();
            }

            @Override
            public void onFail() {
                Log.e("query_item_member","查询失败");
            }
        });
    }

    private void showList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < bookItemMembers.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("member",bookItemMembers.get(i).getItemMember());

            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list, R.layout.list_book_item_member,
                new String[] {"member"}, new int[] {R.id.tv_item_member});

        lv_book_item_member.setAdapter(simpleAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ResultRequestCode.REQUEST_ADD_ITEM_MEMBER:
                getBookItemMeberList();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                Intent intent = new Intent(BookItemMemberDialog.this, AddItemMemberDialog.class);
                startActivityForResult(intent, ResultRequestCode.REQUEST_ADD_ITEM_MEMBER);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookItemMember member = bookItemMembers.get(position);
        Log.i("item_member",member.getItemMember());
        Intent intent = new Intent(BookItemMemberDialog.this, AddBookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("member",member);
        intent.putExtras(bundle);
        setResult(ResultRequestCode.RESULT_ITEM_MEMBER,intent);
        finish();
    }
}
