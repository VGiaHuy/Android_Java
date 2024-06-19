package com.example.de2_cau2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.de2_cau2.MainActivity;
import com.example.de2_cau2.Model.SanPham;
import com.example.de2_cau2.R;

import java.util.List;

public class SanPhamAdapters extends BaseAdapter {
    MainActivity context;
    int layout_items;
    List<SanPham> sanPhamList;

    public SanPhamAdapters(MainActivity context, int layout_items, List<SanPham> sanPhamList) {
        this.context = context;
        this.layout_items = layout_items;
        this.sanPhamList = sanPhamList;
    }

    @Override
    public int getCount() {
        return sanPhamList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanPhamList.get(position);
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
            holder.txtSanPham = convertView.findViewById(R.id.txt_tenSanPham);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //binding data
        SanPham p = sanPhamList.get(position);
        holder.txtSanPham.setText(p.getTen());

        holder.txtSanPham.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.EventsLongClick(p);
                return true;
            }
        });

        return convertView;
    }


    private class ViewHolder {
        TextView txtSanPham;
    }
}
