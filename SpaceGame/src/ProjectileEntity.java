import org.newdawn.slick.opengl.Texture;

public class ProjectileEntity extends Entity{
	public ProjectileEntity(Texture texture, int x, int y) {
		super(texture, x, y);
	}
	
	public ProjectileEntity(Texture texture, int x, int y, int size) {
		super(texture, x, y, size);
	}
}
