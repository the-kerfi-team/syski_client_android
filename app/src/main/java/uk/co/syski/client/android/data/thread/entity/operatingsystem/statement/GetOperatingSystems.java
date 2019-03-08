package uk.co.syski.client.android.data.thread.entity.operatingsystem.statement;

import android.os.AsyncTask;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.dao.OperatingSystemDao;
import uk.co.syski.client.android.model.OperatingSystemModel;

public final class GetOperatingSystems extends AsyncTask<UUID, Void, List<OperatingSystemModel>> {

    @Override
    protected List<OperatingSystemModel> doInBackground(UUID... uuids) {
        OperatingSystemDao OperatingSystemDao = SyskiCache.GetDatabase().OperatingSystemDao();
        return OperatingSystemDao.GetOperatingSystems(uuids);
    }

}
