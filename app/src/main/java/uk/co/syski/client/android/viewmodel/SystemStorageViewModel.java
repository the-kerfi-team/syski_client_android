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
import uk.co.syski.client.android.model.database.entity.StorageEntity;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.model.repository.StorageRepository;
import uk.co.syski.client.android.model.viewmodel.SystemStorageModel;

public class SystemStorageViewModel extends AndroidViewModel {

    private StorageRepository mStorageRepository;
    private LiveData<List<SystemStorageModel>> mSystemStorageList;
    private UUID systemId;

    public SystemStorageViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mStorageRepository = Repository.getInstance().getStorageRepository();
        mSystemStorageList = mStorageRepository.getSystemStoragesLiveData(systemId, application.getBaseContext());
    }

    public LiveData<List<SystemStorageModel>> get() {
        return mSystemStorageList;
    }

}
