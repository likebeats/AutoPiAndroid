package com.manpreet.autopi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.manpreet.autopi.model.Light;
import com.manpreet.autopi.model.User;
import com.manpreet.autopi.store.UserStore;

public class MainActivity extends Activity {

    User user;
    Switch lightSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Session session = Session.getInstance();
        user = session.currentUser;

        final Light light = user.lights.get(0);

        TextView titleLabel = (TextView)findViewById(R.id.titleLabel);
        titleLabel.setText("Welcome, "+session.currentUser.username);

        lightSwitch = (Switch)findViewById(R.id.lightSwitch);
        lightSwitch.setChecked(light.status);
        lightSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                new PerformChangeLightStatusTask().execute(String.valueOf(light.id), String.valueOf(isChecked));
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:

                Session.getInstance().destroySession();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Check login credentials in background task
     */
    private class PerformChangeLightStatusTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            int id = Integer.parseInt(params[0]);
            return UserStore.setLightStatus(id, params[1]);
        }

        @Override
        protected void onPostExecute(final String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("RESULT:" +result);
                }
            });
        }
    }

}
