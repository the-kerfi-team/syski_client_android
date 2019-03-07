package uk.co.syski.client.android.api.requests.system;

import android.content.Context;

import org.json.JSONArray;

import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIAuthorizationRequest;

public class APISystemRestartRequest extends APIAuthorizationRequest<JSONArray> {

    public APISystemRestartRequest(Context context, UUID systemId) {
        super(context, Method.POST, "system/" + systemId + "/restart", null, null, null);
    }

}