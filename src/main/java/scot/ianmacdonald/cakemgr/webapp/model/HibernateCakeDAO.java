package scot.ianmacdonald.cakemgr.webapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.ServiceRegistry;

/**
 * A concrete implementation of the CakeDAO interface for use with a Hibernate
 * in-memory DB. Uses singleton access to to a org.hibernate.SessionFactory
 * configured for use with a lightweight in-memory DB.
 * 
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class HibernateCakeDAO implements CakeDAO {

	private static boolean dbIsInitialised = false;

	private static SessionFactory sessionFactory = null;
	
	static {
		// initialise the SessionFactory when the class is first loaded
		buildSessionFactory();
	}

	private static void buildSessionFactory() {
		try {
			if (sessionFactory == null) {
				Configuration configuration = new Configuration()
						.configure(HibernateCakeDAO.class.getResource("/hibernate.cfg.xml"));
				StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
				serviceRegistryBuilder.applySettings(configuration.getProperties());
				ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			}
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Constructor which checks if the HInernate in-memory DB is initialised
	 * 
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

				List<CakeEntity> cakeList = PojoJsonTranslator.jsonToPojoList(buffer.toString(), CakeEntity[].class);
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

		Session session = sessionFactory.openSession();
		@SuppressWarnings("unchecked")
		List<CakeEntity> list = session.createCriteria(CakeEntity.class).list();
		session.close();
		return list;
	}

	@Override
	public CakeEntity create(CakeEntity cakeEntity) {
		
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.persist(cakeEntity);
			System.out.println("adding cake entity");
			session.getTransaction().commit();
		} catch (ConstraintViolationException ex) {
			/*
			 * Silently catching Exceptions is generally not good practice, but since 1. The
			 * data source for this exercise is given as canonical 2. It contains duplicates
			 * 3. The uniqueness constraints on the DB table appear to be intentional it is
			 * assumed duplicates can be ignored on initialisation of the DB, but should not
			 * be when creating new entries purposefully. TODO: introduce error handling for
			 * uniqueness constraint violation.
			 */
		}
		session.close();
		return cakeEntity;
	}

}
