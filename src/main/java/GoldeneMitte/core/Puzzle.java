package GoldeneMitte.core;

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
    public static boolean solvePuzzle(Box box, ArrayList<Brick> bricks) {
        if (unsolvableFromStart(box, bricks)) {
            return false;
        }
        Map<Brick, Set<Integer>> distinctBricks = distinctBricks(bricks);
        return solvePuzzle(box, distinctBricks, Optional.of(new Position(0,0,0)));
    }
    private static boolean solvePuzzle(Box box, Map<Brick, Set<Integer>> distinctBricks, Optional<Position> optPosition) {
        if (distinctBricks.isEmpty()) {
            return true;
        }
        if (optPosition.isEmpty()) {
            return false;
        }
        Position position = optPosition.get();
        for (Brick brick: new ArrayList<>(distinctBricks.keySet())) {
            Set<Integer> ids = distinctBricks.get(brick);
            int id = ids.iterator().next();
            ids.remove(id);
            if (ids.isEmpty()) {
                distinctBricks.remove(brick);
            }

            if(tryBrick(box, distinctBricks, new Brick(id, brick.x(), brick.y(), brick.z()), position)){
                return true;
            }
            distinctBricks.putIfAbsent(brick, new HashSet<>());
            distinctBricks.get(brick).add(id);
        }
        return false;
    }
    private static boolean tryBrick(Box box, Map<Brick, Set<Integer>> distinctBricks, Brick brick, Position position) {
        for(Map<Character,Character> orientation : brick.orientations()) {
            if(box.placeBrick(brick, orientation, position)){
                Optional<Position> next = box.nextFreeCell(position);
                if(solvePuzzle(box, distinctBricks, next)){
                    return true;
                }
                else{
                    box.removeBrick(brick, orientation, position);
                }
            }
        }
        return false;
    }

    public static Map<Brick, Set<Integer>> distinctBricks(List<Brick> bricks) {
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
    private static boolean unsolvableFromStart(Box box, ArrayList<Brick> bricks) {
        int boxVolume = box.getSize() * box.getSize() * box.getSize();
        int volumeBricks = 0;
        for(Brick brick : bricks){
            volumeBricks += brick.x()*brick.y()*brick.z();
        }
        System.out.println(String.format("Boxvolume: %d, Brickvolume: %d", boxVolume, volumeBricks));
        return volumeBricks != boxVolume - 1;
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
            if(box.positionOutsideBox(candidate)) continue;
            if(!box.isEmpty(candidate)) continue;
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
    public static int getOrientation(Character character, Brick brick) {
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
    public static Box parseBoxString(String boxString) {
        String regex = "\\s";
        String[] parts = boxString.split(regex);
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try{
                numbers[i] = Integer.parseInt(parts[i]);
            }
            catch(NumberFormatException _){}
        }
        return new Box(numbers[0]);
    }
    public static Brick parseBrickString(int id, String brickString) {
        String regex = "\\s";
        String[] parts = brickString.split(regex);
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try{
                numbers[i] = Integer.parseInt(parts[i]);
            }
            catch(NumberFormatException e){
                e.printStackTrace();
                throw new IllegalArgumentException("Invalid brick string: " + brickString);
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

    public static Brick[] parseMultipleBricksString(String multipleBricksString) {
        String regex = "\r?\n";
        String[] brickStrings = multipleBricksString.split(regex);
        Brick[] bricks = new Brick[brickStrings.length];
        for (int i = 0; i < bricks.length; i++) {
            bricks[i] = parseBrickString(i, brickStrings[i]);
        }
        return bricks;
    }
}
