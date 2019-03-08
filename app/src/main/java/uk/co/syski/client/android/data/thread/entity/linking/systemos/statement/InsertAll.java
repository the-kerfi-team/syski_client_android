package uk.co.syski.client.android.data.thread.entity.linking.systemos.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemOSDao;
import uk.co.syski.client.android.data.entity.linking.SystemOSEntity;

public final class InsertAll extends AsyncTask<SystemOSEntity, Void, Void> {

    @Override
    protected Void doInBackground(SystemOSEntity... systemOSEntities) {
        SystemOSDao SystemOSDao = SyskiCache.GetDatabase().SystemOSDao();
        SystemOSDao.InsertAll(systemOSEntities);
        return null;
    }

}
