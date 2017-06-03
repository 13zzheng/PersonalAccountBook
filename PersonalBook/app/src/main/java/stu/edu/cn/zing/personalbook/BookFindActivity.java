package stu.edu.cn.zing.personalbook;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemMember;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTradingWay;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.BookPayment;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.dialog.DateDoublePickerDialog;
import stu.edu.cn.zing.personalbook.dialog.FinderDialog;
import stu.edu.cn.zing.personalbook.dialog.PaymentPickerDialog;
import stu.edu.cn.zing.personalbook.listadapter.BookListAdapter;
import stu.edu.cn.zing.personalbook.model.MemberFinder;
import stu.edu.cn.zing.personalbook.model.PaymentFinder;
import stu.edu.cn.zing.personalbook.model.Querier;
import stu.edu.cn.zing.personalbook.model.TradingWayFinder;
import stu.edu.cn.zing.personalbook.model.TypeFinder;

public class BookFindActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_date;
    private TextView tv_payment;
    private TextView tv_type;
    private TextView tv_member;
    private TextView tv_trading_way;
    private TextView tv_no_data;

    private ImageView img_back;

    private Button btn_reset;
    private Button btn_find;

    private ListView lv_find_list;


    private String startYear;
    private String startMonth;
    private String startDay;
    private String endYear;
    private String endMonth;
    private String endDay;

    private User currentUser;

    private PaymentFinder paymentFinder;
    private TypeFinder typeFinder;
    private MemberFinder memberFinder;
    private TradingWayFinder tradingWayFinder;

    private List<BookItems> bookItemsList;


    private Querier querier;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_book_find);
    }

    @Override
    public void initViews() {
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_payment = (TextView) findViewById(R.id.tv_payment);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_member = (TextView) findViewById(R.id.tv_member);
        tv_trading_way = (TextView) findViewById(R.id.tv_trading_way);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_find = (Button) findViewById(R.id.btn_find);
        lv_find_list = (ListView) findViewById(R.id.lv_find_list);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
        img_back = (ImageView) findViewById(R.id.img_back);
    }

    @Override
    public void initListeners() {
        tv_date.setOnClickListener(this);
        tv_payment.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        tv_member.setOnClickListener(this);
        tv_trading_way.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_find.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }
    private int formatDate(String d) {
        if (d.substring(0,1) == "0") {
            return Integer.valueOf(d.substring(1));
        } else {
            return Integer.valueOf(d);
        }
    }

    @Override
    public void initData() {
        querier = new Querier();
        currentUser = BmobUser.getCurrentUser(User.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        String[] d = date.split("-");
        startYear = endYear = d[0];
        startMonth = endMonth = d[1];
        startDay = "01";
        endDay = d[2];

        setPymentFinder();
        setTypeFinder();
        setMemberFinder();
        setTradingWayFinder();

        initDate();
    }

    private void initDate() {
        tv_date.setText(startYear + "年" + startMonth + "月" + startDay + "日" + "-" +
                endYear + "年" + endMonth + "月" + endDay + "日");
    }

    private void initPayment() {
        if (paymentFinder.isEnable()) {
            if (paymentFinder.isLess() && paymentFinder.isGreat()) {
                if (paymentFinder.getGreatValue() == paymentFinder.getLessValue()) {
                    tv_payment.setText("价格" + " = " + paymentFinder.getLessValue());
                } else {
                    tv_payment.setText(paymentFinder.getGreatValue() + " ≤ " + "价格" + " ≤ " + paymentFinder.getLessValue());
                }
            } else if (paymentFinder.isLess()) {
                tv_payment.setText("价格" + " ≤ " + paymentFinder.getLessValue());
            } else if (paymentFinder.isGreat()) {
                tv_payment.setText("价格" + " ≥ " + paymentFinder.getGreatValue());
            } else {
                tv_payment.setText("价格");
            }

            if (paymentFinder.isInput() && paymentFinder.isOutput()) {
                tv_payment.setTextColor(getColor(R.color.report_middle));
            } else if (paymentFinder.isInput()) {
                tv_payment.setTextColor(getColor(R.color.report_positive));
            } else if (paymentFinder.isOutput()) {
                tv_payment.setTextColor(getColor(R.color.report_negative));
            }
        }else {
            tv_payment.setText("价格");
            tv_payment.setTextColor(getColor(R.color.primary_text));
        }
    }

    private void initType() {
        if (typeFinder.isEnable()) {
            int size = 0;
            for (int i = 0; i < typeFinder.getIsCheckedList().size(); i++) {
                if (typeFinder.getIsCheckedList().get(i)) {
                    size++;
                }
            }
            String s = "(" + size + ")";
            String text = "类别" + s;
            tv_type.setText(text);

            SpannableStringBuilder builder = new SpannableStringBuilder(tv_type.getText().toString());
            builder.setSpan(new ForegroundColorSpan(getColor(R.color.colorPrimary)), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }else {
            tv_type.setText("类型");
        }
    }

    private void initMember() {
        if (memberFinder.isEnable()) {
            int size = 0;
            for (int i = 0; i < memberFinder.getIsCheckedList().size(); i++) {
                if (memberFinder.getIsCheckedList().get(i)) {
                    size++;
                }
            }

            tv_member.setText("成员" + "(" + size + ")");
        } else {
            tv_member.setText("成员");
        }
    }

    private void initTradingWay() {
        if (tradingWayFinder.isEnable()) {
            int size = 0;
            for (int i = 0; i < tradingWayFinder.getIsCheckedList().size(); i++) {
                if (tradingWayFinder.getIsCheckedList().get(i)) {
                    size++;
                }
            }

            tv_trading_way.setText("交易方式" + "(" + size + ")");
        } else {
            tv_trading_way.setText("交易方式");
        }
    }

    private void setPymentFinder() {
        paymentFinder = new PaymentFinder();
    }

    private void setTypeFinder() {
        typeFinder = new TypeFinder();
        querier.queryCurrentItemType(currentUser, new FindListenerUI<BookItemType>() {
            @Override
            public void onSucess(List<BookItemType> list) {

                for (int i = 0; i < list.size(); i++) {
                    typeFinder.addName(list.get(i).getType());
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void setMemberFinder() {
        memberFinder = new MemberFinder();
        querier.queryCurrentItemMember(currentUser, new FindListenerUI<BookItemMember>() {
            @Override
            public void onSucess(List<BookItemMember> list) {
                for (int i = 0; i < list.size(); i++) {
                    memberFinder.addName(list.get(i).getItemMember());
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void setTradingWayFinder() {
        tradingWayFinder = new TradingWayFinder();
        querier.queryCurrentItemTradingWay(currentUser, new FindListenerUI<BookItemTradingWay>() {
            @Override
            public void onSucess(List<BookItemTradingWay> list) {

                for (int i = 0; i < list.size(); i++) {
                    tradingWayFinder.addName(list.get(i).getItemTradingWay());
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void reset() {
        paymentFinder.reset();
        typeFinder.reset();
        memberFinder.reset();
        tradingWayFinder.reset();

        initPayment();
        initType();
        initMember();
        initTradingWay();

    }

    private int flag = 0;
    private void find() {
        bookItemsList = new ArrayList<>();
        BmobQuery<BookItemTitle> titleQuery = new BmobQuery<>();

        titleQuery.addWhereGreaterThanOrEqualTo("year", Integer.valueOf(startYear));
        titleQuery.addWhereLessThanOrEqualTo("year", Integer.valueOf(endYear));
        titleQuery.addWhereGreaterThanOrEqualTo("month", formatDate(startMonth));
        titleQuery.addWhereLessThanOrEqualTo("month", formatDate(endMonth));
        titleQuery.addWhereGreaterThanOrEqualTo("day", formatDate(startDay));
        titleQuery.addWhereLessThanOrEqualTo("day", formatDate(endDay));
        titleQuery.addWhereEqualTo("book", Book.getCurrentBook());

        titleQuery.order("-year,-month,-day");

        titleQuery.findObjects(new FindListener<BookItemTitle>() {
            @Override
            public void done(final List<BookItemTitle> list, BmobException e) {
                if (e == null) {
                    Log.i("BookMonthItemList","获取Title数据 size:" + list.size());

                    for (int i = 0; i < list.size(); i++) {
                        BmobQuery<BookPayment> paymentQuery = paymentFinder.getQuery();
                        BmobQuery<BookItem> q2 = new BmobQuery<>();
                        final BookItems bookitems = new BookItems();
                        bookitems.setTitle(list.get(i));
                        bookItemsList.add(bookitems);
                        if (paymentQuery != null) {
                            q2.addWhereMatchesQuery("payment", "BookPayment", paymentQuery);
                        }
                        q2.addWhereEqualTo("itemTitle", list.get(i));
                        q2.order("-createdAt");
                        q2.include("payment,itemTitle,itemInfo.itemType,itemInfo.itemTradingWay,itemInfo.itemMember");
                        q2.findObjects(new FindListener<BookItem>() {
                            @Override
                            public void done(List<BookItem> list2, BmobException e) {
                                if (e == null) {
                                    filter(list2);
                                    flag++;
                                    if (list2.size() > 0) {
                                        //当有符合条件的Item时
                                        float totalPay = 0;
                                        for (int j = 0; j < list2.size(); j++) {
                                            Log.i("LIST", list2.get(j).getItemInfo().getRemarks());
                                            if (list2.get(j).getPayment().getPayType() == BookPaymentType.TYPE_POSITIVE) {
                                                totalPay += list2.get(j).getPayment().getPay();
                                            } else {
                                                totalPay -= list2.get(j).getPayment().getPay();
                                            }
                                        }

                                        bookitems.getTitle().setTotalPay(totalPay);
                                        bookitems.setItems(list2);
                                        Log.i("Size", bookitems.getItems().size() + "");

                                    } else if (list2.size() == 0) {
                                        bookItemsList.remove(bookitems);
                                    }

                                    if (flag == list.size()) {
                                        //数据全部获取完成
                                        flag = 0;
                                        Log.i("FindList","绘制FindList");
                                        if (bookItemsList.size() == 0) {
                                            showNoData();
                                        }else {
                                            showData();
                                            BookListAdapter bookListAdapter = new BookListAdapter(BookFindActivity.this, bookItemsList);
                                            lv_find_list.setAdapter(bookListAdapter);
                                        }
                                    }

                                }
                            }
                        });
                    }
                } else {
                }
            }
        });
    }

    private void filter(List<BookItem> list) {
        List<String> payment = typeFinder.getCheckedList();
        List<String> member = memberFinder.getCheckedList();
        List<String> tradingWay = tradingWayFinder.getCheckedList();
        List<Integer> removeLsit = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (payment != null) {
                if (!payment.contains(list.get(i).getItemInfo().getItemType().getType())) {
                    if (!removeLsit.contains(i)) {
                        removeLsit.add(i);
                    }
                }
            }
            if (member != null) {
                if (! member.contains(list.get(i).getItemInfo().getItemMember().getItemMember())) {
                    if (!removeLsit.contains(i)) {
                        removeLsit.add(i);
                    }
                }
            }
            if (tradingWay != null) {
                if (! tradingWay.contains(list.get(i).getItemInfo().getItemTradingWay().getItemTradingWay())) {
                    if (!removeLsit.contains(i)) {
                        removeLsit.add(i);
                    }
                }
            }
        }
        for (int i = 0; i < removeLsit.size(); i++) {
            list.remove(removeLsit.get(i) - i);
        }
    }

    private void showData() {
        tv_no_data.setVisibility(View.GONE);
        lv_find_list.setVisibility(View.VISIBLE);
    }

    private void showNoData() {
        tv_no_data.setVisibility(View.VISIBLE);
        lv_find_list.setVisibility(View.GONE);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) return;
        switch (requestCode) {
            case ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER:
                if (resultCode == ResultRequestCode.RESULT_DATE_DOUBLE_PICKER) {
                    Bundle bundle = data.getExtras();
                    startYear = String.valueOf(bundle.getInt("startYear"));
                    startMonth = String.valueOf(bundle.getInt("startMonth"));
                    startDay = String.valueOf(bundle.getInt("startDay"));
                    endYear = String.valueOf(bundle.getInt("endYear"));
                    endMonth = String.valueOf(bundle.getInt("endMonth"));
                    endDay = String.valueOf(bundle.getInt("endDay"));

                    if (startMonth.length() == 1) {
                        startMonth = "0" + startMonth;
                    }
                    if (startDay.length() == 1) {
                        startDay = "0" + startDay;
                    }
                    if (endMonth.length() == 1) {
                        endMonth = "0" +endMonth;
                    }
                    if (endDay.length() == 1) {
                        endDay = "0" + endDay;
                    }

                    initDate();
                }
                break;

            case ResultRequestCode.REQUEST_PAYMENT_PICKER:
                if (resultCode == ResultRequestCode.RESULT_PAYMENT_PICKER) {
                    Bundle bundle = data.getExtras();
                    paymentFinder = (PaymentFinder) bundle.getSerializable("paymentFinder");
                    initPayment();
                }
                break;

            case ResultRequestCode.REQUEST_FINDER_PICKER:
                if (resultCode == ResultRequestCode.RESULT_TYPE_PICKER) {
                    typeFinder = (TypeFinder) data.getExtras().getSerializable("finder");
                    initType();
                }else if (resultCode == ResultRequestCode.RESULT_MEMBER_PICKER) {
                    memberFinder = (MemberFinder) data.getExtras().getSerializable("finder");
                    initMember();
                }else if (resultCode == ResultRequestCode.RESULT_TRADING_WAY_PICKER) {
                    tradingWayFinder = (TradingWayFinder) data.getExtras().getSerializable("finder");
                    initTradingWay();
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                Intent intent1 = new Intent(BookFindActivity.this, DateDoublePickerDialog.class);
                Bundle bundle = new Bundle();
                bundle.putInt("request", ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER);
                intent1.putExtras(bundle);
                startActivityForResult(intent1, ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER);
                break;

            case R.id.tv_payment:
                Intent intent2 = new Intent(BookFindActivity.this, PaymentPickerDialog.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("paymentFinder", paymentFinder);
                intent2.putExtras(bundle1);
                startActivityForResult(intent2, ResultRequestCode.REQUEST_PAYMENT_PICKER);
                break;

            case R.id.tv_type:
                if (typeFinder != null) {
                    Intent intent3 = new Intent(BookFindActivity.this, FinderDialog.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("finder", typeFinder);
                    bundle3.putInt("request", ResultRequestCode.RESULT_TYPE_PICKER);
                    intent3.putExtras(bundle3);
                    startActivityForResult(intent3, ResultRequestCode.REQUEST_FINDER_PICKER);
                }
                break;

            case R.id.tv_member:
                if (memberFinder != null) {
                    Intent intent3 = new Intent(BookFindActivity.this, FinderDialog.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("finder", memberFinder);
                    bundle3.putInt("request", ResultRequestCode.RESULT_MEMBER_PICKER);
                    intent3.putExtras(bundle3);
                    startActivityForResult(intent3, ResultRequestCode.REQUEST_FINDER_PICKER);
                }
                break;

            case R.id.tv_trading_way:
                if (tradingWayFinder != null) {
                    Intent intent3 = new Intent(BookFindActivity.this, FinderDialog.class);
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("finder", tradingWayFinder);
                    bundle3.putInt("request", ResultRequestCode.RESULT_TRADING_WAY_PICKER);
                    intent3.putExtras(bundle3);
                    startActivityForResult(intent3, ResultRequestCode.REQUEST_FINDER_PICKER);
                }
                break;

            case R.id.btn_reset:
                reset();
                break;

            case R.id.btn_find:
                find();
                break;

            case R.id.img_back:
                finish();
                break;
        }
    }
}
