import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        List<User> users = new ArrayList<>();

        int n;

        try{
            n = Integer.parseInt(scanner.nextLine());
        }
        catch(Exception e){
            return;
        }

        int count = 0;

        while(count < n){

            String name = scanner.nextLine();

            int age;

            try{
                age = Integer.parseInt(scanner.nextLine());
            }
            catch(Exception e){
                System.out.println("Could not parse a number. Please try again");
                continue;
            }

            if(age <= 0){
                System.out.println("Incorrect input. Age <= 0");
                continue;
            }

            users.add(new User(name, age));
            count++;
        }

        List<String> adults =
                users.stream()
                        .filter(user -> user.getAge() >= 18)
                        .map(User::getName)
                        .collect(Collectors.toList());

        for(int i = 0; i < adults.size(); i++){

            System.out.print(adults.get(i));

            if(i != adults.size()-1){
                System.out.print(", ");
            }
        }
    }
}