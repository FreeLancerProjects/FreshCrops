package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.Fragment_SubCategory;
import com.appzone.freshcrops.models.MainCategory;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.MyHolder>{
    private Context context;
    private List<MainCategory.SubCategory> subCategoryList;
    private Fragment_SubCategory fragment_subCategory;
    private int lastSelectedItem =0;
    private SparseBooleanArray sparseBooleanArray;
    public DepartmentAdapter(Context context, List<MainCategory.SubCategory> subCategoryList, Fragment_SubCategory fragment_subCategory) {
        this.context = context;
        this.subCategoryList = subCategoryList;
        this.fragment_subCategory = fragment_subCategory;
        sparseBooleanArray = new SparseBooleanArray();
        sparseBooleanArray.put(lastSelectedItem,true);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_category_toolbar_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        final MainCategory.SubCategory subCategory = subCategoryList.get(position);
        holder.BindData(subCategory);

        if (sparseBooleanArray.get(position))
        {
            holder.fl.setBackgroundResource(R.drawable.selected_sub_category);
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.white));
        }else
            {
                holder.fl.setBackgroundResource(R.drawable.unselected_sub_category);
                holder.tv_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 MainCategory.SubCategory subCategory = subCategoryList.get(holder.getAdapterPosition());
                lastSelectedItem = holder.getAdapterPosition();
                sparseBooleanArray.clear();
                sparseBooleanArray.put(lastSelectedItem,true);
                notifyDataSetChanged();

                fragment_subCategory.setItemForDepartment(subCategory);




            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private FrameLayout fl;
        public MyHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            fl = itemView.findViewById(R.id.fl);


        }

        public void BindData(MainCategory.SubCategory subCategory)
        {
            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            if (lang.equals("ar"))
            {
                tv_name.setText(subCategory.getName_ar());
            }else
                {
                    tv_name.setText(subCategory.getName_en());

                }

        }
    }
}
