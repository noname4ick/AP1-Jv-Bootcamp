public class Dog extends Animal {
    Dog(String name,int age,double weight){
        super(name,age,weight);
    }
    public double getFeedInfoKg(){
        return ((this.getWeight() * 0.3)*100) / 100;
    }
    @Override
    public String toString(){
        return "Dog name = " + this.getName() + ", age = " + this.getAge() + ",  mass = " + this.getWeight() + ", feed = " + this.getFeedInfoKg();
    }
}
