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
 * A utility class for converting POJO objects to JSON and vice versa Works with
 * single objects or with List<T>
 * 
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class PojoJsonConverter {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	public static <T> T jsonToPojo(final String cakeJson, Class<T> clazz) throws PojoJsonConverterException {

		try {

			return objectMapper.readValue(cakeJson, clazz);

		} catch (IOException ioe) {

			throw new PojoJsonConverterException(
					"Problem converting JSON object to " + clazz.getName(), ioe);

		}

	}

	public static <T> List<T> jsonToPojoList(final String pojoListJson, Class<T[]> clazz) throws PojoJsonConverterException {

		try {
			
			T[] arrayResult = objectMapper.readValue(pojoListJson, clazz);
			List<T> list = Arrays.asList(arrayResult);
			return list;
			
		}  catch (IOException ioe) {

			throw new PojoJsonConverterException(
					"Problem converting JSON array of " + clazz.getName() + " to List", ioe);

		}

	}

	public static String pojoToJson(final Object pojo) throws PojoJsonConverterException {

		try {
			
			return objectMapper.writeValueAsString(pojo);
			
		} catch (IOException ioe) {

			throw new PojoJsonConverterException(
					"Problem converting " + pojo.getClass().getName() + " to JSON object", ioe);

		}
		
	}

	public static <T> String pojoListToJson(final List<T> pojoList) throws PojoJsonConverterException {

		try {
			
			return objectMapper.writeValueAsString(pojoList.toArray());
			
		} catch (IOException ioe) {

			throw new PojoJsonConverterException(
					"Problem converting " + pojoList.getClass().getName() + " to JSON array", ioe);

		}
		
	}

}
