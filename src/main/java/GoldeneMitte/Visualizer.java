package GoldeneMitte;

import GoldeneMitte.core.Box;
import GoldeneMitte.core.Brick;
import GoldeneMitte.core.FileManager;
import GoldeneMitte.core.Puzzle;
import GoldeneMitte.visualizer.controller.VisualizerController;
import GoldeneMitte.visualizer.model.VoxelModel;
import GoldeneMitte.visualizer.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class Visualizer extends Application {
    @Override
    public void start(Stage stage) {
        String text = FileManager.readFile("src/main/resources/input/raetsel4.txt");
        String[] parts = text.trim().split("\\s+", 5);
        String box_string = parts[0] + " " + parts[1] + " " + parts[2];
        String bricks_string = parts[4];
        Box box = Puzzle.parseBoxString(box_string);
        Brick[] bricks = Puzzle.parseMultipleBricksString(bricks_string);
        ArrayList<Brick> bricksList = new ArrayList<>(Arrays.asList(bricks));

        boolean is_solved = Puzzle.solvePuzzle(box, bricksList);
        System.out.println(is_solved);
        System.out.println(box.printBox());
        FileManager.writeFile("src/main/resources/output/raetsel4.txt", box.printBox());
        VoxelModel model = new VoxelModel(box);
        MainView view = new MainView(model);
        new VisualizerController(model, view);

        stage.setTitle("Demo" + (is_solved ? "" : " (nicht gelöst)"));
        stage.setScene(new Scene(view, 1200, 780));
        stage.show();
    }
}