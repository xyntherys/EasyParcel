package com.easyparcel.gmap;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.easyparcel.CourierActivity;
import com.easyparcel.ProductActivity;
import com.easyparcel.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by t on 5/24/17.
 */

public class MapAPIfragment extends Fragment implements AdapterView.OnItemClickListener, OnMapReadyCallback {

    AutoCompleteTextView autoCompViewD;
    AutoCompleteTextView autoCompViewS;
    String desLatLng;
    String srcLatLng;
    LinearLayout holder;
    LinearLayout openHolder;
    TextView open;
    Button getRoute;
    Button nextMap;
    private String passAddress;

    boolean routeAdded = false;

    private static String TAG = "auto";

    TripPathShow tripPathShow;
    GeoCode geoCode;
    TextView close;

    ArrayList<String> arrayList;
    List<LatLng> pathList = new ArrayList<>();

    SupportMapFragment supportMapFragment;

    public String getPassAddress() {
        return passAddress;
    }

    public void setPassAddress(String passAddress) {
        this.passAddress = passAddress;
    }

    public void setDesLatLng(String desLatLng) {
        this.desLatLng = desLatLng;
    }

    public void setSrcLatLng(String srcLatLng) {
        this.srcLatLng = srcLatLng;
    }

    public String getDesLatLng() {
        //desLatLng="23.738588, 90.392267";
        return desLatLng;
    }

    public String getSrcLatLng() {
        return srcLatLng;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapapi, container,
                false);

        autoCompViewD = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextViewD);
        holder = (LinearLayout) v.findViewById(R.id.holder);
        openHolder = (LinearLayout) v.findViewById(R.id.openHolder);
        open = (TextView) v.findViewById(R.id.open);
        autoCompViewD.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.map_item));
        autoCompViewD.setOnItemClickListener(this);
        autoCompViewS = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextViewS);
        close = (TextView) v.findViewById(R.id.close);
        autoCompViewS.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.map_item));
        autoCompViewS.setOnItemClickListener(this);

        getRoute = (Button) v.findViewById(R.id.getRoute);
        nextMap = (Button) v.findViewById(R.id.nextMap);
        arrayList = new ArrayList<>();
        geoCode = new GeoCode(getContext());

        supportMapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapfragment);
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(23.727358, 90.389717))      // Sets the center of the map to Mountain View
                .zoom(7)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.727358, 90.389717), 7 ));
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        close.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                holder.setVisibility(View.INVISIBLE);
                openHolder.setVisibility(View.VISIBLE);


                return true;
            }
        });

        open.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.setVisibility(View.VISIBLE);
                openHolder.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        autoCompViewS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (autoCompViewS.getRight() - autoCompViewS.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        SingleShotLocationProvider.requestSingleUpdate(getContext(), new SingleShotLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                                Log.d("Location", location.toString());
                                srcLatLng = String.valueOf(location.getLat()) + "," + String.valueOf(location.getLang());
                                autoCompViewS.setText("Your location");


                            }
                        });

                        return true;
                    }
                }
                return false;
            }
        });


        getRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                routeAdded = true;

                holder.setVisibility(View.INVISIBLE);
                openHolder.setVisibility(View.VISIBLE);

                //Toast.makeText(getContext(),autoCompViewD.getText().toString()+autoCompViewS.getText().toString(),Toast.LENGTH_SHORT).show();
                Log.d("check", getDesLatLng() + getSrcLatLng());

                if(desLatLng == null || srcLatLng == null){
                    showSnackBarMessage("Please select Source and Destination.");
                }

                else{
                    tripPathShow = new TripPathShow(getContext(), getDesLatLng(), getSrcLatLng());

                    desLatLng = null;
                    srcLatLng = null;
                    autoCompViewD.setText("");
                    autoCompViewS.setText("");

                    Log.d("check", tripPathShow.getURL());

                    tripPathShow.jsonReq(tripPathShow.getURL(), new TripPathShow.CallBack() {
                        @Override
                        public void onSuccess(List<LatLng> list) {
                            pathList = list;
                            supportMapFragment.getMapAsync(MapAPIfragment.this);
                            Log.d("check", String.valueOf(pathList));

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                }

            }
        });


        nextMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(routeAdded == false) showSnackBarMessage("Please select Source and Destination.");

                else{
//                    Intent intent = new Intent(getActivity().getApplicationContext(), ProductActivity.class);
//                    intent.putExtra("address", getPassAddress());
//                    startActivity(intent);

                    Intent intent = new Intent(getActivity().getApplicationContext(), CourierActivity.class);
                    intent.putExtra("address", getPassAddress());
                    startActivity(intent);
                }
            }
        });


        supportMapFragment.getMapAsync(this);

        return v;
    }


    void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private ArrayList makeJsonObjectRequest(String input) {

        String urlJsonObj = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
                input +
                "&types=establishment&location=23.726574,90.389868&radius=20000&strictbounds&key=AIzaSyAuDPbEB8OfpLi2aXcPa4KnTQyiuQurZ_Y\n";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, response -> {
                    Log.d(TAG, String.valueOf(response));

                    ArrayList<String> arrayLt = autocomplete(response);

                    for (String s : arrayLt) {
                        Log.d(TAG, "1.......>>>>>>" + s);
                    }
                    arrayList = arrayLt;

                }, error -> {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();

                });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
        return arrayList;
    }


    public static ArrayList autocomplete(JSONObject jsonObject) {
        ArrayList resultList = null;

        try {
            JSONObject jsonObj = jsonObject;
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {

        String str = (String) adapterView.getItemAtPosition(position);
        setPassAddress(str);

        geoCode.setPlace(str);
        Log.d("check", geoCode.getURL());

        geoCode.jsonReq(geoCode.getURL(), new GeoCode.CallBack() {
            @Override
            public void onSuccess(Double lat, Double lng) {

                if (desLatLng == null) {
                    desLatLng = String.valueOf(lat) + "," + String.valueOf(lng);
                } else {
                    srcLatLng = String.valueOf(lat) + "," + String.valueOf(lng);
                }
            }

            @Override
            public void onFail(String msg) {

            }
        });
        //Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (pathList.size() > 0) {
            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(pathList.get(1))      // Sets the center of the map to Mountain View// Sets the zoom// Sets the orientation of the camera to east
                    .tilt(60).zoom(15)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            googleMap.clear();

            Polyline line = googleMap.addPolyline(new PolylineOptions()
                    .addAll(pathList)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );



            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            googleMap.addMarker(new MarkerOptions().position(pathList.get(0)));
            googleMap.addMarker(new MarkerOptions().position(pathList.get(pathList.size()-1)));


            Log.d("check","done");

            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            timerThread.start();

        }
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = makeJsonObjectRequest(constraint.toString());
                        Log.d(TAG, "performFiltering obtained result: "+resultList);

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        Log.d(TAG, "publishResults: "+results);
                        notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "publishResults: "+"NOthing found");
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        desLatLng = null;
        srcLatLng = null;

    }

    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }
    }
}
