package uk.co.syski.client.android.data.thread.entity.user.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.UserDao;
import uk.co.syski.client.android.data.entity.UserEntity;

public class InsertAll extends AsyncTask<UserEntity, Void, Void> {

        @Override
        protected Void doInBackground(UserEntity... userEntities){
            UserDao UserDao = SyskiCache.GetDatabase().UserDao();
            UserDao.InsertAll(userEntities);
            return null;
        }
}
