package uk.co.syski.client.android.data.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
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
    UUID sysId;


    public CPURepository() {
        this.mCPUDao = SyskiCache.GetDatabase().CPUDao();
        this.mCPUEntities = new MutableLiveData();

        try {
            mCPUEntities.setValue(new getAsyncTask(mCPUDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<List<CPUEntity>> get() {
        return mCPUEntities;
    }

    public void insert(CPUEntity cpuEntity) {
        new insertAsyncTask(mCPUDao).execute(cpuEntity);
        updateData();
    }

    public void update(CPUEntity cpuEntity) {
        new updateAsyncTask(mCPUDao).execute(cpuEntity);
        updateData();
    }

    public void updateData() {
        try {
            mCPUEntities.postValue(new getAsyncTask(mCPUDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<CPUEntity>> {
        private CPUDao mAsyncTaskDao;

        getAsyncTask(CPUDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<CPUEntity> doInBackground(final Void... voids) {
            List<CPUEntity> result = mAsyncTaskDao.getCPUs(UUID.fromString());
            return result;
        }
    }

    private static class insertAsyncTask extends AsyncTask<CPUEntity, Void, Void> {

        private CPUDao mAsyncTaskDao;

        insertAsyncTask(CPUDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final CPUEntity... cpuEntities) {
            mAsyncTaskDao.InsertAll(cpuEntities);
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
