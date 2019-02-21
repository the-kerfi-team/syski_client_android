package uk.co.syski.client.android.data.thread.entity.linking.systemcpu.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemCPUDao;
import uk.co.syski.client.android.data.entity.linking.SystemCPUEntity;

public final class InsertAll extends AsyncTask<SystemCPUEntity, Void, Void> {

    @Override
    protected Void doInBackground(SystemCPUEntity... systemCPUEntities) {
        SystemCPUDao SystemCPUDao = SyskiCache.GetDatabase().SystemCPUDao();
        SystemCPUDao.InsertAll(systemCPUEntities);
        return null;
    }

}
