package stu.edu.cn.zing.personalbook.dialog;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemMember;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.User;

/**
 * Created by Administrator on 2017/3/31.
 */

public class AddItemMemberDialog extends BaseActivity implements View.OnClickListener {
    private EditText et_item_member;
    private TextView tv_add;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_book_item_member);
    }

    @Override
    public void initViews() {
        et_item_member = (EditText) findViewById(R.id.et_item_member);
        tv_add = (TextView) findViewById(R.id.tv_add);
    }

    @Override
    public void initListeners() {
        tv_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(RESULT_OK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                addItemMember();
                break;
        }
    }

    private void addItemMember() {
        if (TextUtils.isEmpty(et_item_member.getText())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String member = et_item_member.getText().toString();
        final BookItemMember bookItemMember= new BookItemMember();
        User user = BmobUser.getCurrentUser(User.class);
        bookItemMember.setUser(user);
        bookItemMember.setItemMember(member);
        bookItemMember.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //添加成功
                    setResult(ResultRequestCode.RESULT_ADD_ITEM_MEMBER_SUCCESS);
                    finish();
                } else {
                    //添加失败
                    Log.e("add_item_member","添加失败");
                }
            }
        });
    }
}
