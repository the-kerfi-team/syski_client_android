package uk.co.syski.client.android.data.thread.entity.storage.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.StorageDao;
import uk.co.syski.client.android.data.entity.StorageEntity;

public final class InsertAll extends AsyncTask<StorageEntity, Void, Void> {

    @Override
    protected Void doInBackground(StorageEntity... storageEntities) {
        StorageDao StorageDao = SyskiCache.GetDatabase().StorageDao();
        StorageDao.InsertAll(storageEntities);
        return null;
    }

}
