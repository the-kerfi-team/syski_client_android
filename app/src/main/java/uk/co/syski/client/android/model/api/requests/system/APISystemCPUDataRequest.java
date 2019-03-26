package uk.co.syski.client.android.model.api.requests.system;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import uk.co.syski.client.android.model.api.requests.APIAuthorizationRequest;

public class APISystemCPUDataRequest extends APIAuthorizationRequest<JSONArray>
{

    private UUID mSystemId;

    public APISystemCPUDataRequest(Context context, UUID systemId, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(context, Method.POST, "system/" + systemId.toString() + "/cpu/data", null, listener, errorListener);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));
            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
