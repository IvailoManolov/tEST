class EntityGeneration {

	public static Entity generateRandomEntity() {
		
		int r = randomIntWithRange(1, 45);
		// how often each entity is generated, depends on a range
		// smaller asteroids are generated the most
		if(r >= 1 && r <= 2) {
			return generateDiamond();
		} else if (r >= 3 && r <= 4) {
			return generateLargeAsteroid();
		} else if ( r >= 5 && r <= 10) {
			return generateMediumAsteroid();
		} else if (r >= 7 && r <= 45) {
			return generateSmallAsteroid();
		}
		
		return generateSmallAsteroid();
	}
	
	public static PlanetEntity generatePlanet() {
		PlanetEntity planet = new PlanetEntity(
				Textures.planetTextures[(randomIntWithRange(0, Textures.planetTextures.length - 1))], 0, 0, 1);
		return planet;
	}
	
	public static AsteroidEntity generateSmallAsteroid() {
		AsteroidEntity asteroid = generateAsteroidWithSize(1);
		asteroid.setVelocityX(randomIntWithRange(-6, -20));
		return asteroid;
	}
	
	public static AsteroidEntity generateMediumAsteroid() {
		AsteroidEntity asteroid = generateAsteroidWithSize(2);
		asteroid.setVelocityX(randomIntWithRange(-2, -4));
		return asteroid;
	}
	
	public static AsteroidEntity generateLargeAsteroid() {
		AsteroidEntity asteroid = generateAsteroidWithSize(3);
		asteroid.setVelocityX(-2);
		return asteroid;
	}
	
	public static AsteroidEntity generateAsteroidWithSize(int size) {
		return new AsteroidEntity(
				Textures.asteroidTextures[(randomIntWithRange(0, Textures.asteroidTextures.length - 1))], 0, 0, size);
	}
	
	public static DiamondEntity generateDiamond() {
		DiamondEntity diamond = new DiamondEntity(
				Textures.diamondTextures[(randomIntWithRange(0, Textures.diamondTextures.length - 1))], 0, 0, 1);
		diamond.setVelocityX(-6);
		return diamond;
	}
	
	public static int randomIntWithRange(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
}
