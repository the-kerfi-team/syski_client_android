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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemCPURequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.CPUDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemCPUEntity;
import uk.co.syski.client.android.model.viewmodel.SystemCPUModel;

public enum CPURepository {
    INSTANCE;

    // Database DAO's
    private CPUDao mCPUDao;
    private SystemCPUDao mSystemCPUDao;

    // CPU Cache in Memory
    private HashMap<UUID, CPUEntity> mCPUEntities;
    private HashMap<UUID, List<SystemCPUEntity>> mSystemCPUEntities;
    private HashMap<UUID, List<SystemCPUModel>> mSystemCPUModels;

    // LiveData
    private MutableLiveData<HashMap<UUID, List<SystemCPUModel>>> mLiveDataSystemCPUModels;

    CPURepository() {
        mCPUDao = SyskiCache.GetDatabase().CPUDao();
        mSystemCPUDao = SyskiCache.GetDatabase().SystemCPUDao();

        mCPUEntities = new HashMap<>();
        mSystemCPUEntities = new HashMap<>();
        mSystemCPUModels = new HashMap<>();

        mLiveDataSystemCPUModels = new MutableLiveData<>();
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
            for (Map.Entry<UUID, List<SystemCPUEntity>> entry : mSystemCPUEntities.entrySet())
            {
                List<SystemCPUModel> CPUModels = mSystemCPUModels.get(entry.getKey());
                if (CPUModels == null)
                {
                    CPUModels = new LinkedList<>();
                }
                for (SystemCPUEntity systemCPUEntity : entry.getValue())
                {
                    CPUEntity cpuEntity = mCPUEntities.get(systemCPUEntity.CPUId);
                    CPUModels.add(new SystemCPUModel(cpuEntity.ModelName, cpuEntity.ManufacturerName, cpuEntity.ArchitectureName, cpuEntity.ClockSpeed, cpuEntity.CoreCount, cpuEntity.ThreadCount));
                }
                mSystemCPUModels.put(entry.getKey(), CPUModels);
            }
            mLiveDataSystemCPUModels.postValue(mSystemCPUModels);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemCPURequest(context, systemId));
    }

    public LiveData<List<SystemCPUModel>> getSystemCPUsLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemCPUModels, new Function<HashMap<UUID, List<SystemCPUModel>>, List<SystemCPUModel>>() {
            @Override
            public List<SystemCPUModel> apply(HashMap<UUID, List<SystemCPUModel>> input) {
                List<SystemCPUModel> cpuModelList = input.get(systemId);
                if (cpuModelList == null)
                {
                    cpuModelList = new LinkedList<>();
                }
                if (cpuModelList.isEmpty())
                {
                    cpuModelList.add(new SystemCPUModel());
                }
                return cpuModelList;
            }
        });
    }

    public List<SystemCPUEntity> getSystemCPUs(UUID systemId)
    {
        return mSystemCPUEntities.get(systemId);
    }

    public CPUEntity getCPU(UUID id)
    {
        return mCPUEntities.get(id);
    }

    public void upsert(UUID systemId, List<SystemCPUEntity> systemCPUEntities, List<CPUEntity> cpuEntities)
    {
        try {
            new upsertCPUAsyncTask(mCPUDao, cpuEntities).execute().get();
            new upsertSystemCPUAsyncTask(mSystemCPUDao, systemId, systemCPUEntities).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        List<SystemCPUModel> systemCPUModels = new LinkedList<>();
        for (CPUEntity cpuEntity : cpuEntities)
        {
            mCPUEntities.put(cpuEntity.Id, cpuEntity);
        }
        for (SystemCPUEntity systemCPUEntity : systemCPUEntities)
        {
            CPUEntity cpuEntity = mCPUEntities.get(systemCPUEntity.CPUId);
            systemCPUModels.add(new SystemCPUModel(cpuEntity.ModelName, cpuEntity.ManufacturerName, cpuEntity.ArchitectureName, cpuEntity.ClockSpeed, cpuEntity.CoreCount, cpuEntity.ThreadCount));
        }
        mSystemCPUEntities.put(systemId, systemCPUEntities);
        mSystemCPUModels.put(systemId, systemCPUModels);
        mLiveDataSystemCPUModels.postValue(mSystemCPUModels);
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

    private static class loadSystemCPUEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<SystemCPUEntity>>> {

        private SystemCPUDao mSystemCPUDao;
        private HashMap<UUID, List<SystemCPUEntity>> mSystemCPUEntities;

        loadSystemCPUEntitiesAsyncTask(SystemCPUDao systemCPUDao, HashMap<UUID, List<SystemCPUEntity>> systemCPUEntities) {
            mSystemCPUDao = systemCPUDao;
            mSystemCPUEntities = systemCPUEntities;
        }

        protected HashMap<UUID, List<SystemCPUEntity>> doInBackground(final Void... voids) {
            for (SystemCPUEntity SystemCPUEntity : mSystemCPUDao.get())
            {
                List<SystemCPUEntity> CPUEntities = mSystemCPUEntities.get(SystemCPUEntity.SystemId);
                if (CPUEntities == null)
                {
                    CPUEntities = new LinkedList<>();
                }
                CPUEntities.add(SystemCPUEntity);
                mSystemCPUEntities.put(SystemCPUEntity.SystemId, CPUEntities);
            }
            return mSystemCPUEntities;
        }

    }

    private static class upsertCPUAsyncTask extends AsyncTask<Void , Void, Void> {

        private CPUDao mCPUDao;

        private List<CPUEntity> mCPUEntities;

        upsertCPUAsyncTask(CPUDao cpuDao, List<CPUEntity> cpuEntities) {
            mCPUDao = cpuDao;
            mCPUEntities = cpuEntities;
        }

        protected Void doInBackground(final Void... voids) {
            for (CPUEntity cpuEntity: mCPUEntities) {
                mCPUDao.upsert(cpuEntity);
            }
            return null;
        }

    }

    private static class upsertSystemCPUAsyncTask extends AsyncTask<Void , Void, Void> {

        private SystemCPUDao mSystemCPUDao;
        private UUID mSystemId;

        private List<SystemCPUEntity> mSystemCPUEntities;

        upsertSystemCPUAsyncTask(SystemCPUDao systemCPUDao, UUID systemId, List<SystemCPUEntity> systemCPUEntities) {
            mSystemCPUDao = systemCPUDao;
            mSystemId = systemId;
            mSystemCPUEntities = systemCPUEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemCPUDao.deleteBySystemId(mSystemId);
            for (SystemCPUEntity systemCPUEntity: mSystemCPUEntities) {
                mSystemCPUDao.insert(systemCPUEntity);
            }
            return null;
        }

    }

}
