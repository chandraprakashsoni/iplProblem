import java.io.IOException;
import java.text.ParseException;

import services.MatchService;


public class IplDatabase {

	public static void main(String[] args) throws IOException, ParseException{
		MatchService matchService = new MatchService();
		//Calling method to load data from files
		if(args.length == 0) {
			System.out.println("Please enter the file name as argument");
			System.exit(0);
		}
		String matches = args[0];
		String deliveries = args[1];
		
		matchService.loadData(matches, deliveries);
		
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.println("1. Top 4 teams which elected to field first after winning toss in year 2016 and 2017");
		System.out.println("-------------------------------------------------------------------------------------");
		//Calling method
		matchService.printTopFourFieldingTeam();
		
		System.out.println("\n------------------------------------------------------------------------------------------------------------");
		System.out.println("2. List total number of fours, sixes, total score with respect to team and year");
		System.out.println("------------------------------------------------------------------------------------------------------------");
		//Calling method
		matchService.printFoursSixesScore();
		
		System.out.println("\n-------------------------------------------------------------------------------------");
		System.out.println("3. Top 10 best economy rate bowler with respect to year who bowled at least 10 overs.");
		System.out.println("-------------------------------------------------------------------------------------");
		//Calling method
		matchService.printTopEconomyRateofBowler();
		
		System.out.println("\n--------------------------------------------------------------");
		System.out.println("4. Team name which has Highest Run Rate with respect to year");
		System.out.println("--------------------------------------------------------------");
		//Calling method
		matchService.printHighestNetRunRate();
	}			
}
