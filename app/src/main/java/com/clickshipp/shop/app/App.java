package com.clickshipp.shop.app;


import android.content.Context;
import androidx.multidex.MultiDexApplication;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.onesignal.OneSignal;
import com.clickshipp.shop.constant.ConstantValues;
import com.clickshipp.shop.databases.DB_Handler;
import com.clickshipp.shop.databases.DB_Manager;
import com.clickshipp.shop.models.GeoFencing.GeofencingList;
import com.clickshipp.shop.models.coupons_model.CouponDetails;
import com.clickshipp.shop.models.order_model.OrderDetails;
import com.clickshipp.shop.models.drawer_model.Drawer_Items;
import com.clickshipp.shop.models.banner_model.BannerDetails;
import com.clickshipp.shop.models.category_model.CategoryDetails;
import com.clickshipp.shop.models.device_model.AppSettingsDetails;
import com.clickshipp.shop.models.order_model.OrderShippingMethod;
import com.clickshipp.shop.models.points.PointsList;
import com.clickshipp.shop.models.product_model.ProductDetails;
import com.clickshipp.shop.models.user_model.UserDetails;
import com.clickshipp.shop.utils.ExampleNotificationOpenedHandler;

import io.branch.referral.Branch;


/**
 * App extending Application, is used to save some Lists and Objects with Application Context.
 **/


public class App extends MultiDexApplication {

    // Application Context
    private static Context context;
    private static DB_Handler db_handler;


    private List<Drawer_Items> drawerHeaderList;
    private Map<Drawer_Items, List<Drawer_Items>> drawerChildList;
    
    
    private AppSettingsDetails appSettings;
    private List<BannerDetails> bannersList = new ArrayList<>();
    private List<CategoryDetails> categoriesList = new ArrayList<>();
    private List<PointsList> pointsLists = new ArrayList<>();
    private List<GeofencingList> geoFencingList = new ArrayList<>();
    
    
    private UserDetails userDetails = new UserDetails();
    private OrderDetails orderDetails = new OrderDetails();
    
    private List<ProductDetails> productDetails = new ArrayList<>();
    private OrderShippingMethod shippingService = new OrderShippingMethod();
    private UserDetails shippingAddress = new UserDetails();
    private UserDetails billingAddress = new UserDetails();
    String customerID;
    
    private CouponDetails couponDetails = new CouponDetails();
    @Override
    public void onCreate() {
        super.onCreate();

        // set App Context
        context = this.getApplicationContext();

        // initialize DB_Handler and DB_Manager
        db_handler = new DB_Handler();
        DB_Manager.initializeInstance(db_handler);
    
        // Get the CustomerID from SharedPreferences
        ConstantValues.USER_ID = customerID = this.getContext().getSharedPreferences("UserInfo", getContext().MODE_PRIVATE).getString("userID", "");
    
    
        if (ConstantValues.DEFAULT_NOTIFICATION.equalsIgnoreCase("onesignal")) {
        
            OneSignal.sendTag("app", "AndroidWoocommerce20");
        
            // initialize OneSignal
            OneSignal.startInit(this)
                    .filterOtherGCMReceivers(true)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.InAppAlert)
                    .unsubscribeWhenNotificationsAreDisabled(false)
                    .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler(context))
                    .init();
        
        }
    
        // Initialize the Branch object
        Branch.getAutoInstance(getApplicationContext());
    }

    
    //*********** Returns Application Context ********//

    public static Context getContext() {
        return context;
    }

    
    public List<Drawer_Items> getDrawerHeaderList() {
        return drawerHeaderList;
    }

    public void setDrawerHeaderList(List<Drawer_Items> drawerHeaderList) {
        this.drawerHeaderList = drawerHeaderList;
    }

    public Map<Drawer_Items, List<Drawer_Items>> getDrawerChildList() {
        return drawerChildList;
    }

    public void setDrawerChildList(Map<Drawer_Items, List<Drawer_Items>> drawerChildList) {
        this.drawerChildList = drawerChildList;
    }
    
    
    
    public AppSettingsDetails getAppSettingsDetails() {
        return appSettings;
    }
    
    public void setAppSettingsDetails(AppSettingsDetails appSettings) {
        this.appSettings = appSettings;
    }
    
    
    public List<BannerDetails> getBannersList() {
        return bannersList;
    }
    
    public void setBannersList(List<BannerDetails> bannersList) {
        this.bannersList = bannersList;
    }
    
    public List<CategoryDetails> getCategoriesList() {
        return categoriesList;
    }
    
    public void setCategoriesList(List<CategoryDetails> categoriesList) {
        this.categoriesList = categoriesList;
    }
    
    
    public List<PointsList> getPointsList() {
        return pointsLists;
    }
    
    public void setPointsList(List<PointsList> pointsList) {
        this.pointsLists = pointsList;
    }
    
    public OrderDetails getOrderDetails() {
        return orderDetails;
    }
    
    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }
    
    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }
    
    public UserDetails getUserDetails() {
        return userDetails;
    }
    
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    
    public OrderShippingMethod getShippingService() {
        return shippingService;
    }
    
    public void setShippingService(OrderShippingMethod shippingService) {
        this.shippingService = shippingService;
    }
    
    public CouponDetails getCouponDetails() {
        return couponDetails;
    }
    
    public void setCouponDetails(CouponDetails couponDetails) {
        this.couponDetails = couponDetails;
    }
    
    
    public List<GeofencingList> getGeoFencingList() {
        return geoFencingList;
    }
    
    public void setGeoFencingList(List<GeofencingList> geoFencingList) {
        this.geoFencingList = geoFencingList;
    }
}

