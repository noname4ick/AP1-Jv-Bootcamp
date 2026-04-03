package com.rogue.domain.game;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


public class MessageLog {

    private final Deque<String> recent =
            new ArrayDeque<>();

    private static final int KEEP =
            8;

    public void add(
            String message){

        if(message == null
                || message.isBlank()){

            return;
        }

        recent.addLast(
                message.trim());

        while(recent.size() > KEEP){

            recent.removeFirst();
        }
    }


    public String getLast(){

        return recent.isEmpty()
                ? ""
                : recent.getLast();
    }

    public List<String> getRecent(){

        return List.copyOf(
                recent);
    }

    public void clear(){

        recent.clear();
    }

}
