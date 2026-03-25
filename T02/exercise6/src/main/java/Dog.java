public class Dog extends Animal{
    Dog(String name, int age){
        super(name,age);
    }
    @Override
    public String toString(){
        return "Dog name = " + getName() + ", age = " + getAge();
    }
}