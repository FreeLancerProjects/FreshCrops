package com.appzone.freshcrops.activities_fragments.activity_chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.adapters.ChatAdapter;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.models.ChatRoom_UserIdModel;
import com.appzone.freshcrops.models.MessageModel;
import com.appzone.freshcrops.models.MessageModelList;
import com.appzone.freshcrops.models.ResponseModel;
import com.appzone.freshcrops.models.TypingModel;
import com.appzone.freshcrops.models.UserChatModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.remote.Api;
import com.appzone.freshcrops.singletone.UserSingleTone;
import com.appzone.freshcrops.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import pl.tajchert.waitingdots.DotsTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout ll_back,ll_rate,ll_typing;
    private ImageView image_back,image_client,image_call,image_client_typing;
    private CircleImageView image_delegate,image_delegate_typing;
    private TextView tv_delegate,tv_name,tv_rate;
    private SimpleRatingBar rateBar;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ChatAdapter adapter;
    private ProgressBar progBar,progBar_loadMore;
    private DotsTextView tv_wait_dot;
    private Button btn_send;
    private EditText edt_msg_content;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private UserChatModel userChatModel;
    private Preferences preferences;
    private String current_lang;
    private int page_index = 1;
    private List<MessageModel> messageModelList;
    private boolean isLoading = false,startTyping = false;
    private MediaPlayer mediaPlayer;
    private MyAsyncTask myAsyncTask;
    private TypingModel typingModel;

    @Override
    protected void attachBaseContext(Context base)
    {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        getDataFromIntent();
    }
    private void initView()
    {
        messageModelList = new ArrayList<>();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        preferences = Preferences.getInstance();

        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        Paper.init(this);
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);

        image_back = findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }

        ll_back = findViewById(R.id.ll_back);
        ll_rate = findViewById(R.id.ll_rate);
        ll_typing = findViewById(R.id.ll_typing);
        image_client = findViewById(R.id.image_client);
        image_call = findViewById(R.id.image_call);
        image_client_typing = findViewById(R.id.image_client_typing);
        image_delegate = findViewById(R.id.image_delegate);
        image_delegate_typing = findViewById(R.id.image_delegate_typing);
        tv_delegate = findViewById(R.id.tv_delegate);
        tv_name = findViewById(R.id.tv_name);
        tv_rate = findViewById(R.id.tv_rate);
        rateBar = findViewById(R.id.rateBar);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(false);
        recView.setLayoutManager(manager);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBar_loadMore = findViewById(R.id.progBar_loadMore);
        progBar_loadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        tv_wait_dot = findViewById(R.id.tv_wait_dot);
        tv_wait_dot.setPeriod(1500);
        btn_send = findViewById(R.id.btn_send);
        edt_msg_content = findViewById(R.id.edt_msg_content);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(userChatModel.getAlter_phone()))
                {
                    createCallDialog(userChatModel.getPhone(),userChatModel.getAlter_phone());
                }else
                    {
                        CreateIntentCall(userChatModel.getPhone());

                    }

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_msg_content.getText().toString().trim();
                if (!TextUtils.isEmpty(msg))
                {
                    edt_msg_content.setText("");
                    startTyping = false;
                    SendMessage(msg);
                }
            }
        });

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy<0)
                {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    int lastVisibleItem = manager.findLastVisibleItemPosition();
                    int threshold = messageModelList.size()-10;
                    if (lastVisibleItem < threshold && !isLoading)
                    {
                        isLoading = true;
                        int page_index = ChatActivity.this.page_index +1;
                        progBar_loadMore.setVisibility(View.VISIBLE);
                        loadMore(page_index);
                    }
                }
            }
        });

        recView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom)
                {
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (messageModelList.size()>0)
                                    {
                                        recView.smoothScrollToPosition(messageModelList.size()-1);

                                    }
                                }
                            },10);
                }
            }
        });
        edt_msg_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = edt_msg_content.getText().toString().trim();
                if (msg.length()>0)
                {
                    if (!startTyping)
                    {
                        startTyping =true;
                        ChangeTyping(Tags.START_TYPING);
                    }
                }else
                    {
                        ChangeTyping(Tags.END_TYPING);

                        startTyping =false;

                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            this.userChatModel = (UserChatModel) intent.getSerializableExtra("user_chat_data");
            UpdateUI(this.userChatModel);
        }
    }
    private void UpdateUI(UserChatModel userChatModel)
    {

        ChatRoom_UserIdModel model = new ChatRoom_UserIdModel(userChatModel.getId(),userChatModel.getRoom_id());
        preferences.create_update_chat_user_id_room_id(this,model);
        if (userModel.getUser().getRole().equals(Tags.user_client))
        {
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userChatModel.getImage())).fit().into(image_delegate);
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userChatModel.getImage())).fit().into(image_delegate_typing);

            image_delegate_typing.setVisibility(View.VISIBLE);
            image_delegate.setVisibility(View.VISIBLE);
            tv_delegate.setVisibility(View.VISIBLE);

            tv_rate.setText("("+userChatModel.getRate()+")");
            SimpleRatingBar.AnimationBuilder builder = rateBar.getAnimationBuilder();
            builder.setDuration(1500);
            builder.setRepeatCount(0);
            builder.setRatingTarget((float) userChatModel.getRate());
            builder.setInterpolator(new AccelerateInterpolator());
            builder.start();
            ll_rate.setVisibility(View.VISIBLE);

            image_client.setVisibility(View.GONE);
            image_client_typing.setVisibility(View.GONE);


        }else if (userModel.getUser().getRole().equals(Tags.user_delegate))
        {
            image_client.setVisibility(View.VISIBLE);
            image_client_typing.setVisibility(View.VISIBLE);

        }

        tv_name.setText(userChatModel.getName());

        getMessages();
    }
    private void getMessages()
    {
        Log.e("room",userChatModel.getRoom_id()+"_");
        Api.getService()
                .getMessage(userChatModel.getRoom_id(),userModel.getToken(),page_index)
                .enqueue(new Callback<MessageModelList>() {
                    @Override
                    public void onResponse(Call<MessageModelList> call, Response<MessageModelList> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                if (response.body().getData().size()>0)
                                {
                                    messageModelList.clear();
                                    messageModelList.addAll(response.body().getData());

                                    if (adapter == null)
                                    {
                                        adapter = new ChatAdapter(messageModelList,userModel.getUser().getId(),userChatModel.getImage(),ChatActivity.this);
                                        recView.setAdapter(adapter);
                                        recView.scrollToPosition(messageModelList.size()-1);

                                    }else
                                        {
                                            adapter.notifyDataSetChanged();
                                            recView.scrollToPosition(messageModelList.size()-1);

                                        }


                                }
                            }
                        }else
                            {
                                try {
                                    progBar.setVisibility(View.GONE);

                                    Log.e("Error_code",response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<MessageModelList> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void loadMore(final int page_index)
    {
        Api.getService()
                .getMessage(userChatModel.getRoom_id(),userModel.getToken(),page_index)
                .enqueue(new Callback<MessageModelList>() {
                    @Override
                    public void onResponse(Call<MessageModelList> call, Response<MessageModelList> response) {
                        if (response.isSuccessful())
                        {
                            progBar_loadMore.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {

                                if (response.body().getData().size()>0)
                                {
                                    messageModelList.addAll(response.body().getData());
                                    ChatActivity.this.page_index  = response.body().getCurrent_page();
                                    adapter.notifyDataSetChanged();
                                    isLoading = false;


                                }else
                                    {
                                        isLoading = false;

                                    }
                            }
                        }else
                        {

                            try {
                                isLoading = false;
                                progBar_loadMore.setVisibility(View.GONE);

                                Log.e("Error_code",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModelList> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void SendMessage(String msg)
    {
        Calendar calendar = Calendar.getInstance();
        MessageModel messageModel = new MessageModel(userChatModel.getRoom_id(),userModel.getUser().getId(),userChatModel.getId(),msg,Tags.msg_text,calendar.getTimeInMillis());
        Api.getService()
                .sendMessage(messageModel,userModel.getToken())
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                messageModelList.add(response.body());

                                if (adapter == null)
                                {
                                    if (messageModelList.size()>0)
                                    {
                                        adapter = new ChatAdapter(messageModelList,userModel.getUser().getId(),userChatModel.getImage(),ChatActivity.this);
                                        recView.setAdapter(adapter);
                                        adapter.notifyItemInserted(messageModelList.size()-1);
                                        recView.scrollToPosition(messageModelList.size()-1);
                                    }

                                }else
                                    {
                                        adapter.notifyItemInserted(messageModelList.size()-1);
                                        recView.scrollToPosition(messageModelList.size()-1);
                                    }


                            }
                        }else
                            {
                                try {
                                    Log.e("error_code",response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void ChangeTyping(int typing)
    {
        if (typingModel==null)
        {
            typingModel = new TypingModel(userChatModel.getRoom_id(),userChatModel.getId(),typing);
        }else
            {
                typingModel.setRoom_id(userChatModel.getRoom_id());
                typingModel.setReceiver_id(userChatModel.getId());
                typingModel.setStatus(typing);
            }


            Api.getService()
                    .typing(typingModel,userModel.getToken())
                    .enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            if (response.isSuccessful())
                            {
                                Log.e("success","success");
                            }else
                                {
                                    Log.e("Error",response.code()+"");
                                    try {
                                        Log.e("Error",response.errorBody().string()+"");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {

                            try {
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });

    }

    private void CreateIntentCall(String phone)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+"00966"+phone));
        startActivity(intent);
    }

    private void createCallDialog(final String phone, final String alter_phone)
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_call,null);
        CardView cardView_ph1 = view.findViewById(R.id.cardView_ph1);
        CardView cardView_ph2 = view.findViewById(R.id.cardView_ph2);

        TextView tv_phone1 = view.findViewById(R.id.tv_phone1);
        TextView tv_phone2 = view.findViewById(R.id.tv_phone2);

        tv_phone1.setText(phone);
        tv_phone2.setText(alter_phone);

        cardView_ph1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dialog.dismiss();
             CreateIntentCall(phone);
            }
        });

        cardView_ph2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CreateIntentCall(alter_phone);


            }
        });
        dialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLastMessage(MessageModel messageModel)
    {
        Log.e("msg1515",messageModel.getMsg());
        if (adapter == null)
        {
            Log.e("11112222","11112222");

            messageModelList.add(messageModel);
            adapter = new ChatAdapter(messageModelList,userModel.getUser().getId(),userChatModel.getImage(),ChatActivity.this);
            recView.setAdapter(adapter);
            adapter.notifyItemInserted(messageModelList.size()-1);
            recView.scrollToPosition(messageModelList.size()-1);

        }else
        {
            Log.e("11112222","8888888888");

            messageModelList.add(messageModel);
            adapter.notifyItemInserted(messageModelList.size()-1);
            recView.scrollToPosition(messageModelList.size()-1);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenForTyping(TypingModel typingModel)
    {

        if (typingModel.getRoom_id() == userChatModel.getRoom_id())
        {
            if (typingModel.getStatus() == Tags.START_TYPING)
            {

                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();

                ll_typing.setVisibility(View.VISIBLE);
            }else
            {
                if (mediaPlayer!=null)
                {
                    mediaPlayer.release();
                }
                ll_typing.setVisibility(View.GONE);

            }
        }

    }

    public class MyAsyncTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mediaPlayer = MediaPlayer.create(ChatActivity.this,R.raw.typing);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            mediaPlayer.start();
            return null;
        }
    }

    @Override
    protected void onDestroy()
    {
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }

        if (mediaPlayer!=null)
        {
            mediaPlayer.release();
        }
        super.onDestroy();
    }
}
