package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.entity.CPUEntity;

public class CPURepository {

    private CPUDao mCPUDao;
    private MutableLiveData<List<CPUEntity>> mCPUEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<CPUEntity>> mSystemCPUEntities;

    public CPURepository() {
        mCPUDao = SyskiCache.GetDatabase().CPUDao();
        mCPUEntities = new MutableLiveData();
        mSystemCPUEntities = new MutableLiveData();
        try {
            mCPUEntities.postValue(new getAsyncTask(mCPUDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<List<CPUEntity>> get() {
        return mCPUEntities;
    }

    public MutableLiveData<List<CPUEntity>> get(final UUID systemId) {
        if (mDataUpdated || mActiveSystemId == null || !mActiveSystemId.equals(systemId))
        {
            mActiveSystemId = systemId;
            updateSystemCPUData();
            mDataUpdated = false;
        }
        return mSystemCPUEntities;
    }

    public void insert(CPUEntity cpuEntity) {
        new insertAsyncTask(mCPUDao).execute(cpuEntity);
        updateData();
    }

    public void update(CPUEntity cpuEntity) {
        new updateAsyncTask(mCPUDao).execute(cpuEntity);
        updateData();
    }

    public void updateSystemCPUData() {
        try {
            mSystemCPUEntities.postValue(new getSystemCPUAsyncTask(mCPUDao).execute(mActiveSystemId).get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            mCPUEntities.postValue(new getAsyncTask(mCPUDao).execute().get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getSystemCPUAsyncTask extends AsyncTask<UUID, Void, List<CPUEntity>> {
        private CPUDao mAsyncTaskDao;

        getSystemCPUAsyncTask(CPUDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<CPUEntity> doInBackground(final UUID... uuids) {
            List<CPUEntity> result = mAsyncTaskDao.getSystemCPUs(uuids);
            return mAsyncTaskDao.getSystemCPUs(uuids);
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<CPUEntity>> {
        private CPUDao mAsyncTaskDao;

        getAsyncTask(CPUDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<CPUEntity> doInBackground(final Void... voids) {
            return mAsyncTaskDao.get();
        }
    }

    private static class insertAsyncTask extends AsyncTask<CPUEntity, Void, Void> {

        private CPUDao mAsyncTaskDao;

        insertAsyncTask(CPUDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CPUEntity... cpuEntities) {
            mAsyncTaskDao.insert(cpuEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<CPUEntity, Void, Void> {

        private CPUDao mAsyncTaskDao;

        updateAsyncTask(CPUDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CPUEntity... cpuEntities) {
            mAsyncTaskDao.update(cpuEntities);
            return null;
        }
    }


}
