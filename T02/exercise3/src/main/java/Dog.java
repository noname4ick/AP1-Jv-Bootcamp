public class Dog extends Animal implements Omnivore{
    Dog(String name,int age){
        super(name,age);
    }
    public String hunt(){
        return "I can hunt for robbers";
    }
    @Override
    public String toString(){
        return "Dog name = " + this.getName() + ", age = " +this.getAge() +" "+ this.hunt();
    }
}