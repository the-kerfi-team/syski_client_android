package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.dao.GPUDao;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.GPUEntity;

public class GPURepository {

    private GPUDao mGPUDao;
    private MutableLiveData<List<GPUEntity>> mGPUEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<GPUEntity>> mSystemGPUEntities;

    public GPURepository() {
        mGPUDao = SyskiCache.GetDatabase().GPUDao();
        mGPUEntities = new MutableLiveData();
        mSystemGPUEntities = new MutableLiveData();
        try {
            mGPUEntities.postValue(new GPURepository.getAsyncTask(mGPUDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<List<GPUEntity>> get() {
        return mGPUEntities;
    }

    public MutableLiveData<List<GPUEntity>> get(final UUID systemId) {
        if (mDataUpdated || !mActiveSystemId.equals(systemId))
        {
            mDataUpdated = false;
            mActiveSystemId = systemId;
            updateSystemGPUData();
        }
        return mSystemGPUEntities;
    }

    public void insert(GPUEntity gpuEntity) {
        new GPURepository.insertAsyncTask(mGPUDao).execute(gpuEntity);
        updateData();
    }

    public void update(GPUEntity gpuEntity) {
        new GPURepository.updateAsyncTask(mGPUDao).execute(gpuEntity);
        updateData();
    }

    public void updateSystemGPUData() {
        try {
            mGPUEntities.postValue(new GPURepository.getSystemGPUAsyncTask(mGPUDao).execute(mActiveSystemId).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            mGPUEntities.postValue(new GPURepository.getAsyncTask(mGPUDao).execute().get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getSystemGPUAsyncTask extends AsyncTask<UUID, Void, List<GPUEntity>> {
        private GPUDao mAsyncTaskDao;

        getSystemGPUAsyncTask(GPUDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<GPUEntity> doInBackground(final UUID... uuids) {
            return mAsyncTaskDao.getSystemGPUs(uuids);
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<GPUEntity>> {
        private GPUDao mAsyncTaskDao;

        getAsyncTask(GPUDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<GPUEntity> doInBackground(final Void... voids) {
            return mAsyncTaskDao.get();
        }
    }

    private static class insertAsyncTask extends AsyncTask<GPUEntity, Void, Void> {

        private GPUDao mAsyncTaskDao;

        insertAsyncTask(GPUDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GPUEntity... gpuEntities) {
            mAsyncTaskDao.insert(gpuEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<GPUEntity, Void, Void> {

        private GPUDao mAsyncTaskDao;

        updateAsyncTask(GPUDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GPUEntity... gpuEntities) {
            mAsyncTaskDao.update(gpuEntities);
            return null;
        }
    }

}
