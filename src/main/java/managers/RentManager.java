package managers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.Rent;
import model.repositories.MemoryRepositories.MemoryRentRepository;
import java.util.List;

@ApplicationScoped
public class RentManager {

    @Inject
    private MemoryRentRepository mrr;

    public Rent getRent(Long id) { return mrr.getRent(id); }

    public void createRent(Rent rent) { mrr.addRent(rent); }

    public void finishRent(Long id) { mrr.finishRent(id); }

    public void removeRent(Long id) { mrr.removeRent(id); }

    public List<Rent> getPastRentsByClientId(Long id) { return mrr.getPastRentsByClientId(id); }

    public List<Rent> getCurrentRentsByClientId(Long id) { return mrr.getCurrentRentsByClientId(id); }

    public List<Rent> getPastRentsByPlantId(Long id) { return mrr.getPastRentsByPlantId(id); }

    public List<Rent> getCurrentRentsByPlantId(Long id) { return mrr.getCurrentRentsByPlantId(id); }

    public List<Rent> getRents() { return mrr.getRents(); }
}
