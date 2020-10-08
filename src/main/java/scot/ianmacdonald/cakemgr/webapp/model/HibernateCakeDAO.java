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
		try {

			Configuration configuration = new Configuration()
					.configure(HibernateCakeDAO.class.getResource("/hibernate.cfg.xml"));
			StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
			serviceRegistryBuilder.applySettings(configuration.getProperties());
			ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (Throwable ex) {

			System.err.println("Initial SessionFactory creation failed." + ex);
			// unrecoverable... fail catastrophically
			throw new ExceptionInInitializerError(ex);

		}
	}

	/**
	 * Constructor which checks if the Hibernate in-memory DB is initialised.
	 * Normally initialising data would be an application concern, but since this is
	 * effectively a test DAO implementation, it is included here for convenience
	 * and ease of testing, and treated as a system requirement for error handling.
	 */
	public HibernateCakeDAO() {

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

				List<CakeEntity> cakeList = PojoJsonConverter.jsonToPojoList(buffer.toString(), CakeEntity[].class);

				try {

					cakeList.forEach(x -> create(x));

				} catch (CakeDAOConstraintViolationException cdex) {

					// Silently catching Exceptions is generally not good practice, but since
					// 1. The data source for this exercise is given as canonical
					// 2. It contains duplicates
					// 3. The uniqueness constraints on the DB table appear to be intentional
					// 5. Hibernate logs these errors to standard output
					// ... it is assumed duplicates can be ignored on initialisation of
					// the DB, but should not be when creating new entries purposefully.
				}

			} catch (Exception ex) {
				// A throwable other than a CakeDAOConstraintViolationException occurred
				System.err.println("Initial data load into HibernateCakeDAO failed." + ex);
				// for this app's requirements, this is unrecoverable... fail catastrophically
				throw new ExceptionInInitializerError(ex);

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
	public CakeEntity create(CakeEntity cakeEntity) throws CakeDAOConstraintViolationException {

		Session session = sessionFactory.openSession();
		try {

			session.beginTransaction();
			session.persist(cakeEntity);
			System.out.println("adding cake entity");
			session.getTransaction().commit();

		} catch (ConstraintViolationException ex) {

			// we wish to propagate this runtime exception with the cause included
			// so that clients can decide whether to handle it or not
			throw new CakeDAOConstraintViolationException(
					"org.hibernate.exception.ConstraintViolationException was thrown", ex);

		}

		session.close();
		return cakeEntity;
	}

}
