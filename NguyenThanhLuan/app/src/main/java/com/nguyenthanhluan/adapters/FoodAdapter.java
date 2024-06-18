package com.nguyenthanhluan.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenthanhluan.models.Food;
import com.nguyenthanhluan.test.MainActivity;
import com.nguyenthanhluan.test.R;

import java.util.List;

public class FoodAdapter extends BaseAdapter {
    MainActivity context;
    int itemLayout;
    List<Food> foodList;

    // Hàm khởi tạo để khởi tạo adapter với context, layout của item và danh sách thực phẩm
    public FoodAdapter(MainActivity context, int itemLayout, List<Food> foodList) {
        this.context = context;
        this.itemLayout = itemLayout;
        this.foodList = foodList;
    }

    // Trả về số lượng các mục trong danh sách thực phẩm
    @Override
    public int getCount() {
        return foodList.size();
    }

    // Trả về đối tượng Food tại vị trí chỉ định
    @Override
    public Object getItem(int i) {
        return foodList.get(i);
    }

    // Trả về ID của mục tại vị trí chỉ định
    @Override
    public long getItemId(int i) {
        return 0;
    }

    // Trả về một view cho mỗi mục trong danh sách thực phẩm
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        // Nếu view được tạo lần đầu tiên
        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(itemLayout, null);

            // Khởi tạo ViewHolder với các tham chiếu đến các view trong layout của item
            holder.txtTenMonAn = view.findViewById(R.id.txtTenMonAn);
            holder.txtMoTaMonAn = view.findViewById(R.id.txtMoTaMonAn);
            holder.txtGiaMonAn = view.findViewById(R.id.txtGiaMonAn);
            holder.txtDanhMucMonAn = view.findViewById(R.id.txtDanhMucMonAn);
            holder.imvPhoto = view.findViewById(R.id.imvHAnh);
            holder.imvEdit = view.findViewById(R.id.imvEdit);
            holder.imvDelete = view.findViewById(R.id.imvDelete);

            // Lưu holder với view
            view.setTag(holder);
        }else{
            // Tái sử dụng ViewHolder để tiết kiệm thời gian và tài nguyên
            holder = (ViewHolder) view.getTag();
        }

        // Lấy đối tượng Food tại vị trí hiện tại
        Food w = foodList.get(i);

        // Đặt text cho mỗi TextView trong ViewHolder
        holder.txtTenMonAn.setText(w.getTenMonAn());
        holder.txtMoTaMonAn.setText(w.getMoTaMonAn());
        holder.txtGiaMonAn.setText(String.valueOf(w.getGiaMonAn()));
        holder.txtDanhMucMonAn.setText(w.getDanhMucMonAn());

        // Chuyển đổi mảng byte thành Bitmap và đặt nó vào ImageView
        byte[] photo = w.getHinhAnhMonAn();
        if (photo != null && photo.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            holder.imvPhoto.setImageBitmap(bitmap);
        } else {
// Đặt một hình ảnh mặc định nếu không có hình ảnh nào được cung cấp
            holder.imvPhoto.setImageResource(R.drawable.ic_launcher_background);
        }

        // Lấy lại đối tượng Food cho vị trí hiện tại (bị lặp lại, có thể tối ưu hóa)
        Food p = foodList.get(i);

        // Đặt sự kiện click cho nút edit
        holder.imvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức mở hộp thoại chỉnh sửa trong MainActivity
                context.openEditDialog(p);
            }
        });

        // Đặt sự kiện click cho nút delete
        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi phương thức mở hộp thoại xác nhận xóa trong MainActivity
                context.openDeleteConfirmDialog(p);
            }
        });

        return view; // Trả về view hoàn thành để hiển thị trên màn hình
    }

    // Mẫu ViewHolder để tối ưu hóa việc tìm kiếm view và cải thiện hiệu suất
    public static class ViewHolder {
        ImageView imvPhoto, imvEdit, imvDelete;
        TextView txtTenMonAn, txtMoTaMonAn, txtGiaMonAn, txtDanhMucMonAn;
    }
}