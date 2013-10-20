package com.manpreet.autopi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manpreet.autopi.model.User;
import com.manpreet.autopi.store.UserStore;

public class LoginActivity extends Activity {

    private View mLoginFormView;
    private View mLoginStatusView;
    private EditText usernameTextView;
    private EditText passwordTextView;
    private Button loginBtn;
    private Toast toast;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);

        loginBtn = (Button)findViewById(R.id.sign_in_button);
        usernameTextView = (EditText)findViewById(R.id.username);
        passwordTextView = (EditText)findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("---------- BUTTON CLICKED --------------");

                username = usernameTextView.getText().toString();
                password = passwordTextView.getText().toString();

                showProgress(true);
                if (toast != null) toast.cancel();
                new PerformGetAllUsersTask().execute(username, password);
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginBtn.setClickable(!show);
        mLoginStatusView.setVisibility(View.VISIBLE);
        mLoginStatusView.animate()
                .setDuration(shortAnimTime)
                .alpha(show ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });

        mLoginFormView.setVisibility(View.VISIBLE);
        mLoginFormView.animate()
                .setDuration(shortAnimTime)
                .alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
    }

    public void onTaskComplete(User user) {

        if (user != null) {

            /** Login Successful */

            Session session = Session.getInstance();
            session.username = username;
            session.password = password;
            session.currentUser = user;

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {

            /** Login Failed */

            showProgress(false);
            Context context = getApplicationContext();
            toast = Toast.makeText(context, "Incorrect Username/Password Combination", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    /**
     * Check login credentials in background task
     */
    private class PerformGetAllUsersTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... params) {
            return UserStore.getCurrentUser(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(final User result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onTaskComplete(result);
                }
            });
        }
    }

}
