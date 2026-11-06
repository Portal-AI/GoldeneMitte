package GoldeneMitte.core;

import java.util.Map;
import java.util.Optional;
import java.util.Stack;

public class Box {
    private Stack<Brick> placedBricks = new Stack<>();
    private Brick[][][] grid;
    private final int size;
    private final Position goldBrickPosition;
    private final Brick goldBrick = new Brick(-1, 1,1,1);
    public Box(int size) {
        if (size <= 0 || size % 2 == 0) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.grid = new Brick[size][size][size];
        goldBrickPosition = new Position(size / 2, size / 2, size / 2);
        this.grid[goldBrickPosition.x()][goldBrickPosition.y()][goldBrickPosition.z()] = goldBrick;
    }
    public void removeBrick(Brick brick) {
        placedBricks.remove(brick);
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                for(int k=0; k<size; k++){
                    if(grid[i][j][k] == brick) grid[i][j][k] = null;
                }
            }
        }
    }

    public void removeBrick(Brick brick, Map<Character, Character> orientation, Position position) {
        Position[] cells = getCellsForBrick(brick, orientation, position);
        for (Position cell: cells){
            if(grid[cell.x()][cell.y()][cell.z()] == brick) grid[cell.x()][cell.y()][cell.z()] = null;
        }
    }
    public boolean placeBrick(Brick brick, Map<Character, Character> orientation, Position position) {
        Position[] cells = getCellsForBrick(brick, orientation, position);

        for (Position cell: cells){
            if(!cellFree(cell)) return false;
        }
        for (Position cell: cells){
            grid[cell.x()][cell.y()][cell.z()] = brick;
        }

        placedBricks.push(brick);
        return true;
    }
    private Position[] getCellsForBrick(Brick brick, Map<Character, Character> orientation, Position position){
        int orientationX = Puzzle.getOrientation(orientation.get('x'), brick);
        int orientationY = Puzzle.getOrientation(orientation.get('y'), brick);
        int orientationZ = Puzzle.getOrientation(orientation.get('z'), brick);

        Position[] cells = new Position[brick.x()*brick.y()*brick.z()];
        int dx, dy, dz, idx;
        for(int x=position.x(); x<position.x()+orientationX; x++){
            for(int y=position.y(); y<position.y()+orientationY; y++){
                for(int z=position.z(); z<position.z()+orientationZ; z++){
                    dx = x-position.x();
                    dy = y-position.y();
                    dz = z-position.z();
                    idx = dz * orientationY * orientationX + dy * orientationX + dx;
                    cells[idx] = new Position(x,y,z);
                }
            }
        }
        return cells;
    }

    public Position nextCell(Position position) {
        int x = position.x(), y = position.y(), z = position.z();
        if (positionOutsideBox(position)) {
            throw new IllegalArgumentException(String.format("Position (%d, %d, %d) is outside box of size %d", x,y,z,size));
        }
        x++;
        if (x == size) {
            x = 0;
            y++;
        }
        if (y == size) {
            y = 0; z++;
        }
        return new Position(x,y,z);
    }
    public Optional<Position> nextFreeCell(Position position){
        int x = position.x(), y = position.y(), z = position.z();
        if (positionOutsideBox(position)) {
            throw new IllegalArgumentException(String.format("Position (%d, %d, %d) is outside box of size %d", x,y,z,size));
        }
        do{
            x++;
            if (x == size) {
                x = 0;
                y++;
            }
            if (y == size) {
                y = 0; z++;
            }
        } while(z < size && !isEmpty(x, y, z));
        if(z >= size){
            return Optional.empty();
        }
        return Optional.of(new Position(x,y,z));
    }
    //    prev_position is currently not used
    public Position prevCell(Position position) {
        int x = position.x(), y = position.y(), z = position.z();
        x--;
        if(x < 0){x=size-1; y--;}
        if(y < 0){y=size-1; z--;}
        if(z < 0){z=size-1;}
        return new Position(x,y,z);
    }
    public boolean positionOutsideBox(Position position) {
        if (position.x() < 0 || position.y() < 0 || position.z() < 0) {
            return true;
        }
        return position.x() >= size || position.y() >= size || position.z() >= size;
    }
//    next_orientation_index is currently not used




    private boolean cellFree(Position position) {
        if(positionOutsideBox(position)) return false;
        return isEmpty(position);
    }

    public boolean isEmpty(Position position) {
        int x = position.x();
        int y = position.y();
        int z = position.z();
        if(position.equals(goldBrickPosition)) return false;
        return grid[x][y][z] == null;
    }
    public boolean isEmpty(int x, int y, int z){
        int g = size/2;
        if(x == g && y == g && z == g) return false;
        return grid[x][y][z] == null;
    }

    public String printBox() {
        StringBuilder sb = new StringBuilder();

        sb.append("Box Size: ").append(size).append("x").append(size).append("x").append(size).append("\n");
        sb.append("Gold Brick Position: (")
                .append(goldBrickPosition.x()).append(", ")
                .append(goldBrickPosition.y()).append(", ")
                .append(goldBrickPosition.z()).append(")\n\n");

        for (int z = 0; z < size; z++) {
            sb.append("Layer Z = ").append(z).append("\n");
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x == goldBrickPosition.x() && y == goldBrickPosition.y() && z == goldBrickPosition.z()) {
                        sb.append(" G ");
                    }
                    else if (grid[x][y][z] != null) {
                        int id = grid[x][y][z].id();
                        sb.append(" ").append(id).append(" ");
                    }
                    else {
                        sb.append(" . ");
                    }
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Stack<Brick> getPlacedBricks() {
        return placedBricks;
    }
    public void setPlacedBricks(Stack<Brick> bricks) {
        placedBricks = bricks;
    }
    public Brick[][][] getGrid() {
        return grid;
    }
    public void setGrid(Brick[][][] grid) {
        this.grid = grid;
    }
    public int getSize() {
        return size;
    }
}
