package uk.co.syski.client.android.data.thread.entity.type.statement;

import android.os.AsyncTask;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.TypeDao;
import uk.co.syski.client.android.data.entity.TypeEntity;

public final class DeleteAll extends AsyncTask<TypeEntity, Void, Void> {

    @Override
    protected Void doInBackground(TypeEntity... typeEntities) {
        TypeDao TypeDao = SyskiCache.GetDatabase().TypeDao();
        TypeDao.DeleteAll(typeEntities);
        return null;
    }

}
