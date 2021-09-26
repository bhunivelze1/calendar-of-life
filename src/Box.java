import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Box extends Rectangle {
    
    public Box(boolean used) {
        super(10, 10, Color.WHITE);
        super.setStroke(Color.BLACK);

        if (used) {
            super.setFill(Color.GREENYELLOW);
        }
                
        super.setOnMouseEntered(e -> super.setStroke(Color.WHITE));
        super.setOnMouseExited(e -> super.setStroke(Color.BLACK));
    }
}