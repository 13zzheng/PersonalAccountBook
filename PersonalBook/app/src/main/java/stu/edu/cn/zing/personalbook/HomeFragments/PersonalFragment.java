package stu.edu.cn.zing.personalbook.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import stu.edu.cn.zing.personalbook.AlarmController;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.BookReminder;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.dialog.PasswordModifyDialog;
import stu.edu.cn.zing.personalbook.dialog.ReminderDialog;
import stu.edu.cn.zing.personalbook.dialog.UserNameModifyDialog;
import stu.edu.cn.zing.personalbook.model.TradingWayFinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "tag";

    // TODO: Rename and change types of parameters
    private String tag;

    private Context context;

    private Button btn_logout;
    private LinearLayout ll_user_name;
    private LinearLayout ll_time;
    private LinearLayout ll_password;

    private TextView tv_user_name;
    private TextView tv_time;

    private String userName;

    private BookReminder bookReminder;


    private OnFragmentInteractionListener mListener;

    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tag Parameter 1.
     *
     * @return A new instance of fragment PersonalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String tag) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tag = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        ll_time = (LinearLayout) view.findViewById(R.id.ll_time);
        ll_user_name = (LinearLayout) view.findViewById(R.id.ll_user_name);
        tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        ll_password = (LinearLayout) view.findViewById(R.id.ll_password);


        ll_user_name.setOnClickListener(this);
        ll_time.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        ll_password.setOnClickListener(this);

        userName = BmobUser.getCurrentUser(User.class).getName();

        BmobQuery<BookReminder> reminderBmobQuery = new BmobQuery<>();
        reminderBmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
        reminderBmobQuery.findObjects(new FindListener<BookReminder>() {
            @Override
            public void done(List<BookReminder> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        bookReminder = list.get(0);
                        setTime();
                    }
                }
            }
        });

        setUserName(userName);

    }

    private void setTime() {
        tv_time.setText(bookReminder.getHour() + ":" + formateTime(bookReminder.getMinute()));
    }

    private String formateTime(int i) {
        if (i < 10) {
            return "0" + i;
        }else {
            return i + "";
        }
    }

    private void setUserName(String name) {
        tv_user_name.setText(name);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) {
            return;
        }
        switch (requestCode) {
            case ResultRequestCode.REQUEST_USER_NAME_MODIFY:
                if (resultCode == ResultRequestCode.RESULT_USER_NAME_MODIFY) {
                    userName = BmobUser.getCurrentUser(User.class).getName();
                    setUserName(userName);
                }
                break;
            case ResultRequestCode.REQUEST_TIME_PICKER:
                if (resultCode == ResultRequestCode.RESULT_TIME_PICKER) {
                    Bundle bundle = data.getExtras();
                    bookReminder = (BookReminder) bundle.getSerializable("reminder");
                    setTime();
                    if (bookReminder.getEnable()) {
                        setReminder();
                    }else {
                        cancelReminder();
                    }
                }

                break;
        }
    }

    private void setReminder() {
        AlarmController alarmController = new AlarmController(context);


        Log.i("End",bookReminder.getHour() + " " + bookReminder.getMinute());
        alarmController.setAlarm(
                bookReminder.getHour(),
                bookReminder.getMinute());
    }

    private void cancelReminder() {
        AlarmController alarmController = new AlarmController(context);
        alarmController.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                BmobUser.logOut();
                System.exit(0);
                break;

            case R.id.ll_user_name:
                Intent intent = new Intent(context, UserNameModifyDialog.class);

                startActivityForResult(intent, ResultRequestCode.REQUEST_USER_NAME_MODIFY);
                break;

            case R.id.ll_time:
                Log.i("Time","time");
                Intent timeIntent = new Intent(context, ReminderDialog.class);
                Bundle bundle = new Bundle();
                if (bookReminder != null) {
                    bundle.putSerializable("reminder", bookReminder);
                    timeIntent.putExtras(bundle);
                }
                startActivityForResult(timeIntent, ResultRequestCode.REQUEST_TIME_PICKER);
                break;

            case R.id.ll_password:
                startActivity(new Intent(context, PasswordModifyDialog.class));
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
