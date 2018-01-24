package com.easyparcel;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.easyparcel.gmap.MasterRouteActivity;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import static com.easyparcel.utils.Validation.validateFields;

/**
 * Created by xyntherys on 1/22/18.
 */

public class ProductActivity extends AppCompatActivity {

    private EditText mEtAddress;
    private EditText mEtPhone;
    private EditText mEtMail;
    private Button mBtNext;

    private TextInputLayout tilAddress;
    private TextInputLayout tilPhone;
    private TextInputLayout tilEmail;

    private String address;
    private String type;

    String[] SPINNERLIST = {"Electronics", "Accessories", "Apparels", "Mails"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        address = intent.getStringExtra("address");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, SPINNERLIST);

        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.product_type);

        materialDesignSpinner.setAdapter(arrayAdapter);

        mEtAddress = (EditText) findViewById(R.id.et_address);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        mEtMail = (EditText) findViewById(R.id.et_mail);
        mEtMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mEtAddress.setText(address);

        tilAddress = (TextInputLayout) findViewById(R.id.ti_address);
        tilPhone = (TextInputLayout) findViewById(R.id.ti_phone);
        tilEmail = (TextInputLayout) findViewById(R.id.ti_mail);

        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                type = adapterView.getItemAtPosition(position).toString();
            }
        });

        mBtNext = (Button) findViewById(R.id.btn_next_product);
        mBtNext.setOnClickListener(view -> selectCourier());
    }

    private void selectCourier(){

        setError();

        int error = 0;

        if (!validateFields(mEtAddress.getText().toString())) {
            error++;
            tilAddress.setError("Address can not be empty");
        }

        if (!validateFields(mEtPhone.getText().toString())) {
            error++;
            tilPhone.setError("Enter phone number");
        }

        if (!validateFields(mEtMail.getText().toString())) {
            error++;
            tilEmail.setError("Enter valid Email");
        }

        if(error == 0) {

//            Intent intent = new Intent(getApplicationContext(), CourierActivity.class);
            Intent intent = new Intent(getApplicationContext(), ConfirmActivity.class);

            intent.putExtra("type", type);
            intent.putExtra("address", mEtAddress.getText().toString());
            intent.putExtra("phone", mEtPhone.getText().toString());
            intent.putExtra("email", mEtMail.getText().toString());

            startActivity(intent);
        }

    }

    private void setError(){

        tilAddress.setError(null);
        tilPhone.setError(null);
        tilEmail.setError(null);
    }
}

