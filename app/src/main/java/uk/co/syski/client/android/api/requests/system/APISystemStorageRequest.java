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
import uk.co.syski.client.android.data.entity.StorageEntity;
import uk.co.syski.client.android.data.entity.linking.SystemStorageEntity;
import uk.co.syski.client.android.data.repository.Repository;

public class APISystemStorageRequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;

    public APISystemStorageRequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/storage", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {

            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            for (int i = 0; i < jsonArray.length(); i++) {
                StorageEntity storageEntity = SyskiCache.GetDatabase().StorageDao().get(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));
                if (storageEntity == null) {
                    storageEntity = new StorageEntity();
                    storageEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                    storageEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                    storageEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                    storageEntity.MemoryTypeName = ((JSONObject) jsonArray.get(i)).getString("memoryTypeName");
                    storageEntity.MemoryBytes = Long.parseLong(((JSONObject) jsonArray.get(i)).getString("memoryBytes"));
                    Repository.getInstance().getStorageRepository().insert(storageEntity);
                } else {
                    storageEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                    storageEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                    storageEntity.MemoryTypeName = ((JSONObject) jsonArray.get(i)).getString("memoryTypeName");
                    storageEntity.MemoryBytes = Long.parseLong(((JSONObject) jsonArray.get(i)).getString("memoryBytes"));
                    Repository.getInstance().getStorageRepository().update(storageEntity);
                }

                SystemStorageEntity systemStorageEntity = SyskiCache.GetDatabase().SystemStorageDao().get(mSystemId, i);
                if (systemStorageEntity == null)
                {
                    Repository.getInstance().getStorageRepository().insert(storageEntity, mSystemId, i);
                }
                else
                {
                    systemStorageEntity.StorageId = storageEntity.Id;
                    systemStorageEntity.Slot = i;
                }

            }

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
