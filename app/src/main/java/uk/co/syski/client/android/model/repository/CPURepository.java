package uk.co.syski.client.android.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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

public class CPURepository {

    // Database DAO's
    private CPUDao mCPUDao;
    private SystemCPUDao mSystemCPUDao;

    // CPU Cache in Memory
    private HashMap<UUID, CPUEntity> mCPUEntities;
    private HashMap<UUID, List<UUID>> mSystemCPUEntities;

    // Active System CPUs Cache in Memory
    private UUID mActiveSystemId;
    private boolean mActiveSystemChanged;
    private MutableLiveData<List<CPUEntity>> mActiveSystemCPUEntities;

    public CPURepository() {
        mCPUDao = SyskiCache.GetDatabase().CPUDao();
        mSystemCPUDao = SyskiCache.GetDatabase().SystemCPUDao();

        mCPUEntities = new HashMap<>();
        mSystemCPUEntities = new HashMap<>();

        mActiveSystemCPUEntities = new MutableLiveData<>();
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
                // Load all CPU's if no active System has been set
                mActiveSystemCPUEntities.postValue(new LinkedList<>(mCPUEntities.values()));
            }
            else
            {
                // Load System CPU's
                List<UUID> ActiveSystemCPUsIds = mSystemCPUEntities.get(mActiveSystemId);
                if (ActiveSystemCPUsIds != null) {
                    List<CPUEntity> ActiveSystemCPUs = new LinkedList<>();
                    for (UUID cpuEntityId : ActiveSystemCPUsIds) {
                        ActiveSystemCPUs.add(mCPUEntities.get(cpuEntityId));
                    }
                    mActiveSystemCPUEntities.postValue(ActiveSystemCPUs);
                }
                else
                {
                    mActiveSystemCPUEntities.postValue(null);
                }
            }
            mActiveSystemChanged = false;
        }
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for CPU's
            mCPUEntities = new loadCPUEntitiesAsyncTask(mCPUDao, mCPUEntities).execute().get();

            // Load data from Database for System CPU's
            mSystemCPUEntities = new loadSystemCPUEntitiesAsyncTask(mSystemCPUDao, mSystemCPUEntities).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemCPURequest(context, mActiveSystemId));
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

    public LiveData<List<CPUEntity>> getSystemCPUsLiveData(UUID systemId, Context context)
    {
        setActiveSystem(systemId);
        loadFromCache();
        loadFromAPI(context);
        return mActiveSystemCPUEntities;
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
            new upsertSystemCPUAsyncTask(mCPUDao, mSystemCPUDao, systemId).execute((CPUEntity[]) cpuEntities.toArray()).get();
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
        if (mActiveSystemId == null)
        {
            mActiveSystemCPUEntities.postValue(new LinkedList<>(mCPUEntities.values()));
        }
        else if (mActiveSystemId.equals(systemId))
        {
            mActiveSystemCPUEntities.postValue(cpuEntities);
        }
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

    private static class upsertSystemCPUAsyncTask extends AsyncTask<CPUEntity, Void, Void> {

        private CPUDao mCPUDao;
        private SystemCPUDao mSystemCPUDao;
        private UUID mSystemId;

        upsertSystemCPUAsyncTask(CPUDao cpuDao, SystemCPUDao systemCPUDao, UUID systemId) {
            mCPUDao = cpuDao;
            mSystemCPUDao = systemCPUDao;
            mSystemId = systemId;
        }

        protected Void doInBackground(final CPUEntity... cpuEntities) {
            mSystemCPUDao.deleteBySystemId(mSystemId);
            for (CPUEntity cpuEntity: cpuEntities) {
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
