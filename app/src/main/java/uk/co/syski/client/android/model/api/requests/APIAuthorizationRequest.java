package uk.co.syski.client.android.model.api.requests;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import uk.co.syski.client.android.model.repository.Repository;

public class APIAuthorizationRequest<T> extends APIRequest<T> {

    private Context mContext;

    public APIAuthorizationRequest(Context context, int method, String url, @Nullable String requestBody, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
        super(context, method, url, requestBody, listener, errorListener);
        mContext = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Authorization", "Bearer " + Repository.getInstance().getUserRepository().getUser().AccessToken);
        return params;
    }

}
