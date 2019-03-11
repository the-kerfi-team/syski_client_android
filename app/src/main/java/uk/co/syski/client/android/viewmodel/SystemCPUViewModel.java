package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.repository.CPURepository;
import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.data.repository.SystemRepository;

public class SystemCPUViewModel extends AndroidViewModel {

    private CPURepository mSystemRepository;
    private MutableLiveData<List<CPUEntity>> mSystemCPUList;
    private UUID systemId;

    public SystemCPUViewModel(@NonNull Application application) {
        super(application);
        mSystemRepository = Repository.getCPURepository();
        mSystemCPUList = mSystemRepository.get(systemId);
    }

    public LiveData<List<CPUEntity>> get() { return mSystemCPUList; }

}
