package stu.edu.cn.zing.personalbook.HomeFragments;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import stu.edu.cn.zing.personalbook.AlarmController;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.HomeFragments.BookFragment;
import stu.edu.cn.zing.personalbook.HomeFragments.FinanicalFragment;
import stu.edu.cn.zing.personalbook.HomeFragments.PersonalFragment;
import stu.edu.cn.zing.personalbook.HomeFragments.StatisticalFragment;
import stu.edu.cn.zing.personalbook.NotificationController;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;

public class HomeActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener
,BookFragment.OnFragmentInteractionListener,StatisticalFragment.OnFragmentInteractionListener
,FinanicalFragment.OnFragmentInteractionListener,PersonalFragment.OnFragmentInteractionListener{

    private BottomNavigationBar bottom_navigation_bar;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private List<Fragment> fragmentList;
    private int currentPosition;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void initViews() {
        bottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }

    @Override
    public void initListeners() {
        bottom_navigation_bar.setTabSelectedListener(this);
    }

    @Override
    public void initData() {

        Log.i("Home","Welcome Home");
        Log.i("User", BmobUser.getCurrentUser(User.class).getObjectId());


        initBotoomNavigationBar();
        initFragmentList();
        //AlarmController alarmController = new AlarmController(this);
        //alarmController.setAlarm();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            if (currentPosition == 2) {
                FinanicalFragment fragment = (FinanicalFragment) fragmentList.get(currentPosition);
                fragment.onKeyDown(keyCode, event);
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) return;
        switch (requestCode) {
            case ResultRequestCode.REQUEST_BOOK_ITEM_MODIFY:
                if (resultCode == ResultRequestCode.RESULT_BOOK_ITEM_MODIFY || resultCode == ResultRequestCode.RESULT_ADD_ITEM_SUCCESS) {
                    Log.i("MODIFY", "modify");
                    BookFragment fragment = (BookFragment) fragmentList.get(0);
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;

            case ResultRequestCode.REQUEST_BOOK_ITEM_DELETE:
                if (resultCode == ResultRequestCode.RESULT_BOOK_ITEM_DELETE) {
                    Log.i("DELETE", "delete");
                    BookFragment fragment = (BookFragment) fragmentList.get(0);
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    private void initBotoomNavigationBar() {
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_FIXED);
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottom_navigation_bar
                .addItem(new BottomNavigationItem(R.mipmap.book,getString(R.string.book))
                        .setActiveColor(getColor(R.color.book_color)))
                .addItem(new BottomNavigationItem(R.mipmap.statistical_analysis,getString(R.string.statisical_analysis))
                        .setActiveColor(getColor(R.color.statisical_analysis_color)))
                .addItem(new BottomNavigationItem(R.mipmap.finanical_recommendation, getString(R.string.finanical_recommendation))
                        .setActiveColor(getColor(R.color.finanical_recommendation_color)))
                .addItem(new BottomNavigationItem(R.mipmap.persenal, getString(R.string.personal))
                        .setActiveColor(getColor(R.color.personal_color)))
                .initialise();
    }

    private void initFragmentList() {
        fragmentList = getFragmentList();
        setDefultFragment();
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fs = new ArrayList<>();
        fs.add(BookFragment.newInstance("BookFragment"));
        fs.add(StatisticalFragment.newInstance("StatisticalFragment"));
        fs.add(FinanicalFragment.newInstance("FinanicalFragment"));
        fs.add(PersonalFragment.newInstance("PersonalFragment"));


        return fs;
    }
    private void setDefultFragment(){
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        currentPosition = 0;
        transaction.add(R.id.page_fragment, fragmentList.get(currentPosition));
        transaction.commit();

    }

    @Override
    public void onTabSelected(int position) {
        hideCurrentFragment();
        if (fragmentList != null) {
            if (position < fragmentList.size()) {
                FragmentTransaction transaction = fm.beginTransaction();
                Fragment fragment = fragmentList.get(position);
                if (fragment.isAdded()) {
                    transaction.show(fragment);
                } else {
                    transaction.add(R.id.page_fragment,fragment);
                }
                currentPosition = position;
                transaction.commit();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void hideCurrentFragment() {
        FragmentTransaction transaction = fm.beginTransaction();
        if(fragmentList.get(currentPosition).isAdded()) {
            transaction.hide(fragmentList.get(currentPosition));
        }
        transaction.commit();
    }
}
