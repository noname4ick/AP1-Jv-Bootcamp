import java.util.concurrent.TimeUnit;

public class Cat extends Animal{
    Cat(String name, int age){
        super(name,age);
    }
    @Override
    public String toString(){
        return "Cat name = " + this.getName() + ", age = " + this.getAge();
    }
    public double goToWalk() throws InterruptedException{
        double time = getAge()*0.25;
        TimeUnit.SECONDS.sleep((long)time);
        return time;
    }
}
