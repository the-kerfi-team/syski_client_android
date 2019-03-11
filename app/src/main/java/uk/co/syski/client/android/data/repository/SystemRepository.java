package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;

public class SystemRepository {

    private SystemDao mSystemDao;
    private MutableLiveData<List<SystemEntity>> mSystemEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<CPUEntity>> mSystemCPUEntities;

    public SystemRepository() {
        mSystemDao = SyskiCache.GetDatabase().SystemDao();
        mSystemEntities = new MutableLiveData();
        try {
            mSystemEntities.postValue(new getAsyncTask(mSystemDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<List<SystemEntity>> get() {
        return mSystemEntities;
    }

    public void insert(SystemEntity systemEntity)
    {
        new insertAsyncTask(mSystemDao).execute(systemEntity);
        updateData();
    }

    public void update(SystemEntity systemEntity)
    {
        new updateAsyncTask(mSystemDao).execute(systemEntity);
        updateData();
    }

    public void updateData()
    {
        try {
            mSystemEntities.postValue(new getAsyncTask(mSystemDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<SystemEntity>> {

        private SystemDao mAsyncTaskDao;

        getAsyncTask(SystemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<SystemEntity> doInBackground(final Void... voids) {
            List<SystemEntity> result = mAsyncTaskDao.get();
            return result;
        }

    }

    private static class insertAsyncTask extends AsyncTask<SystemEntity, Void, Void> {

        private SystemDao mAsyncTaskDao;

        insertAsyncTask(SystemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SystemEntity... systemEntities) {
            mAsyncTaskDao.insert(systemEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<SystemEntity, Void, Void> {

        private SystemDao mAsyncTaskDao;

        updateAsyncTask(SystemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SystemEntity... systemEntities) {
            mAsyncTaskDao.update(systemEntities);
            return null;
        }
    }

}
