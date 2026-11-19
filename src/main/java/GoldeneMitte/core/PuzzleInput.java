package GoldeneMitte.core;

import java.util.ArrayList;
import java.util.Arrays;

public record PuzzleInput(Box box, ArrayList<Brick> bricks) {

    public static PuzzleInput fromText(String text) {
        String[] parts = text.trim().split("\\s+", 5);
        String boxString = parts[0] + " " + parts[1] + " " + parts[2];
        String bricksString = parts[4];
        Box box = Puzzle.parseBoxString(boxString);
        Brick[] bricks = Puzzle.parseMultipleBricksString(bricksString);
        return new PuzzleInput(box, new ArrayList<>(Arrays.asList(bricks)));
    }



    public static PuzzleInput fromFile(String fileName) {
        return fromText(FileManager.readFile(fileName));
    }
}
