package scot.ianmacdonald.cakemgr.webapp.model;

import java.util.List;

/**
 * An abstraction of the operations available on Cake DB table.
 * Can be implemented using different DB technologies as appropriate.
 * @author ian.macdonald@ianmacdonald.scot
 *
 */
public interface CakeDAO {
	
	public List<CakeEntity> readAll();

}
