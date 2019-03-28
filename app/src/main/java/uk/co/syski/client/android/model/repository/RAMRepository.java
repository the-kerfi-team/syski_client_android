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
import uk.co.syski.client.android.model.api.requests.system.APISystemRAMRequest;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.RAMDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemRAMDao;
import uk.co.syski.client.android.model.database.entity.CPUEntity;
import uk.co.syski.client.android.model.database.entity.RAMEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemRAMEntity;

public enum RAMRepository {
    INSTANCE;

    // Database DAO's
    private RAMDao mRAMDao;
    private SystemRAMDao mSystemRAMDao;

    // RAM Cache in Memory
    private HashMap<UUID, RAMEntity> mRAMEntities;
    private HashMap<UUID, List<UUID>> mSystemRAMEntities;

    // LiveData
    private MutableLiveData<HashMap<UUID, RAMEntity>> mLiveDataSystemRAMEntities;

    RAMRepository() {
        mRAMDao = SyskiCache.GetDatabase().RAMDao();
        mSystemRAMDao = SyskiCache.GetDatabase().SystemRAMDao();

        mRAMEntities = new HashMap<>();
        mSystemRAMEntities = new HashMap<>();

        mLiveDataSystemRAMEntities = new MutableLiveData<>();
        loadFromDatabase();
    }

    private void loadFromDatabase()
    {
        try {
            // Load data from Database for RAM's
            mRAMEntities = new loadRAMEntitiesAsyncTask(mRAMDao, mRAMEntities).execute().get();

            // Load data from Database for System RAM's
            mSystemRAMEntities = new loadSystemRAMEntitiesAsyncTask(mSystemRAMDao, mSystemRAMEntities).execute().get();

            // Set Data in LiveData
            mLiveDataSystemRAMEntities.postValue(mRAMEntities);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadFromAPI(Context context, UUID systemId)
    {
        //TODO Load from API every user defined time.
        VolleySingleton.getInstance(context).addToRequestQueue(new APISystemRAMRequest(context, systemId));
    }

    public void insert(RAMEntity ramEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, RAMEntity ramEntity)
    {
        //TODO Create insert method
    }

    public void insert(UUID systemId, List<RAMEntity> ramEntities)
    {
        //TODO Create insert method
    }

    public LiveData<List<RAMEntity>> getSystemRAMsLiveData(final UUID systemId, Context context)
    {
        loadFromAPI(context, systemId);
        return Transformations.map(mLiveDataSystemRAMEntities, new Function<HashMap<UUID, RAMEntity>, List<RAMEntity>>() {
            @Override
            public List<RAMEntity> apply(HashMap<UUID, RAMEntity> input) {
                List<RAMEntity> result = new LinkedList<>();
                List<UUID> systemRAMEntities = mSystemRAMEntities.get(systemId);
                if (systemRAMEntities != null)
                {
                    for (UUID systemEntityId : systemRAMEntities)
                    {
                        result.add(input.get(systemEntityId));
                    }
                }
                return result;
            }
        });
    }

    public List<RAMEntity> getSystemRAMs(UUID systemId)
    {
        //TODO Create get System Component method
        return null;
    }

    public RAMEntity get(UUID id)
    {
        return mRAMEntities.get(id);
    }

    public List<RAMEntity> getAll()
    {
        //TODO Create get Components method
        return null;
    }

    public void update(RAMEntity ramEntity)
    {
        //TODO Create update Component method
    }

    public void update(UUID systemId, RAMEntity ramEntity)
    {
        //TODO Create update Components method
    }

    public void update(UUID systemId, List<RAMEntity> ramEntities)
    {
        //TODO Create update Components method
    }

    public void upsert(RAMEntity ramEntity)
    {
        //TODO Create upsert Component method
    }

    public void upsert(UUID systemId, RAMEntity ramEntity)
    {
        //TODO Create upsert Components method
    }

    public void upsert(UUID systemId, List<RAMEntity> ramEntities)
    {
        try {
            new upsertSystemRAMAsyncTask(mRAMDao, mSystemRAMDao, systemId, ramEntities).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<UUID> systemRAMEntities = new LinkedList<>();
        for (RAMEntity ramEntity : ramEntities)
        {
            systemRAMEntities.add(ramEntity.Id);
        }
        mSystemRAMEntities.put(systemId, systemRAMEntities);
        mLiveDataSystemRAMEntities.postValue(mRAMEntities);
    }

    public void delete(RAMEntity ramEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, RAMEntity ramEntity)
    {
        //TODO Create delete Component method
    }

    public void delete(UUID systemId, List<RAMEntity> ramEntities)
    {
        //TODO Create delete Component method
    }

    private static class loadRAMEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, RAMEntity>> {

        private RAMDao mRAMDao;
        private HashMap<UUID, RAMEntity> mRAMEntities;


        loadRAMEntitiesAsyncTask(RAMDao ramDao, HashMap<UUID, RAMEntity> RAMEntities) {
            mRAMDao = ramDao;
            mRAMEntities = RAMEntities;
        }

        protected HashMap<UUID, RAMEntity> doInBackground(final Void... voids) {
            for (RAMEntity ramEntity : mRAMDao.get())
            {
                mRAMEntities.put(ramEntity.Id, ramEntity);
            }
            return mRAMEntities;
        }

    }

    private static class loadSystemRAMEntitiesAsyncTask extends AsyncTask<Void, Void, HashMap<UUID, List<UUID>>> {

        private SystemRAMDao mSystemRAMDao;
        private HashMap<UUID, List<UUID>> mSystemRAMEntities;


        loadSystemRAMEntitiesAsyncTask(SystemRAMDao systemRAMDao, HashMap<UUID, List<UUID>> systemRAMEntities) {
            mSystemRAMDao = systemRAMDao;
            mSystemRAMEntities = systemRAMEntities;
        }

        protected HashMap<UUID, List<UUID>> doInBackground(final Void... voids) {
            for (SystemRAMEntity SystemRAMEntity : mSystemRAMDao.get())
            {
                List<UUID> RAMEntities = mSystemRAMEntities.get(SystemRAMEntity.SystemId);
                if (RAMEntities == null)
                {
                    RAMEntities = new LinkedList<>();
                }
                RAMEntities.add(SystemRAMEntity.RAMId);
                mSystemRAMEntities.put(SystemRAMEntity.SystemId, RAMEntities);
            }
            return mSystemRAMEntities;
        }

    }

    private static class upsertSystemRAMAsyncTask extends AsyncTask<Void , Void, Void> {

        private RAMDao mRAMDao;
        private SystemRAMDao mSystemRAMDao;
        private UUID mSystemId;

        private List<RAMEntity> mRAMEntities;

        upsertSystemRAMAsyncTask(RAMDao ramDao, SystemRAMDao systemRAMDao, UUID systemId, List<RAMEntity> ramEntities) {
            mRAMDao = ramDao;
            mSystemRAMDao = systemRAMDao;
            mSystemId = systemId;

            mRAMEntities = ramEntities;
        }

        protected Void doInBackground(final Void... voids) {
            mSystemRAMDao.deleteBySystemId(mSystemId);
            int i = 0;
            for (RAMEntity ramEntity: mRAMEntities) {
                mRAMDao.upsert(ramEntity);
                SystemRAMEntity systemRAMEntity = new SystemRAMEntity();
                systemRAMEntity.SystemId = mSystemId;
                systemRAMEntity.RAMId = ramEntity.Id;
                systemRAMEntity.DimmSlot = i++;
                mSystemRAMDao.insert(systemRAMEntity);
            }
            return null;
        }

    }

}