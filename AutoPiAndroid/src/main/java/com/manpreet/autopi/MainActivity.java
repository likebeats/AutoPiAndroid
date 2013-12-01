package com.manpreet.autopi;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.manpreet.autopi.model.Blind;
import com.manpreet.autopi.model.Entrance;
import com.manpreet.autopi.model.Light;
import com.manpreet.autopi.model.User;
import com.manpreet.autopi.store.UserStore;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    User user;

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Session session = Session.getInstance();
        user = session.currentUser;

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            if (i == 0) {
                return new LightsFragment();
            } else if (i == 1) {
                return new DoorsFragment();
            } else if (i == 2) {
                return new BlindsFragment();
            } else if (i == 3) {
                return new WindowsFragment();
            }
            return null;

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Lights";
            } else if (position == 1) {
                return "Doors";
            } else if (position == 2) {
                return "Blinds";
            } else if (position == 3) {
                return "Windows";
            }
            return "No Title";
        }
    }

    public class LightsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_lights, container, false);

            LinearLayout linearLayout1 = (LinearLayout)rootView.findViewById(R.id.linearLayout1);
            for (final Light light : user.lights) {

                Switch lightSwitch = new Switch(rootView.getContext());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 22, 0, 0);

                lightSwitch.setLayoutParams(params);
                lightSwitch.setText(light.label);
                lightSwitch.setChecked(light.status);
                lightSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                        new PerformChangeLightStatusTask().execute(String.valueOf(light.id), String.valueOf(isChecked));
                    }

                });

                linearLayout1.addView(lightSwitch);

            }

            return rootView;

        }

        private class PerformChangeLightStatusTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                int id = Integer.parseInt(params[0]);
                boolean status = Boolean.valueOf(params[1]);
                return UserStore.setLightStatus(id, status);
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

    public class DoorsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_doors, container, false);

            LinearLayout linearLayout1 = (LinearLayout)rootView.findViewById(R.id.linearLayout1);
            for (final Entrance entrance : user.entrance) {
                if (entrance.entrance_type.equals("door")) {

                    Switch doorSwitch = new Switch(rootView.getContext());

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 22, 0, 0);

                    doorSwitch.setLayoutParams(params);
                    doorSwitch.setText(entrance.label);
                    doorSwitch.setChecked(entrance.alarm);
                    doorSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                            new PerformChangeDoorStatusTask().execute(String.valueOf(entrance.id), String.valueOf(isChecked));
                        }

                    });

                    linearLayout1.addView(doorSwitch);

                }
            }

            return rootView;

        }

        private class PerformChangeDoorStatusTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                int id = Integer.parseInt(params[0]);
                boolean status = Boolean.valueOf(params[1]);
                return UserStore.setEntranceStatus(id, status);
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

    public class BlindsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_blinds, container, false);

            LinearLayout linearLayout1 = (LinearLayout)rootView.findViewById(R.id.linearLayout1);
            for (final Blind blind : user.blinds) {

                Switch blindSwitch = new Switch(rootView.getContext());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 22, 0, 0);

                blindSwitch.setLayoutParams(params);
                blindSwitch.setText(blind.label);
                blindSwitch.setChecked(blind.status);
                blindSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                        new PerformChangeBlindStatusTask().execute(String.valueOf(blind.id), String.valueOf(isChecked));
                    }

                });

                linearLayout1.addView(blindSwitch);

            }

            return rootView;

        }

        private class PerformChangeBlindStatusTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                int id = Integer.parseInt(params[0]);
                boolean status = Boolean.valueOf(params[1]);
                return UserStore.setBlindStatus(id, status);
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

    public class WindowsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_windows, container, false);

            LinearLayout linearLayout1 = (LinearLayout)rootView.findViewById(R.id.linearLayout1);
            for (final Entrance entrance : user.entrance) {
                if (entrance.entrance_type.equals("window")) {

                    Switch windowSwitch = new Switch(rootView.getContext());

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 22, 0, 0);

                    windowSwitch.setLayoutParams(params);
                    windowSwitch.setText(entrance.label);
                    windowSwitch.setChecked(entrance.alarm);
                    windowSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                            new PerformChangeWindowStatusTask().execute(String.valueOf(entrance.id), String.valueOf(isChecked));
                        }

                    });

                    linearLayout1.addView(windowSwitch);

                }
            }

            return rootView;

        }

        private class PerformChangeWindowStatusTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                int id = Integer.parseInt(params[0]);
                boolean status = Boolean.valueOf(params[1]);
                return UserStore.setEntranceStatus(id, status);
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

}
