import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModelTest {
	
	View view = new View();
	//view.setKeyListener(this);
	
	Model model = new Model(view.getWidth(), view.getHeight(), view.getImageWidth(), view.getImageHeight() );
	@Test
	void testDamagePlant() 
	{
		
		model.damagePlant();
		
	}
	
	@Test
	void testCheckCollision() 
	{
		Plant.plants[0].health = 0;
		model.myPlayer.xLocation = Plant.plants[0].xLocation;
		model.myPlayer.yLocation = Plant.plants[0].yLocation;
		model.testCheckColl();
		
	}
	
	@Test
	void testGetX() {
		Model model = new Model(1,1,1,1);
		assertTrue(model.getX()==0);
				
	}
	
	@Test
	void testGetY() {
		Model model = new Model(1,1,1,1);
		Player myPlayer = new Player(1,6,160,160);
		assertTrue(model.getY()==0);
	}
	
	@Test
	void testGetDirect() {
		Model testModel = new Model(1,1,1,1);
		assertTrue(model.getDirect()==Direction.EAST);
	}
	
	@Test
	void testsetAttributes() {
		Model model = new Model(1,1,1,1);
		model.setPlayerAttributes(0, Direction.NORTH, 80, 90);
		assertTrue((model.dir==0)&&(model.getDirect()==Direction.NORTH)&&(model.xIncr==80)&&(model.yIncr==90));
	}
	
	@Test 
	void testStop() {
		Model model = new Model(1,1,1,1);
		model.stop();
		assertTrue((model.xIncr==0)&&(model.yIncr==0));
	}
	
	
	
	
	
	

}
