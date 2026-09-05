import java.util.HashMap;

public final class Puzzle {
    public static final HashMap<Character, Character>[] orientations = new HashMap[]{
            new HashMap<Character, Character>(){{
                put('x', 'x');
                put('y', 'y');
                put('z', 'z');
            }},
            new HashMap<Character, Character>(){{
                put('x', 'y');
                put('y', 'x');
                put('z', 'z');
            }},
            new HashMap<Character, Character>(){{
                put('x', 'z');
                put('y', 'y');
                put('z', 'x');
            }},
            new HashMap<Character, Character>(){{
                put('x', 'x');
                put('y', 'z');
                put('z', 'y');
            }},
            new HashMap<Character, Character>(){{
                put('x', 'z');
                put('y', 'x');
                put('z', 'y');
            }},
            new HashMap<Character, Character>(){{
                put('x', 'y');
                put('y', 'z');
                put('z', 'x');
            }},
    };
    public static void solvePuzzle(Box box, Brick[] bricks) {}
    public static Box parse_box_string(String box_string) {return null;}
    public static Brick parse_brick_string(String brick_string) {return null;}
}
