package uk.co.syski.client.android.data.thread.entity.systemcpu.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.SystemCPUDao;

public final class GetCPUIds extends AsyncTask<UUID, Void, List<UUID>> {

    @Override
    protected List<UUID> doInBackground(UUID... uuids) {
        SystemCPUDao SystemCPUDao = SyskiCache.GetDatabase().SystemCPUDao();
        return SystemCPUDao.GetCPUIds();
    }

}
