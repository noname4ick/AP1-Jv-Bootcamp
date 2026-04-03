package com.rogue.domain.inventory;

import com.rogue.domain.items.Item;
import com.rogue.domain.items.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Backpack {

    private Map<ItemType,List<Item>> items;

    private static final int LIMIT = 9;

    public Backpack(){

        items = new HashMap<>();

        for(ItemType type: ItemType.values()){

            items.put(type,new ArrayList<>());
        }
    }

    public boolean addItem(Item item){

        List<Item> list = items.get(item.getType());

        if(list.size() >= LIMIT){

            return false;
        }

        list.add(item);

        return true;
    }

    public List<Item> getItems(ItemType type){

        return items.get(type);
    }

    public Item remove(ItemType type,int index){

        List<Item> list = items.get(type);

        if(index >= list.size()){

            return null;
        }

        return list.remove(index);
    }

    public boolean isFull(ItemType type){

        return items.get(type).size() >= LIMIT;
    }

    public void clearAll(){

        for(ItemType type :
                ItemType.values()){

            items.get(type)
                    .clear();
        }
    }

}