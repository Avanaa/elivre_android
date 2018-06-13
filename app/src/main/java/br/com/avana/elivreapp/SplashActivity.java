package br.com.avana.elivreapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Fabric fabric = Fabric.with(this, new Crashlytics());
        Crashlytics.log("Fabric instanciado. Versão: "+fabric.getVersion());
        //ex();
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goMap();
            }
        }, 3000);
    }

    private void goMap(){

        //Intent intent = new Intent(getApplicationContext(), FormActivity.class);
        //Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*private void ex(){
        throw new RuntimeException("Falha");
    }*/
}
