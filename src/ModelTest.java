import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModelTest {
	
	View view = new View();
	//view.setKeyListener(this);
	
	Model model = new Model();
	@Test
	void testDamagePlant() 
	{
		
		model.damagePlant();
		
	}
	
	@Test
	void testCheckCollision() 
	{
		Plant.plants[0].health = 0;
		model.player.xLocation = Plant.plants[0].xLocation;
		model.player.yLocation = Plant.plants[0].yLocation;
		model.testCheckColl();
		
	}
	
	@Test
	void testSpaceKeyPressed() {
		Model model = new Model();
		model.spaceKeyPressed();
		assertTrue(model.spacePressed==true);
	}
	
	@Test 
	void testSpaceKeyReleased() {
		Model model = new Model();
		model.spaceKeyReleased();
		assertTrue(model.spacePressed==false);
	}
	
	

}
