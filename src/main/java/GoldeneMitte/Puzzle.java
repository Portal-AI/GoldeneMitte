package GoldeneMitte;

import java.util.*;

public final class Puzzle {

    public static final List<Map<Character, Character>> orientations = List.of(
            Map.of('x', 'x', 'y', 'y', 'z', 'z'),
            Map.of('x', 'y', 'y', 'x', 'z', 'z'),
            Map.of('x', 'z', 'y', 'y', 'z', 'x'),
            Map.of('x', 'x', 'y', 'z', 'z', 'y'),
            Map.of('x', 'z', 'y', 'x', 'z', 'y'),
            Map.of('x', 'y', 'y', 'z', 'z', 'x')
    );
    public static boolean solve_puzzle(Box box, ArrayList<Brick> bricks) {
        if (unsolvable_from_start(box, bricks)) {
            return false;
        }
        Map<Brick, Set<Integer>> distinct_bricks = distinct_bricks(bricks);
        return solve_puzzle(box, distinct_bricks, Optional.of(new Position(0,0,0)));
    }
    private static boolean solve_puzzle(Box box, Map<Brick, Set<Integer>> distinct_bricks, Optional<Position> opt_position) {
        if (distinct_bricks.isEmpty()) {
            return true;
        }
        if (opt_position.isEmpty()) {
            return false;
        }
        Position position = opt_position.get();
        for (Brick brick: new ArrayList<>(distinct_bricks.keySet())) {
            Set<Integer> ids = distinct_bricks.get(brick);
            int id = ids.iterator().next();
            ids.remove(id);
            if (ids.isEmpty()) {
                distinct_bricks.remove(brick);
            }

            if(try_brick(box, distinct_bricks, new Brick(id, brick.x(), brick.y(), brick.z()), position)){
                return true;
            }
            distinct_bricks.putIfAbsent(brick, new HashSet<>());
            distinct_bricks.get(brick).add(id);
        }
        return false;
    }
    private static boolean try_brick(Box box, Map<Brick, Set<Integer>> distinct_bricks, Brick brick, Position position) {
        for(Map<Character,Character> orientation : brick.orientations()) {
            if(box.place_brick(brick, orientation, position)){
                Optional<Position> next = box.next_free_cell(position);
                if(solve_puzzle(box, distinct_bricks, next)){
                    return true;
                }
                else{
                    box.remove_brick(brick, orientation, position);
                }
            }
        }
        return false;
    }

    public static Map<Brick, Set<Integer>> distinct_bricks(List<Brick> bricks) {
        Set<Integer> ids = new HashSet<>();
        Map<Brick, Set<Integer>> result = new HashMap<>();
        for (Brick brick : bricks) {
            result.merge(brick, new HashSet<>(Set.of(brick.id())), (existing, incoming) -> {
                existing.addAll(incoming);
                return existing;
            });
        }
        return result;
    }
    private static boolean unsolvable_from_start(Box box, ArrayList<Brick> bricks) {
        int box_volume = box.get_size() * box.get_size() * box.get_size();
        int volume_bricks = 0;
        for(Brick brick : bricks){
            volume_bricks += brick.x()*brick.y()*brick.z();
        }
        System.out.println(String.format("Boxvolume: %d, Brickvolume: %d", box_volume, volume_bricks));
        return volume_bricks != box_volume - 1;
    }
    private static ArrayList<Position> neighbors(Box box, Position position, boolean[][][] visited){
        Position [] candidates = new Position[]{
                new Position(position.x(), position.y(), position.z()),
                new Position(position.x() + 1, position.y(), position.z()),
                new Position(position.x(), position.y() + 1, position.z()),
                new Position(position.x(), position.y(), position.z()+1),
                new Position(position.x() - 1, position.y(), position.z()),
                new Position(position.x(), position.y() - 1, position.z()),
                new Position(position.x(), position.y(), position.z()-1),
        };
        ArrayList<Position> result = new ArrayList<>();
        for(Position candidate : candidates){
            if(box.position_outside_box(candidate)) continue;
            if(!box.is_empty(candidate)) continue;
            if(visited[candidate.x()][candidate.y()][candidate.z()]) continue;
            visited[candidate.x()][candidate.y()][candidate.z()] = true;
            result.add(candidate);
        }
        return result;
    }



//    private static boolean unsolvable(Box box, ArrayList<Brick> outside_bricks){
//
//    }
//    private int[] pocket_volumes(Box box){
//        Optional<Position> current = box.next_free_cell(new Position(0,0,0));
//        if(current.isEmpty()) return new int[]{0};
//        boolean[][][] visited = new boolean[box.get_size()][box.get_size()][box.get_size()];
//        ArrayList<Integer> result = new ArrayList<>();
//        while(current.isPresent()){
    ////            queue
//            Position position = current.get();
//            visited[position.x()][position.y()][position.z()] = true;
//
//        }
//    }
    public static int get_orientation(Character character, Brick brick) {
        int x = brick.x();
        int y = brick.y();
        int z = brick.z();
        return switch (character) {
            case 'x' -> x;
            case 'y' -> y;
            case 'z' -> z;
            default -> throw new IllegalArgumentException();
        };
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
