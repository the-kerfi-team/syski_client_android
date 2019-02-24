package uk.co.syski.client.android.data.thread.entity.linking.systemgpu.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemGPUDao;
import uk.co.syski.client.android.data.entity.linking.SystemGPUEntity;

public final class InsertAll extends AsyncTask<SystemGPUEntity, Void, Void> {

    @Override
    protected Void doInBackground(SystemGPUEntity... systemGPUEntities) {
        SystemGPUDao SystemGPUDao = SyskiCache.GetDatabase().SystemGPUDao();
        SystemGPUDao.InsertAll(systemGPUEntities);
        return null;
    }

}
