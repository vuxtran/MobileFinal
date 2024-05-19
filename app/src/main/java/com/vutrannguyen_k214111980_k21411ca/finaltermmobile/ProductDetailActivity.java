package com.vutrannguyen_k214111980_k21411ca.finaltermmobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vutrannguyen_k214111980_k21411ca.model.Cart;
import com.vutrannguyen_k214111980_k21411ca.model.Product;
import com.vutrannguyen_k214111980_k21411ca.utilities.DatabaseHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageViewProduct;
    ImageView imageViewHeart;
    ImageView img_back;
    ImageButton btnDecrease, btnIncrease;
    TextView textViewName, textViewPrice, textViewSalePrice, textViewDescription, textViewInventory;
    private String name, description;
    private double price, salePrice;
    private byte[] thumbnail;
    private int inventory, id;
    private DatabaseHelper dbHelper;
    private Button btnAddToCart;

    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        dbHelper = new DatabaseHelper(this);

        setupActionBar();

        imageViewProduct = findViewById(R.id.imv_ProductThumbDetail);
        textViewName = findViewById(R.id.txt_ProductsDetailName);
        textViewPrice = findViewById(R.id.txt_ProductPrice);
        textViewSalePrice = findViewById(R.id.txt_ProductsSalePrice);
        textViewDescription = findViewById(R.id.txt_ProductsDetailDescription);
        textViewInventory = findViewById(R.id.txt_ProductsDetailQuantity);
        imageViewHeart = findViewById(R.id.img_wishlist);
        img_back = findViewById(R.id.img_back);
        btnDecrease = findViewById(R.id.btn_Decrease);
        btnIncrease = findViewById(R.id.btn_Increase);
        btnAddToCart = findViewById(R.id.btn_AddToCart);

        // Initialize Firebase references
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Cart");

        // Get data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("ProductName");
            description = intent.getStringExtra("ProductDescription");
            price = intent.getDoubleExtra("ProductPrice", 0);
            salePrice = intent.getDoubleExtra("ProductSalePrice", 0);
            thumbnail = intent.getByteArrayExtra("ProductThumbnail");
            inventory = intent.getIntExtra("ProductInventory", 10);
            id = intent.getIntExtra("ProductID", 0);

            // Set data to views
            textViewName.setText(name);
            textViewDescription.setText(description);
            textViewPrice.setText(formatVietnameseCurrency(price));
            textViewSalePrice.setText(formatVietnameseCurrency(salePrice));
            textViewInventory.setText(String.valueOf(inventory));

            if (thumbnail != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
                imageViewProduct.setImageBitmap(bitmap);
            }

            // Set click listener for the heart icon
            imageViewHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageViewHeart.setImageResource(R.drawable.ic_heart);

                    // Add product to wishlist database
                    Product product = new Product(id, name, description, price, salePrice, "", thumbnail, inventory);
                    dbHelper.addProductToWishlist(product);

                    Toast.makeText(ProductDetailActivity.this, "Product has been added to wishlist", Toast.LENGTH_SHORT).show();
                    // Pass the product data to WishlistActivity
                    Intent wishlistIntent = new Intent(ProductDetailActivity.this, WishlistActivity.class);
                    startActivity(wishlistIntent);
                }
            });
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductDetailActivity.this, ProductActivity.class);
                    startActivity(intent);
                }
            });

            // Set click listener for decrease button
            btnDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inventory > 1) {
                        inventory--;
                        textViewInventory.setText(String.valueOf(inventory));
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Inventory cannot be less than 1", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Set click listener for increase button
            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inventory++;
                    textViewInventory.setText(String.valueOf(inventory));
                }
            });

            // Set click listener for add to cart button
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart();
                }
            });
        }
    }

    private void addToCart() {
        // Fetch all existing cart items to determine the highest cartId number
        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long maxCartNumber = 0;
                Pattern pattern = Pattern.compile("cartId(\\d+)");

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String cartId = snapshot.getKey();
                    Matcher matcher = pattern.matcher(cartId);
                    if (matcher.matches()) {
                        long cartNumber = Long.parseLong(matcher.group(1));
                        if (cartNumber > maxCartNumber) {
                            maxCartNumber = cartNumber;
                        }
                    }
                }

                long newCartNumber = maxCartNumber + 1;
                String newCartId = "cartId" + newCartNumber;

                Cart cart = new Cart(
                        id,
                        name,
                        "", // Category ID, if necessary
                        price,
                        salePrice,
                        inventory,
                        Base64.encodeToString(thumbnail, Base64.DEFAULT),
                        "accountId" // Replace with actual account ID
                );

                firebaseDatabase.child(newCartId).setValue(cart);
                Toast.makeText(ProductDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();

                // Optionally, navigate to the CartActivity
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProductDetailActivity.this, "Failed to add to cart: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupActionBar() {
        LinearLayout customActionBar = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_action_bar, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(customActionBar, params);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.search) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (itemId == R.id.cart) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private String formatVietnameseCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount) + "đ";
    }
}
