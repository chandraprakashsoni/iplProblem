package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import entity.Deliveries;
import entity.Match;

public class MatchService {
	private List<Match> matchList;
	
	//Loading all data from files
	public void loadData(String matches, String deliveries) throws IOException, ParseException {
		String matchesLine = null;
		String deliveriesLine = null;
		this.matchList = new ArrayList<Match>();
		
		FileReader matchesFileReader = new FileReader(matches);
		FileReader deliveriesFileReader = new FileReader(deliveries);
		BufferedReader matchesBufferedReader = new BufferedReader(matchesFileReader);
		BufferedReader deliveriesBufferedReader = new BufferedReader(deliveriesFileReader);

		//Reading and loading data from Matches
		while((matchesLine = matchesBufferedReader.readLine()) != null) {
			String[] matchesSplittedArray = matchesLine.split(",");
			//Skip first row and blank rows
			if(matchesSplittedArray[0].equals("MATCH_ID") || matchesSplittedArray[0].equals("") || matchesSplittedArray[0] == null)
				continue;
			//Create Matches	
			Match match = new Match();
			match.matchId = matchesSplittedArray[0];
			match.season = matchesSplittedArray[1];
			match.city = matchesSplittedArray[2];
			match.date = new SimpleDateFormat("yyyy-MM-dd").parse(matchesSplittedArray[3]);
			match.team1 = matchesSplittedArray[4];
			match.team2 = matchesSplittedArray[5];
			match.tossWinner = matchesSplittedArray[6];
			match.tossDecision = matchesSplittedArray[7];
			match.result = matchesSplittedArray[8];
			//If no result then Winner team is blank
			if(matchesSplittedArray[8].equals("no result"))
				match.winnerTeam = "";
			else
				match.winnerTeam = matchesSplittedArray[9];
			//Adding match to matchList
			matchList.add(match);
		}
		
		// Reading and loading data from deliveries
		while((deliveriesLine = deliveriesBufferedReader.readLine()) != null) {
			String[] deliveriesSplittedArray = deliveriesLine.split(",");
			//Skip first row and blank rows
			if(deliveriesSplittedArray[0].equals("MATCH_ID") || deliveriesSplittedArray[0].equals("") || deliveriesSplittedArray[0] == null)
				continue;
			//Create Deliveries
			Deliveries delivery = new Deliveries();
			delivery.matchId = deliveriesSplittedArray[0];
			delivery.inning = deliveriesSplittedArray[1];
			delivery.battingTeam = deliveriesSplittedArray[2];
			delivery.bowlingTeam = deliveriesSplittedArray[3];
			delivery.over = Integer.parseInt(deliveriesSplittedArray[4]);
			delivery.ball = Integer.parseInt(deliveriesSplittedArray[5]);
			delivery.batsman = deliveriesSplittedArray[6];
			delivery.bowler = deliveriesSplittedArray[7];
			delivery.wideRuns = Integer.parseInt(deliveriesSplittedArray[8]);
			delivery.byeRuns = Integer.parseInt(deliveriesSplittedArray[9]);
			delivery.legbyeRuns = Integer.parseInt(deliveriesSplittedArray[10]);
			delivery.noballRuns = Integer.parseInt(deliveriesSplittedArray[11]);
			delivery.penaltyRuns =Integer.parseInt( deliveriesSplittedArray[12]);
			delivery.batsmanRuns = Integer.parseInt(deliveriesSplittedArray[13]);
			delivery.extraRuns = Integer.parseInt(deliveriesSplittedArray[14]);
			delivery.totalRuns = Integer.parseInt(deliveriesSplittedArray[15]);
			Match match = null;
			for(int i = 0; i < matchList.size(); i++) {
				if(matchList.get(i).matchId.equals(deliveriesSplittedArray[0])) {
					match = matchList.get(i);				
					break;
				}
			}
			//Adding deliveries to match
			match.deliveryList.add(delivery);
			
			// add some other meta
			//add run in match.teamScore
			if (!match.teamScores.containsKey(delivery.battingTeam)) {
				match.teamScores.put(delivery.battingTeam, 0);
			}
			int score = match.teamScores.get(delivery.battingTeam);
			match.teamScores.put(delivery.battingTeam, score + delivery.totalRuns);
			
			//add over played in match.overPlayed
			if (!match.overPlayed.containsKey(delivery.battingTeam)) {
				match.overPlayed.put(delivery.battingTeam, 0);
			}
			
			int over = match.overPlayed.get(delivery.battingTeam);
			
			if (delivery.over > over) {
				match.overPlayed.put(delivery.battingTeam, delivery.over);
			}
		}
		
		//Closing the bufferreaders of both files
		matchesBufferedReader.close();
		deliveriesBufferedReader.close();
		
	}
	
//	public List<Match> getMatchList(){
//		return this.matchList;
//	}
	
	//for 1 solution
	public void printTopFourFieldingTeam() {
		SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
		//Creating Map
		Map< String,Integer> topTeams = new HashMap< String,Integer>();
		//Loading data to open according to requirement
		for( int i = 0; i < matchList.size(); i++) {
			Match m = matchList.get(i);
			//Extracting year from date
			String year = formatNowYear.format(m.date);
			if((year.equals("2016") || year.equals("2017")) && m.tossDecision.equals("field"))
			{
				String mapKey = m.tossWinner + "~" + year;
				if(topTeams.containsKey(mapKey)) {
					topTeams.put(mapKey, topTeams.get(mapKey)+1);
				}
				else {
					topTeams.put(mapKey, 1);
				}
			}	
		}
		
		//Sorting Map
		Set<Entry<String, Integer>> set = topTeams.entrySet();
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
        Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        
        //Printing Data
        int count = 0;
        System.out.printf("%10s %30s %20s","YEAR","TEAM","COUNT");
        System.out.println();
		System.out.println("-------------------------------------------------------------------------------------");
        for(Map.Entry<String, Integer> entry:list){
        	String[] teamAndYear = entry.getKey().split("~");
            System.out.printf("%10s %30s %20s",teamAndYear[1],teamAndYear[0],entry.getValue());
            System.out.println();
            if(count == 3) {
            	break;
            }
            count++;
        }	
	}
	
	//for 2 solution
	public void printFoursSixesScore() {
		
		class Result {
			public Result() {
				this.totalScore = 0;
				this.fourCount = 0;
				this.sixCount = 0;
			}
			public int totalScore;
			public int fourCount;
			public int sixCount;
		}
		
		SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
		//Creating Map 
		Map<String, Result> results = new HashMap< String,Result>();
		//Loading data to Map
		for( int i = 0; i < matchList.size(); i++) {
			Match match = matchList.get(i);
			//Extracting year from date
			String year = formatNowYear.format(match.date);
			
			for(int j = 0; j < match.deliveryList.size(); j++) {
				Deliveries delivery = match.deliveryList.get(j);
				String mapKey = year + "~" + delivery.battingTeam;
				if(!results.containsKey(mapKey)) {
					results.put(mapKey, new Result());
				} 
				
				Result r = results.get(mapKey);
				
				if(delivery.batsmanRuns == 6) {
					r.sixCount++;
				} else if(delivery.batsmanRuns == 4) {
					r.fourCount ++;
				}
				r.totalScore = r.totalScore + delivery.batsmanRuns;
			}
		}
		
		//Printing Data
		System.out.printf("%10s %30s %20s %20s %20s","YEAR","TEAM_NAME","FOUR_COUNT","SIX_COUNT","TOTAL_SCORE");
		System.out.println();		
		System.out.println("------------------------------------------------------------------------------------------------------------");
		List<Entry<String, Result>> list = new ArrayList<Entry<String, Result>>(results.entrySet());
		 for(Map.Entry<String, Result> entry:list){
	        	String[] teamAndYear = entry.getKey().split("~");
	            System.out.printf("%10s %30s %20s %20s %20s", teamAndYear[0], teamAndYear[1], entry.getValue().fourCount, entry.getValue().sixCount, entry.getValue().totalScore);
	            System.out.println();
		 }
		
		}

	//for 3 solution
	public void printTopEconomyRateofBowler() {
		class Bowler {
			public Bowler() {
				this.totalRuns = 0;
				this.overs = 0;
			}
			public int totalRuns;
			public int overs;
		}
		SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
		//Creating HashMap
		Map<String, Bowler> bowler = new HashMap<String, Bowler>();

		for( int i = 0; i < matchList.size(); i++) {
			Match match = matchList.get(i);
			//Extracting year from date
			String year = formatNowYear.format(match.date);
			for(int j = 0; j < match.deliveryList.size(); j++) {
				Deliveries delivery = match.deliveryList.get(j);
				String mapKey = year + "~" + delivery.bowler;
				if(!bowler.containsKey(mapKey)) {
					bowler.put(mapKey, new Bowler());
				} 
				
				Bowler b = bowler.get(mapKey);
				b.totalRuns += delivery.wideRuns + delivery.noballRuns + delivery.penaltyRuns + delivery.batsmanRuns + delivery.extraRuns;
				if(delivery.ball == 1) {
					b.overs++;
				}
				}
		}
		
		//Calculating Economy and storing into another Map
		Map<String, Float> bowlerMap = new HashMap<String, Float>();
		List<Entry<String, Bowler>> list = new ArrayList<Entry<String, Bowler>>(bowler.entrySet());
		 for(Map.Entry<String, Bowler> entry:list){
	        	String[] teamAndYear = entry.getKey().split("~");
	        	if(entry.getValue().overs >= 10) {
	        		float economy = (float)entry.getValue().totalRuns  /  entry.getValue().overs;
	        		String mapKey = teamAndYear[0] + "~" + teamAndYear[1];
	        		bowlerMap.put(mapKey, economy);
	        	}
		 }
		 
		//Sorting Map
		Set<Entry<String, Float>> set = bowlerMap.entrySet();
        List<Entry<String, Float>> blist = new ArrayList<Entry<String, Float>>(set);
        Collections.sort( blist, new Comparator<Map.Entry<String, Float>>()
        {
            public int compare( Map.Entry<String, Float> o1, Map.Entry<String, Float> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        
        //Printing Data
        System.out.printf("%10s %30s %20s","YEAR", "PLAYER", "ECONOMY");
        System.out.println();
		System.out.println("-------------------------------------------------------------------------------------");
        int count = 1;
        for(Map.Entry<String, Float> entry:blist){
        	String[] teamAndYear = entry.getKey().split("~");
            System.out.printf("%10s %30s %20s", teamAndYear[0], teamAndYear[1], entry.getValue());
            System.out.println();
            if(count == 10) {
            	break;
            }
            count++;
        }	
	}
	
	//for 4 solution
	public void printHighestNetRunRate() {
		Map<String, Float> netRunRates = new HashMap<String, Float>();
		SimpleDateFormat formatNowYear = new SimpleDateFormat("yyyy");
		
		
		for (int i = 0; i < matchList.size(); i ++ ) {
			Match m = matchList.get(i);
			//Extracting year from date
			String year = formatNowYear.format(m.date);
			
			List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(m.overPlayed.entrySet());
			if (list.size() != 2) {
				continue;
			}
			//Calculating Run Rate
			for(Map.Entry<String, Integer> entry:list){
	        	String teamName = entry.getKey();
	        	float overPlayed = entry.getValue();
	        	float score = m.teamScores.get(teamName);
	        	float runRate = score/overPlayed;
	        	m.runRates.put(teamName, runRate);
	        }
			 
			// team1 net run rate
			float team1NetRunRate = m.runRates.get(m.team1) - m.runRates.get(m.team2);
			String key = year + "~" + m.team1;
			if(netRunRates.containsKey(key)) {
				netRunRates.put(key, netRunRates.get(key)+team1NetRunRate);
			}
			else {
				netRunRates.put(key, team1NetRunRate);
			}
			// team2 net run rate
			float team2NetRunRate = m.runRates.get(m.team2) - m.runRates.get(m.team1);
			key = year + "~" + m.team2;
			if(netRunRates.containsKey(key)) {
				netRunRates.put(key, netRunRates.get(key)+team2NetRunRate);
			}
			else {
				netRunRates.put(key, team2NetRunRate);
			}
		}
		
		//Sorting Map according to NetRunRate
		Set<Entry<String, Float>> set = netRunRates.entrySet();
        List<Entry<String, Float>> blist = new ArrayList<Entry<String, Float>>(set);
        Collections.sort( blist, new Comparator<Map.Entry<String, Float>>()
        {
            public int compare( Map.Entry<String, Float> o1, Map.Entry<String, Float> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        //Finding Highest with respect to year
        Map<String, String> topRunRate = new HashMap<String, String>();
        Float runRate = (float) 0;
        for(Map.Entry<String, Float> entry:blist){
        	String[] teamAndYear = entry.getKey().split("~");
        	String key = teamAndYear[0];
        	
			if(topRunRate.containsKey(key)) {
        		 if(entry.getValue() > runRate)
					topRunRate.put(key, teamAndYear[1]);
				}
				else {
					topRunRate.put(key, teamAndYear[1]);
					runRate = entry.getValue();
				}        	
          
        }
        
        //Sorting data accodring to year
        Set<Entry<String, String>> rSet = topRunRate.entrySet();
        List<Entry<String, String>> rList = new ArrayList<Entry<String, String>>(rSet);
        Collections.sort( rList, new Comparator<Map.Entry<String, String>>()
        {
            public int compare( Map.Entry<String, String> o1, Map.Entry<String, String> o2 )
            {
                return (o2.getKey()).compareTo( o1.getKey() );
            }
        } );
        
        //Printing Data
        System.out.printf("%10s %35s", "YEAR", "TEAM");
        System.out.println();
		System.out.println("--------------------------------------------------------------");
        for(Map.Entry<String, String> entry:rList){
            System.out.printf("%10s %35s", entry.getKey(), entry.getValue());
            System.out.println();
            
        }
	}
}
