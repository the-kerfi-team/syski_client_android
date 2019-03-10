package uk.co.syski.client.android.data.thread.entity.system.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.dao.SystemDao;

public final class DeleteAll extends AsyncTask<SystemEntity, Void, Void> {

    @Override
    protected Void doInBackground(SystemEntity... systemEntities) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        SystemDao.delete(systemEntities);
        return null;
    }
}
