package com.appzone.freshcrops.services;

import com.appzone.freshcrops.models.CouponModel;
import com.appzone.freshcrops.models.DelegateCollectingOrderUploadModel;
import com.appzone.freshcrops.models.DeliveryCostModel;
import com.appzone.freshcrops.models.GainModel;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.models.MessageModel;
import com.appzone.freshcrops.models.MessageModelList;
import com.appzone.freshcrops.models.OrderItemListModel;
import com.appzone.freshcrops.models.OrderToUploadModel;
import com.appzone.freshcrops.models.OrdersModel;
import com.appzone.freshcrops.models.ProductPaginationModel;
import com.appzone.freshcrops.models.ResponseModel;
import com.appzone.freshcrops.models.SimilarProductModel;
import com.appzone.freshcrops.models.TaxModel;
import com.appzone.freshcrops.models.Terms_Condition_Model;
import com.appzone.freshcrops.models.TypingModel;
import com.appzone.freshcrops.models.UpdateOrderStatusModel;
import com.appzone.freshcrops.models.UserModel;
import com.appzone.freshcrops.models.WeekOfferModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Services {

    @GET("api/main-categories")
    Call<MainCategory> getMainCategory();

    @GET("api/sub-category/{sub_category_id}")
    Call<ProductPaginationModel> getProductPagination(@Path("sub_category_id") int sub_category_id, @Query("page") int page_index);

    @GET("api/get-tax")
    Call<TaxModel> getTax();

    @GET("api/featured-products")
    Call<ProductPaginationModel> getOfferedProductPagination(@Query("page") int page_index);

    @GET("api/similar")
    Call<SimilarProductModel> getSimilarProducts(
            @Query("page") int page_index,
            @Query("product_id") int product_id,
            @Query("main_category_id") int main_category_id,
            @Query("sub_category_id") int sub_category_id
    );

    @GET("api/get-terms-and-conditions")
    Call<Terms_Condition_Model> getTermsConditions();

    @FormUrlEncoded
    @POST("api/send-contact")
    Call<ResponseModel> sendContactUs(@Field("name") String name,
                                      @Field("phone") String phone,
                                      @Field("message") String message
    );

    @FormUrlEncoded
    @POST("api/get-recent-search")
    Call<ProductPaginationModel> getRecentSearchProducts(@Field("ids[]") List<String> ids);

    @GET("api/search-products")
    Call<ProductPaginationModel> search(@Query("q") String query, @Query("page") int page_index);

    @GET("api/get-delivery-cost")
    Call<DeliveryCostModel> getDeliveryCost();

    @GET("api/coupons")
    Call<CouponModel> isCouponAvailable();

    @GET("api/week-features")
    Call<WeekOfferModel> getWeekOffers();

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> SignIn(@Field("phone") String phone,
                           @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/sign-up")
    Call<UserModel> SignUp_Client(@Field("name") String name,
                                  @Field("phone") String phone,
                                  @Field("password") String password,
                                  @Field("role") String role
    );

    @Multipart
    @POST("api/sign-up")
    Call<UserModel> SignUp_Delegate(@Part("name") RequestBody name,
                                    @Part("phone") RequestBody phone,
                                    @Part("password") RequestBody password,
                                    @Part("role") RequestBody role,
                                    @Part("gender") RequestBody gender,
                                    @Part MultipartBody.Part avatar,
                                    @Part MultipartBody.Part id_image,
                                    @Part MultipartBody.Part license,
                                    @Part MultipartBody.Part car_license,
                                    @Part MultipartBody.Part car_front,
                                    @Part MultipartBody.Part car_back

    );

    @FormUrlEncoded
    @POST("/api/forget")
    Call<ResponseModel> forgetPassword(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("api/logout")
    Call<ResponseBody> logout(@Field("token") String user_token);

    @FormUrlEncoded
    @POST("api/set-firebase-token")
    Call<ResponseBody> updateFireBaseToken(@Field("token") String user_token,
                                           @Field("fire_base_token") String fireBaseToken
    );

    @POST("api/orders")
    Call<OrdersModel.Order> uploadOrder(@Body OrderToUploadModel orderToUploadModel);

    @FormUrlEncoded
    @POST("/api/edit-profile")
    Call<UserModel> updatePhone(@Field("token") String user_token,
                                @Field("phone") String phone);

    @FormUrlEncoded
    @POST("/api/edit-profile")
    Call<UserModel> updateAlterPhone(@Field("token") String user_token,
                                     @Field("alternative_phone") String phone);

    @Multipart
    @POST("/api/edit-profile")
    Call<UserModel> updateImage(@Part("token") RequestBody user_token,
                                @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("/api/edit-profile")
    Call<UserModel> updatePassword(
            @Field("token") String user_token,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password);

    @GET("/api/orders")
    Call<OrdersModel> getOrders(@Query("token") String token,
                                @Query("type") String type
    );

    @FormUrlEncoded
    @POST("/api/accept-refuse-orders/{order_id}")
    Call<ResponseBody> Accept_Refuse_order(@Path("order_id") int order_id,
                                           @Field("token") String user_token,
                                           @Field("type") String type
    );

    @FormUrlEncoded
    @POST("/api/order-update_status/{order_id}")
    Call<ResponseBody> updateOrderStatus(@Path("order_id") int order_id,
                                         @Field("token") String token,
                                         @Field("status") int order_status);

    @Multipart
    @POST("/api/order-update_status/{order_id}")
    Call<ResponseBody> uploadBillPhoto_OrderStatus(@Path("order_id") int order_id,
                                                   @Part("token") RequestBody token,
                                                   @Part("status") RequestBody order_status,
                                                   @Part MultipartBody.Part bill_photo

    );

    @POST("/api/update-order-products")
    Call<ResponseBody> uploadCollectedProducts(@Body DelegateCollectingOrderUploadModel uploadModel);

    @GET("/api/repeat-order")
    Call<OrderItemListModel> getProductsToSendAgain(@Query("order_id") int order_id);

    @GET("/api/chat-room/{room_id}")
    Call<MessageModelList> getMessage(@Path("room_id") int room_id,
                                      @Query("token") String token,
                                      @Query("page") int page_index

    );

    @POST("/api/send-chat-message")
    Call<MessageModel> sendMessage(@Body MessageModel messageModel,
                                   @Query("token") String token
    );

    @POST("/api/chat-typing")
    Call<ResponseModel> typing(@Body TypingModel typingModel,
                               @Query("token") String token
    );

    @FormUrlEncoded
    @POST("/api/me")
    Call<UserModel> getUserData(@Field("token") String token);

    @GET("/api/get-max-gain")
    Call<GainModel> getMaxGain();

    @POST("/api/orders/{order_id}")
    Call<UpdateOrderStatusModel> updateOrder(@Path("order_id") int order_id,
                                             @Body OrderToUploadModel orderToUploadModel
    );

    @FormUrlEncoded
    @POST("/api/set-rate")
    Call<ResponseModel> addRate(@Field("client_id") int client_id,
                                @Field("delegate_id") int delegate_id,
                                @Field("rate") double rate,
                                @Field("comment") String comment
                                );
}
