package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment.fragment_delegate_orders.Fragment_Delegate_Current_Order;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment.fragment_delegate_orders.Fragment_Delegate_New_Order;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.fragment.fragment_delegate_orders.Fragment_Delegate_Previous_Order;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.share.TimeAgo;
import com.appzone.freshcrops.tags.Tags;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Delegate_Order_Adapter extends RecyclerView.Adapter<Delegate_Order_Adapter.MyHolder> {

    private Context context;
    private List<OrdersModel.Order> orderList;
    private Fragment fragment;

    public Delegate_Order_Adapter(Context context, List<OrdersModel.Order> orderList, Fragment fragment) {
        this.context = context;
        this.orderList = orderList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_current_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        OrdersModel.Order order = orderList.get(position);
        holder.BindData(order);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment instanceof Fragment_Delegate_New_Order)
                {
                    OrdersModel.Order order = orderList.get(holder.getAdapterPosition());
                    Fragment_Delegate_New_Order fragment_delegate_new_order = (Fragment_Delegate_New_Order) fragment;
                    if (order!=null)
                    {
                        fragment_delegate_new_order.setItem(order,holder.getAdapterPosition());
                    }


                }else if (fragment instanceof Fragment_Delegate_Current_Order)
                {

                    OrdersModel.Order order = orderList.get(holder.getAdapterPosition());
                    Fragment_Delegate_Current_Order fragment_delegate_current_order = (Fragment_Delegate_Current_Order) fragment;
                    if (order!=null)
                    {
                        fragment_delegate_current_order.setItem(order,holder.getAdapterPosition());
                    }

                }else if (fragment instanceof Fragment_Delegate_Previous_Order)
                {
                    //
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_order_number,tv_order_total,tv_order_status,tv_date,tv_created_date;
        private ImageView image_order_state;
        private Button btn_details;
        public MyHolder(View itemView) {
            super(itemView);
            tv_order_number = itemView.findViewById(R.id.tv_order_number);
            tv_order_total = itemView.findViewById(R.id.tv_order_total);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_created_date = itemView.findViewById(R.id.tv_created_date);
            btn_details = itemView.findViewById(R.id.btn_details);

            image_order_state = itemView.findViewById(R.id.image_order_state);


        }

        public void BindData(OrdersModel.Order order)
        {
            if (fragment instanceof Fragment_Delegate_New_Order)
            {
                image_order_state.setBackgroundResource(R.drawable.add_bg_gradient);
                image_order_state.setImageResource(R.drawable.clock_white);
                btn_details.setVisibility(View.VISIBLE);
                if (order.getStatus()==0)
                {
                    tv_order_status.setText(R.string.not_approved);

                }
            }else if (fragment instanceof Fragment_Delegate_Current_Order)
            {
                image_order_state.setBackgroundResource(R.drawable.add_bg_gradient);
                image_order_state.setImageResource(R.drawable.clock_white);
                btn_details.setVisibility(View.VISIBLE);

                switch (order.getStatus())
                {
                    case Tags.status_delegate_accept_order:
                        tv_order_status.setText(R.string.delegate_accept_order);
                        break;
                    case Tags.status_delegate_collect_order:
                        tv_order_status.setText(R.string.collecting_order);

                        break;
                    case Tags.status_delegate_already_collect_order:
                        tv_order_status.setText(R.string.order_collected);

                        break;
                    case Tags.status_delegate_delivering_order:
                        tv_order_status.setText(R.string.delivering_order);

                        break;

                }

            }else if (fragment instanceof Fragment_Delegate_Previous_Order)
            {
                image_order_state.setBackgroundResource(R.drawable.circle_fill_primary);
                image_order_state.setImageResource(R.drawable.correct_white);
                tv_order_status.setText(R.string.done2);
                btn_details.setVisibility(View.INVISIBLE);

            }

            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());



            long time = order.getMilli_time()*1000;
            Log.e("time",time+"_");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd-MM-yyyy ",new Locale(lang));
            String date = dateFormat.format(new Date(calendar.getTimeInMillis()));
            tv_date.setText(date);

            String d = TimeAgo.getTimeAgo(time,context);
            tv_created_date.setText(d);

            tv_order_number.setText("#"+new DecimalFormat("#").format(order.getId()));
            tv_order_total.setText(new DecimalFormat("##.##").format(order.getTotal())+" "+context.getString(R.string.rsa));


        }
    }
}
