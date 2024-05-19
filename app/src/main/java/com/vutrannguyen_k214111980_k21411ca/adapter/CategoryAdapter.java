package com.vutrannguyen_k214111980_k21411ca.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.R;
import com.vutrannguyen_k214111980_k21411ca.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imv_cateThumb);
        TextView textView = convertView.findViewById(R.id.txt_cateName);
        Category category = getItem(position);
        if (category != null) {
            textView.setText(category.getName());
            byte[] imageBytes = category.getImage();
            if (imageBytes != null && imageBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }

        return convertView;
    }
}