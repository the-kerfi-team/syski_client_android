package uk.co.syski.client.android.data.thread.system.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemDao;
import uk.co.syski.client.android.data.entity.System;

public final class GetSystems extends AsyncTask<UUID, Void, List<System>> {

    @Override
    protected List<System> doInBackground(UUID... uuids) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        return SystemDao.getSystems(uuids);
    }

}
