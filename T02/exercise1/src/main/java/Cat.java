public class Cat extends Animal {
    Cat(String name,int age,double weight){
        super(name,age,weight);
    }
    public double getFeedInfoKg(){
        return ((this.getWeight() * 0.1)*100) / 100;
    }
    @Override
    public String toString(){
        return "Cat name = " + this.getName() + ", age = " + this.getAge() + ",  mass = " + this.getWeight() + ", feed = " + this.getFeedInfoKg();
    }
}
