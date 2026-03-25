import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Animal> pets = new ArrayList<>();
        int sizeAnimal = 0;

        while (true) {
            try {
                sizeAnimal = Integer.parseInt(scanner.nextLine().trim());
                if (sizeAnimal <= 0) {
                    System.out.println("Couldn't parse a number. Please, try again");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Couldn't parse a number. Please, try again");
            }
        }

        for (int i = 0; i < sizeAnimal; i++) {
            String animal = scanner.nextLine().trim().toLowerCase();

            if (!(animal.equals("dog") || animal.equals("cat") || animal.equals("guinea") || animal.equals("hamster"))) {
                System.out.println("Incorrect input. Unsupported pet type");
                continue;
            }
            String name = scanner.nextLine().trim();

            int age;
            try {
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age <= 0) {
                    System.out.println("Incorrect input. Age <= 0");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Couldn't parse a number. Please, try again");
                continue;
            }
            //adding to the list
            switch (animal){
                case "dog" : pets.add(new Dog(name, age));
                break;
                case "cat" : pets.add(new Cat(name, age));
                break;
                case "guinea" : pets.add(new GuineaPig(name, age));
                break;
                case "hamster" : pets.add(new Hamster(name, age));
                break;
            }
        }

        Collections.sort(pets, (a1, a2) -> {
            boolean a1Herbivore = a1 instanceof Herbivore;
            boolean a2Herbivore = a2 instanceof Herbivore;
            return Boolean.compare(a2Herbivore, a1Herbivore);
        });

        System.out.println();
        for (Animal pet : pets) {
            System.out.println(pet);
        }

        scanner.close();
    }
}