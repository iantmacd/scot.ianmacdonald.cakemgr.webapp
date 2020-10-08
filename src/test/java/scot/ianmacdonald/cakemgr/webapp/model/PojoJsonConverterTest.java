package scot.ianmacdonald.cakemgr.webapp.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests the conversion between JSON String objects and POJOs and vice versa for
 * single objects and for List<T>. Tests that appropriate exceptions are thrown
 * with correct messages under error conditions.
 * 
 * @author ian.macdonald@ianmacdonald.scot
 */
public class PojoJsonConverterTest {

	/*
	 * Rule needed by tests which are expected to throw exceptions
	 */
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	// common test data
	private final String lemonCheeseCakeJson = "{\n" + "  \"id\" : null,\n" + "  \"title\" : \"Lemon cheesecake\",\n"
			+ "  \"desc\" : \"A cheesecake made of lemon\",\n"
			+ "  \"image\" : \"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"\n"
			+ "}";
	private final String cakeListJson = "[ {\n" + "  \"id\" : null,\n" + "  \"title\" : \"Lemon cheesecake\",\n"
			+ "  \"desc\" : \"A cheesecake made of lemon\",\n"
			+ "  \"image\" : \"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"\n"
			+ "}, {\n" + "  \"id\" : null,\n" + "  \"title\" : \"Banana cake\",\n"
			+ "  \"desc\" : \"Donkey kongs favourite\",\n"
			+ "  \"image\" : \"http://ukcdn.ar-cdn.com/recipes/xlarge/ff22df7f-dbcd-4a09-81f7-9c1d8395d936.jpg\"\n"
			+ "} ]";
	private final String malformedLemonCheeseCakeJson = "{\n" + "  \"id\" : null,\n"
			+ "  \"title\" : :Lemon cheesecake\",\n" + "  \"desc\" : \"A cheesecake made of lemon\",\n"
			+ "  \"image\" : \"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"\n"
			+ "}";
	private final String malformedCakeListJson = "[ {\n" + "  \"id\" : null,\n"
			+ "  \"title\" : \"Lemon cheesecake\",\n" + "  \"desc\" : \"A cheesecake made of lemon\",\n"
			+ "  \"image\" : \"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"\n"
			+ "}, {\n" + "  \"id\" : null,\n" + "  \"title\" : \"Banana cake\",\n"
			+ "  \"desc\" : (Donkey kongs favourite\",\n"
			+ "  \"image\" : \"http://ukcdn.ar-cdn.com/recipes/xlarge/ff22df7f-dbcd-4a09-81f7-9c1d8395d936.jpg\"\n"
			+ "} ]";
	private final CakeEntity lemonCheeseCake = new CakeEntity(null, "Lemon cheesecake", "A cheesecake made of lemon",
			"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");
	private final CakeEntity bananaCake = new CakeEntity(null, "Banana cake", "Donkey kongs favourite",
			"http://ukcdn.ar-cdn.com/recipes/xlarge/ff22df7f-dbcd-4a09-81f7-9c1d8395d936.jpg");

	@Test
	public void testJsonToPojo() throws PojoJsonConverterException {

		// create expected result
		final CakeEntity expectedValue = lemonCheeseCake;

		// get actual result
		final CakeEntity actualValue = PojoJsonConverter.jsonToPojo(lemonCheeseCakeJson, CakeEntity.class);

		// compare the results
		assertEquals("The two CakeEntity objects were not equal by value", expectedValue, actualValue);
	}

	@Test
	public void testMalformedJsonToPojo() {

		// set up the expectations for the test
		expectedEx.expect(PojoJsonConverterException.class);
		expectedEx.expectMessage("Problem converting JSON object to scot.ianmacdonald.cakemgr.webapp.model.CakeEntity");

		// exercise the method under test
		PojoJsonConverter.jsonToPojo(malformedLemonCheeseCakeJson, CakeEntity.class);

	}

	@Test
	public void testJsonToPojoList() throws PojoJsonConverterException {

		// create expected result
		final List<CakeEntity> expectedValue = new ArrayList<>();
		expectedValue.add(lemonCheeseCake);
		expectedValue.add(bananaCake);

		// get actual result
		final List<CakeEntity> actualValue = PojoJsonConverter.jsonToPojoList(cakeListJson, CakeEntity[].class);

		// compare the results
		Assert.assertEquals("The two CakeEntity lists were not equal by value", expectedValue, actualValue);
	}

	@Test
	public void testMalformedJsonToPojoList() {

		// set up the expectations for the test
		expectedEx.expect(PojoJsonConverterException.class);
		expectedEx.expectMessage(
				"Problem converting JSON array of [Lscot.ianmacdonald.cakemgr.webapp.model.CakeEntity; to List");

		// exercise the method under test
		PojoJsonConverter.jsonToPojoList(malformedCakeListJson, CakeEntity[].class);

	}

	@Test
	public void testPojoToJson() throws PojoJsonConverterException {

		// create expected result
		final String expectedValue = lemonCheeseCakeJson;

		// get actual result
		final String actualValue = PojoJsonConverter.pojoToJson(lemonCheeseCake);

		// compare the results
		Assert.assertEquals("The two JSON strings were not equal by value", expectedValue, actualValue);

	}

	@Test
	public void testInvalidPojoToJson() {

		// set up the expectations for the test
		expectedEx.expect(PojoJsonConverterException.class);
		expectedEx.expectMessage("Problem converting java.lang.Object to JSON object");

		// exercise the method under test with a non-serializable argument
		PojoJsonConverter.pojoToJson(new Object());

	}

	@Test
	public void testPojoListToJson() throws PojoJsonConverterException {

		// create expected result
		final String expectedValue = cakeListJson;

		// get actual result
		final List<CakeEntity> listToConvert = new ArrayList<>();
		listToConvert.add(lemonCheeseCake);
		listToConvert.add(bananaCake);
		final String actualValue = PojoJsonConverter.pojoListToJson(listToConvert);

		// compare the results
		Assert.assertEquals("The two JSON strings were not equal by value", expectedValue, actualValue);

	}

	@Test
	public void testInvalidPojoListToJson() {

		// set up the expectations for the test
		expectedEx.expect(PojoJsonConverterException.class);
		expectedEx.expectMessage("Problem converting java.util.ArrayList to JSON array");

		// create problematic list argument containing instances of non-serializable
		// objects
		List<Object> objectList = new ArrayList<>();
		objectList.add(new Object());
		objectList.add(new Object());

		// exercise the method under test with a non-serializable argument
		System.out.println("JSON array String is: " + PojoJsonConverter.pojoListToJson(objectList));

	}

}
