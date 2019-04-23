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
import uk.co.syski.client.android.model.api.requests.system.APISystemRAMDataRequest;
import uk.co.syski.client.android.model.database.entity.data.RAMDataEntity;

public class SystemRAMDataRepository {

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mTimerHandler = new Handler();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

    private Application mApplication;
    private final MutableLiveData<List<RAMDataEntity>> mRAMDataEntity;
    private UUID mActiveSystemId;

    public SystemRAMDataRepository(Application application, UUID systemId)
    {
        mRAMDataEntity = new MutableLiveData();
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
                        VolleySingleton.getInstance(mApplication).addToRequestQueue(new APISystemRAMDataRequest(mApplication, mActiveSystemId,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        List ramDataEntities = new ArrayList();
                                        for (int i = 0; i < response.length(); i++)
                                        {
                                            RAMDataEntity ramDataEntity = new RAMDataEntity();
                                            ramDataEntity.SystemId = mActiveSystemId;
                                            try {
                                                ramDataEntity.Free = Integer.parseInt(((JSONObject) response.get(i)).getString("free"));
                                                ramDataEntity.CollectionDateTime = dateFormat.parse(((JSONObject) response.get(i)).getString("collectionDateTime"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            ramDataEntities.add(ramDataEntity);
                                        }
                                        mRAMDataEntity.postValue(ramDataEntities);
                                    }
                                }, null));
                    }
                });
            }
        };

        mTimer.schedule(mTimerTask, 1, 3000);
    }

    public MutableLiveData<List<RAMDataEntity>> get()
    {
        return mRAMDataEntity;
    }

}
