import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        int n = 0;
        boolean check = false;
        do {
            try {
                n = input.nextInt();
                if (n <= 0) {
                    throw new InputMismatchException();
                }
                check = true;
            } catch (InputMismatchException e) {
                System.out.println("Could not parse a number. Please try again");
                input.nextLine();
            }
        }while(!check);
        try{
            int fnumber = fibb(n);
            System.out.println(fnumber);
        }catch(ArithmeticException | StackOverflowError e){
            System.out.println("Too large n");
        }
        input.close();
    }
    private static int fibb( int n) throws ArithmeticException{
        if (n == 1 || n == 2) {
            return 1;
        }
        int a = fibb(n - 1);
        int b = fibb(n - 2);
        int res = a+b;
        if(res < a){
            throw new ArithmeticException("");
        }
        return res;
    }
}
