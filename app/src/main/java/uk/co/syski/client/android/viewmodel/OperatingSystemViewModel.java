package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.repository.OSRepository;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.model.viewmodel.OperatingSystemModel;

public class OperatingSystemViewModel extends AndroidViewModel{

    private OSRepository mOSRepository;
    private LiveData<List<OperatingSystemModel>> mSystemOSList;
    private UUID systemId;

    public OperatingSystemViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mOSRepository = Repository.getInstance().getOSRepository();
        mSystemOSList = mOSRepository.getSystemOSsLiveData(systemId, application.getBaseContext());
    }

    public LiveData<List<OperatingSystemModel>> get() {
        return mSystemOSList;
    }

}
