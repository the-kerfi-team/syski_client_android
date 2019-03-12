package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;
import uk.co.syski.client.android.data.repository.SystemCPUDataRepository;

public class SystemCPULoadDataViewModel extends AndroidViewModel {

    private SystemCPUDataRepository mCPUDataRepository;
    private MutableLiveData<List<CPUDataEntity>> mSystemDataCPUList;
    private UUID systemId;

    public SystemCPULoadDataViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
    }

    public MutableLiveData<List<CPUDataEntity>> get() {
        return mSystemDataCPUList;
    }

}
