package com.appzone.freshcrops.activities_fragments.activity_home.client_home.fragment.fragment_cart;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.freshcrops.R;
import com.appzone.freshcrops.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.freshcrops.adapters.PlaceSearchAdapter;
import com.appzone.freshcrops.models.PlaceSearchModel;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Map extends Fragment implements OnMapReadyCallback {
    private ImageView image_back,image_arrow;
    private LinearLayout ll_back;
    private AutoCompleteTextView tv_search;
    private SupportMapFragment fragment;
    private GoogleMap mMap;
    private AutocompleteFilter filter;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private PlaceSearchAdapter adapter;
    private LinearLayout ll_search_no_result;
    private FrameLayout fl_save;
    private List<PlaceSearchModel> placeSearchModelList;
    private HomeActivity activity;
    public boolean isMapReady = false;
    private Marker marker = null;
    private final float zoom = 16.5f;
    private TextView tv_title;
    private String current_lang;
    private FrameLayout fl_address_container;
    private AddressListener addressListener;
    private String myAddress_title="",order_address_title="";
    private double order_lat=0.0,order_lng=0.0;
    private double current_lat=0.0,current_lng=0.0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Map newInstance() {
        return new Fragment_Map();
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        placeSearchModelList = new ArrayList<>();
        image_back = view.findViewById(R.id.image_back);
        image_arrow = view.findViewById(R.id.image_arrow);
        tv_title = view.findViewById(R.id.tv_title);
        fl_address_container = view.findViewById(R.id.fl_address_container);


        Paper.init(getActivity());
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar")) {

            image_back.setImageResource(R.drawable.arrow_right);
            image_arrow.setImageResource(R.drawable.arrow_right);

        } else {
            image_back.setImageResource(R.drawable.arrow_left);
            image_arrow.setImageResource(R.drawable.arrow_left);

        }

        ll_back = view.findViewById(R.id.ll_back);
        fl_save = view.findViewById(R.id.fl_save);
        tv_search = view.findViewById(R.id.tv_search);
        tv_search.setSelected(false);
        ll_search_no_result = view.findViewById(R.id.ll_search_no_result);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        adapter = new PlaceSearchAdapter(getActivity(), placeSearchModelList,this);
        recView.setAdapter(adapter);
        tv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tv_search.getText().toString().length() > 0) {
                    search(s.toString());

                } else {
                    placeSearchModelList.clear();
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        fl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_lat !=0.0 && order_lng !=0.0)
                {
                    addressListener.onAddressSet(order_address_title,order_lat,order_lng);
                }else
                    {
                        if (current_lat!=0.0 && current_lng!=0.0)
                        {

                            CreateLocationDialog();
                        }else
                            {
                                Toast.makeText(activity, R.string.sel_delv_loc, Toast.LENGTH_LONG).show();
                            }
                    }
            }
        });


        initMap();

    }


    private void updateTitle(String title)
    {
        myAddress_title = title;
        fl_address_container.setVisibility(View.VISIBLE);
        tv_title.setText(title);
    }
    private void initMap() {
        if (fragment==null)
        {
            fragment = SupportMapFragment.newInstance();
            fragment.getMapAsync(this);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.map,fragment).commit();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap!=null)
        {
            isMapReady = true;
            mMap = googleMap;
            mMap.setIndoorEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.maps));
            filter = new AutocompleteFilter.Builder()
                    .setCountry("SA")
                    .build();




        }
    }

    private void CreateLocationDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_location_order,null);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addressListener.onAddressSet(myAddress_title,current_lat,current_lng);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialog.show();
    }
    /////////////////////////Search///////////////////////////
    private void search(String query)
    {
        final Task<AutocompletePredictionBufferResponse> result = Places.getGeoDataClient(getActivity()).getAutocompletePredictions(query, new LatLngBounds(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)), filter);

        result.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                if (task.isSuccessful())
                {
                    placeSearchModelList.clear();
                    adapter.notifyDataSetChanged();

                    AutocompletePredictionBufferResponse result1 = task.getResult();
                    List<String> placeIds = new ArrayList<>();

                    if (result1.getCount()>0)
                    {
                        recView.setVisibility(View.VISIBLE);
                        ll_search_no_result.setVisibility(View.GONE);

                        for (int i=0 ; i<result1.getCount();i++)
                        {
                            placeIds.add(result1.get(i).getPlaceId());
                        }

                        getPlaceDetailsByPlaceId(placeIds);

                    }else
                        {
                            recView.setVisibility(View.GONE);
                            ll_search_no_result.setVisibility(View.VISIBLE);
                        }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CreateToast(getString(R.string.cnt_get_loc));
            }
        });
    }
    private void getPlaceDetailsByPlaceId(List<String> placeIdsList) {


        if (tv_search.getText().toString().length()>0)
        {
            for (String placeId:placeIdsList)
            {
                Places.getGeoDataClient(getActivity()).getPlaceById(placeId)
                        .addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                                if (task.isSuccessful())
                                {
                                    PlaceSearchModel placeSearchModel = new PlaceSearchModel(task.getResult().get(0).getAddress().toString(),task.getResult().get(0).getLatLng().latitude,task.getResult().get(0).getLatLng().longitude);

                                    Log.e("name",placeSearchModel.getName());


                                    placeSearchModelList.add(placeSearchModel);
                                    adapter.notifyItemInserted(placeSearchModelList.size()-1);

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CreateToast(getString(R.string.cnt_get_loc));

                    }
                });
            }
        }else
            {
                placeSearchModelList.clear();
                adapter.notifyDataSetChanged();
            }





    }
    ////////////////////////AddMarker//////////////////////////
    public void UpdateLocation(Location location)
    {
        if (location!=null)
        {
            current_lat = location.getLatitude();
            current_lng = location.getLongitude();

            getTitleFromLatLng(location);
            AddMarker(location);

        }else
            {
                fl_address_container.setVisibility(View.GONE);
            }

    }
    private void AddMarker(Location location)
    {


        if (marker == null)
        {
            IconGenerator iconGenerator = new IconGenerator(activity);
            iconGenerator.setBackground(null);
            View view = LayoutInflater.from(activity).inflate(R.layout.map_icon,null);
            iconGenerator.setContentView(view);
            marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(),iconGenerator.getAnchorV()).position(new LatLng(location.getLatitude(),location.getLongitude())));
        }else
            {
                marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));

            }


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),zoom));
    }

    public void setItem(PlaceSearchModel placeSearchModel)
    {
        order_lat = placeSearchModel.getLat();
        order_lng = placeSearchModel.getLng();
        order_address_title = placeSearchModel.getName();

        placeSearchModelList.clear();
        adapter.notifyDataSetChanged();
        Location location = new Location("");
        location.setLatitude(placeSearchModel.getLat());
        location.setLongitude(placeSearchModel.getLng());
        updateTitle(placeSearchModel.getName());
        AddMarker(location);
    }

    private void getTitleFromLatLng(Location location)
    {
        Geocoder geocoder = new Geocoder(getActivity(),new Locale(current_lang));
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if (addressList.size()>0)
            {
                Address address = addressList.get(0);
                if (address!=null)
                {
                    String my_address = "";
                    if (address.getLocality()!=null)
                    {
                        my_address+=address.getLocality();
                    }

                    if (address.getThoroughfare()!=null)
                    {
                        my_address+=" "+address.getThoroughfare();
                    }

                    if (address.getSubAdminArea()!=null)
                    {
                        my_address+=" "+address.getSubAdminArea();
                    }
                    updateTitle(my_address);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void CreateToast(String msg)
    {
        Toast.makeText(activity,msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        addressListener = activity;

    }

    public interface AddressListener
    {
        void onAddressSet(String address,double lat,double lng);
    }
}
