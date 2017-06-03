package stu.edu.cn.zing.personalbook.HomeFragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import stu.edu.cn.zing.personalbook.MonthComparedActivity;
import stu.edu.cn.zing.personalbook.PieChartColor;
import stu.edu.cn.zing.personalbook.dialog.DateDoublePickerDialog;
import stu.edu.cn.zing.personalbook.dialog.DatePickerDialog;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.dialog.StatisticalTypePickerDialog;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;
import stu.edu.cn.zing.personalbook.listadapter.StatisticalItemListAdapter;
import stu.edu.cn.zing.personalbook.model.StatisticalItem;
import stu.edu.cn.zing.personalbook.model.StatisticalType;
import stu.edu.cn.zing.personalbook.mywidget.HorizaontalProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticalFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "tag";

    // TODO: Rename and change types of parameters
    private String tag;

    private OnFragmentInteractionListener mListener;

    private Context context;

    private String startYear;
    private String startMonth;
    private String startDay;
    private String endYear;
    private String endMonth;
    private String endDay;

    private PieChartView pie_input;
    private PieChartView pie_output;

    private PieChartData pieInputData;
    private PieChartData pieOutputData;


    private float totalInput;
    private float totalOutput;
    private float overage;

    private TextView tv_date;
    private TextView tv_type;
    private TextView tv_input_value;
    private TextView tv_output_value;
    private TextView tv_overage_value;
    private TextView tv_report;

    private ImageView img_line_chart;


    private HorizaontalProgressBar progress_input;
    private HorizaontalProgressBar progress_output;
    private HorizaontalProgressBar progress_overage;

    private ListView lv_input_statistical_item_list;
    private ListView lv_output_statistical_item_list;

    private LinearLayout ll_no_input_data;
    private LinearLayout ll_no_output_data;
    private LinearLayout ll_input_data;
    private LinearLayout ll_output_data;

    private int statisticalType;

    private List<StatisticalItem>  inputStatisticalItemList;
    private List<StatisticalItem>  outputStatisticalItemList;

    private static  List<BookItem> inputItemList;



    public StatisticalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tag Parameter 1.
     *
     * @return A new instance of fragment StatisticalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticalFragment newInstance(String tag) {
        StatisticalFragment fragment = new StatisticalFragment();
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
        View v = inflater.inflate(R.layout.fragment_statistical, container, false);
        init(v);
        return v;
    }


    private void init(View view) {
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        tv_input_value = (TextView) view.findViewById(R.id.tv_input_value);
        tv_output_value = (TextView) view.findViewById(R.id.tv_output_value);
        tv_overage_value = (TextView) view.findViewById(R.id.tv_overage_value);
        tv_report = (TextView) view.findViewById(R.id.tv_report);
        progress_input = (HorizaontalProgressBar) view.findViewById(R.id.progress_input);
        progress_output = (HorizaontalProgressBar) view.findViewById(R.id.progress_ouput);
        progress_overage = (HorizaontalProgressBar) view.findViewById(R.id.progress_overage);
        lv_input_statistical_item_list = (ListView) view.findViewById(R.id.lv_input_statistical_item_list);
        lv_output_statistical_item_list = (ListView) view.findViewById(R.id.lv_output_statistical_item_list);
        ll_no_input_data = (LinearLayout) view.findViewById(R.id.fl_no_input_data);
        ll_no_output_data = (LinearLayout) view.findViewById(R.id.fl_no_output_data);
        ll_input_data = (LinearLayout) view.findViewById(R.id.ll_input_data);
        ll_output_data = (LinearLayout) view.findViewById(R.id.ll_output_data);

        img_line_chart = (ImageView) view.findViewById(R.id.img_line_chart);

        pie_input = (PieChartView) view.findViewById(R.id.pie_input);
        pie_output = (PieChartView) view.findViewById(R.id.pie_output);


        tv_date.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        tv_report.setOnClickListener(this);
        img_line_chart.setOnClickListener(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        String[] d = date.split("-");
        startYear = endYear = d[0];
        startMonth = endMonth = d[1];
        startDay = "01";
        endDay = d[2];

        initDate();

        statisticalType = StatisticalType.TYPE;
        initType();

        initData();

    }

    private void initPieInput() {
        pieInputData = new PieChartData();

        Log.i("PieInput","开始构建收入饼图");
        List<SliceValue> values = new ArrayList<>();
        int size;
        if (inputStatisticalItemList.size() < 10) {
            size = inputStatisticalItemList.size();
        } else {
            size = 10;
        }

        PieChartColor pieChartColor = new PieChartColor(context);

        Log.i("PieInput","开始设置数据 size:" + size);
        for (int i = 0; i < size; i++) {
            Log.i("PieInput","颜色" + pieChartColor.getInputColor(0));
            SliceValue sliceValue = new SliceValue(inputStatisticalItemList.get(i).getPercentage(), pieChartColor.getInputColor(i));
            values.add(sliceValue);
        }
        Log.i("PieInput","设置数据完成");

        pieInputData.setHasLabels(true);//显示表情
        pieInputData.setHasLabelsOnlyForSelected(true);//不用点击显示占的百分比
        pieInputData.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        pieInputData.setHasCenterCircle(true);//是否是环形显示
        pieInputData.setValues(values);//填充数据
        pieInputData.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieInputData.setCenterCircleScale(0.7f);//设置环形的大小级别
        pieInputData.setCenterText1("收入图");//环形中间的文字1
        pieInputData.setCenterText1Color(Color.BLACK);//文字颜色
        pieInputData.setCenterText1FontSize(20);//文字大小

        pie_input.setPieChartData(pieInputData);
        pie_input.setValueSelectionEnabled(true);//选择饼图某一块变大
        pie_input.setAlpha(0.9f);//设置透明度
        pie_input.setCircleFillRatio(1f);
        Log.i("PieInput","构建收入饼图完成");

    }

    private void initPieOutput() {
        pieOutputData = new PieChartData();

        Log.i("PieOutput","开始构建收入饼图");
        List<SliceValue> values = new ArrayList<>();
        int size;
        if (outputStatisticalItemList.size() < 10) {
            size = outputStatisticalItemList.size();
        } else {
            size = 10;
        }

        PieChartColor pieChartColor = new PieChartColor(context);

        Log.i("PieOutput","开始设置数据 size:" + size);
        for (int i = 0; i < size; i++) {
            SliceValue sliceValue = new SliceValue(outputStatisticalItemList.get(i).getPercentage(), pieChartColor.getOutputColor(i));
            values.add(sliceValue);
        }
        Log.i("PieOutput","设置数据完成");

        pieOutputData.setHasLabels(true);//显示表情
        pieOutputData.setHasLabelsOnlyForSelected(true);//不用点击显示占的百分比
        pieOutputData.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        pieOutputData.setHasCenterCircle(true);//是否是环形显示
        pieOutputData.setValues(values);//填充数据
        pieOutputData.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieOutputData.setCenterCircleScale(0.7f);//设置环形的大小级别
        pieOutputData.setCenterText1("支出图");//环形中间的文字1
        pieOutputData.setCenterText1Color(Color.BLACK);//文字颜色
        pieOutputData.setCenterText1FontSize(20);//文字大小

        pie_output.setPieChartData(pieOutputData);
        pie_output.setValueSelectionEnabled(true);//选择饼图某一块变大
        pie_output.setAlpha(0.9f);//设置透明度
        pie_output.setCircleFillRatio(1f);
        Log.i("PieOutput","构建支出饼图完成");
    }

    private void initData() {
        inputStatisticalItemList = new ArrayList<>();
        outputStatisticalItemList = new ArrayList<>();
        Log.i("StatisticalItem","开始加载数据");
        BmobQuery<BookItemTitle> titleQuery = new BmobQuery<>();

        titleQuery.addWhereGreaterThanOrEqualTo("year", Integer.valueOf(startYear));
        titleQuery.addWhereLessThanOrEqualTo("year", Integer.valueOf(endYear));
        titleQuery.addWhereGreaterThanOrEqualTo("month", formatDate(startMonth));
        titleQuery.addWhereLessThanOrEqualTo("month", formatDate(endMonth));
        titleQuery.addWhereGreaterThanOrEqualTo("day", formatDate(startDay));
        titleQuery.addWhereLessThanOrEqualTo("day", formatDate(endDay));
        titleQuery.addWhereEqualTo("book",Book.getCurrentBook());
        Log.i("EndDay",formatDate(startDay) + "");

        boolean isCache = titleQuery.hasCachedResult(BookItemTitle.class);
        if(isCache){
            titleQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            titleQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ONLY
        }

        titleQuery.findObjects(new FindListener<BookItemTitle>() {
            @Override
            public void done(List<BookItemTitle> list, BmobException e) {
                if (e == null) {

                    Log.i("StatisticalItem","获取title size:" + list.size());
                    List<String> tilteId = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        tilteId.add(list.get(i).getObjectId());
                    }

                    BmobQuery<BookItemTitle> q1 = new BmobQuery<>();

                    q1.addWhereContainedIn("objectId", tilteId);

                    BmobQuery<BookItem> itemQuery = new BmobQuery<>();
                    itemQuery.addWhereMatchesQuery("itemTitle", "BookItemTitle", q1);
                    itemQuery.include("payment,itemTitle,itemInfo.itemType,itemInfo.itemTradingWay,itemInfo.itemMember");
                    itemQuery.findObjects(new FindListener<BookItem>() {
                        @Override
                        public void done(List<BookItem> list, BmobException e) {
                            if (e == null) {
                                Log.i("StatisticalItem","获取item size:" + list.size());
                                List<BookItem> inputItems = new ArrayList<>();
                                List<BookItem> outputItems = new ArrayList<>();
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getPayment().getPayType() == BookPaymentType.TYPE_POSITIVE) {
                                        inputItems.add(list.get(i));
                                    } else if (list.get(i).getPayment().getPayType() == BookPaymentType.TYPE_NEGATIVE) {
                                        outputItems.add(list.get(i));
                                    }
                                }
                                inputItemList = inputItems;
                                initInputStatisticalItemList(inputItems);
                                initOutputStatisticalItemList(outputItems);

                                initTitle();

                                initPieInput();
                                initPieOutput();

                                initInputListView();
                                initOutputListView();
                            }
                        }
                    });
                }
            }
        });
    }
    private int formatDate(String d) {
        if (d.substring(0,1) == "0") {
            return Integer.valueOf(d.substring(1));
        } else {
            return Integer.valueOf(d);
        }
    }

    private void initInputStatisticalItemList(List<BookItem> bookItemList) {
        Log.i("StatisticalItem","开始获取inputStatisticalItemList");

        totalInput = 0;
        while (bookItemList.size() != 0) {
            int size = bookItemList.size();
            String name = getNameByType(statisticalType, bookItemList, 0);
            Log.i("StatisticalItem","统计方式：" + name);
            StatisticalItem statisticalItems = new StatisticalItem();
            List<BookItem> bookItems = new ArrayList<>();
            List<Integer> removes = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (name.equals(getNameByType(statisticalType, bookItemList, i))) {
                    bookItems.add(bookItemList.get(i));
                    removes.add(i);
                }
            }
            for (int i = 0; i < removes.size(); i++) {
                bookItemList.remove(removes.get(i) - i);
                //Log.i("StatisticalItem","RemoveBookItemList 剩余 :" + bookItemList.size());
            }
            statisticalItems.init(bookItems);
            statisticalItems.setName(name);
            inputStatisticalItemList.add(statisticalItems);
        }
        Log.i("StatisticalItem","获取inputStatisticalItemList完成 size:" + inputStatisticalItemList.size());


        Collections.sort(inputStatisticalItemList);



        for (int i = 0; i < inputStatisticalItemList.size(); i++) {
            totalInput += inputStatisticalItemList.get(i).getTotalPayment();
        }

        for (int i = 0; i < inputStatisticalItemList.size(); i++) {
            float payment = inputStatisticalItemList.get(i).getTotalPayment();
            inputStatisticalItemList.get(i).setPercentage(payment/totalInput * 100);
        }

    }

    private void initOutputStatisticalItemList(List<BookItem> bookItemList) {
        Log.i("StatisticalItem","开始获取outputStatisticalItemList");

        totalOutput = 0;
        while (bookItemList.size() != 0) {
            int size = bookItemList.size();
            String name = getNameByType(statisticalType, bookItemList, 0);
            StatisticalItem statisticalItems = new StatisticalItem();
            List<BookItem> bookItems = new ArrayList<>();
            List<Integer> removes = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (name.equals(getNameByType(statisticalType, bookItemList, i))) {
                    bookItems.add(bookItemList.get(i));
                    removes.add(i);
                }
            }
            for (int i = 0; i < removes.size(); i++) {
                bookItemList.remove( removes.get(i) - i);
                //Log.i("StatisticalItem","RemoveBookItemList 剩余 :" + bookItemList.size());
            }
            statisticalItems.init(bookItems);
            statisticalItems.setName(name);
            outputStatisticalItemList.add(statisticalItems);
        }
        Log.i("StatisticalItem","获取outputStatisticalItemList完成 size:" + outputStatisticalItemList.size());
        if (outputStatisticalItemList.size() > 1) {
            Collections.sort(outputStatisticalItemList);
        }


        for (int i = 0; i < outputStatisticalItemList.size(); i++) {
            totalOutput += outputStatisticalItemList.get(i).getTotalPayment();
        }

        for (int i = 0; i < outputStatisticalItemList.size(); i++) {
            float payment = outputStatisticalItemList.get(i).getTotalPayment();
            outputStatisticalItemList.get(i).setPercentage(payment/totalOutput * 100);
        }
    }

    private String getNameByType(int type, List<BookItem> bookItemList, int pos) {
        switch (type) {
            case StatisticalType.TYPE:
                return bookItemList.get(pos).getItemInfo().getItemType().getType();


            case StatisticalType.MEMBER:
                return bookItemList.get(pos).getItemInfo().getItemMember().getItemMember();


            case StatisticalType.TRADING_WAY:
                return bookItemList.get(pos).getItemInfo().getItemTradingWay().getItemTradingWay();

        }

        return bookItemList.get(pos).getItemInfo().getItemType().getType();
    }

    private void initTitle() {
        tv_input_value.setText(String.valueOf(totalInput));
        Log.i("input","" + totalInput);
        tv_output_value.setText(String.valueOf(totalOutput));
        overage = totalInput - totalOutput;
        tv_overage_value.setText(String.valueOf(overage));

        float max = Math.max(totalInput, totalOutput);
        if (totalInput > 0) {
            progress_input.setProgress(totalInput / max * 100);
            progress_input.requestLayout();
        }
        Log.i("max",totalInput / max * 100+ "");
        if (totalOutput > 0) {
            progress_output.setProgress(totalOutput / max * 100);
            progress_output.requestLayout();
        }
        if (overage > 0) {
            progress_overage.setProgress(overage / max * 100);
            progress_overage.requestLayout();
        }

    }

    private void initInputListView() {

        if (inputStatisticalItemList.size() > 0) {
            StatisticalItemListAdapter adapter = new StatisticalItemListAdapter(context, inputStatisticalItemList);
            adapter.setProgressColor(getResources().getColor(R.color.payment_in, null));
            lv_input_statistical_item_list.setAdapter(adapter);
            setListViewHeight(lv_input_statistical_item_list);
            setInputData();
        } else {
            setInputNoData();
        }
    }

    private void initOutputListView() {
        if (outputStatisticalItemList.size() > 0) {
            StatisticalItemListAdapter adapter = new StatisticalItemListAdapter(context, outputStatisticalItemList);
            adapter.setProgressColor(getResources().getColor(R.color.payment_out, null));
            lv_output_statistical_item_list.setAdapter(adapter);
            setListViewHeight(lv_output_statistical_item_list);
            setOutputData();
        } else {
            setOutputNoData();
        }
    }

    private void setInputNoData() {
        ll_no_input_data.setVisibility(View.VISIBLE);
        ll_input_data.setVisibility(View.GONE);
    }

    private void setInputData() {
        ll_no_input_data.setVisibility(View.GONE);
        ll_input_data.setVisibility(View.VISIBLE);
    }

    private void setOutputNoData() {
        ll_no_output_data.setVisibility(View.VISIBLE);
        ll_output_data.setVisibility(View.GONE);
    }

    private void setOutputData() {
        ll_no_output_data.setVisibility(View.GONE);
        ll_output_data.setVisibility(View.VISIBLE);
    }

    /*
    由于ListView在ScrollView控件下，无法计算高度只显示一行，需要我们手动计算子View的高度
     */
    public void setListViewHeight(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);

    }





    private void initDate() {
        tv_date.setText(startYear + "年" + startMonth + "月" + startDay + "日" + "-" +
                endYear + "年" + endMonth + "月" + endDay + "日");
    }

    private void initType() {
        switch (statisticalType) {
            case StatisticalType.TYPE:
                tv_type.setText("按照类别");
                break;
            case StatisticalType.MEMBER:
                tv_type.setText("按照账单成员");
                break;
            case StatisticalType.TRADING_WAY:
                tv_type.setText("按照交易方式");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER:
                if (resultCode == ResultRequestCode.RESULT_NO) return;
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
                    initData();
                }
                break;

            case ResultRequestCode.REQUEST_STATISTICAL_TYPE_PICKER:
                if (resultCode == ResultRequestCode.RESULT_NO) return;
                if (resultCode == ResultRequestCode.RESULT_STATISTICAL_TYPE_PICKER) {
                    Bundle bundle = data.getExtras();
                    statisticalType = bundle.getInt("type");
                    initType();

                    initData();
                }
                break;


        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                Log.i("size","" +inputItemList.size());
                Intent intent1 = new Intent(context, DateDoublePickerDialog.class);
                Bundle bundle = new Bundle();
                bundle.putInt("request", ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER);
                intent1.putExtras(bundle);
                startActivityForResult(intent1, ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER);
                break;

            case R.id.tv_type:
                Intent intent2 = new Intent(context, StatisticalTypePickerDialog.class);
                startActivityForResult(intent2, ResultRequestCode.REQUEST_STATISTICAL_TYPE_PICKER);
                break;

            case R.id.tv_report:
                Intent intent3 = new Intent(context, DatePickerDialog.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("request", ResultRequestCode.REQUEST_DATE_PICKER_WITH_REPORT);
                intent3.putExtras(bundle2);
                startActivityForResult(intent3, ResultRequestCode.REQUEST_DATE_PICKER_WITH_REPORT);
                break;
            case R.id.img_line_chart:
                startActivity(new Intent(context, MonthComparedActivity.class));
                break;
        }
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
