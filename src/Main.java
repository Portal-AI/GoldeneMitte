public class Main {
    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String text = FileManager.readFile("input/raetsel1.txt");
        String[] parts = text.trim().split("\\s+", 5);
        String box_string = parts[0] + " " + parts[1] + " " + parts[2];
        int id = Integer.parseInt(parts[3]);
        String brick_str = parts[4];
        Box box = Puzzle.parse_box_string(box_string);
        Brick bricks = Puzzle.parse_brick_string(id, brick_str);
        System.out.println(text);
    }
}
