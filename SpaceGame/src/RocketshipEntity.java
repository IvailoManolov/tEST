import org.newdawn.slick.opengl.Texture;

public class RocketshipEntity extends Entity {
	
	public RocketshipEntity(Texture texture, int x, int y) {
		super(texture, x, y);
	}
	
	public RocketshipEntity(Texture texture, int x, int y, int size) {
		super(texture, x, y, size);
	}
	
	// positions of cannons are used, so projectiles can come out of the middle of the ship
	public int getCannonPosX() {
		return super.getSprite().getWidth() + super.getX();
	}
	
	public int getCannonPosY() {
		return (super.getSprite().getHeight() / 2 + 5) + super.getY();
	}
	
}
