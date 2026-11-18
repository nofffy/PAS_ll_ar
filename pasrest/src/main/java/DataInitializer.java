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
