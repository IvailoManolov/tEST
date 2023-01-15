import java.awt.Rectangle;

import org.newdawn.slick.opengl.Texture;

// an entity can have a velocity, moving on the X-axis and Y-axis
public abstract class Entity {
	private int x;
	private int y;
	private int velocityX;
	private int velocityY;
	private int size = 1;
	private Texture texture;
	private MySprite sprite;
	
	private Rectangle me = new Rectangle();
	private Rectangle him = new Rectangle();

	public Entity(Texture texture, int x, int y) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		this.sprite = createSprite();
	}
	
	public Entity(Texture texture, int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		this.size = size;
		this.sprite = createSprite();
	}
	
	private MySprite createSprite() {
		return new MySprite(this.texture, this.size);
	}

	public boolean collidesWith(Entity other) {
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds((int) other.x, (int) other.y, other.sprite.getWidth(),
				other.sprite.getHeight());

		return me.intersects(him);
	}
	
	public void draw() {
		x += velocityX;
		y += velocityY;
		sprite.draw(x, y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return sprite.getWidth();
	}

	public int getHeight() {
		return sprite.getHeight();
	}

	public int getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(int velocityX) {
		this.velocityX = velocityX;
	}

	public int getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(int velocityY) {
		this.velocityY = velocityY;
	}
	
	public MySprite getSprite() {
		return sprite;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
