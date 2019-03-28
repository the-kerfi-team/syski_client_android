package uk.co.syski.client.android.model.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemsRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.SystemDao;
import uk.co.syski.client.android.model.database.entity.SystemEntity;

public enum SystemRepository {
    INSTANCE;

    // Database DAO's
    private SystemDao mSystemDao;

    // Systems in Memory
    private HashMap<UUID, SystemEntity> mSystemEntities;

    // System in LiveData
    private MutableLiveData<HashMap<UUID, SystemEntity>> mLiveDataSystemEntities;

    SystemRepository() {
        mSystemDao = SyskiCache.GetDatabase().SystemDao();

        mSystemEntities = new HashMap<>();

        mLiveDataSystemEntities = new MutableLiveData<>();

        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for CPU's
            mSystemEntities = new loadSystemEntitiesAsyncTask(mSystemDao, mSystemEntities).execute().get();
            mLiveDataSystemEntities.postValue(mSystemEntities);
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

    public LiveData<HashMap<UUID, SystemEntity>> getSystemsLiveData(Context context)
    {
        loadFromAPI(context);
        return mLiveDataSystemEntities;
    }

    public LiveData<SystemEntity> getSystemLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context);
        return Transformations.map(mLiveDataSystemEntities, new Function<HashMap<UUID, SystemEntity>, SystemEntity>() {
            @Override
            public SystemEntity apply(HashMap<UUID, SystemEntity> input) {
                return input.get(systemId);
            }
        });
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
        mSystemEntities.put(systemEntity.Id, systemEntity);
        mLiveDataSystemEntities.postValue(mSystemEntities);
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
