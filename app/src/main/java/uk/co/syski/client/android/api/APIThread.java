package uk.co.syski.client.android.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.api.requests.system.APISystemsRequest;
import uk.co.syski.client.android.data.SyskiCache;
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
            if (Calendar.getInstance().getTime().after(SyskiCache.GetDatabase().UserDao().getUser().TokenExpiry))
            {
                VolleySingleton.getInstance(mContext).addToRequestQueue(new APITokenRequest(mContext, mUser.Id));
            }
            VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemsRequest(mContext));
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
