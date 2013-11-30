package com.manpreet.autopi;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session session = Session.getInstance();
        Intent intent;

        if (session.username != null && session.password != null) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }

}
