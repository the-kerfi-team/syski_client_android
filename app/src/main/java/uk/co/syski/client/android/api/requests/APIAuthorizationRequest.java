package uk.co.syski.client.android.api.requests;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.api.VolleySingleton;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class APIAuthorizationRequest<T> extends APIRequest<T> {

    private UUID mUUID;
    private Context mContext;

    public APIAuthorizationRequest(Context context, UUID userId, int method, String url, @Nullable String requestBody, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
        super(context, method, url, requestBody, listener, errorListener);
        mContext = context;
        mUUID = userId;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("User-Agent", "Syski APP");
        try {
            params.put("Authorization", "Bearer " + SyskiCacheThread.getInstance().UserThreads.GetAccessToken());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return params;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 401)
        {
            final APIAuthorizationRequest thisObject = this;
            VolleySingleton.getInstance(mContext).addToRequestQueue(new APITokenRequest(mContext, mUUID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleySingleton.getInstance(mContext).addToRequestQueue(thisObject);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Logout User unless not connected to the internet
                    }
                 }));
        }
        return null;
    }

}
