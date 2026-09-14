package GoldeneMitte;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;

public final class Puzzle {
    @SuppressWarnings("unchecked")
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
    public static boolean solve_puzzle(Box box, ArrayList<Brick> bricks) {
        if (unsolvable(box, bricks)) {
            return false;
        }
        return solve_puzzle(box, bricks, Optional.of(new Position(0,0,0)));
    }
    private static boolean solve_puzzle(Box box, ArrayList<Brick> bricks, Optional<Position> opt_position) {
        if (bricks.isEmpty()) {
            return true;
        }
        if (opt_position.isEmpty()) {
            return false;
        }
        Position position = opt_position.get();
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            bricks.remove(i);

            if(try_brick(box, bricks, brick, position)){
                return true;
            }

            bricks.add(i, brick);
        }
        return solve_puzzle(box, bricks, box.next_free_position(position));
    }
    private static boolean try_brick(Box box, ArrayList<Brick> bricks, Brick brick, Position position) {
        for(int i = 0; i < orientations.length; i++){
            if(box.place_brick(brick, orientations[i], position)){
                Optional<Position> next = box.next_free_position(position);
                if(solve_puzzle(box, bricks, next)){
                    return true;
                }
                else{
                    box.remove_brick(brick, orientations[i], position);
                }
            }
        }
        return false;
    }
    private static boolean unsolvable(Box box, ArrayList<Brick> bricks) {
        int box_volume = box.get_size() * box.get_size() * box.get_size();
        int volume_bricks = 0;
        for(Brick brick : bricks){
            volume_bricks += brick.x()*brick.y()*brick.z();
        }
        System.out.println(String.format("Boxvolume: %d, Brickvolume: %d", box_volume, volume_bricks));
        return volume_bricks != box_volume - 1;
    }
    private static int next_orientation_index(int orientation_index) {
        if (orientation_index < 0 || orientation_index >= orientations.length) {
            throw new IllegalArgumentException("Invalid orientation: " + orientation_index);
        }
        if (orientation_index == orientations.length - 1) {
            return 0;
        }
        return orientation_index + 1;
    }
    public static Box parse_box_string(String box_string) {
        String regex = "\\s";
        String[] parts = box_string.split(regex);
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try{
                numbers[i] = Integer.parseInt(parts[i]);
            }
            catch(NumberFormatException _){}
        }
        return new Box(numbers[0]);
    }
    public static Brick parse_brick_string(int id, String brick_string) {
        String regex = "\\s";
        String[] parts = brick_string.split(regex);
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try{
                numbers[i] = Integer.parseInt(parts[i]);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
                throw new IllegalArgumentException("Invalid brick string: " + brick_string);
            }
        }
        for (int i = 0; i < numbers.length; i++) {
            if(numbers[i] < 0 ){
                throw new IllegalArgumentException("Invalid brick dimensions: " + numbers.toString());
            }
        }
        int x = numbers[0], y = numbers[1], z = numbers[2];
        return new Brick(id, x, y, z);
    }

    public static Brick[] parse_multiple_bricks_string(String multiple_bricks_string) {
        String regex = "\r?\n";
        String[] brick_strings = multiple_bricks_string.split(regex);
        Brick[] bricks = new Brick[brick_strings.length];
        for (int i = 0; i < bricks.length; i++) {
            bricks[i] = parse_brick_string(i, brick_strings[i]);
        }
        return bricks;
    }
}
