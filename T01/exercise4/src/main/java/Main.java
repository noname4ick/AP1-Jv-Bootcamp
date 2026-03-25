import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]){
        Scanner input = new Scanner(System.in);
        int size;
        boolean isValid = false;
        do{
            int sum = 0;
            int negCount = 0;
        try{
            size = input.nextInt();
        if(size <= 0){
            System.out.println("Input error. Size <= 0");
            continue;
        }
        int[] arr = new int[size];
        for(int i  = 0; i< size; i++){
            arr[i] = input.nextInt();
            if(arr[i] < 0){
                negCount++;
                sum+=arr[i];
            }
        }
        if(negCount == 0){
            System.out.println("Thera are no negative elements");
        }else{
            System.out.println(sum / negCount);
        }
        }catch(InputMismatchException e){
            System.out.println("Could not parse a number. Please try again");
            input.nextLine();
        }
        isValid = true;
        }while(!isValid);
        input.close();
    }
}
