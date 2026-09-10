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
        gold_brick_position = new Position(size % 2, size % 2, size % 2);
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
    public boolean place_brick(Brick brick, Map<Character, Character> orientation, Position position) {

        return false;
    }
    private boolean place_part(Brick brick, Position position) {
        int x = position.x();
        int y = position.y();
        int z = position.z();
        if(grid[x][y][z] != null ) return false;

        if(Puzzle.position_outside_box(this, position)) return false;

        if(gold_brick_position.x() == x && gold_brick_position.y() == y && gold_brick_position.z() == z) return false;

        grid[x][y][z] = brick;
        return true;
    }

    public String print_box() {
        return "";
    }

    public Brick[] get_placed_bricks() {
        return placed_bricks;
    }
    public void set_placed_bricks(Brick[] bricks) {
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
