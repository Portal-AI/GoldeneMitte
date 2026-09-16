package GoldeneMitte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class Test {
    public static void main(String[] args){
        test_distinct_bricks();
    }
    public static void test_distinct_bricks(){
        String text = FileManager.readFile("src/main/resources/input/raetsel4.txt");
        String[] parts = text.trim().split("\\s+", 5);
        String box_string = parts[0] + " " + parts[1] + " " + parts[2];
        String bricks_string = parts[4];
        Box box = Puzzle.parse_box_string(box_string);
        Brick[] _bricks = Puzzle.parse_multiple_bricks_string(bricks_string);
        System.out.println(Arrays.toString(_bricks));
        ArrayList<Brick> bricksList = new ArrayList<>(Arrays.asList(_bricks));
        Map<Brick, Set<Integer>> bricks = Puzzle.distinct_bricks(bricksList);
        System.out.println(bricks);
    }
}
