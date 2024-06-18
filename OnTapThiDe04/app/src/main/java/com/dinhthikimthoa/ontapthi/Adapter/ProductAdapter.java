package com.dinhthikimthoa.ontapthi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dinhthikimthoa.ontapthi.MainActivity;
import com.dinhthikimthoa.ontapthi.Models.Product;
import com.dinhthikimthoa.ontapthi.R;
import com.dinhthikimthoa.ontapthi.SQLite;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    SQLite context;
    int layout_items;
    List<Product> products;

    public ProductAdapter(SQLite context, int layout_items, List<Product> products) {
        this.context = context;
        this.layout_items = layout_items;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
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
        Product p = products.get(position);
        holder.txtProduct.setText("Mã sản phẩm: "+p.getIdSP()+ "\n"+
                                    "Tên sản phẩm: "+ p.getName() + "\n" +
                                    "Giá bán: "+ p.getPrice()+ "\n");

        return convertView;
    }


    private class ViewHolder {
        TextView txtProduct;
    }
}
