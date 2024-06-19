package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.de05.R;
import com.example.de05.SqliteActivity;
import com.example.model.Sach;

import java.util.List;

public class SachAdapter extends BaseAdapter {
    SqliteActivity context;
    int item;
    List<Sach> sachList;

    public SachAdapter(SqliteActivity context, int item, List<Sach> sanphamList) {
        this.context = context;
        this.item = item;
        this.sachList = sanphamList;
    }


    @Override
    public int getCount() {
        return sachList.size();
    }

    @Override
    public Object getItem(int position) {
        return sachList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(item, null);

            holder.txtMa = convertView.findViewById(R.id.txtMaSp);
            holder.txtTen = convertView.findViewById(R.id.txtTensp);
            holder.txtGia = convertView.findViewById(R.id.txtGiaSp);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sach w = sachList.get(position);

        holder.txtMa.setText(String.valueOf(w.getMasp()));
        holder.txtTen.setText(w.getTensp());
        holder.txtGia.setText(String.valueOf(w.getGiasp()));


        // Sanphammodel p = sanphamList.get(position);


        return convertView;
    }
    public static class ViewHolder {
        TextView txtTen, txtGia, txtMa;


    }
}
