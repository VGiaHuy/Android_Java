package com.example.de05.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.de05.Model.Sach;
import com.example.de05.R;
import com.example.de05.SQLite;

import java.util.List;

public class SachAdapters extends BaseAdapter {
    SQLite context;
    int layout_items;
    List<Sach> sachListh;

    public SachAdapters(SQLite context, int layout_items, List<Sach> sachListh) {
        this.context = context;
        this.layout_items = layout_items;
        this.sachListh = sachListh;
    }


    @Override
    public int getCount() {
        return sachListh.size();
    }

    @Override
    public Object getItem(int position) {
        return sachListh.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null ){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout_items, null);
            holder.txtProduct = convertView.findViewById(R.id.tv_Product);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //binding data
        Sach p = sachListh.get(position);

        holder.txtProduct.setText("Mã sản phẩm: "+p.getMaSach()+ "\n"+
                "Tên sản phẩm: "+ p.getTenSach() + "\n" +
                "Giá bán: "+ p.getGiaSach()+ "\n");

        return convertView;
    }


    private class ViewHolder {
        TextView txtProduct;
    }
}
