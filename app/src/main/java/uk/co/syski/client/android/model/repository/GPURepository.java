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
import uk.co.syski.client.android.model.api.requests.system.APISystemGPURequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.CPUDao;
import uk.co.syski.client.android.model.database.dao.GPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemGPUDao;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.GPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemGPUEntity;

public class GPURepository {

    // Database DAO's
    private GPUDao mGPUDao;
    private SystemGPUDao mSystemGPUDao;

    // GPU Cache in Memory
    private HashMap<UUID, GPUEntity> mGPUEntities;
    private HashMap<UUID, List<UUID>> mSystemGPUEntities;

    // Active System GPUs Cache in Memory
    private UUID mActiveSystemId;
    private boolean mActiveSystemChanged;
    private MutableLiveData<List<GPUEntity>> mActiveSystemGPUEntities;

    public GPURepository() {
        mGPUDao = SyskiCache.GetDatabase().GPUDao();
        mSystemGPUDao = SyskiCache.GetDatabase().SystemGPUDao();


        mGPUEntities = new HashMap<>();
        mSystemGPUEntities = new HashMap<>();

        mActiveSystemGPUEntities = new MutableLiveData<>();
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
                // Load all GPU's if no active System has been set
                mActiveSystemGPUEntities.postValue(new LinkedList<>(mGPUEntities.values()));
            }
            else
            {
                // Load System GPU's
                List<UUID> ActiveSystemGPUsIds = mSystemGPUEntities.get(mActiveSystemId);
                if (ActiveSystemGPUsIds != null)
                {
                    List<GPUEntity> ActiveSystemCPUs = new LinkedList<>();
                    for (UUID gpuEntityId : ActiveSystemGPUsIds) {
                        ActiveSystemCPUs.add(mGPUEntities.get(gpuEntityId));
                    }
                    mActiveSystemGPUEntities.postValue(ActiveSystemCPUs);
                }
                else
                {
                    mActiveSystemGPUEntities.postValue(null);
                }
            }
            mActiveSystemChanged = false;
        }
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for CPU's
            mGPUEntities = new loadGPUEntitiesAsyncTask(mGPUDao, mGPUEntities).execute().get();

            // Load data from Database for System CPU's
            mSystemGPUEntities = new loadSystemGPUEntitiesAsyncTask(mSystemGPUDao, mSystemGPUEntities).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemGPURequest(context, mActiveSystemId));
    }

    public void insert(GPUEntity gpuEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, List<GPUEntity> gpuEntities)
    {
        //TODO Create insert method
    }

    public LiveData<List<GPUEntity>> getSystemGPUsLiveData(UUID systemId, Context context)
    {
        setActiveSystem(systemId);
        loadFromCache();
        loadFromAPI(context);
        return mActiveSystemGPUEntities;
    }

    public List<CPUEntity> getSystemGPUs(UUID systemId)
    {
        //TODO Create get System Component method
        return null;
    }

    public GPUEntity get(UUID id)
    {
        return mGPUEntities.get(id);
    }

    public List<GPUEntity> getAll()
    {
        //TODO Create get Components method
        return null;
    }

    public void update(GPUEntity gpuEntity)
    {
        //TODO Create update Component method
    }

    public void update(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create update Components method
    }

    public void update(UUID systemId, List<GPUEntity> gpuEntities)
    {
        //TODO Create update Components method
    }

    public void upsert(GPUEntity gpuEntity)
    {
        //TODO Create upsert Component method
    }

    public void upsert(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create upsert Components method
    }

    public void upsert(UUID systemId, List<GPUEntity> gpuEntities)
    {
        try {
            new upsertSystemGPUAsyncTask(mGPUDao, mSystemGPUDao, systemId).execute((GPUEntity[]) gpuEntities.toArray()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<UUID> systemCPUEntities = new LinkedList<>();
        for (GPUEntity gpuEntity : gpuEntities)
        {
            systemCPUEntities.add(gpuEntity.Id);
        }
        mSystemGPUEntities.put(systemId, systemCPUEntities);
        if (mActiveSystemId == null)
        {
            mActiveSystemGPUEntities.postValue(new LinkedList<>(mGPUEntities.values()));
        }
        else if (mActiveSystemId.equals(systemId))
        {
            mActiveSystemGPUEntities.postValue(gpuEntities);
        }
    }

    public void delete(GPUEntity gpuEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, GPUEntity gpuEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, List<GPUEntity> gpuEntities)
    {
        //TODO Create delete Component method
    }

    private static class loadGPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, GPUEntity>> {

        private GPUDao mGPUDao;
        private HashMap<UUID, GPUEntity> mGPUEntities;


        loadGPUEntitiesAsyncTask(GPUDao cpuDao, HashMap<UUID, GPUEntity> GPUEntities) {
            mGPUDao = cpuDao;
            mGPUEntities = GPUEntities;
        }

        protected HashMap<UUID, GPUEntity> doInBackground(final Void... voids) {
            for (GPUEntity cpuEntity : mGPUDao.get())
            {
                mGPUEntities.put(cpuEntity.Id, cpuEntity);
            }
            return mGPUEntities;
        }

    }

    private static class loadSystemGPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<UUID>>> {

        private SystemGPUDao mSystemGPUDao;
        private HashMap<UUID, List<UUID>> mSystemCPUEntities;


        loadSystemGPUEntitiesAsyncTask(SystemGPUDao systemGPUDao, HashMap<UUID, List<UUID>> systemGPUEntities) {
            mSystemGPUDao = systemGPUDao;
            mSystemCPUEntities = systemGPUEntities;
        }

        protected HashMap<UUID, List<UUID>> doInBackground(final Void... voids) {
            for (SystemGPUEntity SystemGPUEntity : mSystemGPUDao.get())
            {
                List<UUID> CPUEntities = mSystemCPUEntities.get(SystemGPUEntity.SystemId);
                if (CPUEntities == null)
                {
                    CPUEntities = new LinkedList<>();
                }
                CPUEntities.add(SystemGPUEntity.GPUId);
                mSystemCPUEntities.put(SystemGPUEntity.SystemId, CPUEntities);
            }
            return mSystemCPUEntities;
        }

    }

    private static class upsertSystemGPUAsyncTask extends AsyncTask<GPUEntity, Void, Void> {

        private GPUDao mGPUDao;
        private SystemGPUDao mSystemGPUDao;
        private UUID mSystemId;

        upsertSystemGPUAsyncTask(GPUDao gpuDao, SystemGPUDao systemGPUDao, UUID systemId) {
            mGPUDao = gpuDao;
            mSystemGPUDao = systemGPUDao;
            mSystemId = systemId;
        }

        protected Void doInBackground(final GPUEntity... gpuEntities) {
            mSystemGPUDao.deleteBySystemId(mSystemId);
            for (GPUEntity gpuEntity: gpuEntities) {
                mGPUDao.upsert(gpuEntity);
                SystemGPUEntity systemGPUEntity = new SystemGPUEntity();
                systemGPUEntity.SystemId = mSystemId;
                systemGPUEntity.GPUId = gpuEntity.Id;
                mSystemGPUDao.insert(systemGPUEntity);
            }
            return null;
        }

    }

}
