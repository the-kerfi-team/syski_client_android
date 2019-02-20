package uk.co.syski.client.android.data.thread.entity.cpu.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.CPUDao;
import uk.co.syski.client.android.data.entity.CPU;

public final class DeleteAll extends AsyncTask<CPU, Void, Void> {

    @Override
    protected Void doInBackground(CPU... cpus) {
        CPUDao CPUDao = SyskiCache.GetDatabase().CPUDao();
        CPUDao.DeleteAll(cpus);
        return null;
    }
}
