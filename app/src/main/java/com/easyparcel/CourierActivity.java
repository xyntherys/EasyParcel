package com.easyparcel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.easyparcel.gmap.MasterRouteActivity;

/**
 * Created by xyntherys on 1/21/18.
 */

public class CourierActivity extends AppCompatActivity {

    private RadioGroup couriers;
    private RadioButton c01;
    private RadioButton c02;
    private RadioButton c03;
    private RadioButton c04;
    private RadioButton selectedButton;
    private Button nextCourier;

    private String courierName;
    private String address;

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.courier_item);

        Intent intent = getIntent();
        address = intent.getStringExtra("address");

        couriers = (RadioGroup) findViewById(R.id.couriers);
        c01 = (RadioButton) findViewById(R.id.c01);
        c02 = (RadioButton) findViewById(R.id.c02);
        c03 = (RadioButton) findViewById(R.id.c03);
        c04 = (RadioButton) findViewById(R.id.c04);
        nextCourier = (Button) findViewById(R.id.btn_next_courier);

        nextCourier.setOnClickListener(view -> confirmPage());

    }

    void confirmPage(){

        int selected = couriers.getCheckedRadioButtonId();

//        String s = Integer.toString(selected);

        selectedButton = (RadioButton) findViewById(selected);
        String s = selectedButton.getText().toString();

        Toast.makeText(getApplicationContext(), s, 2).show();

        Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
        intent.putExtra("address", address);
        startActivity(intent);

//        Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);
//        startActivity(intent);
    }


}
