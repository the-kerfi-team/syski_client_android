package uk.co.syski.client.android.data.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import uk.co.syski.client.android.api.VolleySingleton;
import uk.co.syski.client.android.api.requests.system.APISystemCPUDataRequest;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;

public class SystemCPUDataRepository {

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mTimerHandler = new Handler();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

    private Application mApplication;
    private final MutableLiveData<List<CPUDataEntity>> mCPUDataEntity;
    private UUID mActiveSystemId;

    public SystemCPUDataRepository(Application application, UUID systemId)
    {
        mCPUDataEntity = new MutableLiveData();
        mActiveSystemId = systemId;
        mApplication = application;
    }

    public void stop()
    {
        if(mTimer != null){
            mTimer.cancel();
            mTimer.purge();
        }
    }

    public void start(){
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        VolleySingleton.getInstance(mApplication).addToRequestQueue(new APISystemCPUDataRequest(mApplication, mActiveSystemId,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        List cpuDataEntities = new ArrayList();
                                        for (int i = 0; i < response.length(); i++)
                                        {
                                            CPUDataEntity cpuDataEntity = new CPUDataEntity();
                                            cpuDataEntity.SystemId = mActiveSystemId;
                                            try {
                                                cpuDataEntity.Load = Float.parseFloat(((JSONObject) response.get(i)).getString("load"));
                                                cpuDataEntity.Processes = Float.parseFloat(((JSONObject) response.get(i)).getString("processes"));
                                                cpuDataEntity.CollectionDateTime = dateFormat.parse(((JSONObject) response.get(i)).getString("collectionDateTime"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            cpuDataEntities.add(cpuDataEntity);
                                        }
                                        mCPUDataEntity.postValue(cpuDataEntities);
                                    }
                                }, null));
                    }
                });
            }
        };

        mTimer.schedule(mTimerTask, 1, 3000);
    }

    public MutableLiveData<List<CPUDataEntity>> get()
    {
        return mCPUDataEntity;
    }

}
