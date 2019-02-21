package uk.co.syski.client.android.data.thread.entity.operatingsystem;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.data.thread.entity.operatingsystem.statement.*;
import uk.co.syski.client.android.model.OperatingSystemModel;

public class OperatingSystemThreads {
    private static final OperatingSystemThreads ourInstance = new OperatingSystemThreads();

    public static OperatingSystemThreads getInstance() {
        return ourInstance;
    }

    private OperatingSystemThreads() {}

    public List<OperatingSystemModel> GetOperatingSystems(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetOperatingSystems().execute(uuids).get();
    }

    public Void InsertAll(OperatingSystemEntity... operatingSystemEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(operatingSystemEntities).get();
    }

    public Void DeleteAll(OperatingSystemEntity... operatingSystemEntities) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(operatingSystemEntities).get();
    }
}
