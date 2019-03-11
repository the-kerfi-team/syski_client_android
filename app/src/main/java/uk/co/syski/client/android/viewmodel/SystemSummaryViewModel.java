package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.repository.SystemRepository;

public class SystemSummaryViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();

    private SystemRepository mSystemRepository;
    private MutableLiveData<List<SystemEntity>> mSystemList;

    public SystemSummaryViewModel(@NonNull Application application) {
        super(application);
    }
}
