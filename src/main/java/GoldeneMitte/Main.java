package GoldeneMitte;

import java.util.Arrays;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String text = FileManager.readFile("src/main/resources/input/raetsel1.txt");
        String[] parts = text.trim().split("\\s+", 5);
        String box_string = parts[0] + " " + parts[1] + " " + parts[2];
        String bricks_string = parts[4];
        Box box = Puzzle.parse_box_string(box_string);
        Brick[] bricks = Puzzle.parse_multiple_bricks_string(bricks_string);
        ArrayList<Brick> bricksList = new ArrayList<>(Arrays.asList(bricks));
        boolean is_solved = Puzzle.solve_puzzle(box, bricksList);
        System.out.println(is_solved);
        System.out.println(box.print_box());
    }
}
