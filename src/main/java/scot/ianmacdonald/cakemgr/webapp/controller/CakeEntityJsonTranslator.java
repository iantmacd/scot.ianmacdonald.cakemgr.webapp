/**
 * 
 */
package scot.ianmacdonald.cakemgr.webapp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import scot.ianmacdonald.cakemgr.webapp.model.CakeEntity;

/**
 * A utility class for converting CakeEntity objects to JSON and vice versa
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class CakeEntityJsonTranslator {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	public static CakeEntity jsonToCakeEntity(final String cakeJson) throws JsonParseException, JsonMappingException, IOException {
		
		return objectMapper.readValue(cakeJson, CakeEntity.class);
	}
	
	public static List<CakeEntity> jsonToCakeEntityList(final String cakeListJson) throws JsonParseException, JsonMappingException, IOException {
		
		return Arrays.asList(objectMapper.readValue(cakeListJson, CakeEntity[].class));
	}
	
	public static String cakeEntityToJson(final CakeEntity cakeEntity) throws JsonProcessingException {
		
		return objectMapper.writeValueAsString(cakeEntity);
	}
	
	public static String cakeEntityListToJson(final List<CakeEntity> cakeEntityList) throws JsonProcessingException  {
		
		return objectMapper.writeValueAsString(cakeEntityList.toArray());
	}

}
