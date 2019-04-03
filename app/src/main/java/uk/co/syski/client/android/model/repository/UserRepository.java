package uk.co.syski.client.android.model.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.dao.UserDao;
import uk.co.syski.client.android.model.database.entity.UserEntity;

public class UserRepository {

    private UserDao mUserDao;
    private MutableLiveData<List<UserEntity>> mUserEntities;
    private boolean mDataUpdated;
    private UUID mActiveUserId;
    private MutableLiveData<UserEntity> mUserEntity;

    public UserRepository() {
        mUserDao = SyskiCache.GetDatabase().UserDao();
        mUserEntities = new MutableLiveData();
        mUserEntity = new MutableLiveData();
        try {
            mUserEntities.postValue(new getAsyncTask(mUserDao).execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<List<UserEntity>> get() {
        return mUserEntities;
    }

    public MutableLiveData<UserEntity> get(UUID userId) {
        if (mDataUpdated || mActiveUserId == null || !mActiveUserId.equals(userId)) {
            mActiveUserId = userId;
            updateUserData();
            mDataUpdated = false;
        }
        return mUserEntity;
    }

    public UserEntity getUser() {
        if (mDataUpdated) {
            updateUserData();
            mDataUpdated = false;
        }
        return mUserEntity.getValue();
    }

    public void setActiveUserId(UUID userId)
    {
        mActiveUserId = userId;
        updateUserData();
    }

    public void insert(UserEntity userEntity) {
        new insertAsyncTask(mUserDao).execute(userEntity);
        updateData();
    }

    public void update(UserEntity userEntity) {
        new updateAsyncTask(mUserDao).execute(userEntity);
        updateData();
        if (userEntity.Id.equals(mActiveUserId))
        {
            updateUserData();
        }
    }

    public void updateUserData() {
        try {
            mUserEntity.postValue(new getUserAsyncTask(mUserDao).execute(mActiveUserId).get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updateData() {
        try {
            mUserEntities.postValue(new getAsyncTask(mUserDao).execute().get());
            mDataUpdated = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    private static class getUserAsyncTask extends AsyncTask<UUID, Void, UserEntity> {

        private UserDao mAsyncTaskDao;

        getUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        protected UserEntity doInBackground(final UUID... uuids) {
            return mAsyncTaskDao.get(uuids[0]);
        }

    }

    private static class getAsyncTask extends AsyncTask<Void, Void, List<UserEntity>> {

        private UserDao mAsyncTaskDao;

        getAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        protected List<UserEntity> doInBackground(final Void... voids) {
            return mAsyncTaskDao.get();
        }

    }

    private static class insertAsyncTask extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserEntity... userEntities) {
            mAsyncTaskDao.insert(userEntities);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mAsyncTaskDao;

        updateAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserEntity... userEntities) {
            mAsyncTaskDao.update(userEntities);
            return null;
        }

    }

}
