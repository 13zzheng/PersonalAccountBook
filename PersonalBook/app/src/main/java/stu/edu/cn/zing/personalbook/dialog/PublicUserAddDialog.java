package stu.edu.cn.zing.personalbook.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;

public class PublicUserAddDialog extends BaseActivity implements View.OnClickListener {

    private EditText et_public_user;
    private TextView tv_add;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_public_user_add_dialog);
    }

    @Override
    public void initViews() {
        et_public_user = (EditText) findViewById(R.id.et_public_user);
        tv_add = (TextView) findViewById(R.id.tv_add);
    }

    @Override
    public void initListeners() {
        tv_add.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                if (TextUtils.isEmpty(et_public_user.getText())) {
                    Toast.makeText(PublicUserAddDialog.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
                BmobQuery<User> userBmobQuery = new BmobQuery<>();
                userBmobQuery.addWhereEqualTo("username", et_public_user.getText().toString());
                userBmobQuery.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            if (list.size() == 0) {
                                Toast.makeText(PublicUserAddDialog.this, "用户不存在", Toast.LENGTH_SHORT).show();
                            } else {
                                Book book = Book.getCurrentBook();
                                BmobRelation relation = new BmobRelation();
                                relation.add(list.get(0));
                                book.setPublicUsers(relation);
                                book.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            setResult(ResultRequestCode.RESULT_PUBLIC_USER_ADD);
                                            finish();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;
        }
    }
}
