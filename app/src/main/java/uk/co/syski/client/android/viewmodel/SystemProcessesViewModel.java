package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.api.VolleySingleton;
import uk.co.syski.client.android.model.api.requests.system.APISystemKillProcess;
import uk.co.syski.client.android.model.api.requests.system.APISystemProcessDataRequest;
import uk.co.syski.client.android.model.database.entity.data.SystemProcessesEntity;
import uk.co.syski.client.android.model.repository.SystemProcessesRepository;
import uk.co.syski.client.android.model.repository.SystemStorageDataRepository;

public class SystemProcessesViewModel extends AndroidViewModel
{

    private SystemProcessesRepository mProcessesRepository;
    private MutableLiveData<List<SystemProcessesEntity>> mSystemDataProcessesList;
    private UUID systemId;

    public SystemProcessesViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mProcessesRepository = new SystemProcessesRepository(application, systemId);
        mSystemDataProcessesList = mProcessesRepository.get();
        mProcessesRepository.start();
    }

    public MutableLiveData<List<SystemProcessesEntity>> get() {
        return mSystemDataProcessesList;
    }

    public void killProcess(int id)
    {
        VolleySingleton.getInstance(getApplication().getBaseContext()).addToRequestQueue(new APISystemKillProcess(getApplication().getBaseContext(), systemId, id));
    }

    @Override
    public void onCleared()
    {
        mProcessesRepository.stop();
    }

}
