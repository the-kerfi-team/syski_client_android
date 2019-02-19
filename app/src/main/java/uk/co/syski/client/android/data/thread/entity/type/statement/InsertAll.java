package uk.co.syski.client.android.data.thread.entity.type.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.TypeDao;
import uk.co.syski.client.android.data.entity.Type;

public final class InsertAll extends AsyncTask<Type, Void, Void> {

    @Override
    protected Void doInBackground(Type... types) {
        TypeDao TypeDao = SyskiCache.GetDatabase().TypeDao();
        TypeDao.InsertAll(types);
        return null;
    }

}
