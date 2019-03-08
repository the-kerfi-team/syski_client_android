package uk.co.syski.client.android.data.thread.entity.operatingsystem.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.OperatingSystemDao;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;

public final class InsertAll extends AsyncTask<OperatingSystemEntity, Void, Void> {

    @Override
    protected Void doInBackground(OperatingSystemEntity... operatingSystemEntities) {
        OperatingSystemDao OperatingSystemDao = SyskiCache.GetDatabase().OperatingSystemDao();
        OperatingSystemDao.InsertAll(operatingSystemEntities);
        return null;
    }

}
