package uk.co.syski.client.android.api.requests.system;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIAuthorizationRequest;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.entity.MotherboardEntity;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.data.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.data.repository.Repository;

public class APISystemOperatingSystemRequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;

    public APISystemOperatingSystemRequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/operatingsystem", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            for (int i = 0; i < jsonArray.length(); i++) {
                OperatingSystemEntity osEntity = SyskiCache.GetDatabase().OperatingSystemDao().GetOperatingSystems(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));
                if (osEntity == null) {
                    osEntity = new OperatingSystemEntity();
                    osEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                    osEntity.Name = ((JSONObject) jsonArray.get(i)).getString("name");
                    Repository.getInstance().getOSRepository().insert(osEntity, mSystemId);
                } else {
                    // TODO Update OS method
                }
            }
            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
