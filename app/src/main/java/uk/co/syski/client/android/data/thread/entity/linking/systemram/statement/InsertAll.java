package uk.co.syski.client.android.data.thread.entity.linking.systemram.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.linking.SystemRAMDao;
import uk.co.syski.client.android.data.entity.linking.SystemRAMEntity;

public final class InsertAll extends AsyncTask<SystemRAMEntity, Void, Void> {

    @Override
    protected Void doInBackground(SystemRAMEntity... systemRAMEntities) {
        SystemRAMDao SystemRAMDao = SyskiCache.GetDatabase().SystemRAMDao();
        SystemRAMDao.InsertAll(systemRAMEntities);
        return null;
    }

}
