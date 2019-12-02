package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart.Fragment_Map;
import com.appzone.freshcrops.models.PlaceSearchModel;

import java.util.List;

public class PlaceSearchAdapter extends RecyclerView.Adapter<PlaceSearchAdapter.MyHolder>{
    private Context context;
    private List<PlaceSearchModel> placeSearchModelList;
    private Fragment_Map fragment_map;
    public PlaceSearchAdapter(Context context, List<PlaceSearchModel> placeSearchModelList, Fragment_Map fragment_map) {
        this.context = context;
        this.placeSearchModelList = placeSearchModelList;
        this.fragment_map = fragment_map;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_search_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        PlaceSearchModel placeSearchModel = placeSearchModelList.get(position);
        holder.BindData(placeSearchModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceSearchModel placeSearchModel = placeSearchModelList.get(holder.getAdapterPosition());
                fragment_map.setItem(placeSearchModel);

            }
        });
    }

    @Override
    public int getItemCount() {
        return placeSearchModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        public MyHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);

        }

        public void BindData(PlaceSearchModel placeSearchModel)
        {
            tv_name.setText(placeSearchModel.getName());
        }
    }
}
