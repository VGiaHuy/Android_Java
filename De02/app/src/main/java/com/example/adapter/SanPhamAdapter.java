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

import com.example.de02.R;
import com.example.de02.SqliteActivity;
import com.example.model.SanPham;

import java.util.List;

public class SanPhamAdapter extends BaseAdapter {
    SqliteActivity context;
    int itemLayout;
    List<SanPham> sanPhamList;

    public SanPhamAdapter(SqliteActivity context, int itemLayout, List<SanPham> sanPhamList) {
        this.context = context;
        this.itemLayout = itemLayout;
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

        // Nếu view được tạo lần đầu tiên
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(itemLayout, null);

            // Khởi tạo ViewHolder với các tham chiếu đến các view trong layout của item
            holder.txtTen = convertView.findViewById(R.id.txtTen);

            // Lưu holder với view
            convertView.setTag(holder);
        } else {
            // Tái sử dụng ViewHolder để tiết kiệm thời gian và tài nguyên
            holder = (ViewHolder) convertView.getTag();
        }

        // Lấy đối tượng Food tại vị trí hiện tại
        SanPham w = sanPhamList.get(position);

        // Đặt text cho mỗi TextView trong ViewHolder
        holder.txtTen.setText(w.getTenSP());


        return convertView; // Trả về view hoàn thành để hiển thị trên màn hình
    }

    public static class ViewHolder {
        TextView txtTen;
    }
}
