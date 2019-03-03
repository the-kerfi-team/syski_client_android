package uk.co.syski.client.android.data.thread.entity.motherboard.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.MotherboardDao;
import uk.co.syski.client.android.data.entity.MotherboardEntity;

public final class InsertAll extends AsyncTask<MotherboardEntity, Void, Void> {

    @Override
    protected Void doInBackground(MotherboardEntity... motherboardEntities) {
        MotherboardDao MotherboardDao = SyskiCache.GetDatabase().MotherboardDao();
        MotherboardDao.InsertAll(motherboardEntities);
        return null;
    }

}
