package uk.co.syski.client.android.api.requests.system;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Response;

import org.json.JSONArray;

import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIAuthorizationRequest;

public class APISystemCPUDataRequest extends APIAuthorizationRequest<JSONArray>
{

    private UUID mSystemId;

    public APISystemCPUDataRequest(Context context, UUID systemId, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(context, Method.GET, "system/" + systemId.toString() + "/cpu", null, listener, errorListener);
        mSystemId = systemId;
    }
}
