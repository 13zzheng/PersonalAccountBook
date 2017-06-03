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
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTradingWay;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.model.Querier;
import stu.edu.cn.zing.personalbook.user_add_book.AddBookActivity;

/**
 * Created by Administrator on 2017/3/31.
 */

public class BookItemTradingWayDialog extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv_book_item_trading_way;
    private TextView tv_add;

    private List<BookItemTradingWay> bookItemTradingWays;
    private Querier querier;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_item_trading_way);
    }

    @Override
    public void initViews() {
        lv_book_item_trading_way = (ListView) findViewById(R.id.lv_book_item_trading_way);
        tv_add = (TextView) findViewById(R.id.tv_add);
    }

    @Override
    public void initListeners() {
        tv_add.setOnClickListener(this);
        lv_book_item_trading_way.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        setResult(RESULT_OK);
        querier = new Querier();
        getBookItemTradingWayList();
    }

    private void getBookItemTradingWayList() {
        User user = BmobUser.getCurrentUser(User.class);
        querier.queryCurrentItemTradingWay(user, new FindListenerUI<BookItemTradingWay>() {
            @Override
            public void onSucess(List<BookItemTradingWay> list) {
                bookItemTradingWays = list;
                showList();
            }

            @Override
            public void onFail() {
                Log.e("query_item_trading_way","查询失败");
            }
        });
    }

    private void showList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < bookItemTradingWays.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("way",bookItemTradingWays.get(i).getItemTradingWay());

            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list, R.layout.list_book_item_trading_way,
                new String[] {"way"}, new int[] {R.id.tv_item_trading_way});

        lv_book_item_trading_way.setAdapter(simpleAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ResultRequestCode.REQUEST_ADD_ITEM_TRADING_WAY:
                getBookItemTradingWayList();
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                Intent intent = new Intent(BookItemTradingWayDialog.this, AddItemTradingWayDialog.class);
                startActivityForResult(intent, ResultRequestCode.REQUEST_ADD_ITEM_TRADING_WAY);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookItemTradingWay way = bookItemTradingWays.get(position);
        Log.i("item_member",way.getItemTradingWay());
        Intent intent = new Intent(BookItemTradingWayDialog.this, AddBookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("way",way);
        intent.putExtras(bundle);
        setResult(ResultRequestCode.RESULT_ITEM_TRADING_WAY,intent);
        finish();
    }
}
