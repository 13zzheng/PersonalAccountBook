package stu.edu.cn.zing.personalbook.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import stu.edu.cn.zing.personalbook.BookFindActivity;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.dialog.BookPickDialog;
import stu.edu.cn.zing.personalbook.dialog.BudgetDialog;
import stu.edu.cn.zing.personalbook.dialog.DatePickerDialog;
import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookMonthFinancial;
import stu.edu.cn.zing.personalbook.dialog.PublicUsersDialog;
import stu.edu.cn.zing.personalbook.finanicalreport.IFinanicalReportActivity;
import stu.edu.cn.zing.personalbook.model.BookManager;
import stu.edu.cn.zing.personalbook.user_add_book.AddBookActivity;
import stu.edu.cn.zing.personalbook.BookItems;
import stu.edu.cn.zing.personalbook.listadapter.BookListAdapter;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "tag";

    // TODO: Rename and change types of parameters
    private String tag;

    private OnFragmentInteractionListener mListener;

    private Context context;


    private FrameLayout layout_no_data;
    private FrameLayout layout_data;
    private ListView lvBook;

    private TextView tv_book_name;

    private ImageView img_find;
    private ImageView img_public_users;

    private TextView tv_month_input;
    private TextView tv_month_input_value;

    private TextView tv_month_output;
    private TextView tv_month_output_value;

    private TextView tv_budget;
    private TextView tv_budget_overage_value;

    private FloatingActionButton btnAddBook;

    private List<BookItems> bookItemsList;

    private BookMonthFinancial bookMonthFinancial;

    private int flag = 0;

    private int currentYear;
    private int currentMonth;

    private BmobQuery.CachePolicy cachePolicy = BmobQuery.CachePolicy.CACHE_ELSE_NETWORK;

    public BookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tag Parameter 1.
     *
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String tag) {
        BookFragment fragment = new BookFragment();
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
        View v = inflater.inflate(R.layout.fragment_book, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        lvBook = (ListView) v.findViewById(R.id.lvBook);
        layout_no_data = (FrameLayout) v.findViewById(R.id.layout_no_data);
        layout_data = (FrameLayout) v.findViewById(R.id.layout_data);
        tv_book_name = (TextView) v.findViewById(R.id.tv_book_name);
        img_find = (ImageView) v.findViewById(R.id.img_find);
        img_public_users = (ImageView) v.findViewById(R.id.img_public_users);
        tv_month_input = (TextView) v.findViewById(R.id.tv_month_input);
        tv_month_input_value = (TextView) v.findViewById(R.id.tv_month_input_value);
        tv_month_output = (TextView) v.findViewById(R.id.tv_month_output);
        tv_month_output_value = (TextView) v.findViewById(R.id.tv_month_output_value);
        btnAddBook = (FloatingActionButton) v.findViewById(R.id.btnAddBook);
        tv_budget = (TextView) v.findViewById(R.id.tv_budget);
        tv_budget_overage_value = (TextView) v.findViewById(R.id.tv_budget_overage_value);

        setListeners();

        init();



    }

    private void setListeners() {
        btnAddBook.setOnClickListener(this);
        tv_month_input.setOnClickListener(this);
        tv_month_output.setOnClickListener(this);
        tv_budget.setOnClickListener(this);
        tv_book_name.setOnClickListener(this);
        img_find.setOnClickListener(this);
        img_public_users.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) return;
        switch (requestCode) {
            case ResultRequestCode.REQUEST_ADD_ITEM:
                if (resultCode == ResultRequestCode.RESULT_ADD_ITEM_SUCCESS) {
                    //cachePolicy = BmobQuery.CachePolicy.NETWORK_ONLY;
                    initData(currentYear, currentMonth);
                }
                break;
            case ResultRequestCode.REQUEST_BOOK_ITEM_MODIFY:
                if (resultCode == ResultRequestCode.RESULT_BOOK_ITEM_MODIFY || resultCode == ResultRequestCode.RESULT_ADD_ITEM_SUCCESS ) {
                    initData(currentYear, currentMonth);
                }
                break;

            case ResultRequestCode.REQUEST_BOOK_ITEM_DELETE:
                if (resultCode == ResultRequestCode.RESULT_BOOK_ITEM_DELETE) {
                    initData(currentYear, currentMonth);
                }
                break;
            case ResultRequestCode.REQUEST_DATE_PICKER_WITH_NO_DAY:
                if (resultCode == ResultRequestCode.RESULT_DATE_PICKER) {
                    Bundle bundle = data.getExtras();
                    currentYear = bundle.getInt("year");
                    currentMonth = bundle.getInt("month");

                    initData(currentYear, currentMonth);
                }
                break;

            case ResultRequestCode.REQUEST_BUDGET_SETTING:
                if (resultCode == ResultRequestCode.RESULT_BUDGET_SETTING_SUCCESS){
                    BookMonthFinancial bookMonthFinancial;
                    bookMonthFinancial = (BookMonthFinancial) data.getExtras().getSerializable("bookMonthFinancial");
                    setMonthFinancial(bookMonthFinancial);
                }
                break;

            case ResultRequestCode.REQUEST_BOOK_PICKER:
                if (resultCode == ResultRequestCode.RESULT_BOOK_PICKER) {
                    init();
                }
                break;
        }
    }
    private void init() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-E");
        String date = simpleDateFormat.format(new Date());
        String[] d = date.split("-");
        String year = d[0];
        String month = d[1];
        if (month.substring(0,1).equals("0")) {
            month = month.substring(month.length()-1);
        }

        Log.i("PUBLIC",Book.getCurrentBook().isPublic() + "");
        if (Book.getCurrentBook().isPublic()) {
            img_public_users.setVisibility(View.VISIBLE);
        } else {
            img_public_users.setVisibility(View.GONE);
        }


        currentYear = Integer.valueOf(year);
        currentMonth = Integer.valueOf(month);
        initBookName();
        initData(currentYear, currentMonth);
    }

    private void initBookName() {
        tv_book_name.setText(Book.getCurrentBook().getName());
    }

    private void initData(int year, int month) {

        tv_month_input.setText(month + "月收入");
        tv_month_output.setText(month + "月支出");
        initBookMonthItemList(year,month);
        initBookMonthFinancial(year, month);

    }


    private void initBookMonthItemList(int year, int month) {

        flag = 0;
        Log.i("BookMonthItemList","开始获取账单数据");
        bookItemsList = new ArrayList<>();

        BmobQuery<BookItemTitle> q1 = new BmobQuery<>();
        q1.order("-year,-month,-day");
        q1.addWhereEqualTo("year", year);
        q1.addWhereEqualTo("month", month);
        q1.addWhereEqualTo("book",Book.getCurrentBook());


        q1.findObjects(new FindListener<BookItemTitle>() {
            @Override
            public void done(final List<BookItemTitle> list, BmobException e) {
                if (e == null) {
                    Log.i("BookMonthItemList","获取Title数据 size:" + list.size());
                    if (list.size() == 0) {
                        //显示没有数据
                        showNoData();
                    }else {
                        showData();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        final BookItems bookitems = new BookItems();
                        bookitems.setTitle(list.get(i));
                        bookItemsList.add(bookitems);
                        BmobQuery<BookItem> q2 = new BmobQuery<>();
                        q2.addWhereEqualTo("itemTitle", list.get(i));
                        q2.order("-createdAt");
                        q2.include("payment,itemTitle,itemInfo.itemType,itemInfo.itemTradingWay,itemInfo.itemMember");

                        q2.findObjects(new FindListener<BookItem>() {
                            @Override
                            public void done(List<BookItem> list2, BmobException e) {
                                if (e == null) {
                                    flag++;
                                    Log.i("BookMonthItemList", bookitems.getTitle().getMonth() + "月" + bookitems.getTitle().getDay() +
                                            "日"+"获取Item数据 size:" + list2.size() + "flag " + flag);
                                    if (list2.size() == 0 ) {
                                        bookItemsList.remove(bookitems);
                                    }
                                    bookitems.setItems(list2);

                                    if (flag == list.size()) {
                                        //如果bookItemList读取完毕
                                        flag = 0;

                                        Log.i("BookMonthItemList","数据获取完毕，开始绘制ListView");

                                        BookListAdapter bookListAdapter = new BookListAdapter(context, bookItemsList);
                                        lvBook.setAdapter(bookListAdapter);
                                        cachePolicy = BmobQuery.CachePolicy.CACHE_ELSE_NETWORK;
                                    }
                                }
                            }
                        });
                    }
                }else {
                }
            }
        });
    }

    //获得BookMonthFinancial
    private void initBookMonthFinancial(final int year, final int month) {

        BookManager bookManager = new BookManager();
        bookManager.queryBookMonthFinancial(year, month, Book.getCurrentBook(), new FindListenerUI<BookMonthFinancial>() {
            @Override
            public void onSucess(List<BookMonthFinancial> list) {
                if (list.size() > 0) {
                    bookMonthFinancial = list.get(0);
                    setMonthFinancial(bookMonthFinancial);
                }else if (list.size() == 0) {
                    //不存在
                    final BookMonthFinancial monthFinancial = new BookMonthFinancial();
                    monthFinancial.setBook(Book.getCurrentBook());
                    monthFinancial.setYear(year);
                    monthFinancial.setMonth(month);
                    monthFinancial.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                bookMonthFinancial = monthFinancial;
                                setMonthFinancial(bookMonthFinancial);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFail() {

            }
        });
    }
    private void setMonthFinancial(BookMonthFinancial bookMonthFinancial) {
        setMonthInput(bookMonthFinancial.getMonthInput());
        setMonthOutput(bookMonthFinancial.getMonthOutput());

        if (bookMonthFinancial.getBudgetEnable()) {
            //如果预算设置启动
            setBudget(bookMonthFinancial.getBudgetOverageValue());
        } else {
            //未启动
            tv_budget_overage_value.setText("未设置");
        }

    }


    //设置月收入
    private void setMonthInput(float monthInput) {
        tv_month_input_value.setText(String.valueOf(monthInput));
    }

    //设置月支出
    private void setMonthOutput(float monthOutput) {
        tv_month_output_value.setText(String.valueOf(monthOutput));
    }

    //设置预算
    private void setBudget(float budgetOverageValue) {
        tv_budget_overage_value.setText(String.valueOf(budgetOverageValue));
    }


    private void showData() {
        layout_data.setVisibility(View.VISIBLE);
        layout_no_data.setVisibility(View.GONE);
    }
    private void showNoData() {
        layout_data.setVisibility(View.GONE);
        layout_no_data.setVisibility(View.VISIBLE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddBook:
                Intent addBookIntent = new Intent(context, AddBookActivity.class);
                Bundle addBookBundle = new Bundle();
                addBookBundle.putInt("request", ResultRequestCode.REQUEST_ADD_ITEM);
                addBookIntent.putExtras(addBookBundle);
                startActivityForResult(addBookIntent, ResultRequestCode.REQUEST_ADD_ITEM);
                break;
            case R.id.tv_month_input:
            case R.id.tv_month_output:
                Intent intentDatePicker = new Intent(context, DatePickerDialog.class);
                Bundle bundleDatePicker = new Bundle();
                bundleDatePicker.putInt("request", ResultRequestCode.REQUEST_DATE_PICKER_WITH_NO_DAY);
                bundleDatePicker.putInt("year", currentYear);
                bundleDatePicker.putInt("month", currentMonth);
                intentDatePicker.putExtras(bundleDatePicker);
                startActivityForResult(intentDatePicker, ResultRequestCode.REQUEST_DATE_PICKER_WITH_NO_DAY);
                break;
            case R.id.tv_budget:
                Intent intent = new Intent(context, BudgetDialog.class);
                Bundle bundle = new Bundle();
                bundle.putInt("year", currentYear);
                bundle.putInt("month", currentMonth);
                intent.putExtras(bundle);
                startActivityForResult(intent, ResultRequestCode.REQUEST_BUDGET_SETTING);
                break;

            case R.id.tv_book_name:
                Intent intent1 = new Intent(context, BookPickDialog.class);
                startActivityForResult(intent1, ResultRequestCode.REQUEST_BOOK_PICKER);
                break;

            case R.id.img_find:
                startActivity(new Intent(context, BookFindActivity.class));
                break;

            case R.id.img_public_users:
                startActivity(new Intent(context, PublicUsersDialog.class));
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
