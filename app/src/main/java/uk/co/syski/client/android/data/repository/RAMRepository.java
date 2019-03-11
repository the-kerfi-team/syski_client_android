package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.RAMDao;
import uk.co.syski.client.android.data.entity.RAMEntity;

public class RAMRepository {

    private RAMDao mRAMDao;
    private MutableLiveData<List<RAMEntity>> mRAMEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<RAMEntity>> mSystemRAMEntities;

    public RAMRepository() {
        mRAMDao = SyskiCache.GetDatabase().RAMDao();
        mRAMEntities = new MutableLiveData();
        mSystemRAMEntities = new MutableLiveData();
        try {
            mRAMEntities.postValue(new RAMRepository.getAsyncTask(mRAMDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<List<RAMEntity>> get() {
        return mRAMEntities;
    }

    public MutableLiveData<List<RAMEntity>> get(final UUID systemId) {
        if (mDataUpdated || mActiveSystemId == null || !mActiveSystemId.equals(systemId))
        {
            mDataUpdated = false;
            mActiveSystemId = systemId;
            updateSystemRAMData();
        }
        return mSystemRAMEntities;
    }

    public void insert(RAMEntity ramEntity) {
        new RAMRepository.insertAsyncTask(mRAMDao).execute(ramEntity);
        updateData();
    }

    public void update(RAMEntity ramEntity) {
        new RAMRepository.updateAsyncTask(mRAMDao).execute(ramEntity);
        updateData();
    }

    public void updateSystemRAMData() {
        try {
            mRAMEntities.postValue(new RAMRepository.getSystemRAMAsyncTask(mRAMDao).execute(mActiveSystemId).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            mRAMEntities.postValue(new RAMRepository.getAsyncTask(mRAMDao).execute().get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getSystemRAMAsyncTask extends AsyncTask<UUID, Void, List<RAMEntity>> {
        private RAMDao mAsyncTaskDao;

        getSystemRAMAsyncTask(RAMDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<RAMEntity> doInBackground(final UUID... uuids) {
            return mAsyncTaskDao.getSystemRAMs(uuids);
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<RAMEntity>> {
        private RAMDao mAsyncTaskDao;

        getAsyncTask(RAMDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<RAMEntity> doInBackground(final Void... voids) {
            return mAsyncTaskDao.get();
        }
    }

    private static class insertAsyncTask extends AsyncTask<RAMEntity, Void, Void> {

        private RAMDao mAsyncTaskDao;

        insertAsyncTask(RAMDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RAMEntity... ramEntities) {
            mAsyncTaskDao.insert(ramEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<RAMEntity, Void, Void> {

        private RAMDao mAsyncTaskDao;

        updateAsyncTask(RAMDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RAMEntity... ramEntities) {
            mAsyncTaskDao.update(ramEntities);
            return null;
        }
    }

}
