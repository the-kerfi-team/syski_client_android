package uk.co.syski.client.android.data.thread.entity.system.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.entity.SystemEntity;

public final class GetSystems extends AsyncTask<UUID, Void, List<SystemEntity>> {

    @Override
    protected List<SystemEntity> doInBackground(UUID... uuids) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        return SystemDao.get(uuids);
    }

}
