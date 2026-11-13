package GoldeneMitte.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BoxTest {

    private static final Map<Character, Character> IDENTITY = Puzzle.orientations.get(0);

    private Box box;

    @BeforeEach
    void setUp() {
        box = new Box(3);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5})
    void constructor_oddPositiveSize_ok(int size) {

        assertEquals(size, new Box(size).getSize());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 2, 4})
    void constructor_invalidSize_throws(int size) {
        assertThrows(IllegalArgumentException.class, () -> new Box(size));
    }

    @Test
    void goldBrick_sitsInCenter_andIsNeverEmpty() {
        assertFalse(box.isEmpty(new Position(1, 1, 1)));
        assertFalse(box.isEmpty(1, 1, 1));
        assertTrue(box.isEmpty(new Position(0, 0, 0)));
    }

    @Test
    void positionOutsideBox_insideAndOutsideBounds() {
        assertFalse(box.positionOutsideBox(new Position(0, 0, 0)));
        assertFalse(box.positionOutsideBox(new Position(2, 2, 2)));
        assertTrue(box.positionOutsideBox(new Position(-1, 0, 0)));
        assertTrue(box.positionOutsideBox(new Position(0, 3, 0)));
        assertTrue(box.positionOutsideBox(new Position(0, 0, 3)));
    }

    @Test
    void nextCell_movesAndWrapsAtBounds() {
        assertEquals(new Position(1, 0, 0), box.nextCell(new Position(0, 0, 0)));
        assertEquals(new Position(0, 1, 0), box.nextCell(new Position(2, 0, 0)));
        assertEquals(new Position(0, 0, 1), box.nextCell(new Position(2, 2, 0)));
    }

    @Test
    void nextCell_outsideBox_throws() {
        assertThrows(IllegalArgumentException.class, () -> box.nextCell(new Position(3, 0, 0)));
        assertThrows(IllegalArgumentException.class, () -> box.nextCell(new Position(0, -1, 0)));
    }

    @Test
    void nextFreeCell_skipsGoldBrick() {
        assertEquals(Optional.of(new Position(2, 1, 1)), box.nextFreeCell(new Position(0, 1, 1)));
    }

    @Test
    void nextFreeCell_lastCell_returnsEmpty() {
        assertTrue(box.nextFreeCell(new Position(2, 2, 2)).isEmpty());
    }

    @Test
    void nextFreeCell_outsideBox_throws() {
        assertThrows(IllegalArgumentException.class, () -> box.nextFreeCell(new Position(-1, 0, 0)));
    }

    @Test
    void placeBrick_insideBox_fillsCellsAndStacksBrick() {
        Brick brick = new Brick(0, 1, 1, 3);

        assertTrue(box.placeBrick(brick, IDENTITY, new Position(0, 0, 0)));

        assertFalse(box.isEmpty(new Position(0, 0, 2)));
        assertEquals(1, box.getPlacedBricks().size());
    }

    @Test
    void placeBrick_overlappingBrick_returnsFalse() {
        box.placeBrick(new Brick(0, 1, 1, 3), IDENTITY, new Position(0, 0, 0));

        assertFalse(box.placeBrick(new Brick(1, 1, 1, 1), IDENTITY, new Position(0, 0, 1)));
        assertEquals(1, box.getPlacedBricks().size());
    }

    @Test
    void placeBrick_stickingOutOfBox_returnsFalse() {
        assertFalse(box.placeBrick(new Brick(0, 1, 1, 3), IDENTITY, new Position(0, 0, 1)));
        assertTrue(box.isEmpty(new Position(0, 0, 1)));
    }

    @Test
    void placeBrick_onGoldBrick_returnsFalse() {
        assertFalse(box.placeBrick(new Brick(0, 1, 1, 1), IDENTITY, new Position(1, 1, 1)));
    }

    @Test
    void removeBrick_freesCellsAndRemovesFromStack() {
        Brick brick = new Brick(0, 1, 1, 3);
        box.placeBrick(brick, IDENTITY, new Position(0, 0, 0));

        box.removeBrick(brick);

        assertTrue(box.isEmpty(new Position(0, 0, 0)));
        assertTrue(box.getPlacedBricks().isEmpty());
    }

    @Test
    void printBox_containsHeaderAndGold() {
        String out = box.printBox();
        assertTrue(out.contains("Box Size: 3x3x3"));
        assertTrue(out.contains(" G "));
    }
}
