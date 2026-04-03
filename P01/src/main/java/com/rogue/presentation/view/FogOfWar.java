package com.rogue.presentation.view;

import com.rogue.domain.entities.Player;
import com.rogue.domain.entities.Position;
import com.rogue.domain.map.Level;
import com.rogue.domain.map.Room;
import com.rogue.domain.map.Tile;
import com.rogue.domain.map.TileType;

public class FogOfWar {

    public boolean[][] computeVisible(
            Level level,
            Player player){

        int h =
                level.getMap().length;

        int w =
                level.getMap()[0].length;

        boolean[][] vis =
                new boolean[h][w];

        Position pp =
                player.getPosition();

        Room pr =
                level.findRoomContaining(
                        pp);

        for(int y = 0;
            y < h;
            y++){

            for(int x = 0;
                x < w;
                x++){

                Position t =
                        new Position(
                                x,
                                y);

                if(pr != null
                        && pr.contains(t)){

                    vis[y][x] = true;
                }
                else{

                    vis[y][x] =
                            hasLineOfSight(
                                    level,
                                    pp.getX(),
                                    pp.getY(),
                                    x,
                                    y);
                }
            }
        }

        return vis;
    }

    public void reveal(
            Level level,
            boolean[][] vis){

        Tile[][] map =
                level.getMap();

        int h = map.length;

        int w = map[0].length;

        for(int y = 0;
            y < h;
            y++){

            for(int x = 0;
                x < w;
                x++){

                if(vis[y][x]){

                    map[y][x]
                            .setExplored(
                                    true);
                }
            }
        }
    }

    private boolean hasLineOfSight(
            Level level,
            int x0,
            int y0,
            int x1,
            int y1){

        int x = x0;

        int y = y0;

        int dx =
                Math.abs(
                        x1 - x0);

        int dy =
                Math.abs(
                        y1 - y0);

        int sx =
                x0 < x1
                        ? 1
                        : -1;

        int sy =
                y0 < y1
                        ? 1
                        : -1;

        int err =
                dx - dy;

        int guard = 0;

        while(guard++ < 500){

            if(x == x1
                    && y == y1){

                return true;
            }

            if(x != x0
                    || y != y0){

                TileType tt =
                        level.getMap()[y][x]
                                .getType();

                if(tt == TileType.WALL
                        || tt == TileType.DOOR){

                    return false;
                }
            }

            int e2 =
                    2 * err;

            if(e2 > -dy){

                err -= dy;

                x += sx;
            }

            if(e2 < dx){

                err += dx;

                y += sy;
            }
        }

        return false;
    }

}
