package uk.co.syski.client.android.model.api.requests.system;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.api.requests.APIAuthorizationRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.RAMEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemRAMEntity;
import uk.co.syski.client.android.model.repository.Repository;

public class APISystemRAMRequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;

    public APISystemRAMRequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/ram", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {

            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            List<RAMEntity> newRAMEntities = new LinkedList<>();
            for (int i = 0; i < jsonArray.length(); i++) {

                // Load from Database
                //RAMEntity ramEntity = SyskiCache.GetDatabase().RAMDao().GetRAM(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));

                // Load from Repository (Using Cache)
                RAMEntity ramEntity = Repository.getInstance().getRAMRepository().get(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));

                if (ramEntity == null) {
                    ramEntity = new RAMEntity();
                }
                ramEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                ramEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                ramEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                ramEntity.MemoryTypeName = ((JSONObject) jsonArray.get(i)).getString("memoryTypeName");
                ramEntity.MemoryBytes = Long.parseLong(((JSONObject) jsonArray.get(i)).getString("memoryBytes"));
                newRAMEntities.add(ramEntity);
            }
            Repository.getInstance().getRAMRepository().upsert(mSystemId, newRAMEntities);

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
