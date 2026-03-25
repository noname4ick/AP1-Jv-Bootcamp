public class GuineaPig extends Animal implements Herbivore {
    GuineaPig(String name, int age) {
        super(name, age);
    }
    public String chill(){
        return "I can chill for 12 hours";
    }
    @Override
    public String toString(){
        return "GuineaPig name = " + this.getName() + ", age = " +this.getAge() +" "+ this.chill();
    }
}
