package com.appzone.freshcrops.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_order_details.fragments.Fragment_Delegate_Collecting_Order_Products;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DelegateCollectOrderAdapter extends RecyclerView.Adapter<DelegateCollectOrderAdapter.MyHolder>{

    private Context context;
    private List<OrdersModel.Products> productsList;
    private Fragment_Delegate_Collecting_Order_Products fragment;
    public DelegateCollectOrderAdapter(Context context, List<OrdersModel.Products> productsList,Fragment_Delegate_Collecting_Order_Products fragment) {
        this.context = context;
        this.productsList = productsList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delegate_collect_order_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        OrdersModel.Products products = productsList.get(position);
        holder.BindData(products);
        holder.btn_show_alternative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersModel.Products products = productsList.get(holder.getAdapterPosition());
                fragment.setItemToShowAlternativeProducts(products.getAlternative(),holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_name,tv_price,tv_amount;
        public LinearLayout ll_view_background,ll_view_foreground,ll_delete,ll_add;
        private Button btn_show_alternative;
        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            ll_view_background = itemView.findViewById(R.id.ll_view_background);
            ll_view_foreground = itemView.findViewById(R.id.ll_view_foreground);
            ll_delete = itemView.findViewById(R.id.ll_delete);
            ll_add = itemView.findViewById(R.id.ll_add);
            btn_show_alternative = itemView.findViewById(R.id.btn_show_alternative);

        }

        public void BindData(OrdersModel.Products products)
        {
            if (products.getProduct().getImage().size()>0)
            {
                Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+products.getProduct().getImage().get(0))).fit().priority(Picasso.Priority.HIGH).into(image);

            }


            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            if (lang.equals("ar"))
            {
                tv_name.setText(products.getProduct().getName_ar()+" "+products.getProduct_price().getSize_ar());
            }else
            {
                tv_name.setText(products.getProduct().getName_en()+" "+products.getProduct_price().getSize_en());

            }

            if (products.getFeature()!=null)
            {

                tv_price.setText(new DecimalFormat("##.##").format(products.getFeature().getDiscount())+" "+context.getString(R.string.rsa));

            }else
                {
                    tv_price.setText(new DecimalFormat("##.##").format(products.getProduct_price().getNet_price())+" "+context.getString(R.string.rsa));

                }

                tv_amount.setText(new DecimalFormat("#").format(products.getQuantity())+"");

            if (products.getAlternative()==null)
            {
                btn_show_alternative.setVisibility(View.GONE);
            }else
                {
                    btn_show_alternative.setVisibility(View.VISIBLE);

                }

        }
    }
}
