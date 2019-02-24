package uk.co.syski.client.android.data.thread.entity.storage.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.StorageDao;
import uk.co.syski.client.android.data.entity.StorageEntity;

public final class GetStorages extends AsyncTask<UUID, Void, List<StorageEntity>> {

    @Override
    protected List<StorageEntity> doInBackground(UUID... uuids) {
        StorageDao StorageDao = SyskiCache.GetDatabase().StorageDao();
        return StorageDao.GetStorages(uuids);
    }

}
