package stu.edu.cn.zing.personalbook.listadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.model.Finder;

/**
 * Created by Administrator on 2017/5/11.
 */

public class BookItemFinderListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Finder finder;
    public BookItemFinderListAdapter(Context context, Finder finder) {
        this.context = context;
        this.finder = finder;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return finder.getNameList().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_book_item_finder, parent, false);
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setText(finder.getNameList().get(position));

        holder.checkBox.setChecked(finder.getIsCheckedList().get(position));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView == holder.checkBox) {
                    finder.getIsCheckedList().set(position, isChecked);
//                    Log.i("TEXT", position + " " + finder.getNameList().get(position));
//                    Log.i("CHECK", position + " " + finder.getIsCheckedList().get(position));
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        CheckBox checkBox;
    }
}
