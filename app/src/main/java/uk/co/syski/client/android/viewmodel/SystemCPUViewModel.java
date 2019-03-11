package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.repository.CPURepository;
import uk.co.syski.client.android.data.repository.Repository;

public class SystemCPUViewModel extends AndroidViewModel {

    private CPURepository mCPURepository;
    private MutableLiveData<List<CPUEntity>> mSystemCPUList;
    private UUID systemId;

    public SystemCPUViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mCPURepository = Repository.getInstance().getCPURepository();
        mSystemCPUList = mCPURepository.get(systemId);
    }

    public LiveData<List<CPUEntity>> get() { return mSystemCPUList; }

}
