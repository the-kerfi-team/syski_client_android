package uk.co.syski.client.android.data.thread.entity.user.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.UserDao;

public class UserAccessToken extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... Voids){
        UserDao UserDao = SyskiCache.GetDatabase().UserDao();
        return UserDao.getAccessToken();
    }

}