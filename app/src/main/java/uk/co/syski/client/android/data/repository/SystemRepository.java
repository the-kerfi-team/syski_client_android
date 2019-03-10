package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.entity.SystemEntity;

public class SystemRepository {

    private SystemDao mSystemDao;
    private MutableLiveData<List<SystemEntity>> mSystemEntities;

    public SystemRepository() {
        mSystemDao = SyskiCache.GetDatabase().SystemDao();
        mSystemEntities = new MutableLiveData();
        try {
            mSystemEntities.setValue(new getAsyncTask(mSystemDao).execute().get());
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
        update();
    }

    public void create(SystemEntity systemEntity)
    {
        new createAsyncTask(mSystemDao).execute(systemEntity);
        update();
    }

    public void update()
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
            return mAsyncTaskDao.get();
        }

    }

    private static class insertAsyncTask extends AsyncTask<SystemEntity, Void, Void> {

        private SystemDao mAsyncTaskDao;

        insertAsyncTask(SystemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SystemEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    private static class createAsyncTask extends AsyncTask<SystemEntity, Void, Void> {

        private SystemDao mAsyncTaskDao;

        createAsyncTask(SystemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SystemEntity... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }

    }

}
