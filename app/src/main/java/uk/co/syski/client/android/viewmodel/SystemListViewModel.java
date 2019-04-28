package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.model.repository.SystemRepository;
import uk.co.syski.client.android.model.viewmodel.SystemModel;

public class SystemListViewModel extends AndroidViewModel {

    private SystemRepository mSystemRepository;
    private LiveData<List<SystemModel>> mSystemList;

    public SystemListViewModel(@NonNull Application application) {
        super(application);
        mSystemRepository = Repository.getInstance().getSystemRepository();
        mSystemList = mSystemRepository.getSystemsLiveData(application);
        mSystemRepository.start(application);
    }

    public LiveData<List<SystemModel>> get() {
        return mSystemList;
    }

    public void delete(UUID id){
        mSystemRepository.delete(id);
    }

    @Override
    public void onCleared()
    {
        mSystemRepository.stop();
    }

}
