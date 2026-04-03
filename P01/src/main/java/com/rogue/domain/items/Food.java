package com.rogue.domain.items;

public class Food extends Item{

    private int healValue;
    public Food(){
        super(ItemType.FOOD,
                "food");
    }
    public Food(int heal){
        super(
                ItemType.FOOD,
                "food");
        this.healValue = heal;
    }

    public int getHealValue(){

        return healValue;
    }

}