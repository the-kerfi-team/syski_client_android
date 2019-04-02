package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.SystemCPUModel;
import uk.co.syski.client.android.model.repository.CPURepository;
import uk.co.syski.client.android.model.repository.Repository;

public class SystemCPUViewModel extends AndroidViewModel {

    private CPURepository mCPURepository;

    private UUID systemId;
    private LiveData<List<SystemCPUModel>> mSystemCPUList;

    public SystemCPUViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mCPURepository = Repository.getInstance().getCPURepository();
        mSystemCPUList = mCPURepository.getSystemCPUsLiveData(systemId, application.getBaseContext());
    }

    public LiveData<List<SystemCPUModel>> get() {
        return mSystemCPUList;
    }

}
