
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * 
 * This is a <em>very basic</em> skeleton to init a game and run it.
 * 
 * Task: Create levels and make the hero to move between different levels. Use
 * Entity, HeroEntity, LevelTitle and MySprite classes.
 * 
 * @author Nedjalko Milenkov
 * @version 1.0 $Id$
 */
public class Game {

	/** Game title */
	public static final String GAME_TITLE = "My Game";

	/** Screen size */
	private static int SCREEN_SIZE_WIDTH = Display.getDisplayMode().getWidth();
	private static int SCREEN_SIZE_HEIGHT = Display.getDisplayMode().getHeight();

	/** Desired frame time */
	private static final int FRAMERATE = 60;

	/** Exit the game */
	private boolean exitGame = false;
	
	//background
	private MySprite backgroundSprite;
	
	// game over
	private boolean gameOver;
	private boolean lastRender;
	
	// rocketship 
	private RocketshipEntity rocketship;
	private int rocketshipStartX = 50;
	private int rocketshipStartY;

	// moving entities
	private ArrayList<Entity> movingEntities;
	private long toCreateEntityTime;
	private long lastEntityTime;
	private int entityCoolDownTime;

	// planets
	private ArrayList<Entity> movingPlanets;
	private long toCreatePlanetTime;
	private long lastPlanetTime;
	private final int planetCoolDownTime = 40000;
	private int planetMoveFrames;
	private boolean arePlanetsMoving;

	// level
	private long levelStartTime;
	private long levelEndTime;
	private final long levelDurationTime = 5000;
	private int level;

	// projectile
	private ArrayList<Entity> projectiles;
	private long lastProjectileTime;
	private final int projectileCooldownTime = 750;

	// lives
	private int lives;
	private final int livesStartPosX = SCREEN_SIZE_WIDTH - 150;
	private final int livesStartPosY = 25;
	private final int distanceBetweenLives = 30;
	private MySprite shipLifeSprite;

	// diamond score
	private MySprite diamondSprite;
	private final int diamondSpriteX = 150;
	private final int diamondSpriteY = 25;
	private int diamondScore;
	private int diamondScoreFontX;
	private final int diamondScoreFontY = 25;
	
	
	// asteroid score
	private MySprite asteroidSprite;
	private int asteroidSpriteX = 500;
	private int asteroidSpriteY = 25;
	private int asteroidScore;
	private int asteroidScoreFontX;
	private final int asteroidScoreFontY = 25;
	
	// here we store the array lists of all the moving entities like diamonds, asteroids and projectiles
	// that can exit the screen, and we need to clean up
	private ArrayList<ArrayList<Entity>> allEntitiesToBeCleanedUp;

	private TrueTypeFont font;

	/**
	 * Application init
	 * 
	 */
	public static void main(String[] args) {
		Game myGame = new Game();
		myGame.start();
	}

	public void start() {
		try {
			init();
			run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			Sys.alert(GAME_TITLE, "An error occured and the game will exit.");
		} finally {
			cleanup();
		}

		System.exit(0);
	}

	/**
	 * Initialise the game
	 * 
	 * @throws Exception if init fails
	 */
	private void init() throws Exception {
		// Create a fullscreen window with 1:1 orthographic 2D projection, and
		// with
		// mouse, keyboard, and gamepad inputs.
		try {
			initGL(SCREEN_SIZE_WIDTH, SCREEN_SIZE_HEIGHT);

			initTextures();
		} catch (IOException e) {
			e.printStackTrace();
			exitGame = true;
		}
	}

	private void initGL(int width, int height) {
		try {
			Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			Display.setTitle(GAME_TITLE);
			Display.create();

			// Enable vsync if we can
			Display.setVSyncEnabled(true);

			// Start up the sound system
			AL.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		Font awtFont = new Font("Times New Roman", Font.BOLD, 50);
		font = new TrueTypeFont(awtFont, true);
	}

	private void initTextures() throws IOException {
		backgroundSprite = new MySprite(Textures.backgroundTexture);
		shipLifeSprite = new MySprite(Textures.shipLifeTexture);
		
		diamondSprite = new MySprite(Textures.diamondScoreTexture);
		diamondScoreFontX = 10 + diamondSprite.getWidth() + diamondSpriteX;
		
		asteroidSprite = new MySprite(Textures.asteroidScoreTexture);
		asteroidScoreFontX = 10 + asteroidSprite.getWidth() + asteroidSpriteX;
		
		rocketship = new RocketshipEntity(Textures.rocketshipTexture, rocketshipStartX, 0);
		rocketshipStartY = SCREEN_SIZE_HEIGHT / 2 - rocketship.getHeight() / 2;
		rocketship.setY(rocketshipStartY);
	}

	/**
	 * Runs the game (the "main loop")
	 */
	private void run() {
		startNewGame();
		
		while (!exitGame) {
			// Always call Window.update(), all the time
			Display.update();
			
			if (Display.isCloseRequested()) {
				// Check for O/S close requests
				exitGame = true;
			} else if(gameOver) {
				// after the game is over, it should be only listened for
				// keyboard inputs to escape the game, or start a new one
				control();
				if(!lastRender) {
					// render one last time after the game has finished
					render();
					lastRender = true;
				}
			}else if (Display.isActive()) {
				// The window is in the foreground, so we should play the game
				control();
				logic();
				render();
				Display.sync(FRAMERATE);
			} else {
				// The window is not in the foreground, so we can allow other
				// stuff to run and
				// infrequently update
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				logic();
				if (Display.isVisible() || Display.isDirty()) {
					// Only bother rendering if the window is visible or dirty
					render();
				}
			}
		}
	}

	private void cleanup() {
		// TODO: save anything you want to disk here

		// Stop the sound
		AL.destroy();

		// Close the window
		Display.destroy();
	}
	
	// handle keyboard input
	private void control() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			exitGame = true;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && gameOver) {
			startNewGame();
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			if (rocketship.getY() > 150) {
				rocketship.setY(rocketship.getY() - 10);
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			if (rocketship.getY() + rocketship.getHeight() < SCREEN_SIZE_HEIGHT) {
				rocketship.setY(rocketship.getY() + 10);
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			boolean canShoot = false;

			if (lastProjectileTime == 0) {
				canShoot = true;
			} else {
				canShoot = checkIfTimePassed(lastProjectileTime, getTimeNow(), projectileCooldownTime);
			}
			
			// there is a cooldown for shooting projectiles
			if (canShoot) {
				ProjectileEntity projectile = new ProjectileEntity(Textures.projectileTexture,
						rocketship.getCannonPosX(), rocketship.getCannonPosY());
				projectile.setVelocityX(10);
				projectiles.add(projectile);
				lastProjectileTime = getTimeNow();
			}
		}
	}
	
	// Do calculations
	private void logic() {
		arePlanetsMoving = false;
		planetMoveFrames--;
		
		// planets have to move the slowest of all moving objects
		// to create the illusion that they are far back in the distance,
		// therefore they move every 3 frames
		if (planetMoveFrames <= 0) {
			planetMoveFrames = 3;
			arePlanetsMoving= true;
		}

		levelEndTime = getTimeNow();
		toCreateEntityTime = getTimeNow();
		generateRandomEntity();

		generatePlanet();
		toCreatePlanetTime = getTimeNow();

		if (isNewLevel()) {
			levelStartTime = getTimeNow();
			levelEndTime = 0;
			level++;
			
			// with every new level, the difficulty of the game increases
			// by lowering the time of asteroids getting generated
			if (entityCoolDownTime >= 800) {
				entityCoolDownTime -= 100 * level;
			}
		}

		// Check if any projectiles have intersected with any asteroids or diamonds
		for (int p = 0; p < movingEntities.size(); p++) {
			for (int s = 0; s < projectiles.size(); s++) {
				Entity movingEntity = movingEntities.get(p);
				Entity projectile = projectiles.get(s);

				if (projectile.collidesWith(movingEntity)) {
					if (movingEntity instanceof AsteroidEntity) {
						int durability = ((AsteroidEntity) movingEntity).getDurability();
						((AsteroidEntity) movingEntity).setDurability(durability - 1);
						
						// asteroids of bigger size can take more hits before they get destroyed
						if (durability - 1 <= 0) {
							removeEntity(movingEntity);
						}
					} else if (movingEntity instanceof DiamondEntity) {
						removeEntity(movingEntity);
					}
					removeProjectile(projectile);
					break; // Break this cycle after removing
							// a projectile, or we can go out of the array list bounds
				}
			}
		}

		// Check if any asteroids have intersected with the rocketship
		for (int p = 0; p < movingEntities.size(); p++) {
			Entity entity = movingEntities.get(p);

			if (rocketship.collidesWith(entity)) {
				if (entity instanceof AsteroidEntity) {
					removeLife();
					asteroidScore -= 100;
				} else if (entity instanceof DiamondEntity) {
					this.diamondScore += 100;
				}

				removeEntity(entity);
			}
		}
		
		// iterating through an arraylist, containing arraylists
		// iterator is used to avoid ConcurrentModicationException
		for (ArrayList<Entity> entityGroup : allEntitiesToBeCleanedUp) {
			Iterator<Entity> iterator = entityGroup.iterator();

			while (iterator.hasNext()) {
				Entity currentObject = (Entity) iterator.next();
				if (!isObjectOnScreen(currentObject)) {
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Render the current frame
	 */
	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		Color.white.bind();
		
		backgroundSprite.draw(0, 0);

		if (movingPlanets != null) {
			for (Entity entity : movingPlanets) {
				if (arePlanetsMoving) {
					entity.setX(entity.getX() - 1);
				}
				entity.draw();
			}
		}

		if (movingEntities != null) {
			for (Entity entity : movingEntities) {
				entity.draw();
			}
		}

		if (projectiles != null) {
			for (Entity projectile : projectiles) {
				projectile.draw();
			}
		}

		rocketship.draw();
		displayLives();
		
		diamondSprite.draw(diamondSpriteX, diamondSpriteY);
		font.drawString(diamondScoreFontX, diamondScoreFontY + 5, "" + diamondScore, Color.white);
		
		asteroidSprite.draw(asteroidSpriteX, asteroidSpriteY);
		font.drawString(asteroidScoreFontX, asteroidScoreFontY + 5, "" + asteroidScore, Color.white);
		
		if(gameOver) {
			showEndScore();
		}
	}

	public void removeEntity(Entity entity) {
			if(entity instanceof AsteroidEntity) {
				AsteroidEntity destroyedAsteroid = (AsteroidEntity) entity;
				asteroidScore += destroyedAsteroid.getSize() * 100;
				
				// If the asteroid is of size 2 and bigger, when it gets destroyed
				// additional smaller asteroids are created on the position it was
				// destroyed, all flying in different directions on the Y-axis
				if(destroyedAsteroid.getSize() >= 2) {
					AsteroidEntity newAsteroid = EntityGeneration.generateSmallAsteroid();
					newAsteroid.setVelocityY(-1);
					newAsteroid.setX(destroyedAsteroid.getX());
					newAsteroid.setY(destroyedAsteroid.getY());
					movingEntities.add(newAsteroid);
					
					newAsteroid = EntityGeneration.generateSmallAsteroid();
					newAsteroid.setVelocityY(1);
					newAsteroid.setX(destroyedAsteroid.getX());
					newAsteroid.setY(destroyedAsteroid.getY());
					movingEntities.add(newAsteroid);
					
					if(destroyedAsteroid.getSize() == 3) {
						newAsteroid = EntityGeneration.generateSmallAsteroid();
						newAsteroid.setVelocityY(1);
						newAsteroid.setX(destroyedAsteroid.getX());
						newAsteroid.setY(destroyedAsteroid.getY());
						movingEntities.add(newAsteroid);
					}
				}
				
			}
			movingEntities.remove(entity);
	}

	public void removeProjectile(Object object) {
		if (object instanceof ProjectileEntity) {
			ProjectileEntity projectileEntity = (ProjectileEntity) object;
			projectiles.remove(projectileEntity);
		}
	}

	public long getTimeNow() {
		Date date = new Date();
		return date.getTime();
	}
	
	// function for checking if a time period has passed
	public boolean checkIfTimePassed(long pastTime, long currentTime, long timeToPass) {
		if (currentTime - pastTime < timeToPass) {
			return false;
		}

		return true;
	}

	public void displayLives() {
		int nextLifePosX = livesStartPosX;

		// draw lives from right to left, therefore substract from the screen width (x
		// axis)
		for (int i = 0; i < lives; i++) {
			shipLifeSprite.draw(nextLifePosX, livesStartPosY);
			nextLifePosX -= shipLifeSprite.getWidth() + distanceBetweenLives;
		}
	}
	
	// checks if an object is on the screen
	public boolean isObjectOnScreen(Entity object) {
		int objectX = object.getX();
		int objectY = object.getY();
		int objectWidth = object.getWidth();
		int objectHeight = object.getHeight();
		
		if (objectX + objectWidth < 0 || objectX > SCREEN_SIZE_WIDTH) {
			return false;
		}
		
		// useful for asteroids that might fly out of the screen on the Y-axis
		if (objectY + objectHeight < 0 || objectY - objectHeight >= SCREEN_SIZE_HEIGHT) {
			return true;
		}

		return true;
	}
	
	// can create an asteroid of different sizes or diamonds
	private void generateRandomEntity() {
		if (checkIfTimePassed(lastEntityTime, toCreateEntityTime, entityCoolDownTime)) {
			Entity entity = (Entity) EntityGeneration.generateRandomEntity();
			// add random position here
			entity.setX(SCREEN_SIZE_WIDTH);
			entity.setY(EntityGeneration.randomIntWithRange(150, SCREEN_SIZE_HEIGHT));
			movingEntities.add(entity);
			lastEntityTime = getTimeNow();
		}
	}
	
	// creates planets that are moving in the background
	private void generatePlanet() {
		if (checkIfTimePassed(lastPlanetTime, toCreatePlanetTime, planetCoolDownTime) && movingPlanets.size() <= 1
				|| toCreatePlanetTime == 0) {
			PlanetEntity planet = EntityGeneration.generatePlanet();
			planet.setX(EntityGeneration.randomIntWithRange(SCREEN_SIZE_WIDTH, SCREEN_SIZE_WIDTH));
			planet.setY(EntityGeneration.randomIntWithRange(0, SCREEN_SIZE_HEIGHT));
			movingPlanets.add(planet);
			lastPlanetTime = getTimeNow();
		}
	}

	private boolean isNewLevel() {
		if (checkIfTimePassed(levelStartTime, levelEndTime, levelDurationTime)) {
			return true;
		}

		return false;
	}
	
	private void removeLife() {
		lives--;
		
		if(lives <= 0) {
			gameOver = true;
		}
	}
	
	// used to show the end score once the game is over
	private void showEndScore() {
		int x = SCREEN_SIZE_WIDTH / 2 - 200;
		int y = SCREEN_SIZE_HEIGHT / 2;
		
		font.drawString(x, y - 300, "Ship has been destroyed!", Color.white);
		font.drawString(x, y - 200, "Diamonds collected: " + diamondScore, Color.yellow);
		font.drawString(x, y - 100, "Asteroids destroyed: " + asteroidScore, Color.yellow);
		font.drawString(x, y, "Press ENTER to restart");
	}
	
	// reset variables and start a new game
	private void startNewGame() {
		gameOver = false;
		lastRender = false;
		
		rocketship.setX(rocketshipStartX);
		rocketship.setY(rocketshipStartY);
		
		movingEntities = new ArrayList<Entity>();
		toCreateEntityTime = 0;
		lastEntityTime = getTimeNow();
		entityCoolDownTime = 2000;
		
		movingPlanets = new ArrayList<Entity>();
		toCreatePlanetTime = 0;
		lastPlanetTime = getTimeNow();
		planetMoveFrames = 0;
		
		levelStartTime = getTimeNow();
	    levelEndTime = 0;
		level = 0;
		
		projectiles = new ArrayList<Entity>();
		lastProjectileTime = 0;
		
		lives = 3;
		
		diamondScore = 0;
		asteroidScore = 0;
		
		allEntitiesToBeCleanedUp = new ArrayList<ArrayList<Entity>>(
				Arrays.asList(projectiles, movingEntities, movingPlanets));
	}

}
