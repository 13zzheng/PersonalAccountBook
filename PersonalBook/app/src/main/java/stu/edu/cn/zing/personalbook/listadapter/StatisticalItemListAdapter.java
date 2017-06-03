package stu.edu.cn.zing.personalbook.listadapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import stu.edu.cn.zing.personalbook.PieChartColor;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.model.StatisticalItem;
import stu.edu.cn.zing.personalbook.mywidget.HorizaontalProgressBar;

/**
 * Created by Administrator on 2017/5/7.
 */

public class StatisticalItemListAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater layoutInflater;
    private List<StatisticalItem> statisticalItemList;
    private int progressColor = Color.BLACK;
    private PieChartColor pieChartColor;

    public StatisticalItemListAdapter(Context context, List<StatisticalItem> statisticalItemList) {
        this.context = context;
        this.statisticalItemList = statisticalItemList;
        layoutInflater = layoutInflater.from(context);
        pieChartColor = new PieChartColor(context);
    }
    @Override
    public int getCount() {
        return statisticalItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return statisticalItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_statistical_item, parent, false);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            holder.tv_payment = (TextView) convertView.findViewById(R.id.tv_payment);
            holder.tv_precentage = (TextView) convertView.findViewById(R.id.tv_precentage);
            holder.progress_payment = (HorizaontalProgressBar) convertView.findViewById(R.id.progress_payment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StatisticalItem statisticalItem = (StatisticalItem) getItem(position);
        holder.tv_name.setText(statisticalItem.getName());
        holder.tv_number.setText(statisticalItem.getSize() + "笔");
        holder.tv_payment.setText("¥" + statisticalItem.getTotalPayment());
        holder.tv_precentage.setText(String.valueOf(statisticalItem.getPercentage()).substring(0,4) + "%");

        holder.progress_payment.setProgress(statisticalItem.getPercentage());
        if (progressColor == context.getColor(R.color.report_positive)) {
            holder.progress_payment.setColor(pieChartColor.getInputColor(position));
        } else {
            holder.progress_payment.setColor(pieChartColor.getOutputColor(position));
        }
        holder.progress_payment.invalidate();
        return convertView;
    }


    static class ViewHolder{
        TextView tv_name;
        TextView tv_number;
        TextView tv_payment;
        TextView tv_precentage;
        HorizaontalProgressBar progress_payment;
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
    }


}
