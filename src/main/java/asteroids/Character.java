import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import javafx.scene.shape.*;

public abstract class Character{

  private boolean status;
	private Polygon shape;
	private Point2D velocity;

	public Character(Polygon polygon, int x, int y){
		this.shape = polygon;
		this.shape.setTranslateX(x);
    this.shape.setFill(Color.WHITE);
		this.shape.setTranslateY(y);
    this.status = true;
		this.velocity = new Point2D(0,0);

	}

  public void setAlive(boolean val){
    this.status = val;
  }

  public boolean isAlive(){
    return this.status;
  }

	public Polygon getShape(){
		return this.shape;
	}

	public void setMovement(Point2D point){
		this.velocity =  point;
	}

	public Point2D getMovement(){
	return this.velocity;
	}

	public void accelerate(){
		double changeX = Math.cos(Math.toRadians(this.shape.getRotate()));
		double changeY = Math.sin(Math.toRadians(this.shape.getRotate()));

		changeX *= 0.05;
		changeY *= 0.05;


		this.velocity = this.velocity.add(changeX,changeY);

		if (this.velocity.magnitude() > 2) {  // Max speed of 5
			this.velocity = this.velocity.normalize().multiply(2);
		}

	}

//	public void move(double width, double height){
//
//		if (this.velocity.magnitude() > 5) {  // Max speed of 5
//			this.velocity = this.velocity.normalize().multiply(5);
//		}
//		this.velocity = this.velocity.multiply(0.99); // Slows down by 1% per frame
//
//		double x = this.shape.getTranslateX() + this.velocity.getX();
//		double y = this.shape.getTranslateY() + this.velocity.getY();
//
//		if (x > width) x = 0;  
//		if (x < 0) x = width;  // Wrap left-to-right
//		if (y > height) y = 0;
//		if (y < 0) y = height;  // Wrap top-to-bottom
//
//		this.shape.setTranslateX(x);
//		this.shape.setTranslateY(y);
//	}

	public void move(){
		this.shape.setTranslateX(this.shape.getTranslateX() + this.velocity.getX());
		this.shape.setTranslateY(this.shape.getTranslateY() + this.velocity.getY());
	}

	public void move(double WIDTH, double HEIGHT){
		this.shape.setTranslateX(this.shape.getTranslateX() + this.velocity.getX());
		this.shape.setTranslateY(this.shape.getTranslateY() + this.velocity.getY());

		if (this.shape.getTranslateX() < 0) {
			this.shape.setTranslateX(this.shape.getTranslateX() + WIDTH);
		}

		if (this.shape.getTranslateX() > WIDTH) {
			this.shape.setTranslateX(this.shape.getTranslateX() % WIDTH);
		}

		if (this.shape.getTranslateY() < 0) {
			this.shape.setTranslateY(this.shape.getTranslateY() + HEIGHT);
		}

		if (this.shape.getTranslateY() > HEIGHT) {
			this.shape.setTranslateY(this.shape.getTranslateY() % HEIGHT);
		}
	}

	public void rotateLeft(int val){
		this.shape.setRotate(this.shape.getRotate() - val);
	}

	public void rotateRight(int val){
		this.shape.setRotate(this.shape.getRotate() + val);
	}
	public void rotateLeft(){
		this.shape.setRotate(this.shape.getRotate() - 1);
	}

	public void rotateRight(){
		this.shape.setRotate(this.shape.getRotate() + 1);
	}

	public boolean collide(Character other){
		Shape collisionArea = Shape.intersect(this.shape,other.getShape());
		return collisionArea.getBoundsInLocal().getWidth() != -1;
	}

}

