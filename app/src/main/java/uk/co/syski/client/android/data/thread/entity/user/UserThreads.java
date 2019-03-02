package uk.co.syski.client.android.data.thread.entity.user;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.UserEntity;
import uk.co.syski.client.android.data.thread.entity.user.statement.InsertAll;
import uk.co.syski.client.android.data.thread.entity.user.statement.UserAccessToken;
import uk.co.syski.client.android.data.thread.entity.user.statement.UserCount;

public class UserThreads {
    private static final UserThreads ourInstance = new UserThreads();

    public static UserThreads getInstance() {
        return ourInstance;
    }

    private UserThreads() {}

    public boolean HasData() throws ExecutionException, InterruptedException {
        return (new UserCount().execute().get() > 0);
    }

    public String GetAccessToken() throws ExecutionException, InterruptedException {
        return new UserAccessToken().execute().get();
    }

    public Void InsertAll(UserEntity... userEntities) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(userEntities).get();
    }

}
