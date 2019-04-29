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

public class APISystemKillProcess extends APIAuthorizationRequest<JSONObject> {

    private int processId;

    public APISystemKillProcess(Context context, UUID systemId, int processId){
        super(context,Method.POST,"system/"+systemId+"/processes/kill",null,null,null);
        this.processId = processId;
    }

    @Override
    public byte[] getBody() {
        try {
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject();
                jsonBody.put("id", processId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String requestBody = jsonBody.toString();
            return requestBody == null ? null : requestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response){
        try{
            JSONObject jsonArray=new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers,PROTOCOL_CHARSET)));
            return Response.success(jsonArray,HttpHeaderParser.parseCacheHeaders(response));
        }catch(UnsupportedEncodingException |JSONException e){
            return Response.error(new ParseError(e));
        }
    }
    
}
