package com.appzone.freshcrops.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.models.MessageModel;
import com.appzone.freshcrops.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ChatAdapter extends RecyclerView.Adapter {
    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT= 2;
    private List<MessageModel> messageModelList;
    private int current_user_id;
    private String chat_user_image;
    private Context context;

    public ChatAdapter(List<MessageModel> messageModelList, int current_user_id, String chat_user_image, Context context) {
        this.messageModelList = messageModelList;
        this.current_user_id = current_user_id;
        this.chat_user_image = chat_user_image;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_MESSAGE_LEFT)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_message_left_row,parent,false);
            return new MsgLeftHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.chat_message_right_row,parent,false);
                return new MsgRightHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel = messageModelList.get(holder.getAdapterPosition());
        if (holder instanceof MsgLeftHolder)
        {
            MsgLeftHolder msgLeftHolder = (MsgLeftHolder) holder;
            msgLeftHolder.BindData(messageModel);

        }else if (holder instanceof  MsgRightHolder)
        {
            MsgRightHolder msgRightHolder = (MsgRightHolder) holder;
            msgRightHolder.BindData(messageModel);
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MsgLeftHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_message_content,tv_time;
        public MsgLeftHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_message_content = itemView.findViewById(R.id.tv_message_content);
            tv_time = itemView.findViewById(R.id.tv_time);

        }


        public void BindData(MessageModel messageModel)
        {

            Picasso.with(context).load(Tags.IMAGE_URL+chat_user_image).fit().priority(Picasso.Priority.HIGH).into(image);
            tv_message_content.setText(messageModel.getMsg());

            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",new Locale(lang));
            String msg_time = dateFormat.format(new Date(messageModel.getMsg_time()*1000));
            tv_time.setText(msg_time);
        }
    }

    public class MsgRightHolder extends RecyclerView.ViewHolder {
        private TextView tv_message_content,tv_time;

        public MsgRightHolder(View itemView) {
            super(itemView);
            tv_message_content = itemView.findViewById(R.id.tv_message_content);
            tv_time = itemView.findViewById(R.id.tv_time);
        }

        public void BindData(MessageModel messageModel)
        {

            tv_message_content.setText(messageModel.getMsg());

            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",new Locale(lang));
            String msg_time = dateFormat.format(new Date(messageModel.getMsg_time()*1000));
            tv_time.setText(msg_time);
        }
    }
    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModelList.get(position);
        if (messageModel.getSender_id()== current_user_id)
        {
            return ITEM_MESSAGE_RIGHT;
        }else
            {
                return ITEM_MESSAGE_LEFT;
            }
    }
}
