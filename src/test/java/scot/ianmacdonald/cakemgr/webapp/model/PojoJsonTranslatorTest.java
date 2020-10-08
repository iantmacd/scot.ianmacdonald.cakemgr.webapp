package scot.ianmacdonald.cakemgr.webapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import junit.framework.Assert;
import scot.ianmacdonald.cakemgr.webapp.model.CakeEntity;
import scot.ianmacdonald.cakemgr.webapp.model.PojoJsonConverter;

public class PojoJsonTranslatorTest {
	
	// common test data
	private final String lemonCheeseCakeJson = "{\n"
			+ "  \"id\" : null,\n"
			+ "  \"title\" : \"Lemon cheesecake\",\n"
			+ "  \"desc\" : \"A cheesecake made of lemon\",\n"
			+ "  \"image\" : \"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"\n"
			+ "}";
	private final String cakeListJson = "[ {\n"
			+ "  \"id\" : null,\n"
			+ "  \"title\" : \"Lemon cheesecake\",\n"
			+ "  \"desc\" : \"A cheesecake made of lemon\",\n"
			+ "  \"image\" : \"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"\n"
			+ "}, {\n"
			+ "  \"id\" : null,\n"
			+ "  \"title\" : \"Banana cake\",\n"
			+ "  \"desc\" : \"Donkey kongs favourite\",\n"
			+ "  \"image\" : \"http://ukcdn.ar-cdn.com/recipes/xlarge/ff22df7f-dbcd-4a09-81f7-9c1d8395d936.jpg\"\n"
			+ "} ]";
	private final CakeEntity lemonCheeseCake = new CakeEntity(null, "Lemon cheesecake", "A cheesecake made of lemon",
			"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");
	private final CakeEntity bananaCake = new CakeEntity(null, "Banana cake", "Donkey kongs favourite", "http://ukcdn.ar-cdn.com/recipes/xlarge/ff22df7f-dbcd-4a09-81f7-9c1d8395d936.jpg");

	@Test
	public void testJsonToPojo() throws JsonParseException, JsonMappingException, IOException {

		// create expected result
		final CakeEntity expectedValue = lemonCheeseCake;

		// get actual result
		final CakeEntity actualValue = PojoJsonConverter.jsonToPojo(lemonCheeseCakeJson, CakeEntity.class);

		// compare the results
		Assert.assertEquals("The two CakeEntity objects were not equal by value", expectedValue, actualValue);
	}
	
	@Test
	public void testJsonToPojoList() throws JsonParseException, JsonMappingException, IOException {
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
	public void testPojoToJson() throws JsonProcessingException {
		
		// create expected result
		final String expectedValue = lemonCheeseCakeJson;
		
		// get actual result
		final String actualValue = PojoJsonConverter.pojoToJson(lemonCheeseCake);
		
		// compare the results
		Assert.assertEquals("The two JSON strings were not equal by value", expectedValue, actualValue);
		
	}
	
	@Test
	public void testPojoListToJson() throws JsonProcessingException {
		
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

}
