import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);
        String filePath = console.nextLine();

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Input error. File doesn't exist");
            return;
        }

        try {
            Scanner fileScanner = new Scanner(file);

            if (!fileScanner.hasNext()) {
                System.out.println("Invalid format of array size");
                return;
            }

            int count;

            try {
                count = Integer.parseInt(fileScanner.next());
            } catch (Exception e) {
                System.out.println("Invalid format of array size");
                return;
            }

            if (count <= 0) {
                System.out.println("Input error. Size <= 0");
                return;
            }

            List<Double> numbers = new ArrayList<>();

            while (fileScanner.hasNext() && numbers.size() < count) {

                if (fileScanner.hasNextDouble()) {
                    numbers.add(fileScanner.nextDouble());
                } else {
                    fileScanner.next();
                }
            }

            if (numbers.size() < count) {
                System.out.println("Input error. Insufficient number of elements");
                return;
            }

            double min = numbers.get(0);
            double max = numbers.get(0);

            for (double num : numbers) {
                if (num < min) {
                    min = num;
                }

                if (num > max) {
                    max = num;
                }
            }

            PrintWriter writer = new PrintWriter("result.txt");
            writer.println(min + " " + max);
            writer.close();

            System.out.println(count);

            for (double num : numbers) {
                System.out.print(num + " ");
            }

            System.out.println();
            System.out.println("Saving min and max values in file");

        } catch (Exception e) {
            System.out.println("Input error");
        }
    }
}