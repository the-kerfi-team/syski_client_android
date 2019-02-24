package uk.co.syski.client.android.data.thread.entity.motherboard;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.MotherboardEntity;
import uk.co.syski.client.android.data.thread.entity.motherboard.statement.*;

public class MotherboardThreads {
    private static final MotherboardThreads ourInstance = new MotherboardThreads();

    public static MotherboardThreads getInstance() {
        return ourInstance;
    }

    private MotherboardThreads() {}

    public List<MotherboardEntity> GetMotherboards(UUID... uuids) throws ExecutionException, InterruptedException {
        return new GetMotherboards().execute(uuids).get();
    }

    public Void InsertAll(MotherboardEntity... motherboardEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(motherboardEntities).get();
    }

    public Void DeleteAll(MotherboardEntity... motherboardEntities) throws ExecutionException, InterruptedException {
        return new DeleteAll().execute(motherboardEntities).get();
    }

}
