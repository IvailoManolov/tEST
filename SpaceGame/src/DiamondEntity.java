import org.newdawn.slick.opengl.Texture;

public class DiamondEntity extends Entity{
	public DiamondEntity(Texture texture, int x, int y) {
		super(texture, x, y);
	}
	
	public DiamondEntity(Texture texture, int x, int y, int size) {
		super(texture, x, y, size);
	}
}
