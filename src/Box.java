import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Box {
    private Stack<Brick> placed_bricks;
    private Brick[][][] grid;
    private final int size;
    private Position gold_brick_position;
    public Box(int size) {
        if (size <= 0 || size % 2 == 0) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.grid = new Brick[size][size][size];
        gold_brick_position = new Position(size / 2, size / 2, size / 2);
    }
    public void remove_brick(Brick brick) {
        placed_bricks.remove(brick);
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                for(int k=0; k<size; k++){
                    if(grid[i][j][k] == brick) grid[i][j][k] = null;
                }
            }
        }
    }
    public boolean place_brick(Brick brick, Map orientation, Position position) {
        int orientation_x = get_orientation((Character) orientation.get('x'), brick);
        int orientation_y = get_orientation((Character) orientation.get('y'), brick);
        int orientation_z = get_orientation((Character) orientation.get('z'), brick);
        int start_x = position.x();
        int start_y = position.y();
        int start_z = position.z();

        for(int x=start_x; x<start_x+orientation_x; x++){
            for(int y=start_y; y<start_y+orientation_y; y++){
                for(int z=start_z; z<start_z+orientation_z; z++){
                    Position pos = new Position(x, y, z);
                    if(!place_part(brick, pos)) return false;
                }
            }
        }

        for(int x=start_x; x<start_x+orientation_x; x++){
            for(int y=start_y; y<start_y+orientation_y; y++){
                for(int z=start_z; z<start_z+orientation_z; z++){
                    grid[x][y][z] = brick;
                }
            }
        }
        if (placed_bricks == null) {
            placed_bricks = new Stack<>();
        }
        placed_bricks.push(brick);
        return true;
    }

    private int get_orientation(Character character, Brick brick) {
        int x = brick.x();
        int y = brick.y();
        int z = brick.z();
        switch (character){
            case 'x':
                return x;
            case 'y':
                 return y;
            case 'z':
                return z;
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean place_part(Brick brick, Position position) {
        if(Puzzle.position_outside_box(this, position)) return false;
        int x = position.x();
        int y = position.y();
        int z = position.z();
        if(grid[x][y][z] != null ) return false;

        if(gold_brick_position.x() == x && gold_brick_position.y() == y && gold_brick_position.z() == z) return false;

        return true;
    }

    public String print_box() {
        StringBuilder sb = new StringBuilder();

        sb.append("Box Size: ").append(size).append("x").append(size).append("x").append(size).append("\n");
        sb.append("Gold Brick Position: (")
                .append(gold_brick_position.x()).append(", ")
                .append(gold_brick_position.y()).append(", ")
                .append(gold_brick_position.z()).append(")\n\n");

        for (int z = 0; z < size; z++) {
            sb.append("Layer Z = ").append(z).append("\n");
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x == gold_brick_position.x() && y == gold_brick_position.y() && z == gold_brick_position.z()) {
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

    public Stack<Brick> get_placed_bricks() {
        return placed_bricks;
    }
    public void set_placed_bricks(Stack<Brick> bricks) {
        placed_bricks = bricks;
    }
    public Brick[][][] get_grid() {
        return grid;
    }
    public void set_grid(Brick[][][] grid) {
        this.grid = grid;
    }
    public int get_size() {
        return size;
    }
}
