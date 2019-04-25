package uk.co.syski.client.android.model.api.requests.system;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import uk.co.syski.client.android.model.api.requests.APIAuthorizationRequest;
import uk.co.syski.client.android.model.database.entity.BIOSEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemBIOSEntity;
import uk.co.syski.client.android.model.repository.Repository;

public class APISystemBIOSRequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;

    public APISystemBIOSRequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/bios", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            BIOSEntity biosEntity = Repository.getInstance().getBIOSRepository().getBIOS(UUID.fromString(jsonObject.getString("id")));
            if (biosEntity == null) {
                biosEntity = new BIOSEntity();
            }
            biosEntity.Id = UUID.fromString(jsonObject.getString("id"));
            biosEntity.ManufacturerName = jsonObject.getString("manufacturerName");
            biosEntity.Caption = jsonObject.getString("caption");
            biosEntity.Version = jsonObject.getString("version");
            biosEntity.Date = jsonObject.getString("date");

            SystemBIOSEntity systemBIOSEntity = new SystemBIOSEntity();
            systemBIOSEntity.SystemId = mSystemId;
            systemBIOSEntity.BIOSId = biosEntity.Id;

            Repository.getInstance().getBIOSRepository().upsert(mSystemId, systemBIOSEntity, biosEntity);

            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException | UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }


}
