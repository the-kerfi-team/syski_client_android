package uk.co.syski.client.android.data.thread.systemtype.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemTypeDao;
import uk.co.syski.client.android.data.entity.SystemType;

public final class DeleteAll extends AsyncTask<SystemType, Void, Void> {

    @Override
    protected Void doInBackground(SystemType... systemTypes) {
        SystemTypeDao SystemTypeDao = SyskiCache.GetDatabase().SystemTypeDao();
        SystemTypeDao.DeleteAll(systemTypes);
        return null;
    }

}
