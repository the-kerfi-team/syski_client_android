package uk.co.syski.client.android.data.threads.system;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.system.System;
import uk.co.syski.client.android.data.threads.system.statements.*;

public class SystemThreads {
    private static final SystemThreads ourInstance = new SystemThreads();

    public static SystemThreads getInstance() {
        return ourInstance;
    }

    private SystemThreads() {}

    private IndexSystems IndexSystems = new IndexSystems();
    private GetSystems GetSystems = new GetSystems();
    private InsertAll InsertAll = new InsertAll();
    private DeleteAll DeleteAll = new DeleteAll();

    public List<System> IndexSystems() throws ExecutionException, InterruptedException {
        return IndexSystems.execute().get();
    }

    public List<System> GetSystems(UUID... Ids) throws ExecutionException, InterruptedException {
        return GetSystems.execute(Ids).get();
    }

    public Void InsertAll(System... systems) throws ExecutionException, InterruptedException {
        return InsertAll.execute(systems).get();
    }

    public Void DeleteAll(System... systems) throws ExecutionException, InterruptedException {
        return DeleteAll.execute(systems).get();
    }
}
