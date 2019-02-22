package uk.co.syski.client.android.data.thread.entity.ram.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.RAMDao;
import uk.co.syski.client.android.data.entity.RAMEntity;

public final class DeleteAll extends AsyncTask<RAMEntity, Void, Void> {

    @Override
    protected Void doInBackground(RAMEntity... ramEntities) {
        RAMDao RAMDao = SyskiCache.GetDatabase().RAMDao();
        RAMDao.DeleteAll(ramEntities);
        return null;
    }

}
