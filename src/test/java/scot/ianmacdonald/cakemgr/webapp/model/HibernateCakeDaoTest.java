package scot.ianmacdonald.cakemgr.webapp.model;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

/**
 * JUnit test class for {@link HibernateCakeDao}
 * Also effectively tests the {@link HibernateUtil} class in this configuration
 * 
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class HibernateCakeDaoTest {

	@Test
	public void testReadAll() throws IOException {

		// get a reference to a HibernateCakeDao
		final CakeDao cakeDao = new HibernateCakeDao();

		// test the readAll() method returns the values expected based on the JSON data feed
		final List<CakeEntity> expectedValues = createInitialExpectedValues();

		// call the method under test
		final List<CakeEntity> actualValues = cakeDao.readAll();
		
		// compare the expected and actual values using List.equals(Object o)
		Assert.assertEquals(expectedValues, actualValues);
	}
	
	@Test
	public void testCreate() {
		
		// get a reference to the HibernateCakeDao
		CakeDao cakeDao = null;
		try {
			cakeDao = new HibernateCakeDao();
		} catch (CakeDaoConstraintViolationException e) {
			e.printStackTrace();
			fail("An IOException was thrown when creating a reference to the CakeDao");
		}
		
		// create the CakeEntity we are going to add to the DB
		CakeEntity banoffeePie = new CakeEntity(null, "Banoffee Pie", "Is it banana or toffee?  Who can tell?",
				"https://www.bakedbyanintrovert.com/wp-content/uploads/2019/02/Banoffee-Pie-Recipe-Image-735x735.jpg");
		List<CakeEntity> expectedValues = createInitialExpectedValues();
		expectedValues.add(banoffeePie);
		
		// add banoffee pie to the DB
		cakeDao.create(banoffeePie);
		
		// get the new list of actual values by reading all cakes from the DB
		List<CakeEntity> actualValues = cakeDao.readAll();
		
		// compare the expected and actual values using List.equals(Object o)
		Assert.assertEquals(expectedValues, actualValues);
		
		// test that a cake violating the uniqueness constraint on title is not created
		CakeEntity lemonCheeseCake = new CakeEntity(null, "Lemon cheesecake", "A cheesecake made of lemon",
				"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");
		try {
		cakeDao.create(lemonCheeseCake);
		} catch (CakeDaoConstraintViolationException ex) {
			// this is expected, we are choosing to do nothing here to check what's in the DB
		}
		actualValues = cakeDao.readAll();
		Assert.assertEquals(expectedValues, actualValues);
	}

	/**
	 * Create a List of CakeEntity values corresponding to the JSON feed
	 */
	private List<CakeEntity> createInitialExpectedValues() {
		final List<CakeEntity> expectedValues = new ArrayList<>();
		CakeEntity lemonCheeseCake =  new CakeEntity(null, "Lemon cheesecake", "A cheesecake made of lemon",
				"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");
		CakeEntity victoriaSponge = new CakeEntity(null, "victoria sponge", "sponge with jam",
				"http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg");
		CakeEntity carrotCake = new CakeEntity(null, "Carrot cake", "Bugs bunnys favourite",
				"http://www.villageinn.com/i/pies/profile/carrotcake_main1.jpg");
		CakeEntity bananaCake = new CakeEntity(null, "Banana cake", "Donkey kongs favourite",
				"http://ukcdn.ar-cdn.com/recipes/xlarge/ff22df7f-dbcd-4a09-81f7-9c1d8395d936.jpg");
		CakeEntity birthdayCake = new CakeEntity(null, "Birthday cake", "a yearly treat",
				"http://cornandco.com/wp-content/uploads/2014/05/birthday-cake-popcorn.jpg");
		
		expectedValues.add(lemonCheeseCake);
		expectedValues.add(victoriaSponge);
		expectedValues.add(carrotCake);
		expectedValues.add(bananaCake);
		expectedValues.add(birthdayCake);
		return expectedValues;
	}

}
