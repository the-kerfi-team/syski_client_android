package uk.co.syski.client.android.data.thread.system;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.thread.system.statement.*;

public class SystemThreads {
    private static final SystemThreads ourInstance = new SystemThreads();

    public static SystemThreads getInstance() {
        return ourInstance;
    }

    private SystemThreads() {}

    public List<System> IndexSystems() throws ExecutionException, InterruptedException {
        return new GetAllSystems().execute().get();
    }

    public List<System> GetSystems(UUID... Ids) throws ExecutionException, InterruptedException {
        return new GetSystems().execute(Ids).get();
    }

    public Void InsertAll(System... systems) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(systems).get();
    }

    public Void DeleteAll(System... systems) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(systems).get();
    }
}
