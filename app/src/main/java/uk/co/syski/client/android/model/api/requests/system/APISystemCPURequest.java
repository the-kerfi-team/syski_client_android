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
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.repository.CPURepository;
import uk.co.syski.client.android.model.repository.Repository;

public class APISystemCPURequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;
    private CPURepository mCPURepository;

    public APISystemCPURequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/cpu", null, null, null);
        mSystemId = systemId;
        mCPURepository = Repository.getInstance().getCPURepository();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            List<CPUEntity> newCPUEntities = new LinkedList<>();
            for (int i = 0; i < jsonArray.length(); i++) {

                // Load from Database
                //CPUEntity cpuEntity = SyskiCache.GetDatabase().CPUDao().get());

                // Load from Repository (Using Cache)
                CPUEntity cpuEntity = Repository.getInstance().getCPURepository().get(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));

                if (cpuEntity == null)
                {
                    cpuEntity = new CPUEntity();
                }
                cpuEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                cpuEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                cpuEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                cpuEntity.ArchitectureName = ((JSONObject) jsonArray.get(i)).getString("architectureName");
                cpuEntity.ClockSpeed = (int) Double.parseDouble(((JSONObject) jsonArray.get(i)).getString("clockSpeed"));
                cpuEntity.CoreCount = (int) Double.parseDouble(((JSONObject) jsonArray.get(i)).getString("coreCount"));
                cpuEntity.ThreadCount = (int) Double.parseDouble(((JSONObject) jsonArray.get(i)).getString("threadCount"));
                newCPUEntities.add(cpuEntity);
            }
            Repository.getInstance().getCPURepository().upsert(mSystemId, newCPUEntities);

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
