import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        boolean check  = false;
        Scanner input = new Scanner(System.in);
        do{
            try{
                int n = input.nextInt();
                int count = 0;
                if(n <= 0){
                    System.out.println("Input error. Size <= 0.");
                    continue;
                }
                int[] arr = new int[n];
                int i = 0;
                while(i < n){
                    arr[i] = input.nextInt();
                    if(isRepeated(arr[i])){
                        count++;
                    }
                    i++;
                }
                if(count == 0){
                    System.out.println("There are no such items.");
                }else{
                    int[] arr2 = new int[count];
                    i = 0;
                    int j = 0;
                    while(i<n){
                        if(isRepeated(arr[i])){
                            arr2[j++] = arr[i];
                        }
                        i++;
                    }
                    i = 0;
                    while(i<count){
                        System.out.print(arr2[i]+" ");
                        i++;
                    }
                }
                check = true;
            }catch (InputMismatchException e){
                System.out.println("Could not parse a number. Please try again");
                input.nextLine();
            }
        }while(!check);
        input.close();
    }
    private static boolean isRepeated(int n){
        int tail = n % 10;
        int head = 0;
        while(n != 0){
            head = n % 10;
            n = n / 10;
        }
        return head == tail;
    }

}
