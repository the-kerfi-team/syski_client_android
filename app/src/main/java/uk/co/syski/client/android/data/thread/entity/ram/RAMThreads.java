package uk.co.syski.client.android.data.thread.entity.ram;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.thread.entity.ram.statement.*;

public class RAMThreads {
    private static final RAMThreads ourInstance = new RAMThreads();

    public static RAMThreads getInstance() {
        return ourInstance;
    }

    private RAMThreads() {}

    public List<RAMEntity> GetRAMs(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetRAMs().execute(uuids).get();
    }

    public Void InsertAll(RAMEntity... ramEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(ramEntities).get();
    }

    public Void DeleteAll(RAMEntity... ramEntities) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(ramEntities).get();
    }
}
