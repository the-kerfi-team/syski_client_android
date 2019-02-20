package uk.co.syski.client.android.data.thread.entity.linking.systemtype.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemTypeDao;
import uk.co.syski.client.android.data.entity.linking.SystemType;

public final class InsertAll extends AsyncTask<SystemType, Void, Void> {

    @Override
    protected Void doInBackground(SystemType... systemTypes) {
        SystemTypeDao SystemTypeDao = SyskiCache.GetDatabase().SystemTypeDao();
        SystemTypeDao.InsertAll(systemTypes);
        return null;
    }

}
