package uk.co.syski.client.android.data.thread.system.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.dao.SystemDao;

public final class InsertAll extends AsyncTask<System, Void, Void> {

    @Override
    protected Void doInBackground(System... Systems) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        SystemDao.InsertAll(Systems);
        return null;
    }

}
