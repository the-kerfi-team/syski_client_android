package uk.co.syski.client.android.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import uk.co.syski.client.android.R;

public class APIPaths
{
    public static String getURL(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String api_url = sp.getString("pref_api_url", context.getString(R.string.pref_api_url_default));
        String api_port = sp.getString("pref_api_port", context.getString(R.string.pref_api_port_default));
        String api_path = sp.getString("pref_api_path", context.getString(R.string.pref_api_path_default));
        return "https://" + api_url + ":" + api_port + api_path;
    }
}

