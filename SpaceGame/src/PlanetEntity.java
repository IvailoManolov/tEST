import org.newdawn.slick.opengl.Texture;

public class PlanetEntity extends Entity{
	public PlanetEntity(Texture texture, int x, int y) {
		super(texture, x, y);
	}
	
	public PlanetEntity(Texture texture, int x, int y, int size) {
		super(texture, x, y, size);
	}
}
