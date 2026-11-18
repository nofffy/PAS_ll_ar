package pasrest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pasrest.model.UserTypes.Admin;
import pasrest.model.UserTypes.Client;
import pasrest.model.UserTypes.Gardener;
import pasrest.model.Plant;
import pasrest.model.PlantTypes.Carnivorous;
import pasrest.model.PlantTypes.Monstera;
import pasrest.model.PlantTypes.Succulent;
import pasrest.model.Rent;
import pasrest.model.User;
import pasrest.model.repositories.PlantRepository;
import pasrest.model.repositories.RentRepository;
import pasrest.model.repositories.UserRepository;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public DataInitializer(RentRepository rentRepository, UserRepository userRepository, PlantRepository plantRepository) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    @Override
    public void run(String... args) {
        User admin = userRepository.addUser(new Admin("mega-unikat1", true));
        User gardener = userRepository.addUser(new Gardener("garden676767676767", true));
        User client = userRepository.addUser(new Client("client1230987654321", false));
        User inactiveClient = userRepository.addUser(new Client("banned_michael_pirate", false));
        User clientRent = userRepository.addUser(new Client("rentowy_tomasz-999000", true));

        Plant carnivorous = plantRepository.addPlant(new Carnivorous("se_jem_muchypozdrawiam", "zielony", 89.0, "muchy"));
        Plant monstera = plantRepository.addPlant(new Monstera("monsterek-mega-unikat", "green", 22.2, 10));
        Plant kaktus = plantRepository.addPlant(new Succulent("Kaktus Meksykańska masakra", "purple", 20.0, true));
        Plant plant4 = plantRepository.addPlant(new Monstera("do testu usuniecia-test123", "red", 10.0, 5));
        Plant plantRent = plantRepository.addPlant(new Monstera("halotomaszroslinatwoja", "red", 10.0, 5));

        Rent activeRent = rentRepository.addRent(new Rent(client, monstera, LocalDateTime.now().minusDays(1)));
        Rent pastRent = rentRepository.addRent(new Rent(client, kaktus, LocalDateTime.now().minusDays(10)));
        rentRepository.finishRent(pastRent.getId());
    }
}
