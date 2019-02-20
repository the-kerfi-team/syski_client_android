package uk.co.syski.client.android.data.thread.entity.operatingsystem.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.OperatingSystemDao;
import uk.co.syski.client.android.data.entity.OperatingSystem;

public final class DeleteAll extends AsyncTask<OperatingSystem, Void, Void> {

    @Override
    protected Void doInBackground(OperatingSystem... operatingSystems) {
        OperatingSystemDao OperatingSystemDao = SyskiCache.GetDatabase().OperatingSystemDao();
        OperatingSystemDao.DeleteAll(operatingSystems);
        return null;
    }

}
