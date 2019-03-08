package uk.co.syski.client.android.data.thread.entity.motherboard.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.MotherboardDao;
import uk.co.syski.client.android.data.entity.MotherboardEntity;

public final class GetMotherboards extends AsyncTask<UUID, Void, List<MotherboardEntity>> {

    @Override
    protected List<MotherboardEntity> doInBackground(UUID... uuids) {
        MotherboardDao MotherboardDao = SyskiCache.GetDatabase().MotherboardDao();
        return MotherboardDao.GetMotherboards(uuids);
    }

}
