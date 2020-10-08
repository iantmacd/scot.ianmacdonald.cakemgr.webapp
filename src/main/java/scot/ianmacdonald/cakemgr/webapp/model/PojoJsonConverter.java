/**
 * 
 */
package scot.ianmacdonald.cakemgr.webapp.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * A utility class for converting POJO objects to JSON and vice versa
 * Works with single objects or with List<T>
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class PojoJsonConverter {
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
	public static <T> T jsonToPojo(final String cakeJson, Class<T> clazz) throws IOException {
		
		return objectMapper.readValue(cakeJson, clazz);
	}
	
	public static <T> List<T> jsonToPojoList(final String pojoListJson, Class<T[]> clazz) throws IOException {
		
		T[] arrayResult = objectMapper.readValue(pojoListJson, clazz);
		List<T> list = Arrays.asList(arrayResult);
		return list;
	}
	
	public static String pojoToJson(final Object pojo) throws IOException {
		
		return objectMapper.writeValueAsString(pojo);
	}
	
	public static <T> String pojoListToJson(final List<T> pojoList) throws IOException  {
		
		return objectMapper.writeValueAsString(pojoList.toArray());
	}

}
