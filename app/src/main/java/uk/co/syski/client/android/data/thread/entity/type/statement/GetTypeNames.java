package uk.co.syski.client.android.data.thread.entity.type.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.TypeDao;

/**
 * Created by t7014146 on 19/02/19.
 */

public final class GetTypeNames extends AsyncTask<UUID, Void, List<String>> {

    @Override
    protected List<String> doInBackground(UUID... uuids) {
        TypeDao TypeDao = SyskiCache.GetDatabase().TypeDao();
        return TypeDao.getTypeNames(uuids);
    }

}
