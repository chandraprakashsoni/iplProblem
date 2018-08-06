package entity;

public class Deliveries {
	public String matchId;
	public String inning;
	public String battingTeam;
	public String bowlingTeam;
	public int over;
	public int ball;
	public String batsman;
	public String bowler;
	public int wideRuns;
	public int byeRuns;
	public int legbyeRuns;
	public int noballRuns;
	public int penaltyRuns;
	public int batsmanRuns;
	public int extraRuns;
	public int totalRuns;
	
	@Override
	public String toString() {
		return "Deliveries [matchId=" + matchId + ", inning=" + inning + ", battingTeam=" + battingTeam
				+ ", bowlingTeam=" + bowlingTeam + ", over=" + over + ", ball=" + ball + ", batsman=" + batsman
				+ ", bowler=" + bowler + ", wideRuns=" + wideRuns + ", byeRuns=" + byeRuns + ", legbyeRuns="
				+ legbyeRuns + ", noballRuns=" + noballRuns + ", penaltyRuns=" + penaltyRuns + ", batsmanRuns="
				+ batsmanRuns + ", extraRuns=" + extraRuns + ", totalRuns=" + totalRuns + "]";
	}	
}
