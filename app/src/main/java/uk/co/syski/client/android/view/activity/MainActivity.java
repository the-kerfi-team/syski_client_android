package uk.co.syski.client.android.view.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.APIRequest;
import uk.co.syski.client.android.model.api.requests.auth.APILoginRequest;
import uk.co.syski.client.android.model.api.requests.auth.APIRegisterRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.repository.Repository;


public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        SyskiCache.BuildDatabase(getApplicationContext());
        prefs = getApplicationContext().getSharedPreferences(getString(R.string.preference_usrID_key), Context.MODE_PRIVATE);
        String userId = prefs.getString(getString(R.string.preference_usrID_key), null);
        if (userId != null) {
            Repository.getInstance().getUserRepository().setActiveUserId(UUID.fromString(userId));
            startActivity(new Intent(this, SystemListMenu.class));
            finish();
        } else {
            setTheme(R.style.AppTheme);
            setContentView(R.layout.activity_main);

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mSectionsPagerAdapter.addFragment(new Tab_Login(), "Login");
            mSectionsPagerAdapter.addFragment(new Tab_Register(), "Register");

            mViewPager = findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tabs);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.channel_id);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sys_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class Tab_Authentication extends Fragment {

        protected boolean mDisableButton;
        protected EditText mEmailView;
        protected EditText mPasswordView;

        public Tab_Authentication() {

        }

        protected void sendAPIRequest(String APIRequest) {
            APIRequest request = null;
            if (APIRequest.equalsIgnoreCase("register")) {
                request = new APIRegisterRequest(getContext(),
                        mEmailView.getText().toString(),
                        mPasswordView.getText().toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                RequestSuccessful(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                RequestFailed(error);
                            }
                        });
            } else if (APIRequest.equalsIgnoreCase("login")) {
                request = new APILoginRequest(getContext(),
                        mEmailView.getText().toString(),
                        mPasswordView.getText().toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                RequestSuccessful(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                RequestFailed(error);
                            }
                        });
            }
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }

        protected void RequestSuccessful(JSONObject response) {
            startActivity(new Intent(getActivity(), SystemListMenu.class));
            getActivity().finish();
        }

        protected void RequestFailed(VolleyError error) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
            mDisableButton = false;
        }

        protected boolean isEmailValid(String email) {
            return email.contains("@");
        }

        protected boolean isPasswordValid(String password) {
            return password.length() > 6;
        }
    }

    public static class Tab_Login extends Tab_Authentication {

        public Tab_Login() {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main_tab_login, container, false);

            mEmailView = view.findViewById(R.id.email);
            mPasswordView = view.findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = view.findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            return view;
        }

        private void attemptLogin() {
            if (!mDisableButton) {
                mDisableButton = true;
                mEmailView.setError(null);
                mPasswordView.setError(null);

                final String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                } else if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                    mDisableButton = false;
                } else {
                    sendAPIRequest("login");
                }
            }
        }

    }

    public static class Tab_Register extends Tab_Authentication {

        public Tab_Register() {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main_tab_register, container, false);

            mEmailView = view.findViewById(R.id.email);
            mPasswordView = view.findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptRegister();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignUpButton = view.findViewById(R.id.email_sign_up_button);
            mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptRegister();
                }
            });

            return view;
        }

        private void attemptRegister() {
            if (!mDisableButton) {
                mDisableButton = true;
                mEmailView.setError(null);
                mPasswordView.setError(null);

                final String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                    mPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mPasswordView;
                    cancel = true;
                }

                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                } else if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    focusView = mEmailView;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                    mDisableButton = false;
                } else {
                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("email", email);
                        jsonBody.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sendAPIRequest("register");
                }
            }
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }
}
