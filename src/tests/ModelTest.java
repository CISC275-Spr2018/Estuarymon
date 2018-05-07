package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MVC.Controller;
import MVC.Model;
import MVC.View;
import MapObjects.Animal;
import MapObjects.Litter;
import MapObjects.LitterType;
import MapObjects.Plant;
import Player.Direction;
import Player.Player;
/**
 * 
 * @author Juan Villacis 
 *
 */
class ModelTest {
	
	//View view = new View(new Animal());
	//view.setKeyListener(this);
	
	Model model = new Model(1000,1000);
	@Test
	void testDamagePlant() 
	{
		
		model.damagePlant();
		
	}
	
	@Test
	void testSpaceKeyPressed() {
		Model model = new Model(1000,1000);
		model.spaceKeyPressed();
		assertTrue(model.getSpacePressed()==true);
	}
	
	@Test
	void testSpaceKeyRelease() {
		Model model = new Model(1000,1000);
		model.spaceKeyReleased();
		assertTrue(model.getSpacePressed()==false);
	}
	
	@Test
	void testAnimalWallCollisionLeft() {
		Model model = new Model(1000,1000);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setDirection(Direction.WEST);
		model.animalWallCollision();
		//assertTrue(model.getCrabDirection()==1);
		assertTrue(model.getAnimal().getDirection()==Direction.EAST);
	}
	
	@Test
	void testAnimalWallCollisionRight() {
		Model model = new Model(1000,1000);
		model.getAnimal().setXLocation(model.getWidth());
		
		model.animalWallCollision();
		
	}
	
	@Test
	void testAnimalWallCollisionTop() {
		Model model = new Model(1000,1000);
		model.getAnimal().setYLocation(0);
		model.getAnimal().setDirection(Direction.NORTH);;
		model.animalWallCollision();
		assertTrue(model.getAnimal().getDirection()==Direction.SOUTH);
	}
	
	@Test
	void testAnimalWallCollisionBottom() {
		Model model = new Model(1000,1000);
		model.getAnimal().setYLocation(model.getHeight());
		model.getAnimal().setDirection(Direction.SOUTHWEST);
		model.animalWallCollision();
		assertTrue(model.getAnimal().getDirection()==Direction.NORTHWEST);
	}
	
	@Test
	void testPlayerLitterCollision() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(1);
		model.getPlayer().setYLocation(1);
		Litter l = new Litter();
		l.setXLocation(1);
		l.setYLocation(1);
		Litter.litterSet.add(l);
		assertTrue(model.testCheckColl()==true);
		Litter.litterSet.remove(l);
	}
	
	@Test
	void testPlayerLitterCollisionFalse() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(1);
		model.getPlayer().setYLocation(1);
		Litter l = new Litter();
		l.setXLocation(200);
		l.setYLocation(200);
		Litter.litterSet.add(l);
		assertTrue(model.testCheckColl()==false);
		Litter.litterSet.remove(l);
	}
	
	@Test
	void testPlayerPlantCollision() {
		Model model = new Model(1000,1000);
		Plant.plants[0].health = 0;
		model.getPlayer().setXLocation(Plant.plants[0].getXLocation()+10);
		model.getPlayer().setYLocation(Plant.plants[0].getYLocation()-8);
		assertTrue(model.testCheckColl()==true);
	}
	
	@Test
	void testPlayerPlantCollisionFalse() {
		Model model = new Model(1000,1000);
		Plant.plants[0].health = 0;
		model.getPlayer().setXLocation(300);
		model.getPlayer().setYLocation(300);
		assertTrue(model.testCheckColl()==false);
	}
	
	@Test
	void testPlayerTrashBinCollision() {
		Model model = new Model(1000,1000);
		model.getPlayer().hasLitter = true;
		model.getPlayer().setXLocation(model.getTBin().getXLocation());
		model.getPlayer().setYLocation(model.getTBin().getYLocation());
		Litter  l = new Litter();
		l.setType(LitterType.TRASH);
		model.setPickedUpLitter(l);
		assertTrue(model.testCheckColl()==true);
	}
	
	@Test
	void testPlayerTrashBinCollisionFalse() {
		Model model = new Model(1000,1000);
		model.getPlayer().hasLitter = true;
		model.getPlayer().setXLocation(0);
		model.getPlayer().setYLocation(0);
		assertTrue(model.testCheckColl()==false);
	}
	
	@Test
	void testPlayerRecycleBinCollision() {
		Model model = new Model(1000,1000);
		model.getPlayer().hasLitter = true;
		model.getPlayer().setXLocation(model.getRBin().getXLocation());
		model.getPlayer().setYLocation(model.getRBin().getYLocation());
		Litter  l = new Litter();
		l.setType(LitterType.RECYCLABLE);
		model.setPickedUpLitter(l);
		assertTrue(model.testCheckColl()==true);
	}
	
	@Test 
	void testAnimalLitterCollision() {
		Model model = new Model(1000,1000);
		model.getAnimal().setXLocation(50);
		model.getAnimal().setYLocation(50);
		Litter l = new Litter();
		l.setXLocation(50);
		l.setYLocation(50);
		Litter.litterSet.add(l);
		assertTrue(model.testCheckColl()==true);
		Litter.litterSet.remove(l);
	}
	
	@Test 
	void testAnimalLitterCollisionFalse() {
		Model model = new Model(1000,1000);
		model.getAnimal().setXLocation(50);
		model.getAnimal().setYLocation(50);
		Litter l = new Litter();
		l.setXLocation(900);
		l.setYLocation(900);
		Litter.litterSet.add(l);
		assertTrue(model.testCheckColl()==true);
		Litter.litterSet.remove(l);
	}
	
	/*@Test
	void testUpdatingAnimalLocationEast() {
		Model model = new Model(1000,1000);
		model.setCrabDirection(1);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getXLocation()==model.getAnimalXIncr());
	}
	
	@Test
	void testUpdatingAnimalLocationWest() {
		Model model = new Model(1000,1000);
		model.setCrabDirection(2);
		model.getAnimal().setXLocation(3);
		model.getAnimal().setYLocation(3);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getXLocation()==0);
	}
	
	@Test
	void testUpdatingAnimalLocationSouth() {
		Model model = new Model(1000,1000);
		model.setCrabDirection(3);
		model.getAnimal().setXLocation(3);
		model.getAnimal().setYLocation(3);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getYLocation()==0);
	}
	
	@Test
	void testUpdatingAnimalLocationNorth() {
		Model model = new Model(1000,1000);
		model.setCrabDirection(4);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getYLocation()==model.getAnimalYIncr());
	}
	*/
	@Test 
	void testSpawnLitterXCoord() {
		Model model = new Model(1000,1000);
		Litter l = model.spawnLitter();
		assertTrue(l.getXLocation() >=0 && l.getXLocation() <=(model.getWidth()-l.getWidth()));
		Litter.litterSet.remove(l);
	}
	
	@Test
	void testSpawnLitterYCoord() {
		Model model = new Model(1000,1000);
		Litter l = model.spawnLitter();
		assertTrue(l.getYLocation() >=0 && l.getYLocation() <=(model.getHeight()-l.getHeight()));
		Litter.litterSet.remove(l);
		
	}
	
	@Test
	void testGetPickedUpLitter() {
			Model model = new Model(1000,1000);
			model.getPlayer().setXLocation(1);
			model.getPlayer().setYLocation(1);
			model.spaceKeyPressed();
			Litter l = new Litter();
			l.setXLocation(1);
			l.setYLocation(1);
			Litter.litterSet.add(l);
			model.testCheckColl();
			assertTrue(model.getPickedUpLitter()==l);
			Litter.litterSet.remove(l);
	}
	
	@Test
	void testGetAnimalXIncr() {
		Model model = new Model(1000,1000);
		assertTrue(model.getAnimalXIncr()==4);
	}
	
	@Test 
	void testGetAnimalYIncr() {
		Model model = new Model(1000,1000);
		assertTrue(model.getAnimalYIncr()==4);
	}
	
	@Test
	void testGetScore() {
		Model model = new Model(1000,1000);
		assertTrue(model.getScore()==0);
	}
	
	@Test
	void testPlayerAnimalCollision() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		assertTrue(model.testCheckColl()==true);
	}
	
	@Test
	void testPlayerAnimalCollision2() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		model.getPlayer().setDirection(Direction.SOUTHEAST);
		model.testCheckColl();
		assertTrue(model.getAnimal().getDirection()==Direction.SOUTHEAST);
	}
	
	@Test
	void testScore() {
		Model model = new Model(1000,1000);
		model.changeScore(-5);
		assertTrue(model.getScore()==0);
	}
	
	@Test
	void testPlayerAnimalCollision3() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		model.getPlayer().setDirection(Direction.WEST);
		model.testCheckColl();
		assertTrue(model.getAnimal().getDirection()==Direction.WEST);
	}
	
	@Test
	void testPlayerAnimalCollision4() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		model.getPlayer().setDirection(Direction.NORTH);
		model.testCheckColl();
		assertTrue(model.getAnimal().getDirection()==Direction.NORTH);
		
		
	}
	
	@Test
	void testPlayerAnimalCollision5() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		model.getPlayer().setDirection(Direction.SOUTH);
		model.testCheckColl();
		assertTrue(model.getAnimal().getDirection()==Direction.SOUTH);
	}
	
	@Test
	void testPlayerAnimalCollision6() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		model.getPlayer().setDirection(Direction.NORTHEAST);
		model.testCheckColl();
		assertTrue(model.getAnimal().getDirection()==Direction.NORTHEAST);
	}
	
	@Test
	void testPlayerAnimalCollision7() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		model.getPlayer().setDirection(Direction.NORTHWEST);
		model.testCheckColl();
		assertTrue(model.getAnimal().getDirection()==Direction.NORTHWEST);
	}
	
	@Test
	void testPlayerAnimalCollision8() {
		Model model = new Model(1000,1000);
		model.getPlayer().setXLocation(0);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.getPlayer().setYLocation(0);
		model.getPlayer().setDirection(Direction.SOUTHWEST);
		model.testCheckColl();
		assertTrue(model.getAnimal().getDirection()==Direction.SOUTHWEST);
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	

}
