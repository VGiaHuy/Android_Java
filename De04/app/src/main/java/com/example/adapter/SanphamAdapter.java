package com.example.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.database.SanphamDB;
import com.example.de04.MainActivity;
import com.example.de04.R;
import com.example.model.Sanphammodel;

import java.util.List;

public class SanphamAdapter extends BaseAdapter {
    MainActivity context;
    int item;
    List<Sanphammodel> sanphamList;

    public SanphamAdapter(MainActivity context, int item, List<Sanphammodel> sanphamList) {
        this.context = context;
        this.item = item;
        this.sanphamList = sanphamList;
    }


    @Override
    public int getCount() {
        return sanphamList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanphamList.get(position);
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

            holder.txtMasp = convertView.findViewById(R.id.txtMaSp);
            holder.txtTensp = convertView.findViewById(R.id.txtTensp);
            holder.txtGiasp = convertView.findViewById(R.id.txtGiaSp);
            holder.imvAnh = convertView.findViewById(R.id.imvAnh);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Sanphammodel w = sanphamList.get(position);

        holder.txtMasp.setText(String.valueOf(w.getMasp()));
        holder.txtTensp.setText(w.getTensp());
        holder.txtGiasp.setText(String.valueOf(w.getGiasp()));

        byte[] photo = w.getAnhsp();
        if (photo != null && photo.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            holder.imvAnh.setImageBitmap(bitmap);
        } else {
            holder.imvAnh.setImageResource(R.drawable.ic_launcher_background);
        }

        // Sanphammodel p = sanphamList.get(position);


        return convertView;
    }
    public static class ViewHolder {
        TextView txtTensp, txtGiasp, txtMasp;
        ImageView imvAnh;
    }
}
