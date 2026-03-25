import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int sizeAnimal = getSizeAnimal(scanner);
        List<Animal> pets = IntStream.range(0, sizeAnimal)
                .mapToObj(i -> createAnimal(scanner))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        ExecutorService executor = Executors.newFixedThreadPool(pets.size());
        List<Future<String>> futures = new ArrayList<>();

        long baseTime = System.currentTimeMillis();

        for (Animal pet : pets) {
            futures.add(executor.submit(() -> {
                long startTimeMillis = System.currentTimeMillis() - baseTime;
                double walkTime = pet.goToWalk();

                try {
                    TimeUnit.SECONDS.sleep((long) walkTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                long endTimeMillis = System.currentTimeMillis() - baseTime;

                double startTime = startTimeMillis / 1000.0;
                double endTime = endTimeMillis / 1000.0;

                return String.format("%s name = %s, age = %d, start time = %.2f, end time = %.2f",
                        pet.getClass().getSimpleName(), pet.getName(), pet.getAge(), startTime, endTime);
            }));
        }

        futures.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        scanner.close();
    }

    private static Animal createAnimal(Scanner scanner) {
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
            return null;
        }
        return animal.equals("dog") ? new Dog(name, age) : new Cat(name, age);
    }

    private static int getSizeAnimal(Scanner scanner) {
        while (true) {
            try {
                int size = Integer.parseInt(scanner.nextLine().trim());
                if (size > 0) {
                    return size;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}