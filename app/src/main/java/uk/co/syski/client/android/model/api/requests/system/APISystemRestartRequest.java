package uk.co.syski.client.android.model.api.requests.system;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;

import java.util.UUID;

import uk.co.syski.client.android.model.api.requests.APIAuthorizationRequest;

public class APISystemRestartRequest extends APIAuthorizationRequest<JSONArray> {

    public APISystemRestartRequest(Context context, UUID systemId) {
        super(context, Method.POST, "system/" + systemId + "/restart", null, null, null);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            Log.v("Syski_Restart", "Status Worked!");
            return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
        } else {
            Log.v("Syski_Restart", "Status Didn't Worked!");
        }
        return Response.error(new ParseError());
    }

}