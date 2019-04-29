package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.data.StorageDataEntity;
import uk.co.syski.client.android.model.repository.SystemStorageDataRepository;

public class SystemStorageDataViewModel extends AndroidViewModel {

    private SystemStorageDataRepository mStorageDataRepository;
    private MutableLiveData<List<StorageDataEntity>> mSystemDataStorageList;
    private UUID systemId;

    public SystemStorageDataViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mStorageDataRepository = new SystemStorageDataRepository(application, systemId);
        mSystemDataStorageList = mStorageDataRepository.get();
        mStorageDataRepository.start();
    }

    public MutableLiveData<List<StorageDataEntity>> get() {
        return mSystemDataStorageList;
    }

    @Override
    public void onCleared()
    {
        mStorageDataRepository.stop();
    }
}
