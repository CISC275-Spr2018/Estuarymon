import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModelTest {
	
	View view = new View(new Animal());
	//view.setKeyListener(this);
	
	Model model = new Model(view.getWidth(), view.getHeight(), view.getImageWidth(), view.getImageHeight(), new Animal());
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
	
	
	

}
