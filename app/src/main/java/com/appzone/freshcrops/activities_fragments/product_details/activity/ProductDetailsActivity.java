package com.appzone.freshcrops.activities_fragments.product_details.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_show_image.Activity_Show_Image;
import com.appzone.freshcrops.adapters.SimilarProductsAdapter;
import com.appzone.freshcrops.adapters.Sizes_Prices_Adapter;
import com.appzone.freshcrops.adapters.SliderPagerAdapter;
import com.appzone.freshcrops.language_helper.LanguageHelper;
import com.appzone.freshcrops.models.MainCategory;
import com.appzone.freshcrops.models.OrderItem;
import com.appzone.freshcrops.models.ProductSize_OfferModel;
import com.appzone.freshcrops.singletone.OrderItemsSingleTone;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;


public class ProductDetailsActivity extends AppCompatActivity {
    private MainCategory.Products product;
    private List<MainCategory.Products> similarProductsList;
    private Button btn_add_to_cart;
    private ImageView image_back,image_increment,image_decrement;
    private ViewPager pager;
    private TabLayout tab;
    private TextView tv_name,tv_price_before_discount,tv_price_after_discount,tv_counter;
    private RecyclerView recView,recViewSimilarProducts;
    private RecyclerView.LayoutManager manager,similarManager;
    private SimilarProductsAdapter similarProductsAdapter;
    private Sizes_Prices_Adapter sizes_prices_adapter;
    private List<String> productImagesList;
    private Timer timer;
    private TimerTask timerTask;
    private List<String> imgsEndPointList;
    private SliderPagerAdapter sliderPagerAdapter;
    private String current_lang;
    private List<ProductSize_OfferModel> productSize_offerModelList;
    private String product_name="";
    private ScaleAnimation scaleAnimation;
    private CardView card_similar;
    private int counter = 1;
    private int lastSelectedPosition = -1;
    private ProductSize_OfferModel productSize_offerModel,alternative_productSize_offerModel = null;
    private int feature_id =-1;

    private OrderItemsSingleTone orderItemsSingleTone;
    /////////////////////////////////////////////////////////
    List<MainCategory.Products> productsList;
    private boolean canStartTimer=false;
    private OrderItem orderItem;
    //// result back;
    private boolean isAddedToCart = false;


    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initView();
        getDataFromIntent();
    }

    private void initView()
    {
        orderItemsSingleTone= OrderItemsSingleTone.newInstance();
        productSize_offerModelList = new ArrayList<>();
        imgsEndPointList = new ArrayList<>();
        productsList = new ArrayList<>();

        ////////////////////////////////////////////////////////
        productImagesList = new ArrayList<>();
        scaleAnimation = new ScaleAnimation(.7f,1f,.7f,1f,1f,1f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);

        image_back = findViewById(R.id.image_back);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);

        image_increment = findViewById(R.id.image_increment);
        image_decrement = findViewById(R.id.image_decrement);
        pager = findViewById(R.id.pager);
        tab = findViewById(R.id.tab);
        tab.setupWithViewPager(pager);
        tv_name = findViewById(R.id.tv_name);
        tv_price_before_discount = findViewById(R.id.tv_price_before_discount);
        tv_price_after_discount = findViewById(R.id.tv_price_after_discount);

        tv_counter = findViewById(R.id.tv_counter);
        recView = findViewById(R.id.recView);
        card_similar = findViewById(R.id.card_similar);

        recViewSimilarProducts = findViewById(R.id.recViewSimilarProducts);
        similarManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recViewSimilarProducts.setLayoutManager(similarManager);
        recView.setHasFixedSize(true);
        recViewSimilarProducts.setItemViewCacheSize(15);
        recViewSimilarProducts.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recViewSimilarProducts.setDrawingCacheEnabled(true);

        manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(manager);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });

        tv_price_before_discount.setPaintFlags(tv_price_before_discount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        image_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_increment.clearAnimation();
                image_decrement.clearAnimation();
                image_increment.startAnimation(scaleAnimation);
                increaseCounter();
            }
        });

        image_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_decrement.clearAnimation();
                image_increment.clearAnimation();
                image_decrement.startAnimation(scaleAnimation);
                decreaseCounter();
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PrepareItemToAddToCart();

            }
        });
    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            product = (MainCategory.Products) intent.getSerializableExtra("product");

            similarProductsList = (List<MainCategory.Products>) intent.getSerializableExtra("similar_products");

            UpdateUi(product);
            UpdateSimilarAdapterUI(similarProductsList);
        }
    }
    private void UpdateUi(MainCategory.Products product)
    {

        UpdateAdapter(product);
        if (current_lang.equals("ar"))
        {
            product_name = product.getName_ar();


        }else
        {
            product_name = product.getName_en();

        }

        UpdateProductName(product_name);
        UpdateViewPagerImages(product);



    }


    private void PrepareItemToAddToCart()
    {
        if (productSize_offerModelList.size()>0)
        {
            btn_add_to_cart.setVisibility(View.VISIBLE);

            if (productSize_offerModel!=null)
            {
                double total_price =(Double.parseDouble(productSize_offerModel.getPrice_after_discount()))*counter;

                if (product.getImage().size()>0)
                {
                    orderItem = new OrderItem(product.getImage().get(0),product.getId(),productSize_offerModel.getFeature_id(),productSize_offerModel.getId(),product.getName_ar(),product.getName_en(),productSize_offerModel.getAr_name(),productSize_offerModel.getEn_name(),counter,Double.parseDouble(productSize_offerModel.getPrice_after_discount()),total_price);
                }else
                {
                    orderItem = new OrderItem("",product.getId(),productSize_offerModel.getFeature_id(),productSize_offerModel.getId(),product.getName_ar(),product.getName_en(),productSize_offerModel.getAr_name(),productSize_offerModel.getEn_name(),counter,Double.parseDouble(productSize_offerModel.getPrice_after_discount()),total_price);

                }

                addProductToCard(orderItem);

            }else
            {
                Toast.makeText(ProductDetailsActivity.this, R.string.ch_size, Toast.LENGTH_SHORT).show();
            }
        }else
            {
                btn_add_to_cart.setVisibility(View.INVISIBLE);
            }

    }


    private void addProductToCard(OrderItem orderItem)
    {

        isAddedToCart = true;
        orderItemsSingleTone.AddProduct(orderItem);
        Toast.makeText(this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
    }


    private void UpdateSimilarAdapterUI(List<MainCategory.Products> similarProductsList)
    {

        if (similarProductsList.size()>0)
        {
            card_similar.setVisibility(View.VISIBLE);
            similarProductsAdapter = new SimilarProductsAdapter(this,similarProductsList);
            recViewSimilarProducts.setAdapter(similarProductsAdapter);




        }else
            {
                card_similar.setVisibility(View.GONE);
            }

    }

    private void UpdateViewPagerImages(MainCategory.Products product)
    {
        productImagesList.clear();
        imgsEndPointList.clear();
        productImagesList.addAll(product.getImage());

        if (productImagesList.size()>0)
        {
            for (String img : productImagesList)
            {
                Log.e("img",img);

                imgsEndPointList.add(img);

            }
            sliderPagerAdapter = new SliderPagerAdapter(imgsEndPointList,this);
            pager.setAdapter(sliderPagerAdapter);

            if (imgsEndPointList.size()>1)
            {
                canStartTimer = true;
                startTimer();

            }else
                {
                    canStartTimer = false;
                    if (timer!=null)
                    {
                        timer.purge();
                        timer.cancel();

                    }

                    if (timerTask!=null)
                    {
                        timerTask.cancel();
                    }
                }


        }
    }

    private void increaseCounter()
    {
        counter+=1;
        updateCounter(counter);
    }
    private void decreaseCounter()
    {
        counter-=1;
        if (counter <1)
        {
            counter = 1;
            updateCounter(counter);
        }else
            {
                updateCounter(counter);

            }
    }
    private void updateCounter(int counter)
    {
        tv_counter.setText(new DecimalFormat("##.##").format(counter));
    }
    private void UpdateProductName(String name)
    {
        tv_name.setText(name);
    }
    private void UpdateAdapter(MainCategory.Products product)
    {
        productSize_offerModelList.clear();
        productSize_offerModelList.addAll(updateProductPrices_SizesData(product));

        if (sizes_prices_adapter == null)
        {
            sizes_prices_adapter = new Sizes_Prices_Adapter(this,productSize_offerModelList);
            recView.setAdapter(sizes_prices_adapter);
        }else
            {
                sizes_prices_adapter.notifyDataSetChanged();
            }


    }

    private List<ProductSize_OfferModel> updateProductPrices_SizesData(MainCategory.Products product)
    {
        List<ProductSize_OfferModel> productSize_offerModelList = new ArrayList<>();

        for (MainCategory.Prices_Sizes prices_sizes : product.getSize_prices())
        {
            ProductSize_OfferModel productSize_offerModel = new ProductSize_OfferModel();
            productSize_offerModel.setId(prices_sizes.getId());
            productSize_offerModel.setAr_name(prices_sizes.getSize_ar());
            productSize_offerModel.setEn_name(prices_sizes.getSize_en());

            String price_after_discount = isOffer(product.getFeatures(),prices_sizes.getId());


            if (!TextUtils.isEmpty(price_after_discount))
            {
                productSize_offerModel.setOffer(true);

                productSize_offerModel.setPrice_before_discount(prices_sizes.getNet_price());
                productSize_offerModel.setPrice_after_discount(price_after_discount);
                productSize_offerModel.setDiscount(getDiscount(prices_sizes.getNet_price(),price_after_discount));
                productSize_offerModel.setFeature_id(feature_id);
                productSize_offerModelList.add(productSize_offerModel);
            }else
                {

                    productSize_offerModel.setOffer(false);
                    productSize_offerModel.setPrice_before_discount(prices_sizes.getNet_price());
                    productSize_offerModel.setPrice_after_discount(prices_sizes.getNet_price());
                    productSize_offerModel.setDiscount("0");
                    productSize_offerModel.setFeature_id(-1);
                    productSize_offerModelList.add(productSize_offerModel);

                }




        }
        return productSize_offerModelList;
    }
    private String getDiscount(String price_before_discount,String price_after_discount)
    {
        double diff = Double.parseDouble(price_before_discount) - Double.parseDouble(price_after_discount);
        double dis = (diff/Double.parseDouble(price_before_discount))*100;
        return String.valueOf((int)dis);
    }
    private String isOffer(List<MainCategory.Features> featuresList, int product_id)
    {
        String price_after_discount = "";
        for (MainCategory.Features features :featuresList )
        {
            if (features.getOld_price().getId()==product_id)
            {
                price_after_discount = features.getDiscount();
                feature_id = features.getFeature_id();
                break;
            }else
                {
                    price_after_discount="";
                }
        }

        return price_after_discount;

    }

    public void setItemForSize(ProductSize_OfferModel productSize_offerModel, int lastSelectedItem)
    {
        this.lastSelectedPosition = lastSelectedItem;

        this.productSize_offerModel = productSize_offerModel;

        updateProductPrices_SizesUI(productSize_offerModel);

    }
    public void setAlternativeItemForSize(ProductSize_OfferModel alternative_productSize_offerModel, int lastSelectedItem)
    {
        this.alternative_productSize_offerModel = alternative_productSize_offerModel;
    }
    private void updateProductPrices_SizesUI(ProductSize_OfferModel productSize_offerModel)
    {
        if (current_lang.equals("ar"))
        {
            UpdateProductName(product_name+" "+productSize_offerModel.getAr_name());

        }else
        {
            UpdateProductName(product_name+" "+productSize_offerModel.getEn_name());

        }

        if (productSize_offerModel.isOffer())
        {
            tv_price_before_discount.setVisibility(View.VISIBLE);
            tv_price_before_discount.setText(new DecimalFormat("##.##").format(Double.parseDouble(productSize_offerModel.getPrice_before_discount()))+" "+getString(R.string.rsa));
            tv_price_after_discount.setText(new DecimalFormat("##.##").format(Double.parseDouble(productSize_offerModel.getPrice_after_discount()))+" "+getString(R.string.rsa));
        }else
        {
            tv_price_before_discount.setVisibility(View.GONE);
            tv_price_after_discount.setText(new DecimalFormat("##.##").format(Double.parseDouble(productSize_offerModel.getPrice_before_discount()))+" "+getString(R.string.rsa));

        }
    }

    public void setItemForDetails(MainCategory.Products products)
    {

        lastSelectedPosition =-1;
        sizes_prices_adapter.UpdateSelectedItem(lastSelectedPosition);

        if (this.product.getId()!=products.getId())
        {
            this.counter = 1;
            updateCounter(counter);
        }

        this.product = products;
        tv_price_after_discount.setText("");
        tv_price_before_discount.setText("");
        UpdateUi(products);

    }

    public void setItemEndPoint(String endPoint) {
        StopTimer();
        Intent intent = new Intent(this, Activity_Show_Image.class);
        intent.putExtra("url",endPoint);
        startActivity(intent);

    }

    private void StopTimer() {
        try {
            timer.purge();
            timer.cancel();
            timerTask.cancel();
        }catch (Exception e){}
    }

    private void startTimer()
    {
        if (canStartTimer)
        {
            timer = new Timer();
            timerTask = new MyTimerTask();
            timer.scheduleAtFixedRate(timerTask,5000,6000);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
    }

    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager.getCurrentItem()<productImagesList.size()-1)
                    {
                        pager.setCurrentItem(pager.getCurrentItem()+1);
                    }else
                    {
                        pager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("price_size",productSize_offerModel);
        outState.putSerializable("product",product);

        outState.putInt("counter",counter);
        outState.putInt("lastSelectedPosition", lastSelectedPosition);

        Parcelable parcelable = manager.onSaveInstanceState();
        outState.putParcelable("state",parcelable);

        Parcelable parcelable2 = similarManager.onSaveInstanceState();
        outState.putParcelable("state2",parcelable2);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null)
        {
            productSize_offerModel = (ProductSize_OfferModel) savedInstanceState.getSerializable("price_size");
            if (productSize_offerModel!=null)
            {
                updateProductPrices_SizesUI(productSize_offerModel);

            }

            product = (MainCategory.Products) savedInstanceState.getSerializable("product");

            if (product!=null)
            {
                UpdateUi(product);

            }
            counter = savedInstanceState.getInt("counter",1);
            updateCounter(counter);

            lastSelectedPosition = savedInstanceState.getInt("lastSelectedPosition",-1);
            sizes_prices_adapter.UpdateSelectedItem(lastSelectedPosition);
            Parcelable parcelable = savedInstanceState.getParcelable("state");
            if (parcelable!=null)
            {
                manager.onRestoreInstanceState(parcelable);

            }
            Parcelable parcelable2 = savedInstanceState.getParcelable("state2");
            if (parcelable2!=null)
            {
                similarManager.onRestoreInstanceState(parcelable2);

            }


        }

    }

    @Override
    public void onBackPressed()
    {
        Back();

    }

    private void Back() {
        if (isAddedToCart)
        {
            Intent intent = getIntent();
            setResult(RESULT_OK,intent);
            finish();
        }else
        {
            finish();
        }
    }

    @Override
    public void onDestroy()
    {
        StopTimer();

        super.onDestroy();

    }


}
