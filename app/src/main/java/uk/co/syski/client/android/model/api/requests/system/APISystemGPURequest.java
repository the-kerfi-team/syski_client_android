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
import uk.co.syski.client.android.model.database.entity.GPUEntity;
import uk.co.syski.client.android.model.repository.Repository;

public class APISystemGPURequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;

    public APISystemGPURequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/gpu", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {

            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            List<GPUEntity> newGPUEntities = new LinkedList<>();
            for (int i = 0; i < jsonArray.length(); i++) {

                // Load from Database
                //GPUEntity gpuEntity = SyskiCache.GetDatabase().GPUDao().get());

                // Load from Repository (Using Cache)
                GPUEntity gpuEntity = Repository.getInstance().getGPURepository().get(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));
                if (gpuEntity == null) {
                    gpuEntity = new GPUEntity();
                }
                gpuEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                gpuEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                gpuEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                newGPUEntities.add(gpuEntity);
            }
            Repository.getInstance().getGPURepository().upsert(mSystemId, newGPUEntities);

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
