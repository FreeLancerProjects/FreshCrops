package com.appzone.freshcrops.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.appzone.freshcrops.models.ChatRoom_UserIdModel;
import com.appzone.freshcrops.models.OrderItem;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.tags.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Preferences {
    private static Preferences instance=null;

    private Preferences() {
    }

    public static Preferences getInstance()
    {
        if (instance==null)
        {
            instance = new Preferences();
        }
        return instance;
    }

    public void create_update_userData(Context context, UserModel userModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",userData);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel getUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data","");
        UserModel userModel = gson.fromJson(user_data,UserModel.class);
        return userModel;
    }

    public void create_update_session(Context context,String session)
    {
        SharedPreferences preferences = context.getSharedPreferences("data_session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("state",session);
        editor.apply();

    }

    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("data_session",Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }
    public void addRecentSearchQuery(Context context ,String query)
    {

        SharedPreferences preferences = context.getSharedPreferences("data_query",Context.MODE_PRIVATE);
        String queries = preferences.getString("queries","");
        if (!TextUtils.isEmpty(queries))
        {

            try {
                List<String> queriesResults = new Gson().fromJson(queries,new TypeToken<List<String>>(){}.getType());

                if (!queriesResults.contains(query))
                {
                    queriesResults.add(0,query);

                    if (queriesResults.size()>10)
                    {
                        queriesResults.remove(queriesResults.size()-1);
                    }

                    SaveQueryList(queriesResults,preferences);

                }

            }catch (Exception e)
            {
                Log.e("Error",e.getMessage()+"Error");
            }
        }else
            {
                List<String> queriesList = new ArrayList<>();
                queriesList.add(0,query);
                SaveQueryList(queriesList,preferences);
            }


    }
    private void SaveQueryList(List<String> queryList,SharedPreferences preferences)
    {
        SharedPreferences.Editor editor = preferences.edit();
        String gson = new Gson().toJson(queryList);
        editor.putString("queries",gson);
        editor.apply();
    }
    public List<String> getAllQueries(Context context)
    {
        List<String> queriesList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("data_query",Context.MODE_PRIVATE);
        String queries = preferences.getString("queries","");
        if (!TextUtils.isEmpty(queries))
        {
            List<String> qList = new Gson().fromJson(queries,new TypeToken<List<String>>(){}.getType());
            queriesList.addAll(qList);
            return queriesList;

        }

        return queriesList;


    }
    public void saveVisitedProductIds(Context context,String id)
    {
        SharedPreferences preferences = context.getSharedPreferences("data_id",Context.MODE_PRIVATE);
        String gson = preferences.getString("ids","");
        if (!TextUtils.isEmpty(gson))
        {
            List<String> visitedIds = new Gson().fromJson(gson,new TypeToken<List<String>>(){}.getType());


            if (!visitedIds.contains(id))
            {
                visitedIds.add(id);
                if (visitedIds.size()>10)
                {
                    visitedIds.remove(visitedIds.size()-1);
                }

                String gsonArray = new Gson().toJson(visitedIds);
                SaveVisitedIds(gsonArray,preferences);

            }
        }else
            {
                String [] idsArray ={id};
                String gsonArray = new Gson().toJson(idsArray);
                SaveVisitedIds(gsonArray,preferences);
            }
    }
    public List<String> getAllVisitedIds(Context context)
    {
        List<String> idsList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("data_id",Context.MODE_PRIVATE);
        String gson = preferences.getString("ids","");
        if (!TextUtils.isEmpty(gson))
        {
            List<String> ids = new Gson().fromJson(gson,new TypeToken<List<String>>(){}.getType());
            idsList.addAll(ids);
        }
        return idsList;
    }
    private void SaveVisitedIds(String gsonArray, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ids",gsonArray);
        editor.apply();
    }

    public void ClearData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        SharedPreferences preferences_session = context.getSharedPreferences("data_session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_session = preferences_session.edit();
        editor_session.clear();
        editor_session.apply();

        SharedPreferences preferences_search = context.getSharedPreferences("data_query",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_search = preferences_search.edit();
        editor_search.clear();
        editor_search.apply();

        SharedPreferences preferences_visited = context.getSharedPreferences("data_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_visited = preferences_visited.edit();
        editor_visited.clear();
        editor_visited.apply();

        SharedPreferences preferences_chat = context.getSharedPreferences("data_chat",Context.MODE_PRIVATE);
        SharedPreferences.Editor preferences_chat_editor = preferences_chat.edit();
        preferences_chat_editor.clear();
        preferences_chat_editor.apply();





        clearCart(context);


    }



    public void create_update_chat_user_id_room_id(Context context, ChatRoom_UserIdModel chatRoom_userIdModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("data_chat",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String data = new Gson().toJson(chatRoom_userIdModel);
        editor.putString("data",data);
        editor.apply();
    }

    public ChatRoom_UserIdModel getChatUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("data_chat",Context.MODE_PRIVATE);
        ChatRoom_UserIdModel model = new Gson().fromJson(preferences.getString("data",""),ChatRoom_UserIdModel.class);
        return model ;
    }

    public void SaveCartItemProducts(Context context,List<OrderItem> orderItemList)
    {
        SharedPreferences preferences = context.getSharedPreferences("data_cart",Context.MODE_PRIVATE);

        String gson = new Gson().toJson(orderItemList);

        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("items",gson);
        edit.apply();
    }


    public List<OrderItem> getCartItems(Context context)
    {
        List<OrderItem> orderItemList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("data_cart",Context.MODE_PRIVATE);

        String gson = preferences.getString("items","");

        if (!TextUtils.isEmpty(gson))
        {
            orderItemList = new Gson().fromJson(gson,new TypeToken<List<OrderItem>>(){}.getType());
        }
        return orderItemList;
    }
    public void clearCart(Context context) {
        SharedPreferences preferences_cart = context.getSharedPreferences("data_cart",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit_cart = preferences_cart.edit();
        edit_cart.clear();
        edit_cart.apply();
    }
}
