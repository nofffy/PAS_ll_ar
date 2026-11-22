package pasrest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pasrest.model.PlantTypes.Basic;
import pasrest.model.Plant;
import pasrest.model.repositories.PlantRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final PlantRepository plantRepository;

    public DataInitializer(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public void run(String... args) {
        Plant p1 = plantRepository.addPlant(new Basic("se_jem_muchypozdrawiam", "zielony", 89.0));
        Plant p2 = plantRepository.addPlant(new Basic("monsterek-mega-unikat", "green", 22.2));
        Plant p3 = plantRepository.addPlant(new Basic("Kaktus Meksykańska masakra", "purple", 20.0));
        Plant p4 = plantRepository.addPlant(new Basic("do testu usuniecia-test123", "red", 10.0));
        Plant p5 = plantRepository.addPlant(new Basic("halotomaszroslinatwoja", "red", 10.0));
    }
}
