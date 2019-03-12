package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.data.repository.OSRepository;
import uk.co.syski.client.android.data.repository.Repository;

public class OperatingSystemViewModel extends AndroidViewModel{

    private OSRepository mOSRepository;
    private MutableLiveData<List<OperatingSystemEntity>> mSystemOSList;
    private UUID systemId;

    public OperatingSystemViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mOSRepository = Repository.getInstance().getOSRepository();
        mSystemOSList = mOSRepository.get(systemId);
    }

    public MutableLiveData<List<OperatingSystemEntity>> get() {
        return mSystemOSList;
    }

}
