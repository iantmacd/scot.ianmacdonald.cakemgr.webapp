/**
 * 
 */
package scot.ianmacdonald.cakemgr.webapp.model;

/**
 * A Factory object for getting a reference to a CakeDao implementation.
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public class CakeDaoFactory {
	
	public static CakeDao getCakeDao() {
		/*
		 * For now, this returns the one concrete type available, but creates an
		 * extension point where configuration or dependency injection could be used
		 * to return different DB access layers, as required.
		 */
		return new HibernateCakeDao();
	}

}
