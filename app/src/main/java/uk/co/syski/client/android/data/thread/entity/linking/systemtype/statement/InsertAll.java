package uk.co.syski.client.android.data.thread.entity.linking.systemtype.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemTypeDao;
import uk.co.syski.client.android.data.entity.linking.SystemTypeEntity;

public final class InsertAll extends AsyncTask<SystemTypeEntity, Void, Void> {

    @Override
    protected Void doInBackground(SystemTypeEntity... systemTypeEntities) {
        SystemTypeDao SystemTypeDao = SyskiCache.GetDatabase().SystemTypeDao();
        SystemTypeDao.InsertAll(systemTypeEntities);
        return null;
    }

}
