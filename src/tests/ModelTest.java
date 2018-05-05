package tests;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MVC.Controller;
import MVC.Model;
import MVC.View;
import MapObjects.Animal;
import MapObjects.Litter;
import MapObjects.Plant;
import Player.Direction;
import Player.Player;

class ModelTest {
	
	//View view = new View(new Animal());
	//view.setKeyListener(this);
	
	Model model = new Model();
	@Test
	void testDamagePlant() 
	{
		
		model.damagePlant();
		
	}
	
	@Test
	void testSpaceKeyPressed() {
		Model model = new Model();
		model.spaceKeyPressed();
		assertTrue(model.getSpacePressed()==true);
	}
	
	@Test
	void testSpaceKeyRelease() {
		Model model = new Model();
		model.spaceKeyReleased();
		assertTrue(model.getSpacePressed()==false);
	}
	
	@Test
	void testAnimalWallCollisionLeft() {
		Model model = new Model();
		model.getAnimal().setXLocation(0);
		model.animalWallCollision();
		assertTrue(model.getCrabDirection()==1);
	}
	
	@Test
	void testAnimalWallCollisionRight() {
		Model model = new Model();
		model.getAnimal().setXLocation(Controller.WORLD_WIDTH);
		model.animalWallCollision();
		assertTrue(model.getCrabDirection()==2);
	}
	
	@Test
	void testAnimalWallCollisionTop() {
		Model model = new Model();
		model.getAnimal().setYLocation(0);
		model.animalWallCollision();
		assertTrue(model.getCrabDirection()==4);
	}
	
	@Test
	void testAnimalWallCollisionBottom() {
		Model model = new Model();
		model.getAnimal().setYLocation(Controller.WORLD_HEIGHT);
		model.animalWallCollision();
		assertTrue(model.getCrabDirection()==3);
	}
	
	@Test
	void testPlayerLitterCollision() {
		Model model = new Model();
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
		Model model = new Model();
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
		Model model = new Model();
		Plant.plants[0].health = 0;
		model.getPlayer().setXLocation(Plant.plants[0].getXLocation()+10);
		model.getPlayer().setYLocation(Plant.plants[0].getYLocation()-8);
		assertTrue(model.testCheckColl()==true);
	}
	
	@Test
	void testPlayerPlantCollisionFalse() {
		Model model = new Model();
		Plant.plants[0].health = 0;
		model.getPlayer().setXLocation(300);
		model.getPlayer().setYLocation(300);
		assertTrue(model.testCheckColl()==false);
	}
	
	@Test
	void testPlayerTrashBinCollision() {
		Model model = new Model();
		model.getPlayer().hasLitter = true;
		model.getPlayer().setXLocation(model.getTBin().getXLocation());
		model.getPlayer().setYLocation(model.getTBin().getYLocation());
		assertTrue(model.testCheckColl()==true);
	}
	
	@Test
	void testPlayerTrashBinCollisionFalse() {
		Model model = new Model();
		model.getPlayer().hasLitter = true;
		model.getPlayer().setXLocation(0);
		model.getPlayer().setYLocation(0);
		assertTrue(model.testCheckColl()==false);
	}
	
	@Test
	void testPlayerRecycleBinCollision() {
		Model model = new Model();
		model.getPlayer().hasLitter = true;
		model.getPlayer().setXLocation(model.getRBin().getXLocation());
		model.getPlayer().setYLocation(model.getRBin().getYLocation());
		assertTrue(model.testCheckColl()==true);
	}
	
	@Test 
	void testAnimalLitterCollision() {
		Model model = new Model();
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
		Model model = new Model();
		model.getAnimal().setXLocation(50);
		model.getAnimal().setYLocation(50);
		Litter l = new Litter();
		l.setXLocation(300);
		l.setYLocation(300);
		Litter.litterSet.add(l);
		assertTrue(model.testCheckColl()==false);
		Litter.litterSet.remove(l);
	}
	
	@Test
	void testUpdatingAnimalLocationEast() {
		Model model = new Model();
		model.setCrabDirection(1);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getXLocation()==model.getAnimalXIncr());
	}
	
	@Test
	void testUpdatingAnimalLocationWest() {
		Model model = new Model();
		model.setCrabDirection(2);
		model.getAnimal().setXLocation(3);
		model.getAnimal().setYLocation(3);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getXLocation()==0);
	}
	
	@Test
	void testUpdatingAnimalLocationSouth() {
		Model model = new Model();
		model.setCrabDirection(3);
		model.getAnimal().setXLocation(3);
		model.getAnimal().setYLocation(3);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getYLocation()==0);
	}
	
	@Test
	void testUpdatingAnimalLocationNorth() {
		Model model = new Model();
		model.setCrabDirection(4);
		model.getAnimal().setXLocation(0);
		model.getAnimal().setYLocation(0);
		model.updatingAnimalLocation();
		assertTrue(model.getAnimal().getYLocation()==model.getAnimalYIncr());
	}
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	

}
