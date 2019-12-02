package com.appzone.freshcrops.activities_fragments.activity_order_details.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Map_Order_Details extends Fragment implements OnMapReadyCallback {
    private static final String TAG_LAT="LAT";
    private static final String TAG_LNG="LNG";
    private LinearLayout ll_back;
    private ImageView image_back;
    private String current_lang;
    private OrderDetailsActivity activity;
    private double order_lat=0.0,order_lng=0.0;
    private SupportMapFragment fragment;
    private GoogleMap mMap;
    private final float zoom = 16.5f;
    private Marker marker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_map_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Map_Order_Details newInstance(double order_lat,double order_lng)
    {
        Bundle bundle = new Bundle();
        bundle.putDouble(TAG_LAT,order_lat);
        bundle.putDouble(TAG_LNG,order_lng);
        Fragment_Map_Order_Details fragment_map_order_details = new Fragment_Map_Order_Details();
        fragment_map_order_details.setArguments(bundle);
        return fragment_map_order_details;

    }
    private void initView(View view)
    {
        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        image_back = view.findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }

        ll_back = view.findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            order_lat = bundle.getDouble(TAG_LAT,0.0);
            order_lng = bundle.getDouble(TAG_LNG,0.0);

        }

        initMap();
    }
    private void initMap()
    {
        if (fragment==null)
        {
            fragment = SupportMapFragment.newInstance();
            fragment.getMapAsync(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.map,fragment).commit();
    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (googleMap!=null)
        {
            mMap = googleMap;
            mMap.setIndoorEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.maps));

            AddMarker(order_lat,order_lng);

        }
    }
    private void AddMarker(double order_lat, double order_lng)
    {
        if (marker == null)
        {
            marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(getBitmapIcon())).position(new LatLng(order_lat,order_lng)));
        }else
        {
            marker.setPosition(new LatLng(order_lat,order_lng));

        }


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(order_lat,order_lng),zoom));

    }
    private Bitmap getBitmapIcon()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.map_user);

        int req_width = 70;
        int req_height = 70;

        float postScale_width = (float) req_width/bitmap.getWidth();
        float postScale_height = (float) req_height/bitmap.getHeight();


        Matrix matrix = new Matrix();
        matrix.postScale(postScale_width,postScale_height);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

    }
}
