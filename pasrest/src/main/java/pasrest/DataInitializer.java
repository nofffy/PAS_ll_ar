package pasrest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pasrest.model.PlantTypes.Basic;
import pasrest.model.Plant;
import pasrest.model.User;
import pasrest.model.UserTypes.Admin;
import pasrest.model.repositories.PlantRepository;
import pasrest.model.repositories.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public DataInitializer(PlantRepository plantRepository, UserRepository userRepository) {
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        Plant p1 = plantRepository.addPlant(new Basic("se_jem_muchypozdrawiam", "zielony", 89.0));
        Plant p2 = plantRepository.addPlant(new Basic("monsterek-mega-unikat", "green", 22.2));
        Plant p3 = plantRepository.addPlant(new Basic("Kaktus Meksykańska masakra", "purple", 20.0));
        Plant p4 = plantRepository.addPlant(new Basic("do testu usuniecia-test123", "red", 10.0));
        Plant p5 = plantRepository.addPlant(new Basic("halotomaszroslinatwoja", "red", 10.0));

        //haslo to 12345
        if (userRepository.getUserByLogin("lukasz") == null) {
            User u1 = userRepository.addUser(new Admin("lukasz", "$2a$12$VNxSem56.0Gc/iYSzClv5OMrjua8SmlpEPnEmfcBFegGsGKlLIYRO", "l@l.com", 21, true));
        }
        if (userRepository.getUserByLogin("aleksander") == null) {
            User u2 = userRepository.addUser(new Admin("aleksander", "$2a$12$VNxSem56.0Gc/iYSzClv5OMrjua8SmlpEPnEmfcBFegGsGKlLIYRO", "a@a.com", 21, true));
        }
    }
}
