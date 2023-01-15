import org.newdawn.slick.opengl.Texture;

public class AsteroidEntity extends Entity{
	// asteroids have different durability based on their size
	private int durability = 1;

	public AsteroidEntity(Texture texture, int x, int y) {
		super(texture, x, y);
	}
	
	public AsteroidEntity(Texture texture, int x, int y, int size) {
		super(texture, x, y, size);
		this.durability = size;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}
}
