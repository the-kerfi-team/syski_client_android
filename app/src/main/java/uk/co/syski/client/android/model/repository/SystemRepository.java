package uk.co.syski.client.android.model.repository;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemCPUDataRequest;
import uk.co.syski.client.android.model.api.requests.system.APISystemPingRequest;
import uk.co.syski.client.android.model.api.requests.system.APISystemRemove;
import uk.co.syski.client.android.model.api.requests.system.APISystemsRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.SystemDao;
import uk.co.syski.client.android.model.database.entity.StorageEntity;
import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.database.entity.data.CPUDataEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemStorageEntity;
import uk.co.syski.client.android.model.viewmodel.SystemModel;
import uk.co.syski.client.android.model.viewmodel.SystemStorageModel;

public enum SystemRepository {
    INSTANCE;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mTimerHandler = new Handler();

    // Database DAO's
    private SystemDao mSystemDao;

    // Systems in Memory
    private HashMap<UUID, SystemEntity> mSystemEntities;
    private HashMap<UUID, SystemModel> mSystemModels;

    // System in LiveData
    private MutableLiveData<HashMap<UUID, SystemModel>> mLiveDataSystemEntities;

    SystemRepository() {
        mSystemDao = SyskiCache.GetDatabase().SystemDao();

        mSystemEntities = new HashMap<>();
        mSystemModels = new HashMap<>();

        mLiveDataSystemEntities = new MutableLiveData<>();
        loadFromDatabase();
    }

    public void start(final Application application){
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        VolleySingleton.getInstance(application.getBaseContext()).addToRequestQueue(new APISystemsRequest(application.getBaseContext()));
                        for (final Map.Entry<UUID, SystemModel> entry: mSystemModels.entrySet()) {
                            VolleySingleton.getInstance(application).addToRequestQueue(new APISystemPingRequest(application, entry.getKey(),
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    SystemModel systemModel = entry.getValue();
                                                    try {
                                                        systemModel.setOnline();
                                                        systemModel.setPing(Float.parseFloat(response.getString("ping")));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    mLiveDataSystemEntities.postValue(mSystemModels);
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    SystemModel systemModel = entry.getValue();
                                                    systemModel.setOffline();
                                                    mLiveDataSystemEntities.postValue(mSystemModels);
                                                }
                                            }
                                    )
                            );
                        }
                    }
                });
            }
        };
        mTimer.schedule(mTimerTask, 1, 10000);
    }

    public void stop()
    {
        if(mTimer != null){
            mTimer.cancel();
            mTimer.purge();
        }
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for CPU's
            mSystemEntities = new loadSystemEntitiesAsyncTask(mSystemDao, mSystemEntities).execute().get();

            // Set Data in LiveData
            for (Map.Entry<UUID, SystemEntity> entry : mSystemEntities.entrySet())
            {
                SystemModel systemModel = mSystemModels.get(entry.getKey());
                if (systemModel == null)
                {
                    SystemEntity systemEntity = entry.getValue();
                    systemModel = new SystemModel(systemEntity.Id, systemEntity.HostName, systemEntity.ModelName, systemEntity.ManufacturerName);
                }
                mSystemModels.put(entry.getKey(), systemModel);
            }
            mLiveDataSystemEntities.postValue(mSystemModels);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemsRequest(context));
    }

    public LiveData<List<SystemModel>> getSystemsLiveData(Context context)
    {
        loadFromAPI(context);
        return Transformations.map(mLiveDataSystemEntities, new Function<HashMap<UUID, SystemModel>, List<SystemModel>>() {
            @Override
            public List<SystemModel> apply(HashMap<UUID, SystemModel> input) {
                return new LinkedList<>(input.values());
            }
        });
    }

    public LiveData<SystemModel> getSystemLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context);
        return Transformations.map(mLiveDataSystemEntities, new Function<HashMap<UUID, SystemModel>, SystemModel>() {
            @Override
            public SystemModel apply(HashMap<UUID, SystemModel> input) {
                return input.get(systemId);
            }
        });
    }

    public SystemEntity get(UUID id)
    {
        return mSystemEntities.get(id);
    }

    public void upsert(SystemEntity systemEntity)
    {
        try {
            new upsertSystemsAsyncTask(mSystemDao).execute(systemEntity).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        mSystemEntities.put(systemEntity.Id, systemEntity);
        if (mSystemModels.get(systemEntity.Id) == null)
        {
            mSystemModels.put(systemEntity.Id, new SystemModel(systemEntity.Id, systemEntity.HostName, systemEntity.ModelName, systemEntity.ManufacturerName));
            mLiveDataSystemEntities.postValue(mSystemModels);
        }
    }

    public void delete(UUID id, Context context)
    {
        try {
            new deleteSystemsAsyncTask(mSystemDao).execute(mSystemEntities.get(id)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        mSystemEntities.remove(id);
        mSystemModels.remove(id);
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemRemove(context, id));
        mLiveDataSystemEntities.postValue(mSystemModels);
    }

    private static class loadSystemEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, SystemEntity>> {

        private SystemDao mSystemDao;
        private HashMap<UUID, SystemEntity> mSystemEntities;


        loadSystemEntitiesAsyncTask(SystemDao systemDao, HashMap<UUID, SystemEntity> SystemEntities) {
            mSystemDao = systemDao;
            mSystemEntities = SystemEntities;
        }

        protected HashMap<UUID, SystemEntity> doInBackground(final Void... voids) {
            for (SystemEntity systemEntity : mSystemDao.get())
            {
                mSystemEntities.put(systemEntity.Id, systemEntity);
            }
            return mSystemEntities;
        }

    }

    private static class upsertSystemsAsyncTask extends AsyncTask<SystemEntity, Void, Void> {

        private SystemDao mSystemDao;

        upsertSystemsAsyncTask(SystemDao systemDao) {
            mSystemDao = systemDao;
        }

        protected Void doInBackground(final SystemEntity... systemEntities) {
            for (SystemEntity systemEntity: systemEntities) {
                mSystemDao.upsert(systemEntity);
            }
            return null;
        }

    }

    private static class deleteSystemsAsyncTask extends AsyncTask<SystemEntity, Void, Void> {
        private SystemDao mSystemDao;

        deleteSystemsAsyncTask(SystemDao systemDao) { mSystemDao = systemDao;}

        protected Void doInBackground(final SystemEntity... systemEntities) {
            for (SystemEntity systemEntity: systemEntities) {
                mSystemDao.delete(systemEntity);
            }
            return null;
        }
    }

}
