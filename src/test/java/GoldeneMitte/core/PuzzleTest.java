package GoldeneMitte.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleTest {

    private static ArrayList<Brick> exampleBricks() {
        return new ArrayList<>(List.of(
                new Brick(0, 1, 3, 3), new Brick(1, 1, 3, 3), new Brick(2, 1, 1, 3),
                new Brick(3, 1, 1, 2), new Brick(4, 1, 1, 2), new Brick(5, 1, 1, 1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"4 4 4", "0 0 0", "-3 -3 -3", "abc", ""})
    void parseBoxString_invalid_throws(String input) {
        assertThrows(IllegalArgumentException.class, () -> Puzzle.parseBoxString(input));
    }

    @Test
    void parseBoxString_valid() {
        assertEquals(5, Puzzle.parseBoxString("5 5 5").getSize());
    }

    @Test
    void parseBrickString_valid() {
        Brick b = Puzzle.parseBrickString(7, "1 2 3");
        assertEquals(7, b.id());
        assertEquals(1, b.x());
        assertEquals(2, b.y());
        assertEquals(3, b.z());
    }

    @Test
    void parseMultipleBricksString_assignsIdsInOrder_andHandlesCRLF() {
        Brick[] bricks = Puzzle.parseMultipleBricksString("1 1 1\r\n2 2 2\r\n3 3 3");
        assertEquals(3, bricks.length);
        assertEquals(2, bricks[2].id());
    }

    @Test
    void solvePuzzle_exampleFromTask_isSolvable() {
        assertTrue(Puzzle.solvePuzzle(new Box(3), exampleBricks()));
    }

    @Test
    void solvePuzzle_volumeOneTooSmall_returnsFalse() {
        ArrayList<Brick> bricks = exampleBricks();
        bricks.remove(5);
        assertFalse(Puzzle.solvePuzzle(new Box(3), bricks));
    }

    @Test
    void solvePuzzle_volumeOneTooBig_returnsFalse() {
        ArrayList<Brick> bricks = exampleBricks();
        bricks.add(new Brick(6, 1, 1, 1));
        assertFalse(Puzzle.solvePuzzle(new Box(3), bricks));
    }

    @Test
    void solvePuzzle_correctVolumeButBrickDoesNotFit_returnsFalse() {
        ArrayList<Brick> bricks = new ArrayList<>();
        bricks.add(new Brick(0, 1, 1, 4));
        for (int i = 1; i <= 22; i++) bricks.add(new Brick(i, 1, 1, 1));
        assertFalse(Puzzle.solvePuzzle(new Box(3), bricks));
    }

    @Test
    void solvePuzzle_isRepeatable_withFreshBox() {
        assertEquals(Puzzle.solvePuzzle(new Box(3), exampleBricks()),
                     Puzzle.solvePuzzle(new Box(3), exampleBricks()));
    }
}
