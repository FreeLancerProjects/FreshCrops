package com.appzone.freshcrops.adapters;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_home.sub_fragments.Fragment_Food_Department;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.MyHolder>{
    private Context context;
    private List<MainCategory.MainCategoryItems> mainCategoryItemsList;
    private Fragment_Food_Department fragment_food_department;
    public MainCategoryAdapter(Context context, List<MainCategory.MainCategoryItems> mainCategoryItemsList, Fragment_Food_Department fragment_food_department) {
        this.context = context;
        this.mainCategoryItemsList = mainCategoryItemsList;
        this.fragment_food_department = fragment_food_department;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_category_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        MainCategory.MainCategoryItems mainCategoryItems = mainCategoryItemsList.get(position);
        holder.BindData(mainCategoryItems);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainCategory.MainCategoryItems mainCategoryItems = mainCategoryItemsList.get(holder.getAdapterPosition());
                fragment_food_department.setItem(mainCategoryItems);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainCategoryItemsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_name;
        public MyHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            image = itemView.findViewById(R.id.image);


        }

        public void BindData(MainCategory.MainCategoryItems mainCategoryItems)
        {
            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            if (lang.equals("ar"))
            {
                tv_name.setText(mainCategoryItems.getName_ar());
            }else
                {
                    tv_name.setText(mainCategoryItems.getName_en());

                }

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+mainCategoryItems.getImage())).priority(Picasso.Priority.HIGH).fit().into(image);
        }
    }
}
