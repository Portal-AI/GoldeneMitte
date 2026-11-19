package GoldeneMitte;

import GoldeneMitte.core.Box;
import GoldeneMitte.core.FileManager;
import GoldeneMitte.core.Puzzle;
import GoldeneMitte.core.PuzzleInput;
import GoldeneMitte.visualizer.controller.VisualizerController;
import GoldeneMitte.visualizer.model.VoxelModel;
import GoldeneMitte.visualizer.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Visualizer extends Application {
    @Override
    public void start(Stage stage) {
        PuzzleInput input = PuzzleInput.fromFile("src/main/resources/input/raetsel4.txt");
        Box box = input.box();

        boolean is_solved = Puzzle.solvePuzzle(box, input.bricks());
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