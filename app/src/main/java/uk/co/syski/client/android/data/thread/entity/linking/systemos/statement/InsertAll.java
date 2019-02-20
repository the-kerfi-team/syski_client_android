package uk.co.syski.client.android.data.thread.entity.linking.systemos.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemOSDao;
import uk.co.syski.client.android.data.entity.linking.SystemOS;

public final class InsertAll extends AsyncTask<SystemOS, Void, Void> {

    @Override
    protected Void doInBackground(SystemOS... systemOS) {
        SystemOSDao SystemOSDao = SyskiCache.GetDatabase().SystemOSDao();
        SystemOSDao.InsertAll(systemOS);
        return null;
    }

}
