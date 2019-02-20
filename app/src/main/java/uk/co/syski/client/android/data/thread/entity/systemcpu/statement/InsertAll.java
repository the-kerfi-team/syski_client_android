package uk.co.syski.client.android.data.thread.entity.systemcpu.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemCPUDao;
import uk.co.syski.client.android.data.entity.SystemCPU;

public final class InsertAll extends AsyncTask<SystemCPU, Void, Void> {

    @Override
    protected Void doInBackground(SystemCPU... systemCPUS) {
        SystemCPUDao SystemCPUDao = SyskiCache.GetDatabase().SystemCPUDao();
        SystemCPUDao.InsertAll(systemCPUS);
        return null;
    }

}
