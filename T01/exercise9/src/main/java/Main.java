import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int count = input.nextInt();
        input.nextLine();
        List<String> list = new ArrayList<>();
        for(int i = 0; i < count;i++){
            list.add(input.nextLine());
        }
        String sub = input.nextLine();
        List<String> res = filter(list,sub);
        for(int i = 0; i < res.size(); i++){

            System.out.print(res.get(i));

            if(i != res.size()-1){
                System.out.print(", ");
            }
        }
    }
    public static List<String> filter(List<String> list, String sub){
        List<String> result = new ArrayList<>();
        for(String str : list){
            if(str.contains(sub)){
                result.add(str);
            }
        }
        return result;
    }
}
