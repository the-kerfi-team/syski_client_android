package uk.co.syski.client.android.data.thread.entity.operatingsystem;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.thread.entity.operatingsystem.statement.*;
import uk.co.syski.client.android.model.OperatingSystem;

public class OperatingSystemThreads {
    private static final OperatingSystemThreads ourInstance = new OperatingSystemThreads();

    public static OperatingSystemThreads getInstance() {
        return ourInstance;
    }

    private OperatingSystemThreads() {}

    public List<OperatingSystem> GetOperatingSystems(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetOperatingSystems().execute(uuids).get();
    }

    public Void InsertAll(uk.co.syski.client.android.data.entity.OperatingSystem... operatingSystems) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(operatingSystems).get();
    }

    public Void DeleteAll(uk.co.syski.client.android.data.entity.OperatingSystem... operatingSystems) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(operatingSystems).get();
    }
}
