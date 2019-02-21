package uk.co.syski.client.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.api.VolleySingleton;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.entity.User;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SyskiCache.BuildDatabase(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                SyskiCache.GetDatabase().clearAllTables();
            }
        }).start();

        System system = new System();
        UUID uuid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        //system.Id = uuid.randomUUID();
        system.Id = uuid;
        system.HostName = "Earth";

        System system1 = new System();
        system1.Id = uuid.randomUUID();
        system1.HostName = "Mars";

        try {
            SyskiCacheThread.getInstance().SystemThreads.InsertAll(system);
            SyskiCacheThread.getInstance().SystemThreads.InsertAll(system1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new Tab_Login(), "Login");
        mSectionsPagerAdapter.addFragment(new Tab_Register(), "Register");

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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

        protected EditText mEmailView;
        protected EditText mPasswordView;

        public Tab_Authentication() {

        }

        protected void sendAPIRequest(String url, JSONObject jsonBody) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
            String api_url = sp.getString("pref_api_url", getString(R.string.pref_api_url_default));
            String api_port = sp.getString("pref_api_port", getString(R.string.pref_api_port_default));
            String api_path = sp.getString("pref_api_path", getString(R.string.pref_api_path_default));

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,  "https://" + api_url + ":" + api_port + api_path + url, jsonBody,
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
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }

        protected void RequestSuccessful(JSONObject response) {
            User user = new User();
            try {
                user.Id = UUID.fromString(response.getString("id"));
                user.Email = response.getString("email");
                user.AccessToken = response.getString("token");
                user.RefreshToken = response.getString("refreshToken");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                SyskiCacheThread.getInstance().UserThreads.InsertAll(user);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getActivity().finish();
            startActivity(new Intent(getActivity(), SysListMenu.class));
        }

        protected void RequestFailed(VolleyError error) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }

        protected boolean isEmailValid(String email)
        {
            return email.contains("@");
        }

        protected boolean isPasswordValid(String password)
        {
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
            } else {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", email);
                    jsonBody.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendAPIRequest("auth/user/login", jsonBody);
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

            mEmailView = (EditText) view.findViewById(R.id.email);
            mPasswordView = (EditText) view.findViewById(R.id.password);
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

            Button mEmailSignUpButton = (Button) view.findViewById(R.id.email_sign_up_button);
            mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptRegister();
                }
            });

            return view;
        }

        private void attemptRegister() {
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
            } else {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", email);
                    jsonBody.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendAPIRequest("auth/user/register", jsonBody);
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
