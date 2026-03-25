import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.Math;
public class Main {
    public static void main(String args[]){
        double aX,bX,cX;
        double aY,bY,cY;
        boolean isValidInput = false;
        Scanner input = new Scanner(System.in);
        do{
            try {
                aX = input.nextDouble();
                aY = input.nextDouble();
                bX = input.nextDouble();
                bY = input.nextDouble();
                cX = input.nextDouble();
                cY = input.nextDouble();

                double d1 = CalculateLength(aX, bX, aY, bY);
                double d2 = CalculateLength(cX, bX, cY, bY);
                double d3 = CalculateLength(aX, cX, aY, cY);

                if (!checkValidTriangle(d1, d2, d3)) {
                    System.out.println("It's not triangle");
                    continue;
                }

                double perimeter = d1 + d2 + d3;
                System.out.printf("Perimeter: %.3f", perimeter);
                isValidInput = true;
                }
            catch (InputMismatchException e){
                System.out.println("Couldn't parse a number, please retry");
                input.nextLine();
            }
            }while(!isValidInput);
        input.close();
    }
    private static double CalculateLength(double x1,double x2,double y1,double y2){
        double len = Math.sqrt(Math.pow((x1 - x2),2)+Math.pow((y2 - y1),2));
        return len;
    }
    private static boolean checkValidTriangle(double A,double B,double C){
        if(A+B > C && A+C>B && B+C>A){
            return true;
        }
        else return false;
    }
}