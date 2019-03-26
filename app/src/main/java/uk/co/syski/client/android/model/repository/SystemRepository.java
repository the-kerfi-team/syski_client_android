package uk.co.syski.client.android.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemCPURequest;
import uk.co.syski.client.android.model.api.requests.system.APISystemsRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.CPUDao;
import uk.co.syski.client.android.model.database.dao.SystemDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.GPUEntity;
import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;

public class SystemRepository {

    // Database DAO's
    private SystemDao mSystemDao;

    // CPU System in Memory
    private HashMap<UUID, SystemEntity> mSystemEntities;

    // Active System CPUs Cache in Memory
    private UUID mActiveSystemId;
    private boolean mActiveSystemChanged;
    private MutableLiveData mActiveSystemEntities;

    public SystemRepository() {
        mSystemDao = SyskiCache.GetDatabase().SystemDao();

        mSystemEntities = new HashMap<>();

        mActiveSystemEntities = new MutableLiveData<>();
        mActiveSystemChanged = true;
        loadFromDatabase();
    }

    private void setActiveSystem(UUID systemId)
    {
        if (mActiveSystemId == null || !mActiveSystemId.equals(systemId))
        {
            mActiveSystemId = systemId;
            mActiveSystemChanged = true;
        }
    }

    private void loadFromCache()
    {
        if (mActiveSystemChanged)
        {
            if (mActiveSystemId == null)
            {
                // Load all System's if no active System has been set
                mActiveSystemEntities.postValue(new LinkedList<>(mSystemEntities.values()));
            }
            else
            {
                // Load System
                mActiveSystemEntities.postValue(mSystemEntities.get(mActiveSystemId));
            }
            mActiveSystemChanged = false;
        }
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for CPU's
            mSystemEntities = new loadSystemEntitiesAsyncTask(mSystemDao, mSystemEntities).execute().get();
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

    public LiveData<List<SystemEntity>> getSystemsLiveData(Context context)
    {
        setActiveSystem(null);
        loadFromCache();
        loadFromAPI(context);
        return mActiveSystemEntities;
    }

    public LiveData<SystemEntity> getSystemLiveData(UUID systemId, Context context)
    {
        setActiveSystem(systemId);
        loadFromCache();
        loadFromAPI(context);
        return mActiveSystemEntities;
    }

    public void refresh()
    {
        setActiveSystem(null);
        loadFromCache();
    }

    public SystemEntity get(UUID id)
    {
        return mSystemEntities.get(id);
    }

    // Add other methods here

    public void upsert(SystemEntity systemEntity)
    {
        try {
            new upsertSystemsAsyncTask(mSystemDao).execute(systemEntity).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (mActiveSystemId == null)
        {
            // Load all System's if no active System has been set
            mActiveSystemEntities.postValue(new LinkedList<>(mSystemEntities.values()));
        }
        else if (mActiveSystemId.equals(systemEntity))
        {
            // Load System
            mActiveSystemEntities.postValue(mSystemEntities.get(mActiveSystemId));
        }
    }

    // Add other methods here

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

}
