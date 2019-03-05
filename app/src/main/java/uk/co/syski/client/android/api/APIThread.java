package uk.co.syski.client.android.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.api.requests.system.APISystemCPURequest;
import uk.co.syski.client.android.api.requests.system.APISystemGPURequest;
import uk.co.syski.client.android.api.requests.system.APISystemMotherboardRequest;
import uk.co.syski.client.android.api.requests.system.APISystemRAMRequest;
import uk.co.syski.client.android.api.requests.system.APISystemStorageRequest;
import uk.co.syski.client.android.api.requests.system.APISystemsRequest;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.UserEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class APIThread extends Thread {

    private static APIThread mInstance;
    private static Context mContext;
    private static UserEntity mUser;
    private static SharedPreferences mSharedPreferences;

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
        try {
            mUser = SyskiCacheThread.getInstance().UserThreads.getUser();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true)
        {
            Date expiryDate = SyskiCache.GetDatabase().UserDao().getUser().TokenExpiry;
            if (expiryDate == null || Calendar.getInstance().getTime().after(expiryDate))
            {
                VolleySingleton.getInstance(mContext).addToRequestQueue(new APITokenRequest(mContext, mUser.Id));
            }
            expiryDate = SyskiCache.GetDatabase().UserDao().getUser().TokenExpiry;
            if (expiryDate != null || Calendar.getInstance().getTime().before(expiryDate)) {
                VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemsRequest(mContext));
                List<SystemEntity> systemEntities = SyskiCache.GetDatabase().SystemDao().getAllSystems();
                for (SystemEntity system : systemEntities) {
                    VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemCPURequest(mContext, system.Id));
                    VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemRAMRequest(mContext, system.Id));
                    VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemStorageRequest(mContext, system.Id));
                    VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemGPURequest(mContext, system.Id));
                    VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemMotherboardRequest(mContext, system.Id));
                }
            }
            int defaultRefreshTime = Integer.parseInt(mContext.getString(R.string.pref_api_refreshinterval_default));
            //int refreshTime = mSharedPreferences.getInt("pref_api_refreshinterval", defaultRefreshTime);
            try {
                Thread.sleep(defaultRefreshTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
