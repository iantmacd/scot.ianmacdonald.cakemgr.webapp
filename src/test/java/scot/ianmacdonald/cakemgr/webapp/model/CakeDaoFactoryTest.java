package scot.ianmacdonald.cakemgr.webapp.model;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

public class CakeDaoFactoryTest {

	@Test
	public void testGetCakeDao() {
		
		// use the CakeDaoFactory to get a reference to a CakeDao and test no exception is thrown
		CakeDao cakeDao = null;
		try {
			cakeDao = CakeDaoFactory.getCakeDao();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			fail("An IOException was thrown when creating a reference to the CakeDao");
		}
		
		/*
		 * not much we can test at the moment except that it's an instance of a HibernateCakeDao
		 * which has its own unit tests
		 */
		Assert.assertTrue("Returned CakeDao was not an instance of HibernateCakeDao", cakeDao instanceof HibernateCakeDao);
		
	}

}
