package scot.ianmacdonald.cakemgr.webapp.model;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import junit.framework.Assert;

public class CakeDAOFactoryTest {

	@Test
	public void testGetCakeDAO() {
		
		// use the CakeDAOFactory to get a reference to a CakeDAO and test no exception is thrown
		CakeDAO cakeDAO = null;
		try {
			cakeDAO = CakeDAOFactory.getCakeDAO();
		} catch (IOException e) {
			e.printStackTrace();
			fail("An IOException was thrown when creating a reference to the CakeDAO");
		}
		
		/*
		 * not much we can test at the moment except that it's an instance of a HibernateCakeDAO
		 * which has its own unit tests
		 */
		Assert.assertTrue("Returned CakeDAO was not an instance of HibernateCakeDAO", cakeDAO instanceof HibernateCakeDAO);
		
	}

}
