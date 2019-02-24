package uk.co.syski.client.android.data.thread.entity.gpu.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.GPUDao;
import uk.co.syski.client.android.data.entity.GPUEntity;

public final class GetGPUs extends AsyncTask<UUID, Void, List<GPUEntity>> {

    @Override
    protected List<GPUEntity> doInBackground(UUID... uuids) {
        GPUDao GPUDao = SyskiCache.GetDatabase().GPUDao();
        return GPUDao.GetGPUs(uuids);
    }

}
