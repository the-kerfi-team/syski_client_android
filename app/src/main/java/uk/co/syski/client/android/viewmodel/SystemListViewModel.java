package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.model.repository.SystemRepository;

public class SystemListViewModel extends AndroidViewModel {

    private SystemRepository mSystemRepository;
    private LiveData<List<SystemEntity>> mSystemList;

    public SystemListViewModel(@NonNull Application application) {
        super(application);
        mSystemRepository = Repository.getInstance().getSystemRepository();
        mSystemList = mSystemRepository.getSystemsLiveData(application);
    }

    public LiveData<List<SystemEntity>> get() {
        return mSystemList;
    }

    public void delete(SystemEntity systemEntity){
        mSystemRepository.delete(systemEntity);
    }

}
