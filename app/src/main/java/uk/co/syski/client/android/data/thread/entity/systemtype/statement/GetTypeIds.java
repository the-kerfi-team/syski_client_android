package uk.co.syski.client.android.data.thread.entity.systemtype.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemTypeDao;

public final class GetTypeIds extends AsyncTask<UUID, Void, List<UUID>> {

    @Override
    protected List<UUID> doInBackground(UUID... uuids) {
        SystemTypeDao SystemTypeDao = SyskiCache.GetDatabase().SystemTypeDao();
        return SystemTypeDao.getTypeIds(uuids);
    }
}
