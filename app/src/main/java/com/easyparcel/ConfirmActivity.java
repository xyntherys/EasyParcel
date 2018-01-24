package com.easyparcel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easyparcel.fragments.RegisterFragment;
import com.easyparcel.model.Parcel;
import com.easyparcel.model.Response;
import com.easyparcel.network.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by xyntherys on 1/21/18.
 */

public class ConfirmActivity extends AppCompatActivity {

    public static final String TAG = ConfirmActivity.class.getSimpleName();

    String type;
    String address;
    String phone;
    String email;

    private Button confirmBtn;
    private Button cancelBtn;
    private TextView confirmInfo;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        mSubscriptions = new CompositeSubscription();

        Intent intent = getIntent();

        type = intent.getStringExtra("type");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");
        email = intent.getStringExtra("email");

//        System.out.println(address);

        confirmBtn = (Button) findViewById(R.id.btn_confirm);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        confirmInfo = (TextView) findViewById(R.id.confirm_info);

        String info = "Type: " + type + "\nAddress: " + address + "\nPhone: " + phone;
        confirmInfo.setText(info);

        confirmBtn.setOnClickListener(view -> bookParcel());

        cancelBtn.setOnClickListener(view -> cancel());

    }

    private void cancel() {

        finish();
    }

    private void bookParcel() {

        com.easyparcel.model.Parcel parcel = new com.easyparcel.model.Parcel();

        parcel.setType(type);
        parcel.setAddress(address);
        parcel.setPhone(phone);
        parcel.setEmail(email);
        parcel.setParcelId(phone);

        System.out.println(parcel.getType());
        System.out.println(parcel.getAddress());
        System.out.println(parcel.getParcelId());
        System.out.println(parcel.getPhone());
        System.out.println(parcel.getEmail());

        bookingProcess(parcel);

        Toast.makeText(getApplicationContext(), "Your Parcel has been booked for delivary!",
                Toast.LENGTH_LONG).show();

//        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//        startActivity(intent);
    }

    private void bookingProcess(Parcel parcel) {

        mSubscriptions.add(NetworkUtil.getRetrofit().book(parcel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Response response) {

        Toast.makeText(getApplicationContext(), response.getMessage(),
                Toast.LENGTH_LONG).show();
//        showSnackBarMessage(response.getMessage());
    }

    private void handleError(Throwable error) {

        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();

            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody, Response.class);
//                showSnackBarMessage(response.getMessage());

                Toast.makeText(getApplicationContext(), response.getMessage(),
                        Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

//            showSnackBarMessage("Network Error.");

//            Toast.makeText(getApplicationContext(), "Network Error",
//                    Toast.LENGTH_LONG).show();
//        }
        }

    }

//    private void showSnackBarMessage(String message) {
//
//        if (getView() != null) {
//            Snackbar.make(, message, Snackbar.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

}
