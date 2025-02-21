package asteroids;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;
import javafx.scene.media.AudioClip;
import java.util.concurrent.atomic.AtomicInteger;

public class AsteroidsApplication extends Application {
  public static int WIDTH = 300;
  public static int HEIGHT = 200;
  public static boolean start = false;

  @Override
  public void start(Stage window) {

    // Audio Clips.
    AudioClip fire = new AudioClip("file:fire.wav");
    AudioClip bang = new AudioClip("file:bang.wav");
    AudioClip crash= new AudioClip("file:shipcrash.mp3");

    // Game Over Text
    Label over = new Label("Game Over");

    // /text to display points;
    Text text = new Text(10,20,"Points: 0");
    text.setFill(Color.WHITE);

    // Making the ship , asteroids and projectiles.
    Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
    ArrayList<Asteroid> asteroids = new ArrayList<>();
    ArrayList<Projectile> projectiles = new ArrayList<>();

    // Moving the asteroids.
    for (int i = 0; i < 5; i++) {
      Random rnd = new Random();
      Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT));
      asteroid.accelerate();
      asteroid.accelerate();
      asteroids.add(asteroid);
    }

    // Creating pane
    Pane pane = new Pane();
    pane.setPrefSize(WIDTH, HEIGHT);
    pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

    // Add ship to pane.
    pane.getChildren().add(ship.getShape());
    // Add each asteroid to pane.
    asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getShape()));
    // Add points to pane.
    pane.getChildren().add(text);
    AtomicInteger points = new AtomicInteger(0);

    Scene scene = new Scene(pane);

    // a map to check which key is pressed or not.
    HashMap<KeyCode, Boolean> map = new HashMap<>();
    // This will change the value of the above map.
scene.setOnKeyPressed(event -> {
    map.put(event.getCode(), Boolean.TRUE);
});

scene.setOnKeyReleased(event -> {
    map.put(event.getCode(), Boolean.FALSE);
});


new AnimationTimer() {
  @Override
  public void handle(long now) {

    // Check if space key is pressed.
    if (map.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3) {
      Projectile projectile = new Projectile((int) ship.getShape().getTranslateX(),
          (int) ship.getShape().getTranslateY());
      projectile.getShape().setRotate(ship.getShape().getRotate());
      projectiles.add(projectile);
      projectile.accelerate();
      projectile.setMovement(projectile.getMovement().normalize().multiply(3));
      fire.play();
      pane.getChildren().add(projectile.getShape());
    }
    if (map.getOrDefault(KeyCode.LEFT, false)) {
      ship.rotateLeft();
    }
    if (map.getOrDefault(KeyCode.RIGHT, false)) {
      ship.rotateRight();
    }
    if (map.getOrDefault(KeyCode.UP, false)) {
      ship.accelerate();
    }

    // Move entities
    ship.move(pane.getWidth(), pane.getHeight());
    asteroids.forEach(asteroid -> asteroid.move(pane.getWidth(), pane.getHeight()));

    // Collect projectiles to remove
    ArrayList<Projectile> projectilesToRemove = new ArrayList<>();

    projectiles.forEach(projectile -> {
      projectile.move(pane.getWidth(), pane.getHeight());

      // Remove projectiles that go out of bounds
      if (projectile.getShape().getTranslateX() < 0 || projectile.getShape().getTranslateX() > pane.getWidth()
          || projectile.getShape().getTranslateY() < 0 || projectile.getShape().getTranslateY() > pane.getHeight()) {
        projectilesToRemove.add(projectile);
          }
    });

    // Handle collisions
    ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
    projectiles.forEach(projectile -> {
      ArrayList<Asteroid> collisions = asteroids.stream()
        .filter(asteroid -> asteroid.collide(projectile))
        .collect(Collectors.toCollection(ArrayList::new));

      if (!collisions.isEmpty()) {
        asteroidsToRemove.addAll(collisions);
        projectilesToRemove.add(projectile);
      }
    });

    // Remove collided asteroids and projectiles
    asteroidsToRemove.forEach(asteroid -> {
      asteroids.remove(asteroid);
      bang.play();
      pane.getChildren().remove(asteroid.getShape());
      text.setText("Points: " + points.addAndGet(100));
    });

    projectilesToRemove.forEach(projectile -> {
      projectiles.remove(projectile);
      pane.getChildren().remove(projectile.getShape());
    });

    // Check if ship collided with an asteroid.
    if (asteroids.stream().anyMatch(asteroid -> ship.collide(asteroid))) {
      crash.play();
      text.setText("Game Over! Points: " + points.get());
      over.setTextFill(Color.RED);
      over.setTranslateX(pane.getWidth() / 2 - 30);
      over.setTranslateY(pane.getHeight() / 2 - 10);
      over.setScaleX(pane.getWidth() / 100);
      over.setScaleY(pane.getWidth() / 100);
      pane.getChildren().add(over);
      
      stop();
    }

    if(Math.random() < 0.005) {
      Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
      if(!asteroid.collide(ship)) {
        asteroids.add(asteroid);
        pane.getChildren().add(asteroid.getShape());
      }
    }
  }
}.start();


window.setScene(scene);
window.setTitle("Asteroids");
window.show();
  }

public static void main(String[] args) {
  launch();
}

public static int partsCompleted() {
  // State how many parts you have completed using the return value of this method
  return 4;
}

}
