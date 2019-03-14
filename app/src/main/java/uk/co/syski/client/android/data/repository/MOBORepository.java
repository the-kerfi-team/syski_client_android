package uk.co.syski.client.android.data.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.MotherboardDao;
import uk.co.syski.client.android.data.entity.MotherboardEntity;

public class MOBORepository {

    private MotherboardDao mMOBODao;
    private MutableLiveData<List<MotherboardEntity>> mMOBOEntities;
    private boolean mDataUpdated;
    private UUID mActiveSystemId;
    private MutableLiveData<List<MotherboardEntity>> mSystemMOBOEntities;

    public MOBORepository() {
        mMOBODao = SyskiCache.GetDatabase().MotherboardDao();
        mMOBOEntities = new MutableLiveData();
        mSystemMOBOEntities = new MutableLiveData();
        try {
            mMOBOEntities.postValue(new MOBORepository.getAsyncTask(mMOBODao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public MutableLiveData<List<MotherboardEntity>> get() {
        return mMOBOEntities;
    }

    public MutableLiveData<List<MotherboardEntity>> get(final UUID systemId) {
        if (mDataUpdated || mActiveSystemId == null || !mActiveSystemId.equals(systemId))
        {
            mActiveSystemId = systemId;
            updateSystemMOBOData();
            mDataUpdated = false;
        }
        return mSystemMOBOEntities;
    }

    public void insert(MotherboardEntity moboEntity) {
        new MOBORepository.insertAsyncTask(mMOBODao).execute(moboEntity);
        updateData();
    }

    public void update(MotherboardEntity moboEntity) {
        new MOBORepository.updateAsyncTask(mMOBODao).execute(moboEntity);
        updateData();
    }

    public void updateSystemMOBOData() {
        try {
            mSystemMOBOEntities.postValue(new MOBORepository.getSystemMOBOAsyncTask(mMOBODao).execute(mActiveSystemId).get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            mMOBOEntities.postValue(new MOBORepository.getAsyncTask(mMOBODao).execute().get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class getSystemMOBOAsyncTask extends AsyncTask<UUID, Void, List<MotherboardEntity>> {
        private MotherboardDao mAsyncTaskDao;

        getSystemMOBOAsyncTask(MotherboardDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<MotherboardEntity> doInBackground(final UUID... uuids) {
            List<MotherboardEntity> result = mAsyncTaskDao.GetMotherboards(uuids);
            return mAsyncTaskDao.GetMotherboards(uuids);
        }
    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<MotherboardEntity>> {
        private MotherboardDao mAsyncTaskDao;

        getAsyncTask(MotherboardDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<MotherboardEntity> doInBackground(final Void... voids) {
            return mAsyncTaskDao.get();
        }
    }

    private static class insertAsyncTask extends AsyncTask<MotherboardEntity, Void, Void> {

        private MotherboardDao mAsyncTaskDao;

        insertAsyncTask(MotherboardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MotherboardEntity... moboEntities) {
            mAsyncTaskDao.insert(moboEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<MotherboardEntity, Void, Void> {

        private MotherboardDao mAsyncTaskDao;

        updateAsyncTask(MotherboardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MotherboardEntity... moboEntities) {
            mAsyncTaskDao.update(moboEntities);
            return null;
        }
    }

}
