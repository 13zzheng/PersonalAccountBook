package stu.edu.cn.zing.personalbook.listadapter;

import android.content.ComponentName;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import stu.edu.cn.zing.personalbook.BookItems;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.bmobclass.BookItem;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemTitle;
import stu.edu.cn.zing.personalbook.bmobclass.BookPaymentType;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BookTitleListAdapter extends BaseAdapter {



    private Context context;
    private LayoutInflater layoutInflater;
    private BookItems bookItems;



    public BookTitleListAdapter(Context context, BookItems bookItems) {
        this.context = context;
        this.bookItems = bookItems;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        if (!bookItems.isOpen()) {
            return 0;
        }
        return bookItems.getItems().size();
    }

    @Override
    public Object getItem(int position) {
            return bookItems.getItems().get(position);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (bookItems.isOpen()) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_book_item, parent, false);
                holder = new ViewHolder();
                holder.tvPayment = (TextView) convertView.findViewById(R.id.tvPayment);
                holder.tvPaymentType = (TextView) convertView.findViewById(R.id.tvPaymentType);
                holder.tvTips = (TextView) convertView.findViewById(R.id.tvTips);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            BookItem item = (BookItem) getItem(position);
            if (item.getItemInfo().getItemType() == null) {
                holder.tvPaymentType.setText("其他");
            } else {
                holder.tvPaymentType.setText(item.getItemInfo().getItemType().getType());
            }
            String remarks = item.getItemInfo().getRemarks();
            if (TextUtils.isEmpty(remarks)) {
                holder.tvTips.setVisibility(View.GONE);
            }
            holder.tvTips.setText(remarks);
            float pay2 = item.getPayment().getPay();
            int payType2 = item.getPayment().getPayType();
            if (payType2 == BookPaymentType.TYPE_POSITIVE) {
                //Pay为正数
                holder.tvPayment.setTextColor(context.getResources().getColor(R.color.payment_in, null));
            } else if (payType2 == BookPaymentType.TYPE_NEGATIVE) {
                //Pay为负数
                holder.tvPayment.setTextColor(context.getResources().getColor(R.color.payment_out, null));
            }
            holder.tvPayment.setText("¥"+String.valueOf(pay2));
            return convertView;
        }

        return convertView;
    }

    static class ViewHolder{
        TextView tvPaymentType;
        TextView tvTips;
        TextView tvPayment;
    }

//    public void test(ListView listView){
//        int state = 0;
//        if (bookItems.isOpen()) {
//            state = View.VISIBLE;
//        } else {
//            state = View.GONE;
//        }
//        for (int i = 0; i < listView.getChildCount(); i++) {
//
//            View view = listView.getChildAt(i);
//            view.setVisibility(state);
//        }
//    }
}
