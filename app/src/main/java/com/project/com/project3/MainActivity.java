package com.project.com.project3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btncau1:
                startIntent(new Intent(this, MapsActivity.class));
                break;
            case R.id.btncau4:
                startIntent(new Intent(this, ContactsActivity.class));
                break;
        }
    }

    private void startIntent(Intent intent) {
        startActivity(intent);
    }
}
