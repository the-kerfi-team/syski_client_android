package uk.co.syski.client.android.data.threads.system.statements;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.system.System;
import uk.co.syski.client.android.data.system.SystemDao;

public final class DeleteAll extends AsyncTask<System, Void, Void> {

    @Override
    protected Void doInBackground(System... systems) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        SystemDao.DeleteAll(systems);
        return null;
    }
}
