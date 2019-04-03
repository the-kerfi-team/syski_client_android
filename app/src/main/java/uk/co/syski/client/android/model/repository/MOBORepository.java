package uk.co.syski.client.android.model.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.persistence.room.Dao;
import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemMotherboardRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.MotherboardDao;
import uk.co.syski.client.android.model.database.dao.OperatingSystemDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemMotherboardDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemOSDao;
import uk.co.syski.client.android.model.database.entity.MotherboardEntity;
import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemMotherboardEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.model.viewmodel.OperatingSystemModel;
import uk.co.syski.client.android.model.viewmodel.SystemMotherboardModel;

public enum MOBORepository {
    INSTANCE;

    // Database DAO's
    private MotherboardDao mMOBODao;
    private SystemMotherboardDao mSystemMOBODao;

    // MOBO Cache in Memory
    private HashMap<UUID, MotherboardEntity> mMOBOEntities;
    private HashMap<UUID, SystemMotherboardEntity> mSystemMOBOEntities;
    private HashMap<UUID, SystemMotherboardModel> mSystemMOBOModels;

    // LiveData
    private MutableLiveData<HashMap<UUID, SystemMotherboardModel>> mLiveDataSystemMOBOModels;

    MOBORepository() {
        mMOBODao = SyskiCache.GetDatabase().MotherboardDao();
        mSystemMOBODao = SyskiCache.GetDatabase().SystemMotherboardDao();

        mMOBOEntities = new HashMap<>();
        mSystemMOBOEntities = new HashMap<>();
        mSystemMOBOModels = new HashMap<>();

        mLiveDataSystemMOBOModels = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for OS's
            mMOBOEntities = new loadMOBOEntitiesAsyncTask(mMOBODao, mMOBOEntities).execute().get();

            // Load data from Database for System OS's
            mSystemMOBOEntities = new loadSystemMOBOEntitiesAsyncTask(mSystemMOBODao, mSystemMOBOEntities).execute().get();

            // Set Data in LiveData
            for (Map.Entry<UUID, SystemMotherboardEntity> entry : mSystemMOBOEntities.entrySet())
            {
                SystemMotherboardEntity systemMOBOEntity = entry.getValue();
                MotherboardEntity moboEntity = mMOBOEntities.get(systemMOBOEntity.MotherboardId);
                mSystemMOBOModels.put(entry.getKey(), new SystemMotherboardModel(moboEntity.ModelName, moboEntity.ManufacturerName, moboEntity.Version));
            }
            mLiveDataSystemMOBOModels.postValue(mSystemMOBOModels);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemMotherboardRequest(context, systemId));
    }

    public LiveData<SystemMotherboardModel> getSystemMotherboardLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemMOBOModels, new Function<HashMap<UUID, SystemMotherboardModel>, SystemMotherboardModel>() {
            @Override
            public SystemMotherboardModel apply(HashMap<UUID, SystemMotherboardModel> input) {
                SystemMotherboardModel moboModel = input.get(systemId);
                if (moboModel == null)
                {
                    moboModel = new SystemMotherboardModel();
                }
                return moboModel;
            }
        });
    }

    public MotherboardEntity getMotherboard(UUID id)
    {
        return mMOBOEntities.get(id);
    }

    public void upsert(UUID systemId, SystemMotherboardEntity systemMOBOEntity, MotherboardEntity moboEntity)
    {
        try {
            new upsertMOBOAsyncTask(mMOBODao, moboEntity).execute().get();
            new upsertSystemMOBOAsyncTask(mSystemMOBODao, systemId, systemMOBOEntity).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        mMOBOEntities.put(moboEntity.Id, moboEntity);
        mSystemMOBOEntities.put(systemId, systemMOBOEntity);
        mSystemMOBOModels.put(systemId, new SystemMotherboardModel(moboEntity.ModelName, moboEntity.ManufacturerName, moboEntity.Version));
        mLiveDataSystemMOBOModels.postValue(mSystemMOBOModels);
    }

    private static class loadMOBOEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, MotherboardEntity>> {

        private MotherboardDao mMotherboardDao;
        private HashMap<UUID, MotherboardEntity> mMOBOEntities;


        loadMOBOEntitiesAsyncTask(MotherboardDao motherboardDao, HashMap<UUID, MotherboardEntity> moboEntities) {
            mMotherboardDao = motherboardDao;
            mMOBOEntities = moboEntities;
        }

        protected HashMap<UUID, MotherboardEntity> doInBackground(final Void... voids) {
            for (MotherboardEntity motherboardEntity : mMotherboardDao.get())
            {
                mMOBOEntities.put(motherboardEntity.Id, motherboardEntity);
            }
            return mMOBOEntities;
        }

    }

    private static class loadSystemMOBOEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, SystemMotherboardEntity>> {

        private SystemMotherboardDao mSystemMotherboardDao;
        private HashMap<UUID, SystemMotherboardEntity> mSystemMOBOEntities;


        loadSystemMOBOEntitiesAsyncTask(SystemMotherboardDao systemMotherboardDao, HashMap<UUID, SystemMotherboardEntity> systemMotherboardEntities) {
            mSystemMotherboardDao = systemMotherboardDao;
            mSystemMOBOEntities = systemMotherboardEntities;
        }

        protected HashMap<UUID, SystemMotherboardEntity> doInBackground(final Void... voids) {
            for (SystemMotherboardEntity systemMotherboardEntity : mSystemMotherboardDao.get())
            {
                mSystemMOBOEntities.put(systemMotherboardEntity.SystemId, systemMotherboardEntity);
            }
            return mSystemMOBOEntities;
        }

    }

    private static class upsertMOBOAsyncTask extends AsyncTask<Void , Void, Void> {

        private MotherboardDao mMotherboardDao;

        private MotherboardEntity mMotherboardEntity;

        upsertMOBOAsyncTask(MotherboardDao motherboardDao, MotherboardEntity motherboardEntity) {
            mMotherboardDao = motherboardDao;
            mMotherboardEntity = motherboardEntity;
        }

        protected Void doInBackground(final Void... voids) {
            mMotherboardDao.upsert(mMotherboardEntity);
            return null;
        }

    }

    private static class upsertSystemMOBOAsyncTask extends AsyncTask<Void , Void, Void> {

        private SystemMotherboardDao mSystemMotherboardDao;
        private UUID mSystemId;

        private SystemMotherboardEntity mSystemMotherboardEntity;

        upsertSystemMOBOAsyncTask(SystemMotherboardDao systemMotherboardDao, UUID systemId, SystemMotherboardEntity systemMotherboardEntity) {
            mSystemMotherboardDao = systemMotherboardDao;
            mSystemId = systemId;
            mSystemMotherboardEntity = systemMotherboardEntity;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemMotherboardDao.deleteBySystemId(mSystemId);
            mSystemMotherboardDao.insert(mSystemMotherboardEntity);
            return null;
        }

    }

}
