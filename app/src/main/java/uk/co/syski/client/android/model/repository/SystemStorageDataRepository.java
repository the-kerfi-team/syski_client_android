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
import uk.co.syski.client.android.model.api.requests.system.APISystemStorageDataRequest;
import uk.co.syski.client.android.model.database.entity.data.StorageDataEntity;

public class SystemStorageDataRepository {

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mTimerHandler = new Handler();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

    private Application mApplication;
    private final MutableLiveData<List<StorageDataEntity>> mStorageDataEntity;
    private UUID mActiveSystemId;

    public SystemStorageDataRepository(Application application, UUID systemId)
    {
        mStorageDataEntity = new MutableLiveData();
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
                        VolleySingleton.getInstance(mApplication).addToRequestQueue(new APISystemStorageDataRequest(mApplication, mActiveSystemId,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        List storageDataEntities = new ArrayList();
                                        for (int i = 0; i < response.length(); i++)
                                        {
                                            StorageDataEntity storageDataEntity = new StorageDataEntity();
                                            storageDataEntity.SystemId = mActiveSystemId;
                                            try {
                                                storageDataEntity.Time = Float.parseFloat(((JSONObject) response.get(i)).getString("time"));
                                                storageDataEntity.Transfers = Float.parseFloat(((JSONObject) response.get(i)).getString("transfers"));
                                                storageDataEntity.Reads = Float.parseFloat(((JSONObject) response.get(i)).getString("reads"));
                                                storageDataEntity.Writes = Float.parseFloat(((JSONObject) response.get(i)).getString("writes"));
                                                storageDataEntity.ByteReads = Float.parseFloat(((JSONObject) response.get(i)).getString("byteReads"));
                                                storageDataEntity.ByteWrites = Float.parseFloat(((JSONObject) response.get(i)).getString("byteWrites"));
                                                storageDataEntity.CollectionDateTime = dateFormat.parse(((JSONObject) response.get(i)).getString("collectionDateTime"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            storageDataEntities.add(storageDataEntity);
                                        }
                                        mStorageDataEntity.postValue(storageDataEntities);
                                    }
                                }, null));
                    }
                });
            }
        };

        mTimer.schedule(mTimerTask, 1, 3000);
    }

    public MutableLiveData<List<StorageDataEntity>> get()
    {
        return mStorageDataEntity;
    }

}
