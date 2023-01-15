import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

// load most of the game textures in this static class

class Textures {
	public static Texture rocketshipTexture = loadTexture("PNG", "res/rocketship.png");
	public static Texture shipLifeTexture = loadTexture("PNG", "res/ship_life1.png");
	public static Texture backgroundTexture = loadTexture("JPEG", "res/background.jpeg");
	public static Texture projectileTexture = loadTexture("PNG", "res/projectile.png");
	public static Texture diamondScoreTexture = loadTexture("PNG", "res/diamond_score.png");
	public static Texture asteroidScoreTexture = loadTexture("PNG", "res/asteroid_score.png");
	
	// store textures in array, randomly generated entities also have a random texture
	final static Texture[] asteroidTextures = {
			loadTexture("PNG", "res/asteroid_1.png"),
			loadTexture("PNG", "res/asteroid_2.png"),
			loadTexture("PNG", "res/asteroid_3.png"),
			loadTexture("PNG", "res/asteroid_4.png"),
	};
	
	final static Texture[] diamondTextures = {
			loadTexture("PNG", "res/diamond_1.png"),
			loadTexture("PNG", "res/diamond_2.png"),
			loadTexture("PNG", "res/diamond_3.png"),
			loadTexture("PNG", "res/diamond_4.png")
	};
	
	final static Texture[] planetTextures = {
			loadTexture("PNG", "res/planets/1.png"),
			loadTexture("PNG", "res/planets/2.png"),
			loadTexture("PNG", "res/planets/3.png"),
			loadTexture("PNG", "res/planets/4.png"),
			loadTexture("PNG", "res/planets/5.png"),
			loadTexture("PNG", "res/planets/6.png"),
			loadTexture("PNG", "res/planets/7.png"),
			loadTexture("PNG", "res/planets/8.png"),
			loadTexture("PNG", "res/planets/9.png"),
			loadTexture("PNG", "res/planets/10.png"),
			loadTexture("PNG", "res/planets/11.png"),
			loadTexture("PNG", "res/planets/12.png"),
			loadTexture("PNG", "res/planets/13.png"),
			loadTexture("PNG", "res/planets/14.png"),
			loadTexture("PNG", "res/planets/15.png"),
			loadTexture("PNG", "res/planets/16.png"),
			loadTexture("PNG", "res/planets/17.png"),
			loadTexture("PNG", "res/planets/18.png"),
			loadTexture("PNG", "res/planets/19.png"),
			loadTexture("PNG", "res/planets/20.png"),
			loadTexture("PNG", "res/planets/21.png"),
			loadTexture("PNG", "res/planets/22.png"),
			loadTexture("PNG", "res/planets/23.png"),
			loadTexture("PNG", "res/planets/24.png"),
			loadTexture("PNG", "res/planets/25.png"),
			loadTexture("PNG", "res/planets/26.png"),
			loadTexture("PNG", "res/planets/27.png"),
	};
	
	// load texture
	private static Texture loadTexture(String fileExtension, String fileName) {
		try {
			return TextureLoader.getTexture(fileExtension, ResourceLoader.getResourceAsStream(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
