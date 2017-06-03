package stu.edu.cn.zing.personalbook.user_add_book;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.dialog.BookItemMemberDialog;
import stu.edu.cn.zing.personalbook.dialog.BookItemTradingWayDialog;
import stu.edu.cn.zing.personalbook.dialog.BookItemTyoeDialog;
import stu.edu.cn.zing.personalbook.dialog.DatePickerDialog;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemInfo;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemMember;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTradingWay;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.BookPayment;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;

/**
 * Created by Administrator on 2017/3/23.
 */

public class AddBookActivity extends BaseActivity implements View.OnClickListener, IAddBookView, CompoundButton.OnCheckedChangeListener{


    private EditText et_pay;
    private EditText et_itemtype;
    private EditText et_member;
    private EditText et_pay_way;
    private EditText et_tips;
    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_title;

    private Switch switch_pay_in_out;
    private TextView tv_pay;
    private TextView tv_pay_currency;

    private ImageView image_cancel;
    private Button btn_save;

    private AddBookPresenter addBookPresenter;

    private BookItemType bookItemType;
    private BookItemMember bookItemMember;
    private BookItemTradingWay bookItemTradingWay;
    private String remarks;

    private BookItem bookItem;
    private BookPayment bookPayment;
    private BookItemInfo bookItemInfo;
    private BookItemTitle bookItemTitle;


    private String year;
    private String month;
    private String day;
    private String week;

    private int request;

    private int bookPaymentType = 0;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_addbook);
    }

    @Override
    public void initViews() {
        et_pay = (EditText) findViewById(R.id.et_pay);
        et_itemtype = (EditText) findViewById(R.id.et_itemtype);
        et_member = (EditText) findViewById(R.id.et_member);
        et_pay_way = (EditText) findViewById(R.id.et_pay_way);
        et_tips = (EditText) findViewById(R.id.et_tips);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        switch_pay_in_out = (Switch) findViewById(R.id.switch_pay_in_out);
        tv_pay = (TextView) findViewById(R.id.tv_pay);
        tv_pay_currency = (TextView) findViewById(R.id.tv_pay_currency);

        image_cancel = (ImageView) findViewById(R.id.image_cancel);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    public void initListeners() {
        image_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        et_itemtype.setOnClickListener(this);
        et_member.setOnClickListener(this);
        et_pay_way.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);

        switch_pay_in_out.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        addBookPresenter = new AddBookPresenter(this);
        setResult(ResultRequestCode.RESULT_NO);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        request = bundle.getInt("request");

        switch (request) {
            case ResultRequestCode.REQUEST_BOOK_ITEM_MODIFY:
                tv_title.setText("修改账单");
                bookItem = (BookItem) bundle.getSerializable("bookItem");
                setItemTradingWay(bookItem.getItemInfo().getItemTradingWay());
                setItemType(bookItem.getItemInfo().getItemType());
                setItemMember(bookItem.getItemInfo().getItemMember());
                setItemPayment(bookItem.getPayment());
                year = formatDate(bookItem.getItemTitle().getYear());
                month = formatDate(bookItem.getItemTitle().getMonth());
                day = formatDate(bookItem.getItemTitle().getDay());
                week = "周" + bookItem.getItemTitle().getWeek();
                setDate();
                setRemarks(bookItem.getItemInfo().getRemarks());
                break;

            case ResultRequestCode.REQUEST_ADD_ITEM:
                tv_title.setText("添加账单");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-E HH:mm");
                String date = simpleDateFormat.format(new Date());
                String[] d = date.split(" ");
                tv_date.setText(d[0]);
                initSwitchFalse();
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_itemtype:
                showItemType();
                break;
            case R.id.et_member:
                showItemMember();
                break;
            case R.id.et_pay_way:
                showTradingWay();
                break;
            case R.id.tv_date:
                Intent intent1 = new Intent(this, DatePickerDialog.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("request", ResultRequestCode.REQUEST_DATE_PICKER);
                intent1.putExtras(bundle1);
                startActivityForResult(intent1, ResultRequestCode.REQUEST_DATE_PICKER);
                break;
            case R.id.tv_time:

                break;
            case R.id.image_cancel :
                finish();
                break;
            case R.id.btn_save :
                switch (request) {
                    case ResultRequestCode.REQUEST_BOOK_ITEM_MODIFY:
                        update();
                        break;

                    case ResultRequestCode.REQUEST_ADD_ITEM:
                        save();
                        break;
                }
                break;
        }
    }
    private void initSwitchFalse(){
        tv_pay.setTextColor(getColor(R.color.payment_in));
        tv_pay_currency.setTextColor(getColor(R.color.payment_in));
        et_pay.setTextColor(getColor(R.color.payment_in));
        et_pay.setHintTextColor(getColor(R.color.payment_in));
        tv_pay.setText("收入");
        bookPaymentType = BookPaymentType.TYPE_POSITIVE;

    }

    private void initSwitchTrue() {
        tv_pay.setTextColor(getColor(R.color.payment_out));
        tv_pay_currency.setTextColor(getColor(R.color.payment_out));
        et_pay.setTextColor(getColor(R.color.payment_out));
        et_pay.setHintTextColor(getColor(R.color.payment_out));
        tv_pay.setText("支出");
        bookPaymentType = BookPaymentType.TYPE_NEGATIVE;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ResultRequestCode.REQUEST_ITEM_TYPE:
                Log.i("pick_item_type","选择成功");
                Bundle bundle = data.getExtras();
                BookItemType bookItemType = (BookItemType) bundle.getSerializable("type");
                setItemType(bookItemType);
                break;

            case ResultRequestCode.REQUEST_ITEM_MEMBER:
                Bundle bundle1 = data.getExtras();
                BookItemMember bookItemMember = (BookItemMember) bundle1.getSerializable("member");
                setItemMember(bookItemMember);
                break;

            case ResultRequestCode.REQUEST_ITEM_TRADING_WAY:
                Bundle bundle2 = data.getExtras();
                BookItemTradingWay bookItemTradingWay = (BookItemTradingWay) bundle2.getSerializable("way");
                setItemTradingWay(bookItemTradingWay);
                break;

            case ResultRequestCode.REQUEST_DATE_PICKER:
                if (resultCode == ResultRequestCode.RESULT_NO) return;
                if (resultCode == ResultRequestCode.RESULT_DATE_PICKER) {
                    Bundle bundle3 = data.getExtras();
                    year = bundle3.getString("year");
                    month = bundle3.getString("month");
                    day = bundle3.getString("day");
                    week = bundle3.getString("week");
                    setDate();
                }
                break;
        }
    }

    private void update() {
        if (TextUtils.isEmpty(et_pay.getText().toString())) {
            Toast.makeText(this, "价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        addBookPresenter.updateBookItem(bookItem);

    }


    public void save() {
        if (TextUtils.isEmpty(et_pay.getText().toString())) {
            Toast.makeText(this, "价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        getBookItemInfo();
        getBookPayment();
        getBookItemTitle();
        addBookPresenter.SaveBookItem(bookItemTitle, bookPayment, bookItemInfo);
    }

    private void getBookItemInfo(){
        bookItemInfo = new BookItemInfo();
        bookItemInfo.setItemMember(bookItemMember);
        bookItemInfo.setItemTradingWay(bookItemTradingWay);
        bookItemInfo.setItemType(bookItemType);
        remarks = et_tips.getText().toString();
        bookItemInfo.setRemarks(remarks);

    }

    private void getBookPayment(){
        bookPayment = new BookPayment();
        bookPayment.setPay(Float.valueOf(et_pay.getText().toString()));

        bookPayment.setPayType(bookPaymentType);

    }


    private void getBookItemTitle() {
        bookItemTitle = new BookItemTitle();

        String date = tv_date.getText().toString();
        String[] d = date.split("-");
        String year = d[0];
        String month = d[1];
        String day = d[2];
        String week = d[3];
        bookItemTitle.setYear(Integer.valueOf(year));
        bookItemTitle.setWeek(week.substring(week.length()-1));
        bookItemTitle.setMonth(formatDate(month));
        bookItemTitle.setDay(formatDate(day));

        bookItemTitle.setBook(Book.getCurrentBook());
        Log.i("currentDate",bookItemTitle.getDay() + bookItemTitle.getMonth() + bookItemTitle.getYear() + bookItemTitle.getWeek());

    }

    private int formatDate(String d) {
        if (d.substring(0,1) .equals("0")) {
            return Integer.valueOf(d.substring(1));
        } else {
            return Integer.valueOf(d);
        }
    }

    private String formatDate(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }

    private void setRemarks(String remarks) {
        et_tips.setText(remarks);
    }


    private void setDate() {
        tv_date.setText(year + "-" + month + "-" + day + "-" + week );
    }
    private void setItemPayment(BookPayment bookPayment) {
        this.bookPayment = bookPayment;
        if (bookPayment.getPayType() == BookPaymentType.TYPE_POSITIVE) {
            initSwitchFalse();
            switch_pay_in_out.setChecked(false);
        } else {
            initSwitchTrue();
            switch_pay_in_out.setChecked(true);
        }
        et_pay.setText(bookPayment.getPay() + "");

    }


    private void setItemType(BookItemType bookItemType) {
        this.bookItemType = bookItemType;
        if (bookItemType != null) {
            et_itemtype.setText(bookItemType.getType());
        }
    }

    private void setItemMember(BookItemMember bookItemMember) {
        this.bookItemMember = bookItemMember;
        if (bookItemMember != null) {
            et_member.setText(bookItemMember.getItemMember());
        }
    }

    private void setItemTradingWay(BookItemTradingWay bookItemTradingWay) {
        this.bookItemTradingWay = bookItemTradingWay;
        if (bookItemTradingWay != null) {
            et_pay_way.setText(bookItemTradingWay.getItemTradingWay());
        }
    }


    private void showItemType() {
        Intent intent1 = new Intent(new Intent(AddBookActivity.this, BookItemTyoeDialog.class));
        startActivityForResult(intent1, ResultRequestCode.REQUEST_ITEM_TYPE);
    }

    private void showItemMember() {
        Intent intent1 = new Intent(new Intent(AddBookActivity.this, BookItemMemberDialog.class));
        startActivityForResult(intent1, ResultRequestCode.REQUEST_ITEM_MEMBER);
    }

    private void showTradingWay() {
        Intent intent1 = new Intent(new Intent(AddBookActivity.this, BookItemTradingWayDialog.class));
        startActivityForResult(intent1, ResultRequestCode.REQUEST_ITEM_TRADING_WAY);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //选中
            initSwitchTrue();
        } else {
            //未选中
            initSwitchFalse();
        }
    }

    @Override
    public void saveBookItemSuccess() {
        setResult(ResultRequestCode.RESULT_ADD_ITEM_SUCCESS);
        finish();
    }

    @Override
    public void updateBookItemSuccess() {
        setResult(ResultRequestCode.RESULT_BOOK_ITEM_MODIFY);
        finish();
    }
}
