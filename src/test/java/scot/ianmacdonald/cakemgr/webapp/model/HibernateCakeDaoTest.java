package scot.ianmacdonald.cakemgr.webapp.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * JUnit test class for {@link HibernateCakeDao} Also effectively tests the
 * {@link HibernateUtil} class in this configuration
 * 
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class HibernateCakeDaoTest {

	/*
	 * Rule needed by tests which are expected to throw exceptions
	 */
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	// data used by tests
	private CakeEntity lemonCheeseCake = new CakeEntity(null, "Lemon cheesecake", "A cheesecake made of lemon",
			"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");

	// object under test
	private CakeDao cakeDao = new HibernateCakeDao();

	@Test
	public void testReadAll() {

		// set up the expected values of the test
		final List<CakeEntity> expectedValues = createInitialExpectedValues();

		// get the actual values for the test
		final List<CakeEntity> actualValues = cakeDao.readAll();

		// compare the expected and actual values
		Assert.assertEquals("Initial CakeEntity values read from the DB were not as expected ", expectedValues,
				actualValues);
	}

	@Test
	public void testCreate() {

		// set up the expected values of the test
		final List<CakeEntity> expectedValues = createInitialExpectedValues();

		// create the CakeEntity we are going to add to the DB
		CakeEntity banoffeePie = new CakeEntity(null, "Banoffee Pie", "Is it banana or toffee?  Who can tell?",
				"https://www.bakedbyanintrovert.com/wp-content/uploads/2019/02/Banoffee-Pie-Recipe-Image-735x735.jpg");
		expectedValues.add(banoffeePie);

		// add banoffee pie to the DB
		cakeDao.create(banoffeePie);

		// get the new list of actual values by reading all cakes from the DB
		List<CakeEntity> actualValues = cakeDao.readAll();

		// compare the expected and actual values using List.equals(Object o)
		Assert.assertEquals(expectedValues, actualValues);

	}

	@Test
	public void testExceptionThrownWhenDuplicateCreated() {

		// set up the expectations for the test
		expectedEx.expect(CakeDaoConstraintViolationException.class);
		expectedEx.expectMessage("A cake with the title [Lemon cheesecake] already exists in the DB");

		// exercise the method under test
		cakeDao.create(lemonCheeseCake);

	}

	/**
	 * Create a List of CakeEntity values corresponding to the JSON feed
	 */
	private List<CakeEntity> createInitialExpectedValues() {
		final List<CakeEntity> expectedValues = new ArrayList<>();
		CakeEntity lemonCheeseCake = new CakeEntity(null, "Lemon cheesecake", "A cheesecake made of lemon",
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
