import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = getValidInteger(scanner);

        List<Animal> pets = IntStream.range(0, count)
                .mapToObj(i -> createAnimal(scanner))
                .filter(Objects::nonNull)
                .toList();

        pets.forEach(System.out::println);

        scanner.close();
    }

    private static int getValidInteger(Scanner scanner) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) return value;
            } catch (NumberFormatException ignored) {}
        }
    }

    private static Animal createAnimal(Scanner scanner) {
        String type = scanner.nextLine().trim().toLowerCase();
        if (!type.equals("dog") && !type.equals("cat")) {
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
            } else if (age > 10) {
                age++;
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return type.equals("dog") ? new Dog(name, age) : new Cat(name, age);
    }
}