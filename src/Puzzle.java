import java.util.HashMap;
import java.util.ArrayList;

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
        return solve_puzzle(box, bricks, new Position(0,0,0));
    }
    private static boolean solve_puzzle(Box box, ArrayList<Brick> bricks, Position position) {
        if (bricks.isEmpty()) {
            return true;
        }
        Position next_pos = next_position(box, position);
        if (next_pos.x() == 0 && next_pos.y() == 0 && next_pos.z() == 0) {
            return false;
        }
        for (int i = 0; i < bricks.size(); i++) {
            Brick brick = bricks.get(i);
            bricks.remove(i);

            if(try_brick(box, bricks, brick, position)){
                return true;
            }

            bricks.add(i, brick);
        }
        return solve_puzzle(box, bricks, next_position(box, position));
    }
    private static Position next_position(Box box, Position position) {
        int x = position.x(), y = position.y(), z = position.z(), size = box.get_size();
        if (position_outside_box(box, position)) {
            throw new IllegalArgumentException(String.format("Position (%d, %d, %d) is outside box of size %d", x,y,z,size));
        }
        x++;
        if(x == size){x = 0;}
        if(x == 0){y++;}
        if(y == size){y = 0;}
        if(y == 0){z++;}
        if(z == size){z = 0;}
        return new Position(x,y,z);
    }
//    prev_position is currently not used
    private static Position prev_position(Box box, Position position) {
        int x = position.x(), y = position.y(), z = position.z(), size = box.get_size();
        x--;
        if(x < 0){x=size-1; y--;}
        if(y < 0){y=size-1; z--;}
        if(z < 0){z=size-1;}
        return new Position(x,y,z);
    }
    public static boolean position_outside_box(Box box, Position position) {
        if (position.x() < 0 || position.y() < 0 || position.z() < 0) {
            return true;
        }
        return position.x() >= box.get_size() || position.y() >= box.get_size() || position.z() >= box.get_size();
    }
    private static boolean try_brick(Box box, ArrayList<Brick> bricks, Brick brick, Position position) {
        for(int i = 0; i < orientations.length; i++){
            if(box.place_brick(brick, orientations[i], position)){
                if(solve_puzzle(box, bricks, next_position(box, position))){
                    return true;
                }
                else{
                    box.remove_brick(brick);
                }
            }
        }
        return false;
    }
//    next_orientation_index is currently not used
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
