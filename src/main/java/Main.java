//do testowania :))

import model.Plant;
import model.PlantTypes.Monstera;

public class Main {
    public static void main(String[] args) {
        Plant plant = new Monstera("monsterek", "green", 22.2, 10);
        System.out.println(plant.getInfo());
    }
}
