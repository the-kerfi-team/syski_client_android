package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.StorageDao;
import uk.co.syski.client.android.data.entity.StorageEntity;

public class StorageRepository {

    private StorageDao mStorageDao;
    private MutableLiveData<List<StorageEntity>> mStorageEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<StorageEntity>> mSystemStorageEntities;

    public StorageRepository() {
        mStorageDao = SyskiCache.GetDatabase().StorageDao();
        mStorageEntities = new MutableLiveData();
        mSystemStorageEntities = new MutableLiveData();
        try {
            mStorageEntities.postValue(new getAsyncTask(mStorageDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<List<StorageEntity>> get() {
        return mStorageEntities;
    }

    public MutableLiveData<List<StorageEntity>> get(final UUID systemId) {
        if (mDataUpdated || mActiveSystemId == null || !mActiveSystemId.equals(systemId))
        {
            mActiveSystemId = systemId;
            updateSystemStorageData();
            mDataUpdated = false;
        }
        return mSystemStorageEntities;
    }

    public void insert(StorageEntity storageEntity) {
        new insertAsyncTask(mStorageDao).execute(storageEntity);
        updateData();
        updateSystemStorageData();
    }

    public void update(StorageEntity storageEntity) {
        new updateAsyncTask(mStorageDao).execute(storageEntity);
        updateData();
        updateSystemStorageData();
    }

    public void updateSystemStorageData() {
        try {
            mSystemStorageEntities.postValue(new getSystemStorageAsyncTask(mStorageDao).execute(mActiveSystemId).get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            mStorageEntities.postValue(new getAsyncTask(mStorageDao).execute().get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getSystemStorageAsyncTask extends AsyncTask<UUID, Void, List<StorageEntity>> {
        private StorageDao mAsyncTaskDao;

        getSystemStorageAsyncTask(StorageDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<StorageEntity> doInBackground(final UUID... uuids) {
            List<StorageEntity> result = mAsyncTaskDao.getSystemCPUs(uuids);
            return mAsyncTaskDao.getSystemCPUs(uuids);
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<StorageEntity>> {
        private StorageDao mAsyncTaskDao;

        getAsyncTask(StorageDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<StorageEntity> doInBackground(final Void... voids) {
            return mAsyncTaskDao.get();
        }
    }

    private static class insertAsyncTask extends AsyncTask<StorageEntity, Void, Void> {

        private StorageDao mAsyncTaskDao;

        insertAsyncTask(StorageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final StorageEntity... cpuEntities) {
            mAsyncTaskDao.insert(cpuEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<StorageEntity, Void, Void> {

        private StorageDao mAsyncTaskDao;

        updateAsyncTask(StorageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final StorageEntity... cpuEntities) {
            mAsyncTaskDao.update(cpuEntities);
            return null;
        }
    }

}
