package uk.co.syski.client.android.model.repository;

public enum Repository {
    INSTANCE;

    public static Repository getInstance() {
        return INSTANCE;
    }

    private UserRepository mUserRepository = new UserRepository();

    public synchronized UserRepository getUserRepository()
    {
        return mUserRepository;
    }

    public synchronized SystemRepository getSystemRepository()
    {
        return SystemRepository.INSTANCE;
    }

    public synchronized CPURepository getCPURepository()
    {
        return CPURepository.INSTANCE;
    }

    public synchronized GPURepository getGPURepository()
    {
        return GPURepository.INSTANCE;
    }

    public synchronized RAMRepository getRAMRepository()
    {
        return RAMRepository.INSTANCE;
    }

    public synchronized StorageRepository getStorageRepository()
    {
        return StorageRepository.INSTANCE;
    }

    public synchronized MOBORepository getMOBORepository()
    {
        return MOBORepository.INSTANCE;
    }

    public synchronized BIOSRepository getBIOSRepository()
    {
        return BIOSRepository.INSTANCE;
    }

    public synchronized OSRepository getOSRepository()
    {
        return OSRepository.INSTANCE;
    }

}
