import java.util.HashMap;

public class Box {
    private Brick[] placed_bricks;
    private Brick[][][] grid;
    private final int size;
    public Box(int size) {
        if (size <= 0 || size % 2 == 0) {
            throw new IllegalArgumentException();
        }
        this.size = size;
        this.grid = new Brick[size][size][size];
    }
    public void remove_brick(Brick brick) {}
    public boolean place_brick(Brick brick, HashMap<Character, Character> orientation, Position position) {return false;}
    private boolean place_part(Brick brick, Position position) {return false;}
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
