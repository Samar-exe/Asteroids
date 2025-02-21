import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class Ship extends Character{

	public Ship(int x , int y){
		super(new Polygon(0 , 0 , 15 , 5 , 0 , 10),x,y);
	}
}
