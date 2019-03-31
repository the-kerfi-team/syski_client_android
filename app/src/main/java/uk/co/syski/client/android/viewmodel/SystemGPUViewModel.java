package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.repository.CPURepository;
import uk.co.syski.client.android.data.repository.GPURepository;
import uk.co.syski.client.android.data.repository.Repository;

public class SystemGPUViewModel extends AndroidViewModel {

    private GPURepository mGPURepository;
    private MutableLiveData<List<GPUEntity>> mSystemGPUList;
    private UUID systemId;

    public SystemGPUViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mGPURepository = Repository.getInstance().getGPURepository();
        mSystemGPUList = mGPURepository.get(systemId);
    }

    public LiveData<List<GPUEntity>> get() { return mSystemGPUList; }

}
