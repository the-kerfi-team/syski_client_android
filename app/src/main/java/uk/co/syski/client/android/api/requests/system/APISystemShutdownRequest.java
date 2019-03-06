package uk.co.syski.client.android.api.requests.system;

import android.content.Context;

import org.json.JSONArray;

import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIAuthorizationRequest;

public class APISystemShutdownRequest extends APIAuthorizationRequest<JSONArray> {

    public APISystemShutdownRequest(Context context, UUID systemId) {
        super(context, Method.POST, "api/commands/shutdown/" + systemId, null, null, null);
    }

}