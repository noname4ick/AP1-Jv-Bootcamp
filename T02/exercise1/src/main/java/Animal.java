public abstract class Animal {
    private String name;
    private int age;
    private double weight;
    Animal(String name, int age, double weight){
        this.name = name;
        this.age = age;
        this.weight = weight;
    }
    public String getName(){
        return this.name;
    }
    public int getAge(){
        return this.age;
    }
    public double getWeight(){
        return this.weight;
    }
    public double getFeedInfoKg(){
        return 0;
    }
}