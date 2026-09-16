package GoldeneMitte;

import java.util.*;

public record Brick(int id, int x, int y, int z, List<Map<Character, Character>> orientations) {

    Brick(int id, int x, int y, int z) {
        this(id, x, y, z, getOrientations(x, y, z));
    }

    private List<Integer> sortedDimensions(){
        List<Integer> dims = new ArrayList<>(List.of(x,y,z));
        Collections.sort(dims);
        return dims;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brick other)) return false;
        return sortedDimensions().equals(other.sortedDimensions());
    }

    @Override
    public int hashCode(){
        return sortedDimensions().hashCode();
    }

    private static List<Map<Character, Character>> getOrientations(int x, int y, int z) {
        List<Map<Character, Character>> result = new ArrayList<>();
        HashSet<Triple> seenTriples = new HashSet<>();

        HashMap<Character, Integer> dimensions = new HashMap<>();
        dimensions.put('x', x);
        dimensions.put('y', y);
        dimensions.put('z', z);

        for (Map<Character, Character> orientation : Puzzle.orientations) {
            int orientationX = dimensions.get(orientation.get('x'));
            int orientationY = dimensions.get(orientation.get('y'));
            int orientationZ = dimensions.get(orientation.get('z'));
            Triple triple = new Triple(orientationX, orientationY, orientationZ);

            // Set.add() returns false if the element was already present
            if (seenTriples.add(triple)) {
                result.add(orientation);
            }
        }

        // Wrap so the field is genuinely read-only, not just "final reference"
        return List.copyOf(result);
    }
}
