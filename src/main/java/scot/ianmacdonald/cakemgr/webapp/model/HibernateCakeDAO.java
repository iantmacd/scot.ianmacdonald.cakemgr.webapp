package scot.ianmacdonald.cakemgr.webapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * A concrete implementation of the CakeDAO interface for use with a Hibernate in-memory DB.
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class HibernateCakeDAO implements CakeDAO {
	
	private static boolean dbIsInitialised = false;
	
	/**
	 * Constructor which checks if the HInernate in-memory DB is initialised
	 * @throws IOException 
	 */
	public HibernateCakeDAO() throws IOException {
		
		if (!dbIsInitialised) {
			// initialise the DB in a Hibernate way
			System.out.println("init started");

			System.out.println("downloading cake json");
			try (InputStream inputStream = new URL(
					"https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json")
							.openStream()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				StringBuffer buffer = new StringBuffer();
				String line = reader.readLine();
				while (line != null) {
					buffer.append(line);
					line = reader.readLine();
				}

				System.out.println("parsing cake json");
				JsonParser parser = new JsonFactory().createParser(buffer.toString());
				if (JsonToken.START_ARRAY != parser.nextToken()) {
					throw new Exception("bad token");
				}

				JsonToken nextToken = parser.nextToken();
				while (nextToken == JsonToken.START_OBJECT) {
					System.out.println("creating cake entity");

					CakeEntity cakeEntity = new CakeEntity();
					System.out.println(parser.nextFieldName());
					cakeEntity.setTitle(parser.nextTextValue());

					System.out.println(parser.nextFieldName());
					cakeEntity.setDescription(parser.nextTextValue());

					System.out.println(parser.nextFieldName());
					cakeEntity.setImage(parser.nextTextValue());

					Session session = HibernateUtil.getSessionFactory().openSession();
					try {
						session.beginTransaction();
						session.persist(cakeEntity);
						System.out.println("adding cake entity");
						session.getTransaction().commit();
					} catch (ConstraintViolationException ex) {
						/*
						 * Silently catching Exceptions is generally not good practice, but since
						 * 1. The data source for this exercise is given as canonical
						 * 2. It contains duplicates
						 * 3. The uniqueness constraints on the DB table appear to be intentional
						 * it is assumed duplicates can be ignored on initialisation of the DB.
						 */
					}
					session.close();

					nextToken = parser.nextToken();
					System.out.println(nextToken);

					nextToken = parser.nextToken();
					System.out.println(nextToken);
				}

			} catch (Exception ex) {
				throw new IOException(ex);
			}

			System.out.println("init finished");
			dbIsInitialised = true;
		}
	}
	
	@Override
	public List<CakeEntity> readAll() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		List<CakeEntity> list = session.createCriteria(CakeEntity.class).list();
		session.close();
		return list;
	}

}
