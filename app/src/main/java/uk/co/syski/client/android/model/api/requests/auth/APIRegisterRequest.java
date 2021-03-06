package uk.co.syski.client.android.model.api.requests.auth;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.api.requests.APIRequest;
import uk.co.syski.client.android.model.database.entity.UserEntity;
import uk.co.syski.client.android.model.repository.Repository;

public class APIRegisterRequest extends APIRequest<JSONObject> {

    private String mEmail;
    private String mPassword;
    private Context mContext;

    public APIRegisterRequest(Context context, String email, String password, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(context, Method.POST, "auth/user/register", null, listener, errorListener);
        mContext = context;
        mEmail = email;
        mPassword = password;
    }

    @Override
    public byte[] getBody() {
        try {
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject();
                jsonBody.put("email", mEmail);
                jsonBody.put("password", mPassword);
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
            UserEntity userEntity = new UserEntity();
            userEntity.Id = UUID.fromString(jsonObject.getString("id"));
            userEntity.Email = jsonObject.getString("email");
            userEntity.AccessToken = jsonObject.getString("access_token");
            userEntity.RefreshToken = jsonObject.getString("refresh_token");
            userEntity.TokenExpiry = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ").parse(jsonObject.getString("expiry"));
            Repository.getInstance().getUserRepository().insert(userEntity);
            Repository.getInstance().getUserRepository().setActiveUserId(userEntity.Id);
            mContext.getSharedPreferences(mContext.getString(R.string.preference_usrID_key), Context.MODE_PRIVATE).edit().putString(mContext.getString(R.string.preference_usrID_key), userEntity.Id.toString()).apply();
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