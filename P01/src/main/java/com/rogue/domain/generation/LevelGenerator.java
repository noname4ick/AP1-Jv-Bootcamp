package com.rogue.domain.generation;

import com.rogue.domain.entities.Position;
import com.rogue.domain.items.Key;
import com.rogue.domain.map.*;

import java.util.*;

public class LevelGenerator {

    private static final int MAP_WIDTH = 80;

    private static final int MAP_HEIGHT = 20;

    private Random random;

    public LevelGenerator(Random random){

        this.random = random;
    }

    public Level generate(
            int levelIndex,
            long seed){

        long attemptSeed = seed;

        for(int attempt = 0;
            attempt < 50;
            attempt++){

            Random rng =
                    new Random(
                            attemptSeed
                                    ^ Long.rotateLeft(
                                    levelIndex,
                                    32));

            Level level =
                    new Level(
                            MAP_WIDTH,
                            MAP_HEIGHT);

            generateRooms(
                    level,
                    rng);

            connectRooms(
                    level,
                    rng);

            if(isConnected(level)){

                placeExit(level);

                placeDoors(level, rng);

                return level;
            }

            attemptSeed++;
        }

        throw new IllegalStateException(
                "Could not generate a connected level");
    }

    private void generateRooms(
            Level level,
            Random rng){

        int mapW = MAP_WIDTH;

        int mapH = MAP_HEIGHT;

        int secW = mapW / 3;

        int secH = mapH / 3;

        for(int i = 0;
            i < 9;
            i++){

            int sx =
                    (i % 3)
                            * secW;

            int sy =
                    (i / 3)
                            * secH;

            int pad = 1;

            int maxW =
                    secW - pad * 2;

            int maxH =
                    secH - pad * 2;

            int w =
                    5 + rng.nextInt(
                            Math.max(
                                    1,
                                    maxW - 5));

            int h =
                    3 + rng.nextInt(
                            Math.max(
                                    1,
                                    maxH - 3));

            int x =
                    sx + pad
                            + rng.nextInt(
                            Math.max(
                                    1,
                                    secW - w - pad));

            int y =
                    sy + pad
                            + rng.nextInt(
                            Math.max(
                                    1,
                                    secH - h - pad));

            Room room =
                    new Room(
                            x,
                            y,
                            w,
                            h);

            level.getRooms()
                    .add(room);

            carveRoom(
                    level,
                    room);
        }
    }

    private void carveRoom(
            Level level,
            Room room){

        for(int y = room.getY();
            y < room.getY()
                    + room.getHeight();
            y++){

            for(int x = room.getX();
                x < room.getX()
                        + room.getWidth();
                x++){

                level.getMap()[y][x]
                        .setType(
                                TileType.FLOOR);
            }
        }
    }
    private void connectRooms(
            Level level,
            Random rng){

        List<Room> rooms =
                level.getRooms();

        for(int i = 0;
            i < rooms.size()-1;
            i++){

            Position a =
                    edgePoint(
                            rooms.get(i),
                            rooms.get(i+1));

            Position b =
                    edgePoint(
                            rooms.get(i+1),
                            rooms.get(i));

            Position mid =
                    randomMidpoint(a,b,rng);

            carveCorridor(level,a,mid);

            carveCorridor(level,mid,b);
        }
    }

    private Position roomInterior(
            Room room,
            Random rng){

        int x =
                room.getX()
                        + 1
                        + rng.nextInt(
                        Math.max(
                                1,
                                room.getWidth()
                                        - 2));

        int y =
                room.getY()
                        + 1
                        + rng.nextInt(
                        Math.max(
                                1,
                                room.getHeight()
                                        - 2));

        return new Position(
                x,
                y);
    }

    private Position randomMidpoint(
            Position a,
            Position b,
            Random rng){

        if(rng.nextBoolean()){

            return new Position(
                    b.getX(),
                    a.getY());
        }

        return new Position(
                a.getX(),
                b.getY());
    }
    private void carveCorridor(
            Level level,
            Position a,
            Position b){

        List<Position> cells =
                new ArrayList<>();

        int x = a.getX();

        int y = a.getY();

        while(x != b.getX()){

            level.getMap()[y][x]
                    .setType(TileType.CORRIDOR);

            cells.add(
                    new Position(x,y));

            x += Integer.compare(
                    b.getX(),
                    x);
        }

        while(y != b.getY()){

            level.getMap()[y][x]
                    .setType(TileType.CORRIDOR);

            cells.add(
                    new Position(x,y));

            y += Integer.compare(
                    b.getY(),
                    y);
        }

        // VERY IMPORTANT FIX
        level.getMap()[y][x]
                .setType(TileType.CORRIDOR);

        cells.add(
                new Position(x,y));

        if(!cells.isEmpty()){

            level.addCorridor(
                    new Corridor(
                            List.copyOf(cells)));
        }
    }


    private boolean isConnected(Level level){

        Tile[][] map =
                level.getMap();

        int h = map.length;

        int w = map[0].length;

        boolean[][] seen =
                new boolean[h][w];

        Position start =
                level.getRooms()
                        .get(0)
                        .center();

        ArrayDeque<Position> q =
                new ArrayDeque<>();

        q.add(start);

        seen[start.getY()][start.getX()] =
                true;

        int reachable = 0;

        while(!q.isEmpty()){

            Position p =
                    q.remove();

            reachable++;

            for(int dy = -1;
                dy <= 1;
                dy++){

                for(int dx = -1;
                    dx <= 1;
                    dx++){

                    if(dx != 0
                            && dy != 0){

                        continue;
                    }

                    int nx =
                            p.getX()
                                    + dx;

                    int ny =
                            p.getY()
                                    + dy;

                    if(nx < 0
                            || ny < 0
                            || ny >= h
                            || nx >= w){

                        continue;
                    }

                    if(seen[ny][nx]){

                        continue;
                    }

                    if(!level.isWalkable(
                            nx,
                            ny)){

                        continue;
                    }

                    seen[ny][nx] = true;

                    q.add(
                            new Position(
                                    nx,
                                    ny));
                }
            }
        }

        for(int i = 0;
            i < 9;
            i++){

            Position c =
                    level.getRooms()
                            .get(i)
                            .center();

            if(!seen[c.getY()][c.getX()]){

                return false;
            }
        }

        return reachable > 10;
    }

    private void placeExit(Level level){

        Room last =
                level.getRooms()
                        .get(8);

        Position pos =
                last.center();

        level.setExit(pos);
    }

    // -------------------------------------------------------------------------
    // Task 6: Doors and Keys
    // -------------------------------------------------------------------------

    /**
     * Places three colored doors (RED, BLUE, GREEN) on corridors between
     * room pairs (1-2), (3-4), (5-6) and the matching keys in rooms 0, 2, 4.
     * Uses BFS to verify the layout has no soft locks before committing.
     */
    private void placeDoors(
            Level level,
            Random rng){

        List<Corridor> corridors =
                level.getCorridors();

        // Need at least 6 corridors (connectRooms creates 2 per room pair = 16 total)
        if(corridors.size() < 6){

            return;
        }

        KeyColor[] colors =
                { KeyColor.RED, KeyColor.BLUE, KeyColor.GREEN };

        // corridor indices that separate room pairs (1-2), (3-4), (5-6)
        // connectRooms creates 2 corridors per gap, so gaps are at 2,6,10
        int[] corridorIndices = { 2, 6, 10 };

        // room indices where keys are placed
        int[] keyRooms = { 0, 2, 4 };

        List<Position> doorPositions =
                new ArrayList<>();

        // Pick a mid-corridor tile for each door
        for(int i = 0; i < 3; i++){

            int ci = corridorIndices[i];

            if(ci >= corridors.size()){

                ci = Math.min(
                        corridors.size() - 1,
                        corridorIndices[i]);
            }

            Corridor corridor =
                    corridors.get(ci);

            List<Position> cells =
                    corridor.getCells();

            if(cells.isEmpty()){

                return;
            }

            // pick a cell near the middle that is pure corridor (not adjacent to room)
            Position doorPos =
                    pickDoorCell(
                            level,
                            cells,
                            rng);

            if(doorPos == null){

                return;
            }

            doorPositions.add(doorPos);
        }

        // Place keys in rooms
        for(int i = 0; i < 3; i++){

            Room room =
                    level.getRooms()
                            .get(keyRooms[i]);

            Position keyPos =
                    roomCenter(room);

            Tile t =
                    level.getMap()
                            [keyPos.getY()]
                            [keyPos.getX()];

            if(t.getItem() == null){

                t.setItem(new Key(colors[i]));
            }
        }

        // Place doors and verify solvability with BFS
        for(int i = 0; i < 3; i++){

            Position pos =
                    doorPositions.get(i);

            Tile t =
                    level.getMap()
                            [pos.getY()]
                            [pos.getX()];

            t.setType(TileType.DOOR);

            t.setDoorColor(colors[i]);
        }

        // Verify no soft locks — if broken, remove all doors/keys and give up
        if(!verifySolvable(level, keyRooms, doorPositions, colors)){

            // Roll back doors
            for(Position pos : doorPositions){

                Tile t =
                        level.getMap()
                                [pos.getY()]
                                [pos.getX()];

                t.setType(TileType.CORRIDOR);

                t.setDoorColor(null);
            }

            // Roll back keys
            for(int i = 0; i < 3; i++){

                Room room =
                        level.getRooms()
                                .get(keyRooms[i]);

                Position keyPos =
                        roomCenter(room);

                Tile t =
                        level.getMap()
                                [keyPos.getY()]
                                [keyPos.getX()];

                if(t.getItem() instanceof Key){

                    t.setItem(null);
                }
            }
        }
    }

    /** Pick a corridor cell that is not directly adjacent to a room floor tile. */
    private Position pickDoorCell(
            Level level,
            List<Position> cells,
            Random rng){

        int mid = cells.size() / 2;

        // Try cells around the middle first
        for(int offset = 0;
            offset < cells.size();
            offset++){

            int idx =
                    (mid + offset) % cells.size();

            Position p = cells.get(idx);

            if(!isAdjacentToFloor(level, p)){

                return p;
            }
        }

        // Fallback: use middle cell even if near room
        return cells.get(mid);
    }

    private boolean isAdjacentToFloor(
            Level level,
            Position p){

        Tile[][] map = level.getMap();

        int[][] dirs = { {0,1},{0,-1},{1,0},{-1,0} };

        for(int[] d : dirs){

            int nx = p.getX() + d[0];

            int ny = p.getY() + d[1];

            if(ny < 0 || ny >= map.length
                    || nx < 0 || nx >= map[0].length){

                continue;
            }

            if(map[ny][nx].getType() == TileType.FLOOR){

                return true;
            }
        }

        return false;
    }

    private Position roomCenter(Room room){

        return new Position(
                room.getX() + room.getWidth() / 2,
                room.getY() + room.getHeight() / 2);
    }

    /**
     * BFS soft-lock verification using modified BFS.
     * Simulates player traversal: collect keys from reachable rooms,
     * unlock doors when key available, repeat until no progress.
     * Returns true if all rooms are reachable.
     */
    private boolean verifySolvable(
            Level level,
            int[] keyRooms,
            List<Position> doorPositions,
            KeyColor[] doorColors){

        Tile[][] map = level.getMap();

        int h = map.length;

        int w = map[0].length;

        Set<KeyColor> heldKeys =
                EnumSet.noneOf(KeyColor.class);

        Set<String> openedDoors =
                new HashSet<>();

        boolean progress = true;

        boolean[][] reachable =
                new boolean[h][w];

        Position start =
                level.getRooms().get(0).center();

        // Iterative BFS: repeat until no more doors can be opened
        while(progress){

            progress = false;

            // BFS treating doors with held keys as passable
            ArrayDeque<Position> queue =
                    new ArrayDeque<>();

            if(!reachable[start.getY()][start.getX()]){

                reachable[start.getY()][start.getX()] = true;

                queue.add(start);
            }

            // Re-seed from all currently reachable tiles
            for(int y = 0; y < h; y++){

                for(int x = 0; x < w; x++){

                    if(reachable[y][x]){

                        queue.add(new Position(x, y));
                    }
                }
            }

            while(!queue.isEmpty()){

                Position p = queue.poll();

                int[][] dirs = { {0,1},{0,-1},{1,0},{-1,0} };

                for(int[] d : dirs){

                    int nx = p.getX() + d[0];

                    int ny = p.getY() + d[1];

                    if(nx < 0 || ny < 0
                            || ny >= h || nx >= w){

                        continue;
                    }

                    if(reachable[ny][nx]){

                        continue;
                    }

                    Tile t = map[ny][nx];

                    if(t.getType() == TileType.DOOR){

                        String key = nx + "," + ny;

                        if(openedDoors.contains(key)){

                            reachable[ny][nx] = true;

                            queue.add(new Position(nx, ny));

                        } else if(heldKeys.contains(t.getDoorColor())){

                            openedDoors.add(key);

                            reachable[ny][nx] = true;

                            queue.add(new Position(nx, ny));

                            progress = true;
                        }

                        continue;
                    }

                    if(t.getType() == TileType.FLOOR
                            || t.getType() == TileType.CORRIDOR
                            || t.getType() == TileType.EXIT){

                        reachable[ny][nx] = true;

                        queue.add(new Position(nx, ny));
                    }
                }
            }

            // Collect keys from newly reachable rooms
            for(int i = 0; i < keyRooms.length; i++){

                Room room =
                        level.getRooms().get(keyRooms[i]);

                Position kp =
                        roomCenter(room);

                if(reachable[kp.getY()][kp.getX()]){

                    Tile kt =
                            map[kp.getY()][kp.getX()];

                    if(kt.getItem() instanceof Key key
                            && !heldKeys.contains(key.getKeyColor())){

                        heldKeys.add(key.getKeyColor());

                        progress = true;
                    }
                }
            }
        }

        // Check all 9 rooms are reachable
        for(int i = 0; i < 9; i++){

            Position c =
                    level.getRooms().get(i).center();

            if(!reachable[c.getY()][c.getX()]){

                return false;
            }
        }

        return true;
    }
    private Position edgePoint(
            Room from,
            Room to){

        int x =
                from.getX()
                        + from.getWidth()/2;

        int y =
                from.getY()
                        + from.getHeight()/2;

        int targetX =
                to.getX()
                        + to.getWidth()/2;

        int targetY =
                to.getY()
                        + to.getHeight()/2;

        if(Math.abs(targetX-x)
                > Math.abs(targetY-y)){

            if(targetX > x){

                x =
                        from.getX()
                                + from.getWidth()-1;
            }
            else{

                x =
                        from.getX();
            }
        }
        else{

            if(targetY > y){

                y =
                        from.getY()
                                + from.getHeight()-1;
            }
            else{

                y =
                        from.getY();
            }
        }

        return new Position(x,y);
    }

}
