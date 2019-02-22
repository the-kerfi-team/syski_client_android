package uk.co.syski.client.android.data.thread.entity.cpu.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.entity.CPUEntity;

public final class GetCPUs extends AsyncTask<UUID, Void, List<CPUEntity>> {

    @Override
    protected List<CPUEntity> doInBackground(UUID... uuids) {
        CPUDao CPUDao = SyskiCache.GetDatabase().CPUDao();
        return CPUDao.getCPUs(uuids);
    }

}
