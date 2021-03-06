package uk.co.syski.client.android.model.api.requests.system;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import uk.co.syski.client.android.model.api.requests.APIAuthorizationRequest;
import uk.co.syski.client.android.model.database.entity.MotherboardEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemMotherboardEntity;
import uk.co.syski.client.android.model.repository.Repository;

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

            //MotherboardEntity motherboardEntity = SyskiCache.GetDatabase().MotherboardDao().get(UUID.fromString(jsonObject.getString("id")));
            MotherboardEntity motherboardEntity = Repository.getInstance().getMOBORepository().getMotherboard(UUID.fromString(jsonObject.getString("id")));
            if (motherboardEntity == null) {
                motherboardEntity = new MotherboardEntity();
            }
            motherboardEntity.Id = UUID.fromString(jsonObject.getString("id"));
            motherboardEntity.ManufacturerName = jsonObject.getString("manufacturerName");
            motherboardEntity.ModelName = jsonObject.getString("modelName");
            motherboardEntity.Version = jsonObject.getString("version");

            SystemMotherboardEntity systemMotherboardEntity = new SystemMotherboardEntity();
            systemMotherboardEntity.SystemId = mSystemId;
            systemMotherboardEntity.MotherboardId = motherboardEntity.Id;

            Repository.getInstance().getMOBORepository().upsert(mSystemId, systemMotherboardEntity, motherboardEntity);

            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException | UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}
