import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        int sec = 0;
        String formattedTime = "";
        sec = Input();
        formattedTime = calculatorTime(sec);
        Output(formattedTime);
    }
        private static int Input(){
            int time = 0;
            Scanner input = new Scanner(System.in);
            boolean isValid = false;
            do{
                try{
                    time = input.nextInt();
                    if(time <= 0){
                        System.out.println("Incorrect time");
                    }else{
                        isValid = true;
                    }

                }catch(InputMismatchException e){
                    System.out.println("Could not parse a number. Please try again");
                    input.nextLine();
                }

        }while(!isValid);
        input.close();
        return time;
        }
        private static String calculatorTime(int sec) {
            LocalTime time = LocalTime.MIDNIGHT.plusSeconds(sec);
            return String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
        }
        private static void Output(String line){
            System.out.println(line);
        }
}
