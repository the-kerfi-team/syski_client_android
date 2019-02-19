package uk.co.syski.client.android.data.thread.entity.type;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.Type;
import uk.co.syski.client.android.data.thread.entity.type.statement.*;

public class TypeThreads {
    private static final TypeThreads ourInstance = new TypeThreads();

    public static TypeThreads getInstance() {
        return ourInstance;
    }

    private TypeThreads() {}

    public List<String> GetTypeNames(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetTypeNames().execute(uuids).get();
    }

    public Void InsertAll(Type... types) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(types).get();
    }

    public Void DeleteAll(Type... types) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(types).get();
    }
}
