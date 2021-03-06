package com.clickshipp.shop.network;

import com.clickshipp.shop.models.GeoFencing.Geofencing;
import com.clickshipp.shop.models.currency_model.CurrencyModel;
import com.clickshipp.shop.models.download.DownloadsModel;
import com.clickshipp.shop.models.language_model.LanguageDetails;
import com.clickshipp.shop.models.notifications.Notification;
import com.clickshipp.shop.models.payment_model.GetAllPaymentMethod;
import com.clickshipp.shop.models.points.PointConversionModel;
import com.clickshipp.shop.models.points.PointsModel;
import com.clickshipp.shop.models.post_model.PostDetails;
import com.clickshipp.shop.models.post_model.PostMedia;
import com.clickshipp.shop.models.product_filters_model.ProductFilters;
import com.clickshipp.shop.models.product_model.FilteredProductList;
import com.clickshipp.shop.models.product_model.ProductReviews;
import com.clickshipp.shop.models.seller_detail_model.SellerDetailModel;
import com.clickshipp.shop.models.seller_detail_model.SellerInfoWC;
import com.clickshipp.shop.models.seller_detail_model.SellerProductsIDS;
import com.clickshipp.shop.models.shipping_model.ShippingMethods;
import com.clickshipp.shop.models.shipping_model.ShippingZone;
import com.clickshipp.shop.models.shipping_model.ShippingZoneLocations;
import com.clickshipp.shop.models.terms_and_policy_model.PolicyResponse;
import com.clickshipp.shop.models.user_model.Nonce;
import com.clickshipp.shop.models.user_model.UpdateUser;
import com.clickshipp.shop.models.user_model.UserData;
import com.clickshipp.shop.models.user_model.UserDetails;
import com.clickshipp.shop.models.post_model.PostCategory;
import com.clickshipp.shop.models.banner_model.BannerData;
import com.clickshipp.shop.models.order_model.OrderDetails;
import com.clickshipp.shop.models.coupons_model.CouponDetails;
import com.clickshipp.shop.models.contact_model.ContactUsData;
import com.clickshipp.shop.models.product_model.ProductDetails;
import com.clickshipp.shop.models.category_model.CategoryDetails;
import com.clickshipp.shop.models.device_model.AppSettingsDetails;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Field;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.FormUrlEncoded;


/**
 * APIRequests contains all the Network Request Methods with relevant API Endpoints
 **/

public interface APIRequests {


    //******************** User Data ********************//

    @GET("api/get_nonce")
    Call<Nonce> getNonce(@QueryMap Map<String, String> args);


    @FormUrlEncoded
    @POST("api/AndroidAppUsers/android_register")
    Call<UserData> processRegistration(@Field("insecure") String insecure,
                                       @Field("display_name") String name,
                                       @Field("username") String username,
                                       @Field("email") String email_address,
                                       @Field("password") String password,
                                       @Field("nonce") String nonce);


    @FormUrlEncoded
    @POST("api/AndroidAppUsers/android_generate_cookie")
    Call<UserData> processLogin(@Field("insecure") String insecure,
                                @Field("username") String customers_username,
                                @Field("password") String customers_password);


    @FormUrlEncoded
    @POST("api/AndroidAppUsers/android_fb_connect")
    Call<UserData> facebookRegistration(@Field("insecure") String insecure,
                                        @Field("access_token") String access_token);


    @FormUrlEncoded
    @POST("api/AndroidAppUsers/android_google_login")
    Call<UserData> googleRegistration(@Field("insecure") String insecure,
                                      @Field("idToken") String idToken,
                                      @Field("userId") String userId,
                                      @Field("email") String email,
                                      @Field("givenName") String givenName,
                                      @Field("familyName") String familyName,
                                      @Field("displayName") String displayName,
                                      @Field("imageUrl") String imageUrl);

    @FormUrlEncoded
    @POST("api/AndroidAppUsers/android_forgot_password")
    Call<UserData> processForgotPassword(@Field("insecure") String insecure,
                                         @Field("email") String customers_email_address);


    @GET("wp-json/wc/v2/customers/{id}")
    Call<UserDetails> getUserInfo(@Path("id") String user_id
    );


    @POST("api/AndroidAppUsers/android_update_user_profile")
    Call<UpdateUser> updateCustomerInfo(@QueryMap Map<String, String> args);

    @PUT("wp-json/wc/v2/customers/{id}")
    Call<UserDetails> updateCustomerAddress(@Path("id") String customerId,
                                            @Body UserDetails requestModelObject);

    //******************** Category Data ********************//

    @GET("wp-json/wc/v2/products/categories")
    Call<List<CategoryDetails>> getAllCategories(@QueryMap Map<String, String> args);


    @GET("wp-json/wc/v2/products/categories/{id}")
    Call<CategoryDetails> getSingleCategory(@Path("id") String category_id);


    //******************** Product Data ********************//

    @GET("wp-json/wc/v2/products")
    Call<List<ProductDetails>> getAllProducts(@QueryMap Map<String, String> args);

    @FormUrlEncoded
    @POST("api/androidappsettings/android_filter_products")
    Call<FilteredProductList> getFilteredProducts(@Field("insecure") String insecure,
                                                  @QueryMap Map<String, String> args);

    @GET("wp-json/wc/v2/products/{id}")
    Call<ProductDetails> getSingleProduct(@Path("id") String product_id);

    @GET("wp-json/wc/v2/products/{id}")
    Call<ProductDetails> getSingleProduct(@Path("id") String product_id,
                                          @Query("currency") String currency);


    @GET("wp-json/wc/v2/products/{id}/variations")
    Call<List<ProductDetails>> getVariations(@Path("id") long product_id);


    @GET("wp-json/wc/v2/products/{id}/variations")
    Call<List<ProductDetails>> searchVariation(@Path("id") String product_id,
                                               @Query("search") String searchValue);


    @GET("wp-json/wc/v2/products/{id}/reviews")
    Call<List<ProductReviews>> getProductReviews(@Path("id") String product_id);


    @FormUrlEncoded
    @POST("api/AndroidAppSettings/android_create_product_review")
    Call<UserData> addProductReview(@Field("insecure") String insecure,
                                    @Field("nonce") String nonce,
                                    @Field("product_id") String product_id,
                                    @Field("rate_star") String rate_star,
                                    @Field("author_name") String author_name,
                                    @Field("author_email") String author_email,
                                    @Field("author_content") String author_content);


    //******************** News Data ********************//
    @FormUrlEncoded
    @POST("api/androidappsettings/android_get_attributes")
    Call<ProductFilters> getFilters(@Field("insecure") String insecure,
                                    @QueryMap Map<String, String> args);


    // ******************** News Data ********************//

    @GET("wp-json/wp/v2/posts")
    Call<List<PostDetails>> getAllPosts(@QueryMap Map<String, String> args);

    @GET("wp-json/wp/v2/posts/{id}/{lang}")
    Call<PostDetails> getSinglePost(@Path("id") String post_id,
                                    @Path("lang") String lang);

    @GET("wp-json/wp/v2/categories")
    Call<List<PostCategory>> getPostCategories(@QueryMap Map<String, String> args);


    @GET("wp-json/wp/v2/media/{id}/{lang}")
    Call<PostMedia> getPostMedia(@Path("id") String post_id,
                                 @Path("lang") String lang);


    /*Get Geofencing Request*/

    @GET("api/androidappsettings/android_get_geofencing_posts/?insecure=cool")
    Call<Geofencing> getGeoFencing();


    //******************** Shipping Data ********************//

    @GET("wp-json/wc/v2/shipping/zones")
    Call<List<ShippingZone>> getShippingZones();


    @GET("wp-json/wc/v2/shipping/zones/{id}/locations")
    Call<List<ShippingZoneLocations>> getShippingZoneLocations(@Path("id") String zone_id);


    @GET("wp-json/wc/v2/shipping/zones/{id}/methods")
    Call<List<ShippingMethods>> getShippingMethods(@Path("id") String zone_id);


    @GET("wp-json/wc/v2/shipping_methods")
    Call<List<ShippingMethods>> getDefaultShippingMethods();


    //******************** Order Data ********************//

    @FormUrlEncoded
    @POST("api/AndroidAppSettings/android_data_link")
    Call<String> placeOrder(@Field("insecure") String insecure,
                            @Field("order_link") String order_data);

    @GET("wp-json/wc/v2/orders")
    Call<List<OrderDetails>> getAllOrders(@QueryMap Map<String, String> args);


    @GET("wp-json/wc/v2/orders/{id}")
    Call<OrderDetails> getSingleOrder(@Path("id") String order_id,
                                      @Query("currency") String currency);


    @PUT("wp-json/wc/v2/orders/{id}")
    Call<OrderDetails> updateOrder(@Path("id") String order_id,
                                   @Query("status") String status);


    //******************** Coupon Data ********************//

    @GET("wp-json/wc/v2/coupons")
    Call<List<CouponDetails>> getCouponInfo(@QueryMap Map<String, String> args);

    @GET("wp-json/wc/v2/coupons/{id}")
    Call<List<CouponDetails>> getSingleCoupon(@Path("id") int coupon_id);


    //******************** Banner Data ********************//

    @GET("api/AndroidAppSettings/android_get_all_banners/?insecure=cool")
    Call<BannerData> getBanners();


    //******************** Contact Us Data ********************//

    @GET("api/AndroidAppUsers/android_send_mail")
    Call<ContactUsData> contactUs(@QueryMap Map<String, String> args);


    //******************** App Settings Data ********************//

    @GET("api/AndroidAppSettings/android_get_all_settings/?insecure=cool")
    Call<AppSettingsDetails> getAppSetting();

    /***************  App Policy Terms  ****************/
    @GET("wp-json/wp/v2/pages")
    Call<List<PolicyResponse>> getPolicyTerms(@Query("include") String id);


    /*Get Downloads*/

    @GET("wp-json/wc/v2/customers/{id}/downloads")
    Call<List<DownloadsModel>> getDownload(@Path("id") String order_id);

    /*Get All Payment Methods*/

    @GET("wp-json/wc/v2/payment_gateways")
    Call<List<GetAllPaymentMethod>> getAllPaymentMethods();

    /*Calculate Tax For Checkout*/

    @GET("api/androidappsettings/android_get_tax/?insecure=cool")
    Call<Object> getTax(@Query("order") String order);


    // Get points list
    @GET("api/androidappusers/android_reward_points")
    Call<PointsModel> getPoints(@QueryMap Map<String, String> args);

    // Get Points Conversion Model

    @GET("api/androidappusers/android_reward_points_settings")
    Call<PointConversionModel> getPoints(@Query("insecure") String inscure);


    //Request Seller products IDS
    @GET("api/androidappsettings/android_vendor_products/?insecure=cool")
    Call<SellerProductsIDS> getAllSellerProductsIDS(@QueryMap Map<String, Integer> args);

    //******************** Request Seller products ********************//

    @GET("wp-json/wc/v2/products")
    Call<List<ProductDetails>> getAllSellerProducts(@QueryMap Map<String, String> args);

    //Request Seller Information for Dukan Plugin

    @GET("wp-json/dokan/v1/stores/{id}")
    Call<SellerDetailModel> getSellerInfo(@Path("id") String id
    );

    // Request Seller Information for WC vendor plugin
    @GET("api/androidappsettings/android_get_vendor_info")
    Call<SellerInfoWC> getSellerInfo(@QueryMap Map<String, String> args);


    //Request Languages Information
    @GET("api/appsettings/get_all_languages/?insecure=cool")
    Call<LanguageDetails> getLanguages();

    // Request Currency Information

    @GET("api/androidappsettings/android_all_currencies/?insecure=cool")
    Call<CurrencyModel> getCurrency();

    //Send Device Token to server
    @GET("api/androidappusers/android_register_device/?insecure=cool")
    Call<String> sendDeviceToken(@QueryMap Map<String, String> args);

    //Request for Notifications
    @GET("api/androidappusers/android_coupon_notification/?insecure=cool")
    Call<Notification> getNotificationList(@QueryMap Map<String, String> args);

    //Request for Notifications Update
    @GET("api/androidappusers/android_notification_update/?insecure=cool")
    Call<String> updateNotificationList(@QueryMap Map<String, String> args);


}

