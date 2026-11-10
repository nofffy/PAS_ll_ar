import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import model.UserTypes.Admin;
import model.UserTypes.Client;
import model.UserTypes.Gardener;
import model.Plant;
import model.PlantTypes.Carnivorous;
import model.PlantTypes.Monstera;
import model.PlantTypes.Succulent;
import model.Rent;
import model.User;
import model.repositories.PlantRepository;
import model.repositories.RentRepository;
import model.repositories.UserRepository;

import java.time.LocalDateTime;


@ApplicationScoped
public class DataInitializer {
    @Inject
    private RentRepository rentRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private PlantRepository plantRepository;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        User admin = new Admin("mega-unikat1", true);
        User gardener = new Gardener("garden676767676767", true);
        User client = new Client("client1230987654321", false);
        User inactiveClient = new Client("banned_michael_pirate", false);
        User clientRent = new Client("rentowy_tomasz-999000", true);

        userRepository.addUser(admin);
        userRepository.addUser(gardener);
        userRepository.addUser(client);
        userRepository.addUser(inactiveClient);
        userRepository.addUser(clientRent);

        Plant carnivorous = new Carnivorous("se_jem_muchypozdrawiam", "zielony", 89.0, true, "muchy");
        Plant monstera = new Monstera("monsterek-mega-unikat", "green", 22.2, true, 10);
        Plant kaktus = new Succulent("Kaktus Meksykańska masakra", "purple", 20.0, true, true);
        Plant plant4 = new Monstera("do testu usuniecia-test123", "red", 10.0, true, 5);
        Plant plantRent = new Monstera("halotomaszroslinatwoja", "red", 10.0, true, 5);

        plantRepository.addPlant(carnivorous);
        plantRepository.addPlant(monstera);
        plantRepository.addPlant(kaktus);
        plantRepository.addPlant(plant4);
        plantRepository.addPlant(plantRent);

        Rent activeRent = new Rent(client, monstera, LocalDateTime.now().minusDays(1));
        rentRepository.addRent(activeRent);

        Rent pastRent = new Rent(client, kaktus, LocalDateTime.now().minusDays(10));
        rentRepository.addRent(pastRent);
        rentRepository.finishRent(pastRent.getId());
    }
}
