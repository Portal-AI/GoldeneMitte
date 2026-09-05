import java.util.HashMap;

public class Box {
    private Brick[] placed_bricks;
    private Brick[][][] grid;
    public Box(int size) {
        if (size <= 0 || size % 2 == 0) {
            throw new IllegalArgumentException();
        }
        grid = new Brick[size][size][size];
    }
    public void remove_brick(Brick brick) {}
    public void place_brick(Brick brick, HashMap<Character, Character> orientation, int x, int y, int z) {}
    private void place_part(Brick brick, int x, int y, int z) {}
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
}
