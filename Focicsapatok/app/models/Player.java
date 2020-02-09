package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="player")
public class Player extends GenericModel{

	@Id
	@GeneratedValue
	@Column(name="player_id")
	public Long playerId;
	
	@Column(name="player_name")
	public String playerName;
	
	@ManyToOne
	@JoinColumn(name="club_id", referencedColumnName="club_id")
	public Club owningClub;
	
	@Column(name="player_license")
	public String playerLicense;
	
	@Column(name="player_salary")
	public Integer playerSalary;
	
	@Column(name="player_contract_start")
	public String playerContractStart;
	
	@Column(name="player_contract_expire")
	public String playerContractExpire;
	
	@Column(name="player_position")
	public String playerPosition;
	
	
	
}
