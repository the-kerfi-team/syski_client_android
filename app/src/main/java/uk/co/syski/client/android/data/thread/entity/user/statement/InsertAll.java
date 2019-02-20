package uk.co.syski.client.android.data.thread.entity.user.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.UserDao;
import uk.co.syski.client.android.data.entity.User;

public class InsertAll extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... Users){
            UserDao UserDao = SyskiCache.GetDatabase().UserDao();
            UserDao.InsertAll(Users);
            return null;
        }
}
