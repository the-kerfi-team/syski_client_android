package uk.co.syski.client.android.data.thread.entity.cpu.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.entity.CPUEntity;

public final class DeleteAll extends AsyncTask<CPUEntity, Void, Void> {

    @Override
    protected Void doInBackground(CPUEntity... cpuEntities) {
        CPUDao CPUDao = SyskiCache.GetDatabase().CPUDao();
        CPUDao.DeleteAll(cpuEntities);
        return null;
    }
}
