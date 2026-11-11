package db.repositories.Memory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;


@ApplicationScoped
@Typed(PlantLockManager.class)
public class PlantLockManager {
    private final Object[] locks = new Object[256];

    public PlantLockManager() {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new Object();
        }
    }

    public Object getLock(String plantId) {
        if (plantId == null) return locks[0];

        int index = Math.abs(plantId.hashCode() % locks.length);
        return locks[index];
    }
}