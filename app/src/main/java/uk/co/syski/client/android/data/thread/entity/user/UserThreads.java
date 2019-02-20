package uk.co.syski.client.android.data.thread.entity.user;

import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.User;
import uk.co.syski.client.android.data.thread.entity.user.statement.InsertAll;

public class UserThreads {
    private static final UserThreads ourInstance = new UserThreads();

    public static UserThreads getInstance() {
        return ourInstance;
    }

    private UserThreads() {}

    public Void InsertAll(User... users) throws ExecutionException, InterruptedException {
        return new InsertAll().execute(users).get();
    }

}
