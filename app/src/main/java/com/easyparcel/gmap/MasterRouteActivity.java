package com.easyparcel.gmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.easyparcel.R;

/**
 * Created by t on 6/8/17.
 */

public class MasterRouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_master_route);

        Fragment fragment = new MapAPIfragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.masterContainer,fragment).commit();






    }
}
