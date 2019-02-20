package uk.co.syski.client.android.data.thread.entity.linking.systemtype;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.linking.SystemType;
import uk.co.syski.client.android.data.thread.entity.linking.systemtype.statement.InsertAll;

public class SystemTypeThreads {
    private static final SystemTypeThreads ourInstance = new SystemTypeThreads();

    public static SystemTypeThreads getInstance() {
        return ourInstance;
    }

    private SystemTypeThreads() {}

    public Void InsertAll(SystemType... SystemTypes) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(SystemTypes).get();
    }
}
