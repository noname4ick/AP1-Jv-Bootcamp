import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Animal> pets = new ArrayList<>();
        AnimalIterator iterator = new AnimalIterator(pets);

        int sizePets;

        while (true) {
            try {
                sizePets = Integer.parseInt(scanner.nextLine().trim());
                if (sizePets <= 0) {
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Couldn't parse a number. Please, try again");
            }
        }

        for(int i = 0; i < sizePets; i++) {
            Animal animal = createAnimal(scanner);
            if (animal != null) {
                pets.add(animal);
            }
        }

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        iterator.reset();
    }

    private static Animal createAnimal (Scanner scanner){
        String animal = scanner.nextLine().trim().toLowerCase();
        if (!animal.equals("dog") && !animal.equals("cat")) {
            System.out.println("Incorrect input. Unsupported pet type");
            return null;
        }

        String name = scanner.nextLine().trim();
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
            if (age <= 0) {
                System.out.println("Incorrect input. Age <= 0");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Couldn't parse a number. Please, try again");
            return null;
        }
        return animal.equals("dog") ? new Dog(name, age) : new Cat(name, age);
    }
}