package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="club")
public class Club extends GenericModel{

	@Id
	@GeneratedValue
	@Column(name="club_id")
	public Long clubId;
	
	@Column(name="club_name")
	public String clubName;
	
	@Column(name="club_established")
	public Integer clubEstablished;
	
	@Column(name="club_country")
	public String clubCountry;
	
	@Column(name="club_region")
	public String clubRegion;
	
	@Column(name="club_city")
	public String clubCity;
	
	@OneToMany(mappedBy="owningClub")
	public List<Player> players;
	
}
