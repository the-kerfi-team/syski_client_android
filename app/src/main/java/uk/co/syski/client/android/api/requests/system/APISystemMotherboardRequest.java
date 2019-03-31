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
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.repository.Repository;

public class APISystemMotherboardRequest extends APIAuthorizationRequest<JSONObject> {

    private UUID mSystemId;

    public APISystemMotherboardRequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/motherboard", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            MotherboardEntity motherboardEntity = SyskiCache.GetDatabase().MotherboardDao().get(UUID.fromString(jsonObject.getString("id")));
                if (motherboardEntity == null) {
                    motherboardEntity = new MotherboardEntity();
                    motherboardEntity.Id = UUID.fromString(jsonObject.getString("id"));
                    motherboardEntity.ManufacturerName = jsonObject.getString("manufacturerName");
                    motherboardEntity.ModelName = jsonObject.getString("modelName");
                    motherboardEntity.Version = jsonObject.getString("version");
                    Repository.getInstance().getMOBORepository().insert(motherboardEntity);

                    SystemEntity systemEntity = SyskiCache.GetDatabase().SystemDao().get(mSystemId);
                    if (systemEntity != null)
                    {
                        systemEntity.MotherboardId = motherboardEntity.Id;
                        SyskiCache.GetDatabase().SystemDao().update(systemEntity);
                    }

            }

            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
