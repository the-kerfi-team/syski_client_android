package uk.co.syski.client.android.data.thread.entity.ram.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.RAMDao;
import uk.co.syski.client.android.data.entity.RAMEntity;

public final class InsertAll extends AsyncTask<RAMEntity, Void, Void> {

    @Override
    protected Void doInBackground(RAMEntity... ramEntities) {
        RAMDao RAMDao = SyskiCache.GetDatabase().RAMDao();
        RAMDao.InsertAll(ramEntities);
        return null;
    }

}
