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
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;

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

            for (int i = 0; i < jsonArray.length(); i++) {
                RAMEntity ramEntity = SyskiCache.GetDatabase().RAMDao().GetRAM(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));
                if (ramEntity == null) {
                    ramEntity = new RAMEntity();
                    ramEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                    ramEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                    ramEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                    ramEntity.MemoryTypeName = ((JSONObject) jsonArray.get(i)).getString("memoryTypeName");
                    ramEntity.MemoryBytes = Long.parseLong(((JSONObject) jsonArray.get(i)).getString("memoryBytes"));

                    SyskiCache.GetDatabase().RAMDao().InsertAll(ramEntity);

                    SystemRAMEntity systemRAMEntity = new SystemRAMEntity();
                    systemRAMEntity.RAMId = ramEntity.Id;
                    systemRAMEntity.SystemId = mSystemId;
                    SyskiCache.GetDatabase().SystemRAMDao().InsertAll(systemRAMEntity);
                } else {
                    // TODO Update RAM method
                }
            }

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
