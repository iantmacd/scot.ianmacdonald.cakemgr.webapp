package scot.ianmacdonald.cakemgr.webapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import scot.ianmacdonald.cakemgr.webapp.controller.CakeEntityJsonTranslator;

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
				
				// read the json data into a StringBuffer
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuffer buffer = new StringBuffer();
				String line = reader.readLine();
				while (line != null) {
					buffer.append(line);
					line = reader.readLine();
				}

				// translate the cake json into List<CakeEntity>
				System.out.println("Translating cake json to Java");
				
				List<CakeEntity> cakeList = CakeEntityJsonTranslator.jsonToCakeEntityList(buffer.toString());
				cakeList.forEach(x -> create(x));

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

	@Override
	public CakeEntity create(CakeEntity cakeEntity) {
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
			 * it is assumed duplicates can be ignored on initialisation of the DB, but
			 * should not be when creating new entries purposefully.
			 * TODO: introduce error handling for uniqueness constraint violation.
			 */
		}
		session.close();
		return cakeEntity;
	}

}
