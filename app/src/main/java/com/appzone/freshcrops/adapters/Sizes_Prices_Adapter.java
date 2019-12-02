package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.product_details.activity.ProductDetailsActivity;
import com.appzone.freshcrops.models.ProductSize_OfferModel;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Sizes_Prices_Adapter extends RecyclerView.Adapter<Sizes_Prices_Adapter.MyHolder>{
    private Context context;
    private List<ProductSize_OfferModel> productSize_offerModelList;
    private int lastSelectedItem =-1;
    private SparseBooleanArray sparseBooleanArray;
    private ProductDetailsActivity activity;

    public Sizes_Prices_Adapter(Context context, List<ProductSize_OfferModel> productSize_offerModelList) {
        this.context = context;
        this.productSize_offerModelList = productSize_offerModelList;
        this.activity = (ProductDetailsActivity) context;
        sparseBooleanArray = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_size_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final ProductSize_OfferModel productSize_offerModel = productSize_offerModelList.get(position);
        holder.BindData(productSize_offerModel);

        if (sparseBooleanArray.get(position))
        {
            holder.fl.setBackgroundResource(R.drawable.selected_sub_category);
            holder.tv_size.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else
            {
                holder.fl.setBackgroundResource(R.drawable.unselected_sub_category);
                holder.tv_size.setTextColor(ContextCompat.getColor(context,R.color.green_text));

            }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedItem = holder.getAdapterPosition();
                UpdateSelectedItem(lastSelectedItem);
                notifyDataSetChanged();

                activity.setItemForSize(productSize_offerModel,lastSelectedItem);





            }
        });

    }

    public void UpdateSelectedItem(int lastSelectedItem)
    {
        sparseBooleanArray.clear();
        sparseBooleanArray.put(lastSelectedItem,true);
    }

    @Override
    public int getItemCount() {
        return productSize_offerModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_size,tv_discount;
        private FrameLayout fl;
        private LinearLayout ll_discount;
        public MyHolder(View itemView) {
            super(itemView);
            fl = itemView.findViewById(R.id.fl);
            ll_discount = itemView.findViewById(R.id.ll_discount);
            tv_size = itemView.findViewById(R.id.tv_size);
            tv_discount = itemView.findViewById(R.id.tv_discount);

        }

        public void BindData(ProductSize_OfferModel productSize_offerModel)
        {
            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            if (lang.equals("ar"))
            {
                tv_size.setText(productSize_offerModel.getAr_name());
            }else
                {
                    tv_size.setText(productSize_offerModel.getEn_name());

                }

                if (productSize_offerModel.isOffer())
                {
                    ll_discount.setVisibility(View.VISIBLE);
                    tv_discount.setText(new DecimalFormat("##.##").format(Integer.parseInt(productSize_offerModel.getDiscount()))+"%");
                }else
                    {
                        ll_discount.setVisibility(View.GONE);
                    }

        }
    }
}
