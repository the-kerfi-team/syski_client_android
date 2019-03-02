package uk.co.syski.client.android.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.requests.system.APISystemsRequest;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class APIThread extends Thread {

    private static APIThread mInstance;
    private static Context mContext;
    private static UUID mUserId;
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
            mUserId = SyskiCacheThread.getInstance().UserThreads.getUser().Id;
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
            VolleySingleton.getInstance(mContext).addToRequestQueue(new APISystemsRequest(mContext, mUserId, null, null));
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
