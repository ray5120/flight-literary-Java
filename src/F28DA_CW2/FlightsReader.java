package F28DA_CW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class FlightsReader {

	public static String[] AIRLINECODS = { "BA", "EK" };
	public static String[] MOREAIRLINECODES = { "AF", "BA", "CX", "CZ", "DL", "EK", "JJ", "KL", "LH", "NH", "QF", "QR",
			"TK", "UA" };
	
	
	private static final File openFlightsFolder = new File("FlightsDataset");
	private static final File openFlightsRoutes = new File(openFlightsFolder, "routes.csv");
	private static final File openFlightsAirports = new File(openFlightsFolder, "airports.csv");
	private static final File openFlightsAirlines = new File(openFlightsFolder, "airlines.csv");

	private HashSet<String[]> airlines;
	private HashSet<String[]> airports;
	private HashSet<String[]> routes;
	
	public static HashMap<String, String> airportsMap;

	/**
	 * Reads the flights dataset for a given list of airline codes (the
	 * {@code AIRLINECODES} and {@code MOREAIRLINECODES} constants are selections of
	 * airline codes available in the dataset). To use this class:
	 * <ol>
	 * <li>Make sure the provided {@code openFlightsAirlines} directory is
	 * accessible by your program (Eclipse: the directory needs to be in your project
	 * directory, not in {@code src})</li>
	 * <li>Create an object of the class FlightsReader by providing a list of
	 * airlines: <br>
	 * {@code FlightsReader reader = new FlightsReader(FlightsReader.AIRLINES);}</li>
	 * <li>If no exception is raised, the list of airlines, airports and routes are
	 * available using the following methods. If an exception is raised, check the
	 * location of the {@code openFlightsAirlines} directory. <br>
	 * <ul>
	 * <li>{@code reader.getAirlines();}</li>
	 * <li>{@code reader.getAirpors();}</li>
	 * <li>{@code reader.getRoutes();}</li>
	 * </ul>
	 * </ol>
	 * 
	 */
	public FlightsReader(String[] includeAirlineCodes) throws FileNotFoundException, FlightItineraryException {
		this.airlines = new HashSet<String[]>();
		this.airports = new HashSet<String[]>();
		this.routes = new HashSet<String[]>();
		
		airportsMap = new HashMap<String, String>();

		// Adding airlines
		HashSet<String> airlinesNeeded = new HashSet<String>();
		for(String a : includeAirlineCodes) {
			airlinesNeeded.add(a);
		}
		HashSet<String> airlinesAvailable = new HashSet<String>();
		this.airlines = new HashSet<String[]>();
		Scanner airlinesScanner = new Scanner(openFlightsAirlines);
		while (airlinesScanner.hasNextLine()) {
			String line = airlinesScanner.nextLine();
			String[] fields = line.split(",");
			String airlineCode = fields[0];
			boolean contained = airlinesNeeded.remove(airlineCode);
			if (contained) {
				airlinesAvailable.add(airlineCode);
				this.airlines.add(fields);
			}
		}
		airlinesScanner.close();
		if (!airlinesNeeded.isEmpty()) {
			throw new FlightItineraryException("Missing airline code(s): " + airlinesNeeded.toString());
		}
		
		// Adding routes, and building list of needed airport
		HashSet<String> airportsNeeded = new HashSet<String>();
		Scanner routesScanner = new Scanner(openFlightsRoutes);
		while(routesScanner.hasNextLine()) {
			String line = routesScanner.nextLine();
			String[] fields = line.split(",");
			String routeCode = fields[0];
			String airlineCode = routeCode.substring(0, 2);
			if (airlinesAvailable.contains(airlineCode)) {
				String from = fields[1];
				String to = fields[3];
				airportsNeeded.add(from);
				airportsNeeded.add(to);
				this.routes.add(fields);
			}
		}
		routesScanner.close();
		
		// Adding airports
		Scanner airportsScanner = new Scanner(openFlightsAirports);
		while (airportsScanner.hasNextLine()) {
			String line = airportsScanner.nextLine();
			String[] fields = line.split(",");
			if (airportsNeeded.contains(fields[0])) {
				this.airports.add(fields);
				airportsMap.put(fields[0], fields[1]);
			}
		}
		airportsScanner.close();
	}
	
	/**
	 * Returns a hash set of airline details (0: airline code, 1: airline name, 2:
	 * airline country)
	 */
	public HashSet<String[]> getAirlines() {
		return this.airlines;
	}

	/**
	 * Returns a hash set of airport details (0: airport code, 1: city, 2: airport
	 * name)
	 */
	public HashSet<String[]> getAirports() {
		return this.airports;
	}

	/**
	 * Returns a hash set of flight route details (0: flight route code (starting
	 * with the two characters airline code), 1: airport code of departure, 2:
	 * departure time GMT, 3: airport code of arrival, 4: arrival time GMT, 5: route
	 * cost)
	 */
	public HashSet<String[]> getRoutes() {
		return this.routes;
	}

	/**
	 * Returns a map of airport code (key) and airport name (value)
	 */
	public HashMap<String, String> getAirportsMap() {
		return airportsMap;
	}
	
	/**
	 * Returns a hashset of airport codes for a particular city
	 */
	public HashSet<String> getAirportsByCity(String cityName){
		HashSet<String> airportCodes = new HashSet<String>();
		for(String[] airport:this.airports){
			if(airport[1].equalsIgnoreCase(cityName)){
				airportCodes.add(airport[0]);
			}
		}
		return airportCodes;
	}
	
		
	/**
	 * Returns a hashset of hardcoded routes to load data for PartA
	 */
	public HashSet<Route> getRoutesForPartA() {
		HashSet<Route> routesForPartA = new HashSet<Route>();
		Route r1 = new Route("EDI","LHR","80");
		Route r2 = new Route("LHR","DXB","130");
		Route r3 = new Route("LHR","SYD","570");
		Route r4 = new Route("DXB","KUL","170");
		Route r5 = new Route("DXB","EDI","190");
		Route r6 = new Route("KUL","SYD","150");
		
		//Assuming that flights operate in both directions for PartA
		Route r7 = new Route("LHR","EDI","80");
		Route r8 = new Route("DXB","LHR","130");
		Route r9 = new Route("SYD","LHR","570");
		Route r10= new Route("KUL","DXB","170");
		Route r11= new Route("EDI","DXB","190");
		Route r12= new Route("SYD","KUL","150");
		
		routesForPartA.add(r1);
		routesForPartA.add(r2);
		routesForPartA.add(r3);
		routesForPartA.add(r4);
		routesForPartA.add(r5);
		routesForPartA.add(r6);
		
		routesForPartA.add(r7);
		routesForPartA.add(r8);
		routesForPartA.add(r9);
		routesForPartA.add(r10);
		routesForPartA.add(r11);
		routesForPartA.add(r12);
		
		return routesForPartA;
	}
	
	/**
	 * Returns a hashset of hardcoded airports to load data for PartA
	 */
	public HashSet<String[]> getAirportsForPartA() {
		HashSet<String[]> airportsForPartA = new HashSet<String[]>();
		String airport1[]= {"EDI","Edinburgh"};
		String airport2[]= {"LHR","London"};
		String airport3[]= {"DXB","Dubai"};
		String airport4[]= {"SYD","Sydney"};
		String airport5[]= {"KUL","Kuala Lumpur"};

		airportsForPartA.add(airport1);
		airportsForPartA.add(airport2);
		airportsForPartA.add(airport3);
		airportsForPartA.add(airport4);
		airportsForPartA.add(airport5);
		
		return airportsForPartA;
	}
}