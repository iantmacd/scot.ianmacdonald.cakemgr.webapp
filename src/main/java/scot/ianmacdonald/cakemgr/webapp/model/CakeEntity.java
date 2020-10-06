package scot.ianmacdonald.cakemgr.webapp.model;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Model POJO representing a DB entry for a type of Cake
 * @author ian.macdonald@ianmacdonald.scot
 * 
 */
@Entity
@Table(name = "Cake", uniqueConstraints = {@UniqueConstraint(columnNames = "ID"), @UniqueConstraint(columnNames = "TITLE")})
public class CakeEntity implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Integer id;

    @Column(name = "TITLE", unique = true, nullable = false, length = 100)
    private String title;

    @Column(name = "DESCRIPTION", unique = false, nullable = false, length = 100)
    private String description;

    @Column(name = "IMAGE", unique = false, nullable = false, length = 300)
    private String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    /*
     * Compare CakeEntity objects by value
     */
    @Override
    public boolean equals(Object other) {
    	if (other instanceof CakeEntity) {
    		return title.equals(((CakeEntity)other).title)
    				&& description.equals(((CakeEntity)other).description)
    				&& image.equals(((CakeEntity)other).image);
    	}
    	else return false;
    }

}