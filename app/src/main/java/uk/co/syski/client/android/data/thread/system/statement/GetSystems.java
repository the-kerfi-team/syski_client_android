package uk.co.syski.client.android.data.threads.system.statements;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.system.SystemDao;
import uk.co.syski.client.android.data.system.System;

public final class GetSystems extends AsyncTask<UUID, Void, List<System>> {

    @Override
    protected List<System> doInBackground(UUID... uuids) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        return SystemDao.getSystems(uuids);
    }

}
