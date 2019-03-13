package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.RAMDao;
import uk.co.syski.client.android.data.dao.RAMDao;
import uk.co.syski.client.android.data.dao.linking.SystemRAMDao;
import uk.co.syski.client.android.data.dao.linking.SystemRAMDao;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;

public class RAMRepository {

    private RAMDao mRAMDao;
    private MutableLiveData<List<RAMEntity>> mRAMEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<RAMEntity>> mSystemRAMEntities;
    private SystemRAMDao mSystemRamDao;

    public RAMRepository() {
        mRAMDao = SyskiCache.GetDatabase().RAMDao();
        mSystemRamDao = SyskiCache.GetDatabase().SystemRAMDao();
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
            mActiveSystemId = systemId;
            updateSystemRAMData();
            mDataUpdated = false;
        }
        return mSystemRAMEntities;
    }

    public void insert(RAMEntity ramEntity) {
        new RAMRepository.insertAsyncTask(mRAMDao).execute(ramEntity);
        updateData();
    }

    public void insert(RAMEntity ramEntity, UUID systemId, int slot) {
        new RAMRepository.insertSystemRAMAsyncTask(mRAMDao, mSystemRamDao, systemId, slot).execute(ramEntity);
        updateData();
        if (mActiveSystemId != null && mActiveSystemId.equals(systemId))
        {
            updateSystemRAMData();
        }
    }

    public void update(RAMEntity ramEntity) {
        new RAMRepository.updateAsyncTask(mRAMDao).execute(ramEntity);
        updateData();
    }

    public void updateSystemRAMData() {
        try {
            mSystemRAMEntities.postValue(new RAMRepository.getSystemRAMAsyncTask(mRAMDao).execute(mActiveSystemId).get());
            mDataUpdated = true;
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

    private static class getSystemRAMAsyncTask extends AsyncTask<UUID, Void, List<RAMEntity>> {
        private RAMDao mAsyncTaskDao;

        getSystemRAMAsyncTask(RAMDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<RAMEntity> doInBackground(final UUID... uuids) {
            List<RAMEntity> result = mAsyncTaskDao.getSystemRAMs(uuids);
            return mAsyncTaskDao.getSystemRAMs(uuids);
        }
    }

    private static class insertSystemRAMAsyncTask extends AsyncTask<RAMEntity, Void, Void> {
        private RAMDao mAsyncTaskRAMDao;
        private SystemRAMDao mAsyncTaskSystemRAMDao;
        private UUID mSystemId;
        private int mSlot;

        insertSystemRAMAsyncTask(RAMDao ramDao, SystemRAMDao systemDao, UUID systemId, int slot) {
            mAsyncTaskRAMDao = ramDao;
            mAsyncTaskSystemRAMDao = systemDao;
            mSystemId = systemId;
            mSlot = slot;
        }

        protected Void doInBackground(final RAMEntity... ramEntities) {
            mAsyncTaskRAMDao.insert(ramEntities);
            SystemRAMEntity systemRAMEntity = new SystemRAMEntity();
            for (RAMEntity ramEntity: ramEntities) {
                systemRAMEntity.RAMId = ramEntity.Id;
                systemRAMEntity.SystemId = mSystemId;
                systemRAMEntity.DimmSlot = mSlot;
                mAsyncTaskSystemRAMDao.insert(systemRAMEntity);
            }
            return null;
        }
    }

}
