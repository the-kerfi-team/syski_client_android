package uk.co.syski.client.android.model.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.util.UUID;

import uk.co.syski.client.android.model.viewmodel.SystemBIOSModel;

public enum BIOSRepository {
    INSTANCE;

    public LiveData<SystemBIOSModel> getSystemBIOSLiveData(final UUID systemId, Context context)
    {
        MutableLiveData mLiveDataSystemCPUModels = new MutableLiveData<>();
        SystemBIOSModel bios = new SystemBIOSModel("Test", "Test", "Test", "Test");
        mLiveDataSystemCPUModels.postValue(bios);
        return mLiveDataSystemCPUModels;
    }

}
