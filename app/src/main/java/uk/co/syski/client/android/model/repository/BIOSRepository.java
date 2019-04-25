package uk.co.syski.client.android.model.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemBIOSRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.BIOSDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemBIOSDao;
import uk.co.syski.client.android.model.database.entity.BIOSEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemBIOSEntity;
import uk.co.syski.client.android.model.viewmodel.SystemBIOSModel;

public enum BIOSRepository {
    INSTANCE;

    // Database DAO's
    private BIOSDao mBIOSDao;
    private SystemBIOSDao mSystemBIOSDao;

    // BIOS Cache in Memory
    private HashMap<UUID, BIOSEntity> mBIOSEntities;
    private HashMap<UUID, SystemBIOSEntity> mSystemBIOSEntities;
    private HashMap<UUID, SystemBIOSModel> mSystemBIOSModels;

    // LiveData
    private MutableLiveData<HashMap<UUID, SystemBIOSModel>> mLiveDataSystemBIOSModels;

    BIOSRepository() {
        mBIOSDao = SyskiCache.GetDatabase().BIOSDao();
        mSystemBIOSDao = SyskiCache.GetDatabase().SystemBIOSDao();

        mBIOSEntities = new HashMap<>();
        mSystemBIOSEntities = new HashMap<>();
        mSystemBIOSModels = new HashMap<>();

        mLiveDataSystemBIOSModels = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for OS's
            mBIOSEntities = new loadBIOSEntitiesAsyncTask(mBIOSDao, mBIOSEntities).execute().get();

            // Load data from Database for System OS's
            mSystemBIOSEntities = new loadSystemBIOSEntitiesAsyncTask(mSystemBIOSDao, mSystemBIOSEntities).execute().get();

            // Set Data in LiveData
            for (Map.Entry<UUID, SystemBIOSEntity> entry : mSystemBIOSEntities.entrySet())
            {
                SystemBIOSEntity systemBIOSEntity = entry.getValue();
                BIOSEntity biosEntity = mBIOSEntities.get(systemBIOSEntity.BIOSId);
                mSystemBIOSModels.put(entry.getKey(), new SystemBIOSModel(biosEntity.ManufacturerName, biosEntity.Caption, biosEntity.Version, biosEntity.Date));
            }
            mLiveDataSystemBIOSModels.postValue(mSystemBIOSModels);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemBIOSRequest(context, systemId));
    }

    public LiveData<SystemBIOSModel> getSystemBIOSLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemBIOSModels, new Function<HashMap<UUID, SystemBIOSModel>, SystemBIOSModel>() {
            @Override
            public SystemBIOSModel apply(HashMap<UUID, SystemBIOSModel> input) {
                SystemBIOSModel moboModel = input.get(systemId);
                if (moboModel == null)
                {
                    moboModel = new SystemBIOSModel();
                }
                return moboModel;
            }
        });
    }

    public BIOSEntity getBIOS(UUID id)
    {
        return mBIOSEntities.get(id);
    }

    public void upsert(UUID systemId, SystemBIOSEntity systemBIOSEntity, BIOSEntity biosEntity)
    {
        try {
            new upsertBIOSAsyncTask(mBIOSDao, biosEntity).execute().get();
            new upsertSystemBIOSAsyncTask(mSystemBIOSDao, systemId, systemBIOSEntity).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        mBIOSEntities.put(biosEntity.Id, biosEntity);
        mSystemBIOSEntities.put(systemId, systemBIOSEntity);
        mSystemBIOSModels.put(systemId, new SystemBIOSModel(biosEntity.ManufacturerName, biosEntity.Caption, biosEntity.Version, biosEntity.Date));
        mLiveDataSystemBIOSModels.postValue(mSystemBIOSModels);
    }

    private static class loadBIOSEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, BIOSEntity>> {

        private BIOSDao mBIOSDao;
        private HashMap<UUID, BIOSEntity> mBIOSEntities;


        loadBIOSEntitiesAsyncTask(BIOSDao motherboardDao, HashMap<UUID, BIOSEntity> moboEntities) {
            mBIOSDao = motherboardDao;
            mBIOSEntities = moboEntities;
        }

        protected HashMap<UUID, BIOSEntity> doInBackground(final Void... voids) {
            for (BIOSEntity motherboardEntity : mBIOSDao.get())
            {
                mBIOSEntities.put(motherboardEntity.Id, motherboardEntity);
            }
            return mBIOSEntities;
        }

    }

    private static class loadSystemBIOSEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, SystemBIOSEntity>> {

        private SystemBIOSDao mSystemBIOSDao;
        private HashMap<UUID, SystemBIOSEntity> mSystemBIOSEntities;


        loadSystemBIOSEntitiesAsyncTask(SystemBIOSDao systemBIOSDao, HashMap<UUID, SystemBIOSEntity> systemBIOSEntities) {
            mSystemBIOSDao = systemBIOSDao;
            mSystemBIOSEntities = systemBIOSEntities;
        }

        protected HashMap<UUID, SystemBIOSEntity> doInBackground(final Void... voids) {
            for (SystemBIOSEntity systemBIOSEntity : mSystemBIOSDao.get())
            {
                mSystemBIOSEntities.put(systemBIOSEntity.SystemId, systemBIOSEntity);
            }
            return mSystemBIOSEntities;
        }

    }

    private static class upsertBIOSAsyncTask extends AsyncTask<Void , Void, Void> {

        private BIOSDao mBIOSDao;

        private BIOSEntity mBIOSEntity;

        upsertBIOSAsyncTask(BIOSDao motherboardDao, BIOSEntity motherboardEntity) {
            mBIOSDao = motherboardDao;
            mBIOSEntity = motherboardEntity;
        }

        protected Void doInBackground(final Void... voids) {
            mBIOSDao.upsert(mBIOSEntity);
            return null;
        }

    }

    private static class upsertSystemBIOSAsyncTask extends AsyncTask<Void , Void, Void> {

        private SystemBIOSDao mSystemBIOSDao;
        private UUID mSystemId;

        private SystemBIOSEntity mSystemBIOSEntity;

        upsertSystemBIOSAsyncTask(SystemBIOSDao systemBIOSDao, UUID systemId, SystemBIOSEntity systemBIOSEntity) {
            mSystemBIOSDao = systemBIOSDao;
            mSystemId = systemId;
            mSystemBIOSEntity = systemBIOSEntity;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemBIOSDao.deleteBySystemId(mSystemId);
            mSystemBIOSDao.insert(mSystemBIOSEntity);
            return null;
        }

    }

}
