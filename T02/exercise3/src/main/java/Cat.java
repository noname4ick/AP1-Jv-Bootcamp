public class Cat extends Animal implements Omnivore{
    Cat(String name, int age){
        super(name,age);
    }
    public String hunt(){
        return "I can hunt for mice";
    }
    @Override
    public String toString(){
        return "Cat name = " + this.getName() + ", age = " +this.getAge() +" "+ this.hunt();
    }
}
