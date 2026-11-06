package GoldeneMitte.visualizer.view;

import javafx.scene.paint.Color;

final class Theme {
    static final Color GOLD_COLOR = Color.web("#D4AF37");
    static final String MAIN_STYLE ="-fx-base: #1e293b;" +
            " -fx-background: #0f172a;";
    static final Color DIMMED_EDGE = Color.rgb(120, 120, 120, 0.4);
    static final Color OUTLINE_EDGE = Color.rgb(0, 0, 0, 1);
    static final Color SUB_SCENE_COLOR = Color.web("#090d16");

    static final Color AMBIENT_COLOR = Color.gray(0.55);
    static final Color POINT_LIGHT_COLOR_BASE_COLOR = Color.web("#7dd3fc", 1.0);
    static final Color POINT_LIGHT_COLOR_FILL = POINT_LIGHT_COLOR_BASE_COLOR.deriveColor(0, 1, 0.5, 1);
    static final Color KEY_LIGHT_COLOR = Color.WHITE;
    static final Color FLOOR_COLOR = Color.web("#3b82f6", 0.6);

    static final String ROW_STYLE = "-fx-background-color: rgba(15,23,42,0.5);" +
            " -fx-border-color: #334155;" +
            " -fx-border-radius: 4;" +
            " -fx-background-radius: 4;" +
            " -fx-cursor: hand;";
    static final String ROW_STYLE_SELECTED =
            "-fx-background-color: rgba(59,130,246,0.25);" +
                    " -fx-border-color: #3b82f6;"
                    + "-fx-border-radius: 4;" +
                    " -fx-background-radius: 4;" +
                    " -fx-cursor: hand;";
    static final String SIDEBAR_HEADER_STYLE = "-fx-font-size: 14px;" +
            " -fx-font-weight: bold;" +
            " -fx-text-fill: #f1f5f9;";
    static final String SIDEBAR_STYLE = "-fx-background-color: #0f172a;" +
            " -fx-border-color: #1e293b;" +
            " -fx-border-width: 0 1 0 0;";

    static final Color DOT_STROKE_COLOR = Color.web("#ffffff", 0.4);
    static final String ID_LABEL_STYLE ="-fx-font-family: monospace;" +
            " -fx-font-weight: bold;" +
            " -fx-text-fill: #e2e8f0;";
    static final String COUNT_LABEL_STYLE = "-fx-text-fill: #94a3b8;" +
            " -fx-font-size: 11px;";

    private Theme() {}

}
