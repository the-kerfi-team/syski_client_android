package uk.co.syski.client.android.api.requests.system;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIAuthorizationRequest;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.MotherboardEntity;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.linking.SystemGPUEntity;
import uk.co.syski.client.android.data.entity.linking.SystemOSEntity;

public class APISystemOperatingSystemRequest extends APIAuthorizationRequest<JSONObject> {

    private UUID mSystemId;

    public APISystemOperatingSystemRequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/operatingsystem", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            OperatingSystemEntity osEntity = SyskiCache.GetDatabase().OperatingSystemDao().GetOperatingSystems(UUID.fromString(jsonObject.getString("id")));
            if (osEntity == null) {
                osEntity = new OperatingSystemEntity();
                osEntity.Id = UUID.fromString(jsonObject.getString("id"));
                osEntity.Name = jsonObject.getString("name");
                SyskiCache.GetDatabase().OperatingSystemDao().InsertAll(osEntity);

                SystemOSEntity systemOSEntity = new SystemOSEntity();
                systemOSEntity.OSId = osEntity.Id;
                systemOSEntity.SystemId = mSystemId;
                systemOSEntity.ArchitectureName = jsonObject.getString("architectureName");
                systemOSEntity.Version = jsonObject.getString("version");
                SyskiCache.GetDatabase().SystemOSDao().InsertAll(systemOSEntity);
            }

            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
