package uk.co.syski.client.android.model.repository;

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

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemProcessDataRequest;
import uk.co.syski.client.android.model.database.entity.data.SystemProcessesEntity;

public class SystemProcessesRepository {

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mTimerHandler = new Handler();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

    private Application mApplication;
    private final MutableLiveData<List<SystemProcessesEntity>> mProcessesDataEntity;
    private UUID mActiveSystemId;

    public SystemProcessesRepository(Application application, UUID systemId)
    {
        mProcessesDataEntity = new MutableLiveData();
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
                        VolleySingleton.getInstance(mApplication).addToRequestQueue(new APISystemProcessDataRequest(mApplication, mActiveSystemId,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        List processesDataEntities = new ArrayList();
                                        for (int i = 0; i < response.length(); i++)
                                        {
                                            SystemProcessesEntity systemProcessesEntity = new SystemProcessesEntity();
                                            systemProcessesEntity.SystemId = mActiveSystemId;
                                            try {
                                                systemProcessesEntity.Id = Integer.parseInt(((JSONObject) response.get(i)).getString("id"));
                                                systemProcessesEntity.KernelTime = Long.parseLong(((JSONObject) response.get(i)).getString("kernelTime"));
                                                systemProcessesEntity.MemSize = Long.parseLong((((JSONObject) response.get(i)).getString("memSize")));
                                                systemProcessesEntity.Name = ((JSONObject) response.get(i)).getString("name");
                                                systemProcessesEntity.ParentId = Integer.parseInt(((JSONObject) response.get(i)).getString("parentId"));
                                                systemProcessesEntity.Path = ((JSONObject) response.get(i)).getString("path");
                                                systemProcessesEntity.Threads = Integer.parseInt(((JSONObject) response.get(i)).getString("threads"));
                                                systemProcessesEntity.UpTime = Long.parseLong(((JSONObject) response.get(i)).getString("upTime"));
                                                systemProcessesEntity.CollectionDateTime = dateFormat.parse(((JSONObject) response.get(i)).getString("collectionDateTime"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            processesDataEntities.add(systemProcessesEntity);
                                        }
                                        mProcessesDataEntity.postValue(processesDataEntities);
                                    }
                                }, null));
                    }
                });
            }
        };

        mTimer.schedule(mTimerTask, 1, 30000);
    }

    public MutableLiveData<List<SystemProcessesEntity>> get()
    {
        return mProcessesDataEntity;
    }

}
