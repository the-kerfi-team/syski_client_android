package uk.co.syski.client.android.data.thread.entity.system.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.dao.SystemDao;

public final class DeleteAll extends AsyncTask<System, Void, Void> {

    @Override
    protected Void doInBackground(System... systems) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        SystemDao.DeleteAll(systems);
        return null;
    }
}
