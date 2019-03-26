package uk.co.syski.client.android.model.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.OperatingSystemDao;
import uk.co.syski.client.android.model.database.dao.linking.SystemOSDao;
import uk.co.syski.client.android.model.database.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.database.entity.linking.SystemOSEntity;
import uk.co.syski.client.android.model.database.model.OperatingSystemModel;

public class OSRepository {

    private OperatingSystemDao mOperatingSystemDao;
    private MutableLiveData<List<OperatingSystemEntity>> mOSEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<OperatingSystemModel>> mSystemOSEntities;
    private SystemOSDao mSystemOSDao;

    public OSRepository() {
        mOperatingSystemDao = SyskiCache.GetDatabase().OperatingSystemDao();
        mSystemOSDao = SyskiCache.GetDatabase().SystemOSDao();
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

    public MutableLiveData<List<OperatingSystemModel>> get(final UUID systemId) {
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

    public void insert(OperatingSystemEntity osEntity, UUID systemId) {
        new OSRepository.insertSystemOSAsyncTask(mOperatingSystemDao, mSystemOSDao, systemId).execute(osEntity);
        updateData();
        if (mActiveSystemId != null && mActiveSystemId.equals(systemId))
        {
            updateSystemOSData();
        }
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

    private static class getSystemOSAsyncTask extends AsyncTask<UUID, Void, List<OperatingSystemModel>> {
        private OperatingSystemDao mAsyncTaskDao;

        getSystemOSAsyncTask(OperatingSystemDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<OperatingSystemModel> doInBackground(final UUID... uuids) {
            List<OperatingSystemModel> result = mAsyncTaskDao.getSystemOperatingSystems(uuids);
            return mAsyncTaskDao.getSystemOperatingSystems(uuids);
        }
    }

    private static class insertSystemOSAsyncTask extends AsyncTask<OperatingSystemEntity, Void, Void> {
        private OperatingSystemDao mAsyncTaskOSDao;
        private SystemOSDao mAsyncTaskSystemOSDao;
        private UUID mSystemId;

        insertSystemOSAsyncTask(OperatingSystemDao osDao, SystemOSDao systemDao, UUID systemId) {
            mAsyncTaskOSDao = osDao;
            mAsyncTaskSystemOSDao = systemDao;
            mSystemId = systemId;
        }

        protected Void doInBackground(final OperatingSystemEntity... osEntities) {
            mAsyncTaskOSDao.insert(osEntities);
            SystemOSEntity systemOSEntity = new SystemOSEntity();
            for (OperatingSystemEntity osEntity: osEntities) {
                systemOSEntity.OSId = osEntity.Id;
                systemOSEntity.SystemId = mSystemId;
                mAsyncTaskSystemOSDao.insert(systemOSEntity);
            }
            return null;
        }
    }
    
}
