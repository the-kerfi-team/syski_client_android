package uk.co.syski.client.android.model.api.requests.system;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

import uk.co.syski.client.android.model.api.requests.APIAuthorizationRequest;

public class APISystemShutdownRequest extends APIAuthorizationRequest<JSONArray> {

    public APISystemShutdownRequest(Context context, UUID systemId) {
        super(context, Method.POST, "system/" + systemId + "/shutdown", null, null, null);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            Log.v("Syski_Shutdown", "Status Worked!");
            return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
        } else {
            Log.v("Syski_Shutdown", "Status Didn't Worked!");
        }
        return Response.error(new ParseError());
    }
}