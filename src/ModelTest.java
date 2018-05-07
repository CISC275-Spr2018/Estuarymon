import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import MVC.Model;
import MapObjects.Animal;
import Player.Direction;

class ModelTest {
//	
//	View view = new View(new Animal());
//	//view.setKeyListener(this);
//	
//	Model model = new Model(view.getWidth(), view.getHeight(), view.getImageWidth(), view.getImageHeight(), new Animal());
//	@Test
//	void testDamagePlant() 
//	{
//		
//		model.damagePlant();
//		
//	}
//	
//	@Test
//	void testCheckCollision() 
//	{
//		Plant.plants[0].health = 0;
//		model.myPlayer.xLocation = Plant.plants[0].xLocation;
//		model.myPlayer.yLocation = Plant.plants[0].yLocation;
//		model.testCheckColl();
//		
//	}
//	
//	@Test
//	void testGetX() {
//		Model model = new Model(1,1,1,1,new Animal());
//		assertTrue(model.getX()==0);
//				
//	}
//	
//	@Test
//	void testGetY() {
//		Model model = new Model(1,1,1,1,new Animal());
//		Player myPlayer = new Player(1,6,160,160);
//		assertTrue(model.getY()==0);
//	}
//	
//	@Test
//	void testGetDirect() {
//		Model testModel = new Model(1,1,1,1,new Animal());
//		assertTrue(model.getDirect()==Direction.EAST);
//	}
//	
//	@Test
//	void testsetAttributes() {
//		Model model = new Model(1,1,1,1,new Animal());
//		model.setAttributes(0, Direction.NORTH, 80, 90);
//		assertTrue((model.dir==0)&&(model.getDirect()==Direction.NORTH)&&(model.xIncr==80)&&(model.yIncr==90));
//	}
//	
//	@Test 
//	void testStop() {
//		Model model = new Model(1,1,1,1,new Animal());
//		model.stop();
//		assertTrue((model.xIncr==0)&&(model.yIncr==0));
//	}
	
	@Test
	void testCrabMotion() {
		Model model = new Model(1000,1000);
		Animal crab = model.getAnimal();
		
		
		crab.setXLocation(0);
		crab.setDirection(Direction.WEST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		assertTrue(crab.getDirection() == Direction.EAST && crab.getXLocation() > 0);
		
		crab.setXLocation(1000);
		crab.setDirection(Direction.EAST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setXLocation(1000);
		crab.setDirection(Direction.SOUTHEAST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setXLocation(1000);
		crab.setDirection(Direction.NORTHEAST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setXLocation(0);
		crab.setDirection(Direction.NORTHWEST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setXLocation(0);
		crab.setDirection(Direction.SOUTHWEST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setYLocation(0);
		crab.setDirection(Direction.NORTH);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setYLocation(1300);
		crab.setDirection(Direction.SOUTH);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setYLocation(0);
		crab.setDirection(Direction.NORTHEAST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setYLocation(0);
		crab.setDirection(Direction.NORTHWEST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setYLocation(1300);
		crab.setDirection(Direction.SOUTHWEST);
		model.animalWallCollision();
		model.updatingAnimalLocation();
		
		crab.setYLocation(1300);
		crab.setDirection(Direction.SOUTHEAST);
		model.animalWallCollision();
		model.updatingAnimalLocation();		
		
	}
	
	
	
	
	
	

}
