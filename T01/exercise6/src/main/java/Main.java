import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int count = getInput(input);
        double[] arr = new double[count];
        addNumberToArray(arr,input);
        selectionSort(arr);
        print(arr);
    }

    public static void selectionSort(double[] result) {
        int size = result.length;

        for (int i = 0; i < size; i++) {
            int minIndex = i;

            for (int j = i + 1; j < size; j++) {
                if (result[j] < result[minIndex]) {
                    minIndex = j;
                }
            }

            double temp = result[i];
            result[i] = result[minIndex];
            result[minIndex] = temp;
        }
    }
    private static void print(double[] arr){
        for (double v : arr) {
            System.out.print(v + " ");
        }
    }

    private static int getInput(Scanner input){
        boolean isEverythingOk = false;
        int count = 0;
        do{
            try{
                count = input.nextInt();
                if(count <= 0){
                    System.out.println("Input error. Size <= 0.");
                    continue;
                }
                isEverythingOk = true;
            }catch (InputMismatchException e){
                System.out.println("Could not parse a number. Please try again");
                input.nextLine();
            }
        }while(!isEverythingOk);
        return count;
    }

    private static void addNumberToArray(double[] arr, Scanner input){
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            boolean valid = false;
            while(!valid){

                try {
                    arr[i] = input.nextDouble();
                    valid = true;
                }catch(InputMismatchException e) {
                        System.out.println("Couldn't parse a number. Please, try again");
                        input.nextLine();
                }
            }
        }
    }
}
