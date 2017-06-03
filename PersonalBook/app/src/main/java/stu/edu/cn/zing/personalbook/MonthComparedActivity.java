package stu.edu.cn.zing.personalbook;

import android.content.Intent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.BookMonthFinancial;
import stu.edu.cn.zing.personalbook.dialog.YearPickerDialog;

public class MonthComparedActivity extends BaseActivity implements View.OnClickListener{

    private LineChartView line_chart;

    private ListView list_view;

    private TextView tv_year;
    private TextView tv_no_data;

    private List<BookMonthFinancial> bookMonthFinancialList;

    private int year;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_month_compared);
    }

    @Override
    public void initViews() {
        line_chart = (LineChartView) findViewById(R.id.line_chart);
        list_view = (ListView) findViewById(R.id.list_view);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_no_data = (TextView) findViewById(R.id.tv_no_data);
    }

    @Override
    public void initListeners() {
        tv_year.setOnClickListener(this);
    }

    @Override
    public void initData() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String y = format.format(new Date());
        year = Integer.valueOf(y);

        getMonthList();
    }

    private void showData() {
        tv_no_data.setVisibility(View.GONE);
        list_view.setVisibility(View.VISIBLE);
    }

    private void showNoData() {
        tv_no_data.setVisibility(View.VISIBLE);
        list_view.setVisibility(View.GONE);
    }

    private void initLineView() {
        line_chart.setZoomEnabled(true); //设置是否能够缩放
        line_chart.setInteractive(true); //设置图标是否能够与用户互动
        line_chart.setValueSelectionEnabled(true);

        List<PointValue> pointValues = new ArrayList<PointValue>();// 节点数据结合
        Axis axisY = new Axis().setHasLines(true);// Y轴属性
        Axis axisX = new Axis();// X轴属性
        axisY.setName("结余");
        axisX.setName("月份");
        ArrayList<AxisValue> axisValuesX = new ArrayList<>();//定义X轴刻度值的数据集合
        ArrayList<AxisValue> axisValuesY = new ArrayList<>();//定义Y轴刻度值的数据集合
        for (int i = 0; i < bookMonthFinancialList.size(); i++) {
            //输入数据
            BookMonthFinancial bookMonth = bookMonthFinancialList.get(i);
            pointValues.add(new PointValue(i + 1, bookMonth.getMonthInput() - bookMonth.getMonthOutput()));

            axisValuesX.add(new AxisValue(i + 1).setValue(bookMonth.getMonth()));
            axisValuesY.add(new AxisValue(i + 1).setValue(bookMonth.getMonthInput() - bookMonth.getMonthOutput()));
        }

        axisX.setTextColor(getColor(R.color.primary_text));
        axisY.setTextColor(getColor(R.color.primary_text));

        axisX.setValues(axisValuesX);
        axisY.setValues(axisValuesY);

        List<Line> lines = new ArrayList<>();//定义线的集合
        Line line = new Line(pointValues);//将值设置给折线
        line.setShape(ValueShape.CIRCLE); //设置节点圆形
        line.setHasLabelsOnlyForSelected(true);
        line.setCubic(true);
        line.setColor(getColor(R.color.colorPrimaryLight));

        lines.add(line);

        LineChartData chartData;
        chartData = new LineChartData(lines);//将线的集合设置为折线图的数据
        chartData.setAxisYLeft(axisY);// 将Y轴属性设置到左边
        chartData.setAxisXBottom(axisX);// 将X轴属性设置到底部
        chartData.setBaseValue(20);

        line_chart.setLineChartData(chartData);
    }

    private void initListView() {
        List<Map<String, Object>> listems = new ArrayList<>();
        for (int i = 0; i < bookMonthFinancialList.size(); i++) {
            Map<String, Object> listem = new HashMap<>();
            float input = bookMonthFinancialList.get(i).getMonthInput();
            float output = bookMonthFinancialList.get(i).getMonthOutput();
            listem.put("month", year + "年" + bookMonthFinancialList.get(i).getMonth() + "月");
            listem.put("input", input + "");
            listem.put("output", output + "");
            listem.put("average", (input - output) + "");

            listems.add(listem);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listems, R.layout.list_month_compared,
                new String[] {"month","input","output","average"}, new int[]{R.id.tv_month, R.id.tv_input, R.id.tv_output, R.id.tv_average});
        list_view.setAdapter(adapter);
    }

    private void getMonthList() {
        bookMonthFinancialList = new ArrayList<>();
        BmobQuery<BookMonthFinancial> query = new BmobQuery<>();
        query.addWhereEqualTo("year", year);
        query.addWhereEqualTo("book", Book.getCurrentBook());
        query.order("month");
        query.findObjects(new FindListener<BookMonthFinancial>() {
            @Override
            public void done(List<BookMonthFinancial> list, BmobException e) {
                if (e == null) {
                    Log.i("list",list.size() + "");
                    if (list.size() == 0) {
                        showNoData();
                    } else {
                        showData();
                    }
                    int j = 0;
                    for (int i = 1; i <= 12; i++) {

                        if (list.size() > j && i == list.get(j).getMonth()) {
                            bookMonthFinancialList.add(list.get(j));
                            j++;

                        } else {
                            BookMonthFinancial b = new BookMonthFinancial();
                            b.setYear(year);
                            b.setMonth(i);
                            b.setMonthInput(0f);
                            b.setMonthOutput(0f);
                            bookMonthFinancialList.add(b);
                        }
                    }

                    for (int i = 0; i < bookMonthFinancialList.size(); i++) {
                        BookMonthFinancial bookMonth = bookMonthFinancialList.get(i);
                        Log.i("Month", bookMonth.getMonth() + "  " + (bookMonth.getMonthInput() - bookMonth.getMonthOutput()));
                    }
                    initLineView();
                    initListView();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) return;
        switch (requestCode) {
            case ResultRequestCode.REQUEST_YEAR_PICKER:
                if (resultCode == ResultRequestCode.RESULT_YEAR_PICKER) {
                    year= data.getExtras().getInt("year");
                    getMonthList();
                    tv_year.setText(year + "年");
                }

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_year:
                Intent intent = new Intent(this, YearPickerDialog.class);
                Bundle bundle = new Bundle();
                bundle.putInt("year", year);
                intent.putExtras(bundle);
                startActivityForResult(intent, ResultRequestCode.REQUEST_YEAR_PICKER);
                break;
        }
    }
}
