package com.rogue.domain.map;

import com.rogue.domain.entities.Enemy;
import com.rogue.domain.entities.Player;
import com.rogue.domain.entities.Position;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private Tile[][] map;

    private List<Room> rooms;

    private List<Corridor> corridors;

    private List<Enemy> enemies;

    private Player player;

    private Position exit;

    public Level(int width,int height){

        map = new Tile[height][width];

        rooms = new ArrayList<>();

        corridors = new ArrayList<>();

        enemies = new ArrayList<>();

        for(int y=0;y<height;y++){

            for(int x=0;x<width;x++){

                map[y][x] = new Tile(TileType.WALL);
            }
        }
    }

    public Tile[][] getMap(){

        return map;
    }

    public List<Room> getRooms(){

        return rooms;
    }

    public List<Enemy> getEnemies(){

        return enemies;
    }

    public List<Corridor> getCorridors(){

        return corridors;
    }

    public void addCorridor(
            Corridor corridor){

        corridors.add(
                corridor);
    }

    /**
     * Marks every tile in the room as explored (e.g. after the player leaves).
     */
    public void exploreRoom(
            Room room){

        for(int y = room.getY();
            y < room.getY()
                    + room.getHeight();
            y++){

            for(int x = room.getX();
                x < room.getX()
                        + room.getWidth();
                x++){

                if(y >= 0
                        && y < map.length
                        && x >= 0
                        && x < map[0].length){

                    map[y][x]
                            .setExplored(
                                    true);
                }
            }
        }
    }

    public void setPlayer(Player p){

        player = p;
    }

    public Player getPlayer(){

        return player;
    }

    public void setExit(Position pos){

        exit = pos;

        map[pos.getY()][pos.getX()].setType(TileType.EXIT);
    }

    public Position getExit(){

        return exit;
    }

    public boolean isWalkable(int x,int y){

        TileType t = map[y][x].getType();

        return t == TileType.FLOOR ||
                t == TileType.CORRIDOR ||
                t == TileType.EXIT;
    }

    public Room findRoomContaining(
            Position p){

        for(Room room :
                rooms){

            if(room.contains(p)){

                return room;
            }
        }

        return null;
    }

}