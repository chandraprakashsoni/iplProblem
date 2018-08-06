package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match {
	public Match() {
		this.deliveryList = new ArrayList<Deliveries>();
		this.overPlayed = new HashMap<>();
		this.teamScores = new HashMap<>();
		this.runRates = new HashMap<>();
	}
	public String matchId;
	public String season;
	public String city;
	public Date date;
	public String team1;
	public String team2;
	public String tossWinner;
	public String tossDecision;
	public String result;
	public String winnerTeam;
	public List<Deliveries> deliveryList;
	
	// other meta
	//how many run a team made
	public Map<String, Integer> teamScores;
	//how many over a team played
	public Map<String, Integer> overPlayed;
	
	//run rate
	public Map<String, Float> runRates;
	
	@Override
	public String toString() {
		return "Match [matchId=" + matchId + ", season=" + season + ", city=" + city + ", date=" + date + ", team1="
				+ team1 + ", team2=" + team2 + ", tossWinner=" + tossWinner + ", tossDecision=" + tossDecision
				+ ", result=" + result + ", winnerTeam=" + winnerTeam + ", deliveries=" + deliveryList + "]";
	}
}
