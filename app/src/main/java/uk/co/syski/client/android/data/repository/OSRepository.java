package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.OperatingSystemDao;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;

public class OSRepository {

    private OperatingSystemDao mOperatingSystemDao;
    private MutableLiveData<List<OperatingSystemEntity>> mOSEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<OperatingSystemEntity>> mSystemOSEntities;

    public OSRepository() {
        mOperatingSystemDao = SyskiCache.GetDatabase().OperatingSystemDao();
        mOSEntities = new MutableLiveData();
        mSystemOSEntities = new MutableLiveData();
        try {
            mOSEntities.postValue(new OSRepository.getAsyncTask(mOperatingSystemDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<List<OperatingSystemEntity>> get() {
        return mOSEntities;
    }

    public MutableLiveData<List<OperatingSystemEntity>> get(final UUID systemId) {
        if (mDataUpdated || mActiveSystemId == null || !mActiveSystemId.equals(systemId))
        {
            mActiveSystemId = systemId;
            updateSystemOSData();
            mDataUpdated = false;
        }
        return mSystemOSEntities;
    }

    public void insert(OperatingSystemEntity operatingSystemEntity) {
        new OSRepository.insertAsyncTask(mOperatingSystemDao).execute(operatingSystemEntity);
        updateData();
    }

    public void update(OperatingSystemEntity operatingSystemEntity) {
        new OSRepository.updateAsyncTask(mOperatingSystemDao).execute(operatingSystemEntity);
        updateData();
    }

    public void updateSystemOSData() {
        try {
            mSystemOSEntities.postValue(new OSRepository.getSystemOSAsyncTask(mOperatingSystemDao).execute(mActiveSystemId).get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            mOSEntities.postValue(new OSRepository.getAsyncTask(mOperatingSystemDao).execute().get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getSystemOSAsyncTask extends AsyncTask<UUID, Void, List<OperatingSystemEntity>> {
        private OperatingSystemDao mAsyncTaskDao;

        getSystemOSAsyncTask(OperatingSystemDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<OperatingSystemEntity> doInBackground(final UUID... uuids) {
            List<OperatingSystemEntity> result = mAsyncTaskDao.getSystemOperatingSystems(uuids);
            return mAsyncTaskDao.getSystemOperatingSystems(uuids);
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<OperatingSystemEntity>> {
        private OperatingSystemDao mAsyncTaskDao;

        getAsyncTask(OperatingSystemDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<OperatingSystemEntity> doInBackground(final Void... voids) {
            return mAsyncTaskDao.get();
        }
    }

    private static class insertAsyncTask extends AsyncTask<OperatingSystemEntity, Void, Void> {

        private OperatingSystemDao mAsyncTaskDao;

        insertAsyncTask(OperatingSystemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final OperatingSystemEntity... osEntities) {
            mAsyncTaskDao.insert(osEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<OperatingSystemEntity, Void, Void> {

        private OperatingSystemDao mAsyncTaskDao;

        updateAsyncTask(OperatingSystemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final OperatingSystemEntity... osEntities) {
            mAsyncTaskDao.update(osEntities);
            return null;
        }
    }
    
}
