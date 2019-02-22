package uk.co.syski.client.android.data.thread.entity.ram.statement;

import android.arch.persistence.room.Query;
import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.RAMDao;
import uk.co.syski.client.android.data.entity.RAMEntity;

public final class GetRAMs extends AsyncTask<UUID, Void, List<RAMEntity>> {

    @Override
    protected List<RAMEntity> doInBackground(UUID... uuids) {
        RAMDao RAMDao = SyskiCache.GetDatabase().RAMDao();
        return RAMDao.GetRAMs(uuids);
    }

}
