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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIAuthorizationRequest;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.linking.SystemCPUEntity;

public class APISystemCPURequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;

    public APISystemCPURequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/cpu", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {

            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            for (int i = 0; i < jsonArray.length(); i++) {
                CPUEntity cpuEntity = SyskiCache.GetDatabase().CPUDao().getCPU(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));
                if (cpuEntity == null)
                {
                    cpuEntity = new CPUEntity();
                    cpuEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                    cpuEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                    cpuEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                    cpuEntity.ArchitectureName = ((JSONObject) jsonArray.get(i)).getString("architectureName");
                    //
                    cpuEntity.ClockSpeed = (int) Double.parseDouble(((JSONObject) jsonArray.get(i)).getString("clockSpeed"));
                    cpuEntity.CoreCount = (int) Double.parseDouble(((JSONObject) jsonArray.get(i)).getString("coreCount"));
                    cpuEntity.ThreadCount = (int) Double.parseDouble(((JSONObject) jsonArray.get(i)).getString("clockSpeed"));

                    SyskiCache.GetDatabase().CPUDao().InsertAll(cpuEntity);

                    SystemCPUEntity systemCPUEntity = new SystemCPUEntity();
                    systemCPUEntity.CPUId = cpuEntity.Id;
                    systemCPUEntity.SystemId = mSystemId;
                    SyskiCache.GetDatabase().SystemCPUDao().InsertAll(systemCPUEntity);
                }
                else
                {
                    // TODO Update CPU method
                }
            }

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
