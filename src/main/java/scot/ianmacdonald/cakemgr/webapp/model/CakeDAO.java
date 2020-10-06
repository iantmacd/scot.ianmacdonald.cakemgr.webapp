package scot.ianmacdonald.cakemgr.webapp.model;

import java.util.List;

/**
 * An abstraction of the operations available on Cake DB table.
 * Can be implemented using different DB technologies as appropriate.
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public interface CakeDAO {
	
	/*
	 * Read all the CakeEntity objects stored in the DB
	 */
	public List<CakeEntity> readAll();
	
	/*
	 * Create a new CakeEntity entry in the DB
	 */
	public CakeEntity create(CakeEntity cakeEntity);

}
