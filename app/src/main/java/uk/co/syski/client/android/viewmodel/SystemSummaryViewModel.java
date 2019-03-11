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
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.data.repository.SystemRepository;

public class SystemSummaryViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();

    private SystemRepository mSystemRepository;
    private MutableLiveData<SystemEntity> mSystem;
    private UUID systemId;

    public SystemSummaryViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mSystemRepository = Repository.getInstance().getSystemRepository();
        mSystem = mSystemRepository.get(systemId);
    }

    public LiveData<SystemEntity> get() { return mSystem; }

}
