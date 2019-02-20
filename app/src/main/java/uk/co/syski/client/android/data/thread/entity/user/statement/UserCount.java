package uk.co.syski.client.android.data.thread.entity.user.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.UserDao;

public class UserCount extends AsyncTask<Void, Void, Integer> {

    @Override
    protected Integer doInBackground(Void... Voids){
        UserDao UserDao = SyskiCache.GetDatabase().UserDao();
        return new Integer(UserDao.UserCount());
    }
}