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
import java.util.UUID;

import uk.co.syski.client.android.api.requests.APIAuthorizationRequest;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.repository.Repository;

public class APISystemsRequest extends APIAuthorizationRequest<JSONArray> {

    public APISystemsRequest(Context context) {
        super(context, Method.GET, "system", null, null, null);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {

            JSONArray jsonArray = new JSONArray(new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));

            for (int i = 0; i < jsonArray.length(); i++) {
                SystemEntity systemEntity = SyskiCache.GetDatabase().SystemDao().get(UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id")));
                if (systemEntity == null)
                {
                    systemEntity = new SystemEntity();
                    systemEntity.Id = UUID.fromString(((JSONObject) jsonArray.get(i)).getString("id"));
                    systemEntity.HostName = ((JSONObject) jsonArray.get(i)).getString("hostName");
                    systemEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                    systemEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                    systemEntity.LastUpdated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS").parse(((JSONObject) jsonArray.get(i)).getString("lastUpdated"));
                    Repository.getSystemRepository().create(systemEntity);
                }
                else
                {
                    systemEntity.HostName = ((JSONObject) jsonArray.get(i)).getString("hostName");
                    systemEntity.ModelName = ((JSONObject) jsonArray.get(i)).getString("modelName");
                    systemEntity.ManufacturerName = ((JSONObject) jsonArray.get(i)).getString("manufacturerName");
                    systemEntity.LastUpdated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS").parse(((JSONObject) jsonArray.get(i)).getString("lastUpdated"));
                    Repository.getSystemRepository().insert(systemEntity);
                }
            }

            SystemEntity system = new SystemEntity();
            UUID uuid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
            system.Id = uuid.randomUUID();
            system.HostName = "Earth";
            system.ManufacturerName = "Not Dell";
            system.ModelName = "Mega Thinkpad";
            Repository.getSystemRepository().insert(system);

            return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        } catch (ParseException e) {
            return Response.error(new ParseError(e));
        }
    }

}
