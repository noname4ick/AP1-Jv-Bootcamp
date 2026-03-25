import java.util.Scanner;

public class Main {
    public static void main(String[] args){

        Scanner input = new Scanner(System.in);

        int previous, current;
        int position = 0;
        boolean isValid = true;

        if(!input.hasNextInt()){
            System.out.println("Input error");
            return;
        }

        previous = input.nextInt();
        position = 1;

        while(input.hasNextInt()){

            current = input.nextInt();
            position++;

            if(current < previous){
                System.out.println("The sequence is not ordered from the ordinal number of the number " + current);
                isValid = false;
                break;
            }

            previous = current;
        }

        if(isValid){
            System.out.println("The sequence is ordered in ascending order");
        }
    }
}