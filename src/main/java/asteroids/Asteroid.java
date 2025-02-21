import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import java.util.Random;

public class Asteroid extends Character{
	private double rotationalMovement;

	public Asteroid(int x, int y){
		super(new PolygonFactory().createPolygon(),x,y);
		Random rnd = new Random();

		super.getShape().setRotate(rnd.nextInt(360));

		int accelerationAmount = 1 + rnd.nextInt(10);
		for (int i = 0; i < accelerationAmount; i++) {
			accelerate();
		}

		this.rotationalMovement = 0.5 - rnd.nextDouble();
	}
	@Override
		public void move(double WIDTH,double HEIGHT) {
			super.move(WIDTH, HEIGHT);
			super.getShape().setRotate(super.getShape().getRotate() + rotationalMovement);
		}

}
