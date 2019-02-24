package uk.co.syski.client.android.data.thread.entity.gpu.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.GPUDao;
import uk.co.syski.client.android.data.entity.GPUEntity;

public final class DeleteAll extends AsyncTask<GPUEntity, Void, Void> {

    @Override
    protected Void doInBackground(GPUEntity... gpuEntities) {
        GPUDao GPUDao = SyskiCache.GetDatabase().GPUDao();
        GPUDao.DeleteAll(gpuEntities);
        return null;
    }

}
