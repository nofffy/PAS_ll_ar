package db;

import db.repositories.Memory.MemoryPlantRepository;
import db.repositories.Memory.MemoryRentRepository;
import db.repositories.Memory.MemoryUserRepository;
import db.repositories.Mongo.MongoPlantRepository;
import db.repositories.Mongo.MongoRentRepository;
import db.repositories.Mongo.MongoUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import model.repositories.PlantRepository;
import model.repositories.RentRepository;
import model.repositories.UserRepository;

@ApplicationScoped
public class RepositoryFactory {
    //mongo lub memory
    private final String ACTIVE_DB = "mongo";

    @Inject
    private MongoPlantRepository mongoPlantRepo;

    @Inject
    private MemoryPlantRepository memoryPlantRepo;

    @Inject
    private MongoUserRepository mongoUserRepo;

    @Inject
    private MemoryUserRepository memoryUserRepo;

    @Inject
    private MongoRentRepository mongoRentRepo;

    @Inject
    private MemoryRentRepository memoryRentRepo;

    @Produces
    public PlantRepository producePlantRepository() {
        return "memory".equals(ACTIVE_DB) ? memoryPlantRepo : mongoPlantRepo;
    }

    @Produces
    public UserRepository produceUserRepository() {
        return "memory".equals(ACTIVE_DB) ? memoryUserRepo : mongoUserRepo;
    }

    @Produces
    public RentRepository produceRentRepository() {
        return "memory".equals(ACTIVE_DB) ? memoryRentRepo : mongoRentRepo;
    }
}
