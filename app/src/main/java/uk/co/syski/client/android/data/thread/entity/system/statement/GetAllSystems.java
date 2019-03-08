package uk.co.syski.client.android.data.thread.entity.system.statement;

import android.os.AsyncTask;

import java.util.List;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.dao.SystemDao;

public final class GetAllSystems extends AsyncTask<Void, Void, List<SystemEntity>>{

    @Override
    protected List<SystemEntity> doInBackground(Void... voids) {
        SystemDao SystemDao = SyskiCache.GetDatabase().SystemDao();
        return SystemDao.getAllSystems();
    }
}
