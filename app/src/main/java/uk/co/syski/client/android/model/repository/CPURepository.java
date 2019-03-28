package uk.co.syski.client.android.model.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemCPURequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.CPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;

public enum CPURepository {
    INSTANCE;

    // Database DAO's
    private CPUDao mCPUDao;
    private SystemCPUDao mSystemCPUDao;

    // CPU Cache in Memory
    private HashMap<UUID, CPUEntity> mCPUEntities;
    private HashMap<UUID, List<UUID>> mSystemCPUEntities;

    // LiveData
    private MutableLiveData<HashMap<UUID, CPUEntity>> mLiveDataSystemCPUEntities;

    CPURepository() {
        mCPUDao = SyskiCache.GetDatabase().CPUDao();
        mSystemCPUDao = SyskiCache.GetDatabase().SystemCPUDao();

        mCPUEntities = new HashMap<>();
        mSystemCPUEntities = new HashMap<>();

        mLiveDataSystemCPUEntities = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for CPU's
            mCPUEntities = new loadCPUEntitiesAsyncTask(mCPUDao, mCPUEntities).execute().get();

            // Load data from Database for System CPU's
            mSystemCPUEntities = new loadSystemCPUEntitiesAsyncTask(mSystemCPUDao, mSystemCPUEntities).execute().get();

            // Set Data in LiveData
            mLiveDataSystemCPUEntities.postValue(mCPUEntities);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemCPURequest(context, systemId));
    }

    public void insert(CPUEntity cpuEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, CPUEntity cpuEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, List<CPUEntity> cpuEntities)
    {
        //TODO Create insert method
    }

    public LiveData<List<CPUEntity>> getSystemCPUsLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemCPUEntities, new Function<HashMap<UUID, CPUEntity>, List<CPUEntity>>() {
            @Override
            public List<CPUEntity> apply(HashMap<UUID, CPUEntity> input) {
                List<CPUEntity> result = new LinkedList<>();
                List<UUID> systemCPUEntities = mSystemCPUEntities.get(systemId);
                if (systemCPUEntities != null)
                {
                    for (UUID systemEntityId : systemCPUEntities)
                    {
                        result.add(input.get(systemEntityId));
                    }
                }
                return result;
            }
        });
    }

    public List<CPUEntity> getSystemCPUs(UUID systemId)
    {
        //TODO Create get System Component method
        return null;
    }

    public CPUEntity get(UUID id)
    {
        return mCPUEntities.get(id);
    }

    public List<CPUEntity> getAll()
    {
        //TODO Create get Components method
        return null;
    }

    public void update(CPUEntity cpuEntity)
    {
        //TODO Create update Component method
    }

    public void update(UUID systemId, CPUEntity cpuEntity)
    {
        //TODO Create update Components method
    }

    public void update(UUID systemId, List<CPUEntity> cpuEntities)
    {
        //TODO Create update Components method
    }

    public void upsert(CPUEntity cpuEntity)
    {
        //TODO Create upsert Component method
    }

    public void upsert(UUID systemId, CPUEntity cpuEntity)
    {
        //TODO Create upsert Components method
    }

    public void upsert(UUID systemId, List<CPUEntity> cpuEntities)
    {
        try {
            new upsertSystemCPUAsyncTask(mCPUDao, mSystemCPUDao, systemId, cpuEntities).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<UUID> systemCPUEntities = new LinkedList<>();
        for (CPUEntity cpuEntity : cpuEntities)
        {
            systemCPUEntities.add(cpuEntity.Id);
        }
        mSystemCPUEntities.put(systemId, systemCPUEntities);
        mLiveDataSystemCPUEntities.postValue(mCPUEntities);
    }

    public void delete(CPUEntity cpuEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, CPUEntity cpuEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, List<CPUEntity> cpuEntities)
    {
        //TODO Create delete Component method
    }

    private static class loadCPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, CPUEntity>> {

        private CPUDao mCPUDao;
        private HashMap<UUID, CPUEntity> mCPUEntities;


        loadCPUEntitiesAsyncTask(CPUDao cpuDao, HashMap<UUID, CPUEntity> CPUEntities) {
            mCPUDao = cpuDao;
            mCPUEntities = CPUEntities;
        }

        protected HashMap<UUID, CPUEntity> doInBackground(final Void... voids) {
            for (CPUEntity cpuEntity : mCPUDao.get())
            {
                mCPUEntities.put(cpuEntity.Id, cpuEntity);
            }
            return mCPUEntities;
        }

    }

    private static class loadSystemCPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<UUID>>> {

        private SystemCPUDao mSystemCPUDao;
        private HashMap<UUID, List<UUID>> mSystemCPUEntities;


        loadSystemCPUEntitiesAsyncTask(SystemCPUDao systemCPUDao, HashMap<UUID, List<UUID>> systemCPUEntities) {
            mSystemCPUDao = systemCPUDao;
            mSystemCPUEntities = systemCPUEntities;
        }

        protected HashMap<UUID, List<UUID>> doInBackground(final Void... voids) {
            for (SystemCPUEntity SystemCPUEntity : mSystemCPUDao.get())
            {
                List<UUID> CPUEntities = mSystemCPUEntities.get(SystemCPUEntity.SystemId);
                if (CPUEntities == null)
                {
                    CPUEntities = new LinkedList<>();
                }
                CPUEntities.add(SystemCPUEntity.CPUId);
                mSystemCPUEntities.put(SystemCPUEntity.SystemId, CPUEntities);
            }
            return mSystemCPUEntities;
        }

    }

    private static class upsertSystemCPUAsyncTask extends AsyncTask<Void , Void, Void> {

        private CPUDao mCPUDao;
        private SystemCPUDao mSystemCPUDao;
        private UUID mSystemId;

        private List<CPUEntity> mCPUEntities;

        upsertSystemCPUAsyncTask(CPUDao cpuDao, SystemCPUDao systemCPUDao, UUID systemId, List<CPUEntity> cpuEntities) {
            mCPUDao = cpuDao;
            mSystemCPUDao = systemCPUDao;
            mSystemId = systemId;

            mCPUEntities = cpuEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemCPUDao.deleteBySystemId(mSystemId);
            for (CPUEntity cpuEntity: mCPUEntities) {
                mCPUDao.upsert(cpuEntity);
                SystemCPUEntity systemCPUEntity = new SystemCPUEntity();
                systemCPUEntity.SystemId = mSystemId;
                systemCPUEntity.CPUId = cpuEntity.Id;
                mSystemCPUDao.insert(systemCPUEntity);
            }
            return null;
        }

    }

}
