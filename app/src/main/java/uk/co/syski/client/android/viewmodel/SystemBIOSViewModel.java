package uk.co.syski.client.android.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.repository.BIOSRepository;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.model.viewmodel.SystemBIOSModel;

public class SystemBIOSViewModel  extends AndroidViewModel {

    private BIOSRepository mBIOSRepository;
    private LiveData<SystemBIOSModel> mSystemBIOS;
    private UUID systemId;

    public SystemBIOSViewModel(@NonNull Application application) {
        super(application);
        systemId = UUID.fromString(application.getSharedPreferences(application.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE).getString(application.getString(R.string.preference_sysID_key), null));
        mBIOSRepository = Repository.getInstance().getBIOSRepository();
        mSystemBIOS = mBIOSRepository.getSystemBIOSLiveData(systemId, getApplication().getBaseContext());
    }

    public LiveData<SystemBIOSModel> get() {
        return mSystemBIOS;
    }

}
