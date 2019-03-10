package uk.co.syski.client.android.data.repository;

public class Repository {

    private static SystemRepository mSystemRepository = new SystemRepository();

    public static SystemRepository getSystemRepository()
    {
        return mSystemRepository;
    }

}
