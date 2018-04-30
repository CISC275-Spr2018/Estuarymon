import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ModelTest {

	@Test
	void test() {
		Model m = new Model(1,1,1,1,new Animal());
		m.damagePlant();
		
	}

}
