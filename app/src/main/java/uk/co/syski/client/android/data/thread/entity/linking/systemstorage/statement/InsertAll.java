package uk.co.syski.client.android.data.thread.entity.linking.systemstorage.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemStorageDao;
import uk.co.syski.client.android.data.entity.linking.SystemStorageEntity;

public final class InsertAll extends AsyncTask<SystemStorageEntity, Void, Void> {

    @Override
    protected Void doInBackground(SystemStorageEntity... systemStorageEntities) {
        SystemStorageDao SystemStorageDao = SyskiCache.GetDatabase().SystemStorageDao();
        SystemStorageDao.InsertAll(systemStorageEntities);
        return null;
    }

}
