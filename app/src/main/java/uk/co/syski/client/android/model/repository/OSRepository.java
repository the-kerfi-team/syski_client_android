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
import uk.co.syski.client.android.model.api.requests.system.APISystemOperatingSystemRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.OperatingSystemDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemOSDao;
import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.model.viewmodel.OperatingSystemModel;

public enum OSRepository {
    INSTANCE;

    // Database DAO's
    private OperatingSystemDao mOperatingSystemDao;
    private SystemOSDao mSystemOSDao;

    // OS Cache in Memory
    private HashMap<UUID, OperatingSystemEntity> mOSEntities;
    private HashMap<UUID, List<SystemOSEntity>> mSystemOSEntities;
    private HashMap<UUID, List<OperatingSystemModel>> mSystemOSModels;

    // LiveData
    private MutableLiveData<HashMap<UUID, List<OperatingSystemModel>>> mLiveDataSystemOSModels;

    OSRepository() {
        mOperatingSystemDao = SyskiCache.GetDatabase().OperatingSystemDao();
        mSystemOSDao = SyskiCache.GetDatabase().SystemOSDao();

        mOSEntities = new HashMap<>();
        mSystemOSEntities = new HashMap<>();
        mSystemOSModels = new HashMap<>();

        mLiveDataSystemOSModels = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for OS's
            mOSEntities = new loadOSEntitiesAsyncTask(mOperatingSystemDao, mOSEntities).execute().get();

            // Load data from Database for System OS's
            mSystemOSEntities = new loadSystemOSEntitiesAsyncTask(mSystemOSDao, mSystemOSEntities).execute().get();

            // Set Data in LiveData
            for (Map.Entry<UUID, List<SystemOSEntity>> entry : mSystemOSEntities.entrySet())
            {
                List<OperatingSystemModel> OSModels = mSystemOSModels.get(entry.getKey());
                if (OSModels == null)
                {
                    OSModels = new LinkedList<>();
                }
                for (SystemOSEntity systemOSEntity : entry.getValue())
                {
                    OperatingSystemEntity osEntity = mOSEntities.get(systemOSEntity.OSId);
                    OSModels.add(new OperatingSystemModel(osEntity.Name, systemOSEntity.ArchitectureName, systemOSEntity.Version));
                }
                mSystemOSModels.put(entry.getKey(), OSModels);
            }
            mLiveDataSystemOSModels.postValue(mSystemOSModels);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemOperatingSystemRequest(context, systemId));
    }

    public LiveData<List<OperatingSystemModel>> getSystemOSsLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemOSModels, new Function<HashMap<UUID, List<OperatingSystemModel>>, List<OperatingSystemModel>>() {
            @Override
            public List<OperatingSystemModel> apply(HashMap<UUID, List<OperatingSystemModel>> input) {
                List<OperatingSystemModel> osModelList = input.get(systemId);
                if (osModelList == null)
                {
                    osModelList = new LinkedList<>();
                }
                if (osModelList.isEmpty())
                {
                    osModelList.add(new OperatingSystemModel());
                }
                return osModelList;
            }
        });
    }

    public List<SystemOSEntity> getSystemOSs(UUID systemId)
    {
        return mSystemOSEntities.get(systemId);
    }

    public OperatingSystemEntity getOS(UUID id)
    {
        return mOSEntities.get(id);
    }

    public void upsert(UUID systemId, List<SystemOSEntity> systemOSEntities, List<OperatingSystemEntity> osEntities)
    {
        try {
            new upsertOSAsyncTask(mOperatingSystemDao, systemId, osEntities).execute().get();
            new upsertSystemOSAsyncTask(mSystemOSDao, systemId, systemOSEntities).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<OperatingSystemModel> systemOSModels = new LinkedList<>();
        for (OperatingSystemEntity osEntity : osEntities)
        {
            mOSEntities.put(osEntity.Id, osEntity);
        }
        for (SystemOSEntity systemOSEntity : systemOSEntities)
        {
            OperatingSystemEntity osEntity = mOSEntities.get(systemOSEntity.OSId);
            systemOSModels.add(new OperatingSystemModel(osEntity.Name, systemOSEntity.ArchitectureName, systemOSEntity.Version));
        }
        mSystemOSEntities.put(systemId, systemOSEntities);
        mSystemOSModels.put(systemId, systemOSModels);
        mLiveDataSystemOSModels.postValue(mSystemOSModels);
    }

    private static class loadOSEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, OperatingSystemEntity>> {

        private OperatingSystemDao mOperatingSystemDao;
        private HashMap<UUID, OperatingSystemEntity> mOSEntities;


        loadOSEntitiesAsyncTask(OperatingSystemDao operatingSystemDao, HashMap<UUID, OperatingSystemEntity> osEntities) {
            mOperatingSystemDao = operatingSystemDao;
            mOSEntities = osEntities;
        }

        protected HashMap<UUID, OperatingSystemEntity> doInBackground(final Void... voids) {
            for (OperatingSystemEntity operatingSystemEntity : mOperatingSystemDao.get())
            {
                mOSEntities.put(operatingSystemEntity.Id, operatingSystemEntity);
            }
            return mOSEntities;
        }

    }

    private static class loadSystemOSEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<SystemOSEntity>>> {

        private SystemOSDao mSystemOSDao;
        private HashMap<UUID, List<SystemOSEntity>> mSystemOSEntities;


        loadSystemOSEntitiesAsyncTask(SystemOSDao systemOSDao, HashMap<UUID, List<SystemOSEntity>> systemOSEntities) {
            mSystemOSDao = systemOSDao;
            mSystemOSEntities = systemOSEntities;
        }

        protected HashMap<UUID, List<SystemOSEntity>> doInBackground(final Void... voids) {
            for (SystemOSEntity systemOSEntity : mSystemOSDao.get())
            {
                List<SystemOSEntity> OSEntities = mSystemOSEntities.get(systemOSEntity.SystemId);
                if (OSEntities == null)
                {
                    OSEntities = new LinkedList<>();
                }
                OSEntities.add(systemOSEntity);
                mSystemOSEntities.put(systemOSEntity.SystemId, OSEntities);
            }
            return mSystemOSEntities;
        }

    }

    private static class upsertOSAsyncTask extends AsyncTask<Void , Void, Void> {

        private OperatingSystemDao mOperatingSystemDao;
        private UUID mSystemId;

        private List<OperatingSystemEntity> mOperatingSystemEntities;

        upsertOSAsyncTask(OperatingSystemDao operatingSystemDao, UUID systemId, List<OperatingSystemEntity> operatingSystemEntities) {
            mOperatingSystemDao = operatingSystemDao;
            mSystemId = systemId;
            mOperatingSystemEntities = operatingSystemEntities;
        }

        protected Void doInBackground(final Void... voids) {
            for (OperatingSystemEntity operatingSystemEntity: mOperatingSystemEntities) {
                mOperatingSystemDao.upsert(operatingSystemEntity);
            }
            return null;
        }

    }

    private static class upsertSystemOSAsyncTask extends AsyncTask<Void , Void, Void> {

        private SystemOSDao mSystemOSDao;
        private UUID mSystemId;

        private List<SystemOSEntity> mSystemCPUEntities;

        upsertSystemOSAsyncTask(SystemOSDao systemOSDao, UUID systemId, List<SystemOSEntity> systemOSEntities) {
            mSystemOSDao = systemOSDao;
            mSystemId = systemId;
            mSystemCPUEntities = systemOSEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemOSDao.deleteBySystemId(mSystemId);
            for (SystemOSEntity systemOSEntity: mSystemCPUEntities) {
                mSystemOSDao.insert(systemOSEntity);
            }
            return null;
        }

    }

}
