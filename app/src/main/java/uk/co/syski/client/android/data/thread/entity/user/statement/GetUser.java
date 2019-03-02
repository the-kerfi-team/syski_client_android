package uk.co.syski.client.android.data.thread.entity.user.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.UserDao;
import uk.co.syski.client.android.data.entity.UserEntity;

public class GetUser extends AsyncTask<Void, Void, UserEntity> {

    @Override
    protected UserEntity doInBackground(Void... Voids) {
        UserDao UserDao = SyskiCache.GetDatabase().UserDao();
        return UserDao.getUser();
    }

}