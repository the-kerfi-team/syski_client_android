package uk.co.syski.client.android.api.requests.auth;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIRequest;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.UserEntity;
import uk.co.syski.client.android.data.repository.Repository;

public class APITokenRequest extends APIRequest<JSONObject> {

    private UUID mUUID;

    public APITokenRequest(Context context, UUID userId) {
        super(context, Method.POST, "auth/user/token/refresh", null, null, null);
        mUUID = userId;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("User-Agent", "Syski APP");
        params.put("Authorization", "Bearer " + Repository.getInstance().getUserRepository().getUser().AccessToken);
        return params;
    }

    @Override
    public byte[] getBody() {
        try {
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject();
                jsonBody.put("refresh_token", Repository.getInstance().getUserRepository().getUser().RefreshToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String requestBody = jsonBody.toString();
            return requestBody == null ? null : requestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));
            if (mUUID.equals(UUID.fromString(jsonObject.getString("id"))))
            {
                return Response.error(new VolleyError());
            }
            UserEntity userEntity = Repository.getInstance().getUserRepository().getUser();
            userEntity.AccessToken = jsonObject.getString("access_token");
            userEntity.RefreshToken = jsonObject.getString("refresh_token");
            userEntity.TokenExpiry = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX").parse(jsonObject.getString("expiry"));
            Repository.getInstance().getUserRepository().update(userEntity);
            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        } catch (ParseException e) {
            return Response.error(new ParseError(e));
        }
    }

}
