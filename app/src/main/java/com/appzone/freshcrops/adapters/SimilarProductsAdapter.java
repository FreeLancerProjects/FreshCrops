package com.appzone.freshcrops.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.product_details.activity.ProductDetailsActivity;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.MyHolder>{
    private Context context;
    private List<MainCategory.Products> productsList;
    private ProductDetailsActivity activity;


    public SimilarProductsAdapter(Context context, List<MainCategory.Products> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.activity = (ProductDetailsActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.similar_product_row,parent,false);
        return new MyHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final MainCategory.Products products = productsList.get(holder.getAdapterPosition());
        holder.BindData(products);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setItemForDetails(products);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private FrameLayout fl_discount_container;
        private ImageView image;
        private TextView tv_discount,tv_name,tv_before_discount,tv_after_discount;
        public MyHolder(View itemView) {
            super(itemView);
            fl_discount_container = itemView.findViewById(R.id.fl_discount_container);
            image = itemView.findViewById(R.id.image);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_before_discount = itemView.findViewById(R.id.tv_before_discount);
            tv_after_discount = itemView.findViewById(R.id.tv_after_discount);

        }

        public void BindData(MainCategory.Products products)
        {
            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            if (lang.equals("ar"))
            {
                tv_name.setText(products.getName_ar());
            }else
            {
                tv_name.setText(products.getName_en());

            }

            if (products.getImage().size()>0)
            {
                Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+products.getImage().get(0))).priority(Picasso.Priority.HIGH).fit().into(image);

            }


            if (products.getFeatures().size()>0)
            {
                MainCategory.Features features = products.getFeatures().get(0);
                try {
                    double price_after_discount = Double.parseDouble(features.getDiscount().trim());
                    double price_before_discount = Double.parseDouble(features.getOld_price().getNet_price().trim());

                    double diff = price_before_discount - price_after_discount;

                    double discount = (diff/price_before_discount)*100;

                    tv_discount.setText(new DecimalFormat("##.##").format((int) discount)+" %");
                    fl_discount_container.setVisibility(View.VISIBLE);

                    tv_before_discount.setPaintFlags(tv_before_discount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_before_discount.setText(new DecimalFormat("##.##").format(Double.parseDouble(products.getFeatures().get(0).getOld_price().getNet_price()))+" "+context.getString(R.string.rsa));
                    tv_after_discount.setText(new DecimalFormat("##.##").format(Double.parseDouble(products.getFeatures().get(0).getDiscount()))+" "+context.getString(R.string.rsa));



                }catch (NumberFormatException e)
                {
                    Log.e("Error",e.getMessage()+"__");
                }
            }else
                {
                    if (products.getSize_prices().size()>0)
                    {
                        tv_before_discount.setText("");
                        tv_after_discount.setText(new DecimalFormat("##.##").format(Double.parseDouble(products.getSize_prices().get(0).getNet_price()))+" "+context.getString(R.string.rsa));

                    }
                    fl_discount_container.setVisibility(View.INVISIBLE);

                }
        }
    }

}
