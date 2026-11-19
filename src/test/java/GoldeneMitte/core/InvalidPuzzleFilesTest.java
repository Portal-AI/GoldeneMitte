package GoldeneMitte.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InvalidPuzzleFilesTest {

    private static final String INPUT_DIR = "src/main/resources/input/";

    @Test
    @DisplayName("raetsel_nicht_lösbar.txt: bricks are too large in total (28 > 26) -> not solvable")
    void unsolvablePuzzleIsRejected() {
        PuzzleInput input = PuzzleInput.fromFile(INPUT_DIR + "raetsel_nicht_lösbar.txt");

        assertEquals(8, input.bricks().size(), "all 8 bricks from the file should be parsed");

        boolean solved = Puzzle.solvePuzzle(input.box(), input.bricks());

        assertFalse(solved, "a puzzle whose bricks exceed the free volume must not be reported as solved");
        assertEquals(1, occupiedCells(input.box()), "only the gold cube may remain in the box");
    }

    @Test
    @DisplayName("raetsel_unvollständig.txt: bricks are too small in total (22 < 26) -> not solvable")
    void incompletePuzzleIsRejected() {
        PuzzleInput input = PuzzleInput.fromFile(INPUT_DIR + "raetsel_unvollständig.txt");
        assertEquals(7, input.bricks().size(), "all 7 bricks from the file should be parsed");

        boolean solved = Puzzle.solvePuzzle(input.box(), input.bricks());

        assertFalse(solved, "a puzzle whose bricks cannot fill the box must not be reported as solved");
        assertEquals(1, occupiedCells(input.box()), "only the gold cube may remain in the box");
    }

    private static int occupiedCells(Box box) {
        int count = 0;
        for (Brick[][] plane : box.getGrid()) {
            for (Brick[] row : plane) {
                for (Brick cell : row) {
                    if (cell != null) count++;
                }
            }
        }
        return count;
    }
}
