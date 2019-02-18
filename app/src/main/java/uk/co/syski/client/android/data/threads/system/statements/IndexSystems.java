package uk.co.syski.client.android.data.threads.system.statements;

import android.os.AsyncTask;

import java.util.List;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.system.System;
import uk.co.syski.client.android.data.system.SystemDao;

public final class IndexSystems extends AsyncTask<Void, Void, List<System>>{

    @Override
    protected List<System> doInBackground(Void... voids) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        return SystemDao.indexSystems();
    }
}
