package com.vutrannguyen_k214111980_k21411ca.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.ProductDetailActivity;
import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.R;
import com.vutrannguyen_k214111980_k21411ca.model.Product;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_products, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imv_ProductImage);
        TextView productName = convertView.findViewById(R.id.txt_ProductName);
        TextView salePrice = convertView.findViewById(R.id.txt_SalePrice);
        TextView productPrice = convertView.findViewById(R.id.txt_ProductPrice);
        TextView ratingCount = convertView.findViewById(R.id.txt_ratingCount);
        TextView ratings = convertView.findViewById(R.id.txt_ratings);

        Product product = getItem(position);
        if (product != null) {
            productName.setText(product.getName());
            productPrice.setText(formatVietnameseCurrency(product.getPrice()));
            salePrice.setText(formatVietnameseCurrency(product.getSalePrice()));
            ratingCount.setText("4");
            ratings.setText("(35 Ratings)");


            byte[] imageBytes = product.getThumbnail();
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                Product product = getItem(position);
                if (product != null) {
                    intent.putExtra("ProductID", product.getId());
                    intent.putExtra("ProductName", product.getName());
                    intent.putExtra("ProductDescription", product.getDescription());
                    intent.putExtra("ProductPrice", product.getPrice());
                    intent.putExtra("ProductSalePrice", product.getSalePrice());
                    intent.putExtra("ProductThumbnail", product.getThumbnail());
                    intent.putExtra("ProductInventory", product.getInventory());
                }
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

        // Helper method to format the currency in Vietnamese format
        private String formatVietnameseCurrency(double amount) {
            DecimalFormat formatter = new DecimalFormat("###,###,###'đ'");
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setGroupingSeparator('.');
            formatter.setDecimalFormatSymbols(symbols);
            return formatter.format(amount);
        }
}
