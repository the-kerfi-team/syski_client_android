package uk.co.syski.client.android.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.api.requests.system.APISystemCPURequest;
import uk.co.syski.client.android.api.requests.system.APISystemsRequest;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.UserEntity;
import uk.co.syski.client.android.data.repository.Repository;

public class APIThread extends Thread {

    private static APIThread mInstance;
    private static Context mContext;
    private static UserEntity mUser;
    private static SharedPreferences mSharedPreferences;
    private static boolean mRunning = true;

    public static synchronized APIThread getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new APIThread(context);
        }
        return mInstance;
    }

    private APIThread(Context context)
    {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void run() {
        while (mRunning) {
            mUser = Repository.getInstance().getUserRepository().getUser();
            if (mUser != null) {
                Date expiryDate = mUser.TokenExpiry;
                if (expiryDate == null || Calendar.getInstance().getTime().after(expiryDate)) {
                    VolleySingleton.getInstance(mContext).addToRequestQueue(new APITokenRequest(mContext, mUser.Id));
                }
                expiryDate = mUser.TokenExpiry;
                if (expiryDate != null && Calendar.getInstance().getTime().before(expiryDate)) {
                    VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemsRequest(mContext));
                    List<SystemEntity> systemEntities = SyskiCache.GetDatabase().SystemDao().get();
                    for (SystemEntity system : systemEntities) {
                        VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemCPURequest(mContext, system.Id));
                        //        VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemRAMRequest(mContext, system.Id));
                        //        VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemStorageRequest(mContext, system.Id));
                        //        VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemGPURequest(mContext, system.Id));
                        //        VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemMotherboardRequest(mContext, system.Id));
                        //         VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemOperatingSystemRequest(mContext, system.Id));
                        //}
                    }
                }
            }
            int refreshTime = (int) Double.parseDouble(mSharedPreferences.getString("pref_api_refreshinterval", mContext.getString(R.string.pref_api_refreshinterval_default)));
            Log.d("Syski_Interval_Time", Integer.toString(refreshTime));
            try {
                Thread.sleep(refreshTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void enable()
    {
        mRunning = true;
        start();
    }

    public void disable()
    {
        mRunning = false;
    }

}
