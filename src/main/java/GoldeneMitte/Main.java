package GoldeneMitte;

import GoldeneMitte.core.Box;
import GoldeneMitte.core.Puzzle;
import GoldeneMitte.core.PuzzleInput;

public class Main {
    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        PuzzleInput input = PuzzleInput.fromFile("src/main/resources/input/raetsel4.txt");
        Box box = input.box();

        boolean is_solved = Puzzle.solvePuzzle(box, input.bricks());
        System.out.println(is_solved);
        System.out.println(box.printBox());
    }
}
