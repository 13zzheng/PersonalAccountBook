package stu.edu.cn.zing.personalbook;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import stu.edu.cn.zing.personalbook.bmobclass.Book;

/**
 * Created by Administrator on 2017/3/15.
 */
public abstract class BaseActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Bmob.initialize(this,Constants.BMOB_APPID);

        setContentView();
        initViews();
        initListeners();
        initData();
    }

    public abstract void setContentView();

    /**
     * 初始化Views
     */
    public abstract void initViews();

    /**
     * 初始化监听器
     */
    public abstract void initListeners();

    /**
     * 初始化相关数据
     */
    public abstract void initData();

}
