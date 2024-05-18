package com.vutrannguyen_k214111980_k21411ca.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vutrannguyen_k214111980_k21411ca.finaltermmobile.R;
import com.vutrannguyen_k214111980_k21411ca.model.Cart;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Cart> cartList;
    private Context context;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        holder.productName.setText(cart.getProductName());
        holder.productPrice.setText(formatVietnameseCurrency(cart.getProductPrice()));
        holder.salePrice.setText(formatVietnameseCurrency(cart.getSalePrice()));
        holder.inventory.setText("Inventory: " + cart.getInventory());

        byte[] imageBytes = Base64.decode(cart.getThumbnail(), Base64.DEFAULT);
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.productImage.setImageBitmap(bitmap);
        } else {
            holder.productImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, salePrice, inventory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imv_ProductImage);
            productName = itemView.findViewById(R.id.txt_ProductName);
            productPrice = itemView.findViewById(R.id.txt_ProductPrice);
            salePrice = itemView.findViewById(R.id.txt_SalePrice);
            inventory = itemView.findViewById(R.id.txt_Inventory);
        }
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
