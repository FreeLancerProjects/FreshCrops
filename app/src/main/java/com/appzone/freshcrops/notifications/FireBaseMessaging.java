package com.appzone.freshcrops.notifications;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_chat.ChatActivity;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.activities_fragments.activity_home.delegate_home.DelegateHomeActivity;
import com.appzone.freshcrops.models.ChatRoom_UserIdModel;
import com.appzone.freshcrops.models.MessageModel;
import com.appzone.freshcrops.models.NotificationRateModel;
import com.appzone.freshcrops.models.OrderStatusModel;
import com.appzone.freshcrops.models.PageModel;
import com.appzone.freshcrops.models.TypingModel;
import com.appzone.freshcrops.models.UserChatModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.preferences.Preferences;
import com.appzone.freshcrops.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {
    private Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String, String> map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key :", key);
            Log.e("value :", map.get(key) + "_");
        }

        ManageNotification(map);


    }

    private void ManageNotification(final Map<String, String> map) {

        String session = getSession();
        if (session.equals(Tags.session_login)) {
            //String notification_type = map.get("type");
            UserModel userModel = getUserData();


            //////////////////////////////chat
             /*   if (notification_type.equals(String.valueOf(Tags.NEW_MESSAGE_NOTIFICATION))) {
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
                    if (className.equals("com.appzone.dukkan.activities_fragments.activity_chat.ChatActivity")) {

                        ChatRoom_UserIdModel model = getChatRoomData();
                        if (model != null) {
                            int room_id = model.getRoomId();

                            if (room_id != Integer.parseInt(map.get("room_id"))) {

                                if (userModel.getUser().getId()==Integer.parseInt(map.get("receiver_id")))
                                {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            createNotification(map);

                                        }
                                    },100);
                                }

                            }else
                                {

                                    int msg_id = Integer.parseInt(map.get("msg_id"));
                                    int room_id2 = Integer.parseInt(map.get("room_id"));
                                    int sender_id = Integer.parseInt(map.get("sender_id"));
                                    int receiver_id = Integer.parseInt(map.get("receiver_id"));
                                    int msg_type = Integer.parseInt(map.get("msg_type"));
                                    String msg = map.get("msg");
                                    long msg_time = Long.parseLong(map.get("msg_time"))*1000;


                                    MessageModel messageModel = new MessageModel(room_id2,sender_id,receiver_id,msg,msg_type,msg_time);
                                    messageModel.setId(msg_id);

                                    EventBus.getDefault().post(messageModel);

                                }
                        } else {
                            createNotification(map);

                        }
                    } else {

                        if (userModel.getUser().getId()==Integer.parseInt(map.get("receiver_id")))
                        {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    createNotification(map);

                                }
                            },100);
                        }

                    }
                } else {

                    if (userModel.getUser().getId() == Integer.parseInt(map.get("receiver_id")))
                    {
                        createNotification(map);

                    }


                }*/

            if (userModel.getUser().getId() == Integer.parseInt(map.get("receiver_id"))) {
                createNotification(map);

            }


        }
    }


    private void createNotification(final Map<String, String> map) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            createNotificationProfessional(map);
        } else {

            createNotificationNative(map);


        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationProfessional(final Map<String, String> map) {
        ///////////////////////////////////
        UserModel userModel = getUserData();

        String notification_type = map.get("type");





        /////////////////////////////////////// chat ////////////////////////////////
        if (notification_type.equals(String.valueOf(Tags.NEW_MESSAGE_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;

            if (userModel.getUser().getId() == Integer.parseInt(map.get("receiver_id"))) {

                if (userModel.getUser().getRole().equals(Tags.user_client))
                {
                    sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;
                }else
                {
                    sound_path = "android.resource://" + getPackageName() + "/" + R.raw.delegate;

                }
            }
            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ChatRoom_UserIdModel model = getChatRoomData();
            String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            if (className.equals("com.appzone.dukkan.activities_fragments.activity_chat.ChatActivity")) {
                if (model != null)
                {

                    int room_id = model.getRoomId();

                    if (room_id == Integer.parseInt(map.get("room_id")) && userModel.getUser().getId()==Integer.parseInt(map.get("receiver_id")))
                    {
                        updateMessages(map);
                    } else
                        {
                            sendNewMsg(map,builder,channel);
                        }
                }

            } else {


                if (userModel.getUser().getId() == Integer.parseInt(map.get("receiver_id"))) {
                    sendNewMsg(map,builder,channel);
                }
            }




        }


        /////////////////////////////////////////////client_send_new_order/////////////////////////
        else if (notification_type.equals(String.valueOf(Tags.NEW_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.delegate;


            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("client_name"));
            builder.setContentText(getString(R.string.new_order));

            Intent intent = new Intent(this, DelegateHomeActivity.class);
            intent.putExtra("status", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.notify(1, builder.build());

            }

            ///////////////////delegate_accepted_order///////////////////////////
        } else if (notification_type.equals(String.valueOf(Tags.ACCEPTED_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.delegate_accept_order));
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_accept_order);


            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    Log.e("6666", "1111111");

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    if (manager != null) {
                        Log.e("d11111111", "1111111");
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(orderStatusModel);
                        EventBus.getDefault().post(new PageModel(0));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 100);

        }


        /////////////////////////////////////collecting order///////////////////////
        else if (notification_type.equals(String.valueOf(Tags.COLLECTING_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.collecting_order));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_collect_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(orderStatusModel);
                        EventBus.getDefault().post(new PageModel(1));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }

        ////////////////////////order_collected/////////////////////

        else if (notification_type.equals(String.valueOf(Tags.COLLECTED_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.order_collected));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_already_collect_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(1));
                        EventBus.getDefault().post(orderStatusModel);

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }


        ///////////////////////////////////////delivering/////////////////////
        else if (notification_type.equals(String.valueOf(Tags.DELIVERING_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.delivering_order));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);
            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_delivering_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(1));
                        EventBus.getDefault().post(orderStatusModel);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }

        ///////////////////////////////////////delivered order/////////////////////
        else if (notification_type.equals(String.valueOf(Tags.DELIVERED_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            String delegate_name = map.get("delegate_name");
            String delegate_avatar = map.get("delegate_avatar");
            int receiver_id = Integer.parseInt(map.get("receiver_id"));
            int delegate_id = Integer.parseInt(map.get("delegate_id"));

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.order_delivered));

            final NotificationRateModel notificationRateModel = new NotificationRateModel(delegate_name,delegate_avatar,receiver_id,delegate_id);

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 3);
            intent.putExtra("rate_data",notificationRateModel);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_delivered_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(2));
                        EventBus.getDefault().post(notificationRateModel);
                        EventBus.getDefault().post(orderStatusModel);

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }


        ////////////////////////////////////cancel order///////////////////////

        else if (notification_type.equals(String.valueOf(Tags.TYPING_MESSAGE_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            String CHANNEL_ID = "channel_id_02";
            CharSequence CHANNEL_NAME = "my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );

            channel.setShowBadge(true);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setChannelId(CHANNEL_ID);

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.ord_cancel_sent_again));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 4);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(0));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);


        }

        ///////////////////////////////////typing///////////////////////////////////////
        else if (notification_type.equals(String.valueOf(Tags.TYPING_MESSAGE_NOTIFICATION))) {

            int room_id = Integer.parseInt(map.get("room_id"));
            int receiver_id = Integer.parseInt(map.get("receiver_id"));
            int status = Integer.parseInt(map.get("status"));


            ChatRoom_UserIdModel model = getChatRoomData();
            if (model != null) {
                if (room_id == getChatRoomData().getRoomId()) {
                    TypingModel typingModel = new TypingModel(room_id, receiver_id, status);
                    EventBus.getDefault().post(typingModel);
                }
            }


        }



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNewMsg(final Map<String, String> map, NotificationCompat.Builder builder, NotificationChannel channel) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int msg_id = Integer.parseInt(map.get("msg_id"));
        int room_id = Integer.parseInt(map.get("room_id"));
        int sender_id = Integer.parseInt(map.get("sender_id"));
        int receiver_id = Integer.parseInt(map.get("receiver_id"));
        int msg_type = Integer.parseInt(map.get("msg_type"));
        String user_name = map.get("user_name");
        String user_phone = map.get("user_phone");

        double rate = Double.parseDouble(map.get("user_rate"));
        String user_avatar = map.get("user_avatar");
        String msg = map.get("msg");

        long msg_time = Long.parseLong(map.get("msg_time")) * 1000;

        builder.setContentTitle(user_name);
        builder.setContentText(msg);

        final MessageModel messageModel = new MessageModel(room_id, sender_id, receiver_id, msg, msg_type, msg_time);
        messageModel.setId(msg_id);


        if (user_avatar.equals("0")) {

            ////////////////////////////////////////
            String ph_alter = map.get("user_alternative_phone");

            UserChatModel userChatModel = new UserChatModel(sender_id, room_id, user_name, user_phone, "", Tags.user_client, 0);
            userChatModel.setAlter_phone(ph_alter);

            Intent intent = new Intent(this, ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user_chat_data", userChatModel);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            if (manager != null) {

                manager.createNotificationChannel(channel);
                manager.notify(1, builder.build());


            }
        } else {
            UserChatModel userChatModel = new UserChatModel(sender_id, room_id, user_name, user_phone, user_avatar, Tags.user_delegate, rate);
            String ph_alter = map.get("user_alternative_phone");
            userChatModel.setAlter_phone(ph_alter);


            Intent intent = new Intent(this, ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user_chat_data", userChatModel);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.notify(1, builder.build());

            }

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper())
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).into(target);

                        }
                    }, 1);

        }

    }

    private void updateMessages(Map<String, String> map) {

        int msg_id = Integer.parseInt(map.get("msg_id"));
        int room_id = Integer.parseInt(map.get("room_id"));
        int sender_id = Integer.parseInt(map.get("sender_id"));
        int receiver_id = Integer.parseInt(map.get("receiver_id"));
        int msg_type = Integer.parseInt(map.get("msg_type"));
        String msg = map.get("msg");

        long msg_time = Long.parseLong(map.get("msg_time")) * 1000;


        final MessageModel messageModel = new MessageModel(room_id, sender_id, receiver_id, msg, msg_type, msg_time);
        messageModel.setId(msg_id);


        EventBus.getDefault().post(messageModel);

    }



    private void sendNewMsgNative(final Map<String, String> map, NotificationCompat.Builder builder) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int msg_id = Integer.parseInt(map.get("msg_id"));
        int room_id = Integer.parseInt(map.get("room_id"));
        int sender_id = Integer.parseInt(map.get("sender_id"));
        int receiver_id = Integer.parseInt(map.get("receiver_id"));
        int msg_type = Integer.parseInt(map.get("msg_type"));
        String user_name = map.get("user_name");
        String user_phone = map.get("user_phone");

        double rate = Double.parseDouble(map.get("user_rate"));
        String user_avatar = map.get("user_avatar");
        String msg = map.get("msg");

        long msg_time = Long.parseLong(map.get("msg_time")) * 1000;

        builder.setContentTitle(user_name);
        builder.setContentText(msg);

        final MessageModel messageModel = new MessageModel(room_id, sender_id, receiver_id, msg, msg_type, msg_time);
        messageModel.setId(msg_id);


        if (user_avatar.equals("0")) {

            ////////////////////////////////////////
            String ph_alter = map.get("user_alternative_phone");

            UserChatModel userChatModel = new UserChatModel(sender_id, room_id, user_name, user_phone, "", Tags.user_client, 0);
            userChatModel.setAlter_phone(ph_alter);

            Intent intent = new Intent(this, ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user_chat_data", userChatModel);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            if (manager != null) {

                manager.notify(1, builder.build());


            }
        } else {
            UserChatModel userChatModel = new UserChatModel(sender_id, room_id, user_name, user_phone, user_avatar, Tags.user_delegate, rate);
            String ph_alter = map.get("user_alternative_phone");
            userChatModel.setAlter_phone(ph_alter);


            Intent intent = new Intent(this, ChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user_chat_data", userChatModel);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            if (manager != null) {
                manager.notify(1, builder.build());

            }

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper())
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("user_avatar"))).into(target);

                        }
                    }, 1);

        }

    }



    private void createNotificationNative(final Map<String, String> map) {
        UserModel userModel = getUserData();

        String notification_type = map.get("type");





        /////////////////////////////////////// chat ////////////////////////////////
        if (notification_type.equals(String.valueOf(Tags.NEW_MESSAGE_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;

            if (userModel.getUser().getId() == Integer.parseInt(map.get("receiver_id"))) {

                if (userModel.getUser().getRole().equals(Tags.user_client))
                {
                    sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;
                }else
                {
                    sound_path = "android.resource://" + getPackageName() + "/" + R.raw.delegate;

                }
            }

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            ChatRoom_UserIdModel model = getChatRoomData();
            String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();

            if (className.equals("com.appzone.dukkan.activities_fragments.activity_chat.ChatActivity")) {
                if (model != null)
                {

                    int room_id = model.getRoomId();

                    if (room_id == Integer.parseInt(map.get("room_id")) && userModel.getUser().getId()==Integer.parseInt(map.get("receiver_id")))
                    {
                        updateMessages(map);
                    } else
                    {
                        sendNewMsgNative(map,builder);
                    }
                }

            } else {


                if (userModel.getUser().getId() == Integer.parseInt(map.get("receiver_id"))) {
                    sendNewMsgNative(map,builder);
                }
            }




        }


        /////////////////////////////////////////////client_send_new_order/////////////////////////
        else if (notification_type.equals(String.valueOf(Tags.NEW_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.delegate;

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("client_name"));
            builder.setContentText(getString(R.string.new_order));

            Intent intent = new Intent(this, DelegateHomeActivity.class);
            intent.putExtra("status", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            if (manager != null) {
                manager.notify(1, builder.build());

            }

            ///////////////////delegate_accepted_order///////////////////////////
        } else if (notification_type.equals(String.valueOf(Tags.ACCEPTED_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.delegate_accept_order));
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_accept_order);


            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    Log.e("6666", "1111111");

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    if (manager != null) {
                        Log.e("d11111111", "1111111");
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(orderStatusModel);
                        EventBus.getDefault().post(new PageModel(0));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 100);

        }


        /////////////////////////////////////collecting order///////////////////////
        else if (notification_type.equals(String.valueOf(Tags.COLLECTING_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.collecting_order));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_collect_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(orderStatusModel);
                        EventBus.getDefault().post(new PageModel(1));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }

        ////////////////////////order_collected/////////////////////

        else if (notification_type.equals(String.valueOf(Tags.COLLECTED_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.order_collected));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_already_collect_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(1));
                        EventBus.getDefault().post(orderStatusModel);

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }


        ///////////////////////////////////////delivering/////////////////////
        else if (notification_type.equals(String.valueOf(Tags.DELIVERING_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.delivering_order));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 2);
            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_delivering_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(1));
                        EventBus.getDefault().post(orderStatusModel);
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }

        ///////////////////////////////////////delivered order/////////////////////
        else if (notification_type.equals(String.valueOf(Tags.DELIVERED_ORDER_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            String delegate_name = map.get("delegate_name");
            String delegate_avatar = map.get("delegate_avatar");
            int receiver_id = Integer.parseInt(map.get("receiver_id"));
            int delegate_id = Integer.parseInt(map.get("delegate_id"));

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.order_delivered));

            final NotificationRateModel notificationRateModel = new NotificationRateModel(delegate_name,delegate_avatar,receiver_id,delegate_id);

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 3);
            intent.putExtra("rate_data",notificationRateModel);

            final OrderStatusModel orderStatusModel = new OrderStatusModel(Tags.status_delegate_delivered_order);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(2));
                        EventBus.getDefault().post(notificationRateModel);
                        EventBus.getDefault().post(orderStatusModel);

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);
        }


        ////////////////////////////////////cancel order///////////////////////

        else if (notification_type.equals(String.valueOf(Tags.TYPING_MESSAGE_NOTIFICATION))) {

            String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.client;


            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));

            final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            builder.setContentTitle(map.get("delegate_name"));
            builder.setContentText(getString(R.string.ord_cancel_sent_again));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("status", 4);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager != null) {
                        manager.notify(1, builder.build());
                        EventBus.getDefault().post(new PageModel(0));

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(FireBaseMessaging.this).load(Uri.parse(Tags.IMAGE_URL + map.get("delegate_avatar"))).into(target);

                }
            }, 1);


        }

        ///////////////////////////////////typing///////////////////////////////////////
        else if (notification_type.equals(String.valueOf(Tags.TYPING_MESSAGE_NOTIFICATION))) {

            int room_id = Integer.parseInt(map.get("room_id"));
            int receiver_id = Integer.parseInt(map.get("receiver_id"));
            int status = Integer.parseInt(map.get("status"));


            ChatRoom_UserIdModel model = getChatRoomData();
            if (model != null) {
                if (room_id == getChatRoomData().getRoomId()) {
                    TypingModel typingModel = new TypingModel(room_id, receiver_id, status);
                    EventBus.getDefault().post(typingModel);
                }
            }


        }



    }





    private String getSession() {
        return preferences.getSession(this);
    }

    private ChatRoom_UserIdModel getChatRoomData() {
        return preferences.getChatUserData(this);
    }

    private UserModel getUserData() {
        return preferences.getUserData(this);
    }
}
