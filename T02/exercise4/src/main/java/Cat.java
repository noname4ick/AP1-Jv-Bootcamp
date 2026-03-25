public class Cat extends Animal{
    Cat(String name,int age){
        super(name,age);
    }
    @Override
    public String toString(){
        return "Cat name = " + this.getName() + ", age = " + this.getAge();
    }
}
