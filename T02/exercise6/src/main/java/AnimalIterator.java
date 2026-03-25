import java.util.List;

public class AnimalIterator implements BaseIterator{
    private List<Animal> pets;
    private int index;
    AnimalIterator(List<Animal> pets){
        this.pets = pets;
        this.index = 0;
    }
    @Override
    public Object next(){
        return pets.get(index++);
    }
    @Override
    public boolean hasNext(){
        return index < pets.size();
    }
    @Override
    public void reset(){
        index = 0;
    }
}
