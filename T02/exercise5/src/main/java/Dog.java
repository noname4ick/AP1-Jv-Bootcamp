import java.util.concurrent.TimeUnit;

public class Dog extends Animal{
    Dog(String name, int age){
        super(name,age);
    }
    @Override
    public String toString(){
        return "Dog name = " + this.getName() + ", age = " + this.getAge();
    }
    public double goToWalk() throws InterruptedException{
        double time = getAge()*0.5;
        TimeUnit.SECONDS.sleep((long)time);
        return time;
    }
}
