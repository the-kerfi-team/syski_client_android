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
import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.model.repository.Repository;

public class APISystemOperatingSystemRequest extends APIAuthorizationRequest<JSONArray> {

    private UUID mSystemId;

    public APISystemOperatingSystemRequest(Context context, UUID systemId) {
        super(context, Method.GET, "system/" + systemId.toString() + "/operatingsystem", null, null, null);
        mSystemId = systemId;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            List<OperatingSystemEntity> newOSEntities = new LinkedList<>();
            List<SystemOSEntity> newSystemOSEntities = new LinkedList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                //OperatingSystemEntity osEntity = SyskiCache.GetDatabase().OperatingSystemDao().GetOperatingSystems(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));
                OperatingSystemEntity osEntity = Repository.getInstance().getOSRepository().getOS(mSystemId);
                if (osEntity == null) {
                    osEntity = new OperatingSystemEntity();
                }
                osEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                osEntity.Name = ((JSONObject) jsonArray.get(i)).getString("name");
                newOSEntities.add(osEntity);

                SystemOSEntity systemOSEntity = new SystemOSEntity();
                systemOSEntity.SystemId = mSystemId;
                systemOSEntity.OSId = osEntity.Id;
                systemOSEntity.ArchitectureName = ((JSONObject) jsonArray.get(i)).getString("architectureName");
                systemOSEntity.Version = ((JSONObject) jsonArray.get(i)).getString("version");
                newSystemOSEntities.add(systemOSEntity);
            }
            Repository.getInstance().getOSRepository().upsert(mSystemId, newSystemOSEntities, newOSEntities);

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

}
