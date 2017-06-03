package stu.edu.cn.zing.personalbook.listadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import stu.edu.cn.zing.personalbook.BookItems;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.dialog.BookItemDeleteDialog;
import stu.edu.cn.zing.personalbook.user_add_book.AddBookActivity;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BookListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<BookItems> bookItemsList;

    private ViewGroup viewGroup;

    public BookListAdapter(Context context, List<BookItems> bookItemsList) {
        this.context = context;
        this.bookItemsList = bookItemsList;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return bookItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        viewGroup = parent;
        final int pos = position;
        if (convertView ==null) {
            convertView = layoutInflater.inflate(R.layout.list_book, parent, false);
            holder = new ViewHolder();
            holder.tvMonthDay = (TextView) convertView.findViewById(R.id.tvMonthDay);
            holder.tvYear = (TextView) convertView.findViewById(R.id.tvYear);
            holder.tvTotalPayment = (TextView) convertView.findViewById(R.id.tvTotalPayment);
            holder.tvWeek = (TextView) convertView.findViewById(R.id.tvWeek);
            holder.lvBookTitle = (ListView) convertView.findViewById(R.id.lvBookTitle);
            holder.titleLayout = (LinearLayout) convertView.findViewById(R.id.titleLayout);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BookItems bookItems = (BookItems) getItem(position);


        BookItemTitle title = bookItems.getTitle();

        holder.tvWeek.setText(title.getWeek());

        holder.tvMonthDay.setText(title.getMonth()+"月"+title.getDay()+"日");
        holder.tvYear.setText(title.getYear() + "年");
        float pay = title.getTotalPay();
        if (pay >= 0) {
            //Pay为正数
            holder. tvTotalPayment.setTextColor(context.getResources().getColor(R.color.payment_in, null));
        } else if (pay < 0) {
            //Pay为负数
            holder.tvTotalPayment.setTextColor(context.getResources().getColor(R.color.payment_out, null));
        }
        holder.tvTotalPayment.setText("¥"+String.valueOf(Math.abs(pay)));



        BookTitleListAdapter bookTitleListAdapter = new BookTitleListAdapter(context,bookItems);
        holder.lvBookTitle.setAdapter(bookTitleListAdapter);
        bookTitleListAdapter.notifyDataSetChanged();
        setListViewHeight(holder.lvBookTitle);
        holder.lvBookTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookItem bookItem = (BookItem) parent.getAdapter().getItem(position);
                Log.i("BookItem",bookItem.getPayment().getPay() + "");
                Intent intent = new Intent(context, AddBookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bookItem", bookItem);
                bundle.putInt("request", ResultRequestCode.REQUEST_BOOK_ITEM_MODIFY);
                intent.putExtras(bundle);
                ((Activity)context).startActivityForResult(intent, ResultRequestCode.REQUEST_BOOK_ITEM_MODIFY);
            }
        });

        holder.lvBookTitle.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BookItem bookItem = (BookItem) parent.getAdapter().getItem(position);
                Intent intent = new Intent(context, BookItemDeleteDialog.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bookItem", bookItem);
                intent.putExtras(bundle);
                ((Activity)context).startActivityForResult(intent, ResultRequestCode.REQUEST_BOOK_ITEM_DELETE);
                return false;
            }
        });

        holder.titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookItems.setOpen(!bookItems.isOpen());
                if (!bookItems.isOpen()) {
                    holder.tvWeek.setTextSize(12);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.tvWeek.getLayoutParams();
                    layoutParams.height = dip2px(context, 24);
                    layoutParams.width = dip2px(context, 24);
                    holder.tvWeek.setLayoutParams(layoutParams);
                } else {
                    holder.tvWeek.setTextSize(16);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.tvWeek.getLayoutParams();
                    layoutParams.height = dip2px(context, 32);
                    layoutParams.width = dip2px(context, 32);
                    holder.tvWeek.setLayoutParams(layoutParams);
                }
                update(viewGroup, pos);
            }
        });
        return convertView;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    static class ViewHolder{
        TextView tvMonthDay;
        TextView tvYear;
        TextView tvTotalPayment;
        TextView tvWeek;
        ListView lvBookTitle;
        LinearLayout titleLayout;
    }

    public void update(ViewGroup viewGroup, int index){
        View view = viewGroup.getChildAt(index);
        ViewHolder holder = (ViewHolder) view.getTag();

        BookTitleListAdapter bookTitleListAdapter = (BookTitleListAdapter) holder.lvBookTitle.getAdapter();
        bookTitleListAdapter.notifyDataSetChanged();

        setListViewHeight(holder.lvBookTitle);

    }




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
}
