package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.Fragment_Search;

import java.util.List;

public class RecentSearchQueryAdapter extends RecyclerView.Adapter <RecentSearchQueryAdapter.MyHolder>{

    private Context context;
    private List<String> queriesList;
    private Fragment_Search fragment_search;
    public RecentSearchQueryAdapter(Context context, List<String> queriesList,Fragment_Search fragment_search) {
        this.context = context;
        this.queriesList = queriesList;
        this.fragment_search = fragment_search;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_search_query_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        String query = queriesList.get(position);
        holder.BindData(query);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = queriesList.get(holder.getAdapterPosition());
                if (q!=null)
                {
                    fragment_search.Search(q);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return queriesList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        public MyHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }

        public void BindData(String query)
        {
            tv_title.setText(query);
        }
    }
}
