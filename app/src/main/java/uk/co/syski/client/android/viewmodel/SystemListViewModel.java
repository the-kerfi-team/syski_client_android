package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.model.repository.SystemRepository;

public class SystemListViewModel extends AndroidViewModel {

    private SystemRepository mSystemRepository;
    private LiveData<HashMap<UUID, SystemEntity>> mSystemList;

    public SystemListViewModel(@NonNull Application application) {
        super(application);
        mSystemRepository = Repository.getInstance().getSystemRepository();
        mSystemList = mSystemRepository.getSystemsLiveData(application);
    }

    public LiveData<HashMap<UUID, SystemEntity>> get() {
        return mSystemList;
    }

    public void openSummary(UUID systemId)
    {

    }

}
