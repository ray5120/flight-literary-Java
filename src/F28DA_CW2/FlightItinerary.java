/**
 * 
 */
package F28DA_CW2;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * 
 *
 */
public class FlightItinerary {

	public static Scanner in;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			System.out.println("Enter 1 for Part A, 2 for Part B, 3 for Part C");
			in = new Scanner(System.in);
			String option = in.nextLine();
			switch(option){
				case "1": partA();
						break;
				case "2": partB();
						break;
				case "3": partC();
						break;
				default: System.out.println("Wrong option");
				        break;
			}
		} catch (FileNotFoundException | FlightItineraryException e) {
			e.printStackTrace();
		} finally {
			in.close();
		}

	}
	
	public static void partA() throws FileNotFoundException, FlightItineraryException{
		SimpleDirectedWeightedGraph<String, Route>  flightGraph = new SimpleDirectedWeightedGraph<String, Route>
	            (Route.class);
		FlightsReader reader = new FlightsReader(FlightsReader.AIRLINECODS);
		
		System.out.println("The following airports are used:");
		
		for(String[] airport:reader.getAirportsForPartA()){
			flightGraph.addVertex(airport[0]);
			System.out.println(airport[1]);
		}
		
		
		for(Route route:reader.getRoutesForPartA()){
			flightGraph.addEdge(route.getSourceAirportCode(),route.getDestinationAirportCode(),route);
			flightGraph.setEdgeWeight(route, Integer.valueOf(route.getRouteCost()));
		}
		
		
		System.out.println("Please enter the Start City:");
		String startCity = in.nextLine();
				
		HashSet<String> startAirports = reader.getAirportsByCity(startCity);
		
		if(startAirports.isEmpty()){
			throw new FlightItineraryException("No airports in the start city");
		}
		
		System.out.println("Please enter the Destination City:");
		String destinationCity = in.nextLine();
		
		HashSet<String> destinationAirports = reader.getAirportsByCity(destinationCity);		

		if(destinationAirports.isEmpty()){
			throw new FlightItineraryException("No airports in the destination city");
		}

		int leastCost=Integer.MAX_VALUE;
		GraphPath<String, Route> leastCostPath = null;
		
		// Handles for multiple airports in the same city
		for(String startAirportCode:startAirports){
			for(String destinationAirportCode:destinationAirports){
				if(flightGraph.containsVertex(startAirportCode) && flightGraph.containsVertex(destinationAirportCode)){
					GraphPath<String, Route> path= DijkstraShortestPath.findPathBetween(flightGraph, startAirportCode, destinationAirportCode);
					if(path.getWeight()<leastCost)
					{
						leastCostPath = path;
						leastCost = (int)path.getWeight();
					}
				}
			}
		}
		
		if(leastCostPath==null)
	        System.out.println("No flights connecting these cities");
		else{
			
			System.out.println("Shortest (i.e. cheapest) path : ");
			
			int index = 1;
			for(Route r:leastCostPath.getEdgeList()){
				System.out.println(index+". "+r);
				index++;
			}
        
			System.out.println("Cost of shortest (i.e. cheapest) path = £"+leastCostPath.getWeight());

		}
	}

	
	public static void partB() throws FileNotFoundException, FlightItineraryException{
		FlightsReader reader = new FlightsReader(FlightsReader.AIRLINECODS);
		
		IFlightItinerary iFlightItinerary = new IFlightItineraryImpl();
		
		iFlightItinerary.populate(reader.getAirlines(), reader.getAirports(), reader.getRoutes());
		
		System.out.println("Please enter the Start City:");
		String startCity = in.nextLine();
		
		System.out.println("Please enter the Destination City:");
		String destinationCity = in.nextLine();
			
		HashSet<String> startAirports = reader.getAirportsByCity(startCity);
		if(startAirports.isEmpty()){
			throw new FlightItineraryException("No airports in the start city");
		}
		
		HashSet<String> destinationAirports = reader.getAirportsByCity(destinationCity);		

		if(destinationAirports.isEmpty()){
			throw new FlightItineraryException("No airports in the destination city");
		}
		
		IItinerary iItinerary = null;
		IItinerary leastCostItinerary = null;
		
		int leastCost=Integer.MAX_VALUE;
		
		for(String startAirportCode:startAirports){
			for(String destinationAirportCode:destinationAirports){
				iItinerary=iFlightItinerary.leastCost(destinationAirportCode, startAirportCode);
				if(iItinerary!=null && iItinerary.totalCost()<leastCost){
					leastCostItinerary = iItinerary;
					leastCost = iItinerary.totalCost();
				}
			}
		}
		
		if(leastCostItinerary==null){
			throw new FlightItineraryException("No flights connecting "+startCity+" to "+destinationCity);
		}
		
		printItinerary(startCity,destinationCity,leastCostItinerary);
	}

	private static void printItinerary(String startCity, String destinationCity, IItinerary iItinerary) throws FlightItineraryException {
		System.out.println("Itinerary for "+startCity+" to "+destinationCity);
		System.out.format("%-5s%-15s%-5s%-7s%-15s%-5s", "Leg", "Leave", "At","On","Arrive","At");
		System.out.println();
		//List<String> stops=iItinerary.getStops();
		//List<String> flights=iItinerary.getFlights();
		List<Route> routes=iItinerary.getRoutes();
		
		String leg;
		String leaveFrom;
		String arriveAt;
		String departTime;
		String on;
		String arriveTime;
		
		for(int i=0;i<iItinerary.totalHop();i++){
			leg = String.format("%-5s", i+1);
			leaveFrom=String.format("%-15s", FlightsReader.airportsMap.get(routes.get(i).getSourceAirportCode()));
			departTime = String.format("%-5s", routes.get(i).getDepartureTime());
			on=String.format("%-7s", routes.get(i).getFlightRouteCode());
			arriveAt=String.format("%-15s", FlightsReader.airportsMap.get(routes.get(i).getDestinationAirportCode()));
			arriveTime = String.format("%-5s", routes.get(i).getArrivalTime());
			System.out.println(leg+leaveFrom+departTime+on+arriveAt+arriveTime);
		}
		System.out.println("Total Journey Cost = £"+iItinerary.totalCost());
		System.out.println("Total Time in the Air = "+iItinerary.airTime());
		//System.out.println("Total Connecting Time = "+iItinerary.connectingTime());
		//System.out.println("Total Journey Time = "+iItinerary.totalTime());
	}
	
	public static void partC() throws FileNotFoundException, FlightItineraryException{
		FlightsReader reader = new FlightsReader(FlightsReader.AIRLINECODS);
		
		IFlightItinerary iFlightItinerary = new IFlightItineraryImpl();
		
		iFlightItinerary.populate(reader.getAirlines(), reader.getAirports(), reader.getRoutes());
		
		
		System.out.println("Enter a suitable option:");
		System.out.println("1 for Least Cost Itinerary");
		System.out.println("2 for Itinerary with Least Number of Changeovers");
		System.out.println("3 for Meet up city (Least cost) for 2 people starting from 2 different airports");
		System.out.println("4 for Meet up city (Least Changeovers) for 2 people starting from 2 different airports");
		System.out.println("5 for Meet up city (Least Time) for 2 people starting from 2 different airports from a given starting time");
		
		String option = in.nextLine();

		
		if("1".equalsIgnoreCase(option.trim())||"2".equalsIgnoreCase(option.trim())){
			System.out.println("Do you want to exclude any airports in the search? [y/n]");
			String isExclude = in.nextLine();
			String[] excludeList = null;
			boolean exclude =false;
			if(isExclude.trim().equalsIgnoreCase("Y")){
				exclude = true;
				System.out.println("Enter the list of airports[3-letter airport code] you want to exclude seperated by a space and press Enter");
				String excluded = in.nextLine();
				excludeList=excluded.split("\\s+");
			}
			
			System.out.println("Please enter the Start City:");
			String startCity = in.nextLine();
			
			System.out.println("Please enter the Destination City:");
			String destinationCity = in.nextLine();
				
			HashSet<String> startAirports = reader.getAirportsByCity(startCity);
			if(startAirports.isEmpty()){
				throw new FlightItineraryException("No airports in the start city");
			}
			
			HashSet<String> destinationAirports = reader.getAirportsByCity(destinationCity);		

			if(destinationAirports.isEmpty()){
				throw new FlightItineraryException("No airports in the destination city");
			}
			
			IItinerary iItinerary = null;
			IItinerary leastCostItinerary = null;
			
			int leastCost=Integer.MAX_VALUE;
			int leastHop=Integer.MAX_VALUE;
			
			if("1".equalsIgnoreCase(option.trim())){
				if(exclude){
					for(String startAirportCode:startAirports){
						for(String destinationAirportCode:destinationAirports){
							iItinerary=iFlightItinerary.leastCost(destinationAirportCode, startAirportCode,Arrays.asList(excludeList));
							if(iItinerary!=null && iItinerary.totalCost()<leastCost){
								leastCostItinerary = iItinerary;
								leastCost = iItinerary.totalCost();
							}
						}
					}
				} else {
					for(String startAirportCode:startAirports){
						for(String destinationAirportCode:destinationAirports){
							iItinerary=iFlightItinerary.leastCost(destinationAirportCode, startAirportCode);
							if(iItinerary!=null && iItinerary.totalCost()<leastCost){
								leastCostItinerary = iItinerary;
								leastCost = iItinerary.totalCost();
							}
						}
					}
				}
			} else {
				if(exclude){
					for(String startAirportCode:startAirports){
						for(String destinationAirportCode:destinationAirports){
							iItinerary=iFlightItinerary.leastHop(destinationAirportCode, startAirportCode,Arrays.asList(excludeList));
							if(iItinerary!=null && iItinerary.totalHop()<leastHop){
								leastCostItinerary = iItinerary;
								leastHop = iItinerary.totalHop();
							}
						}
					}
				} else {
					for(String startAirportCode:startAirports){
						for(String destinationAirportCode:destinationAirports){
							iItinerary=iFlightItinerary.leastHop(destinationAirportCode, startAirportCode);
							if(iItinerary!=null && iItinerary.totalHop()<leastHop){
								leastCostItinerary = iItinerary;
								leastHop = iItinerary.totalHop();
							}
						}
					}
				}
			}
		
			if(leastCostItinerary==null){
				throw new FlightItineraryException("No flights connecting "+startCity+" to "+destinationCity);
			}
			printItinerary(startCity,destinationCity,leastCostItinerary);
			
		} else if ("3".equalsIgnoreCase(option.trim())){
			
			System.out.println("Please enter the Airport Code[3 letter code] of Person 1:");
			String aiportCode1 = in.nextLine();
			System.out.println("Please enter the Airport Code[3 letter code] of Person 2:");
			String aiportCode2 = in.nextLine();
			String meetupPlace = iFlightItinerary.leastCostMeetUp(aiportCode1, aiportCode2);
			if(meetupPlace!=null){
				System.out.println("Least Cost Meet Up city for two people starting from "+aiportCode1+" and "+aiportCode2 +" is "+FlightsReader.airportsMap.get(meetupPlace)+ " ("+meetupPlace+")");
			} else {
				System.out.println("No suitable meet up point found");
			}
		} else if ("4".equalsIgnoreCase(option.trim())){
			System.out.println("Please enter the Airport Code[3 letter code] of Person 1:");
			String aiportCode1 = in.nextLine();
			System.out.println("Please enter the Airport Code[3 letter code] of Person 2:");
			String aiportCode2 = in.nextLine();
			String meetupPlace = iFlightItinerary.leastHopMeetUp(aiportCode1, aiportCode2);
			if(meetupPlace!=null){
				System.out.println("Least Changeover Meet Up city for two people starting from "+aiportCode1+" and "+aiportCode2 +" is "+FlightsReader.airportsMap.get(meetupPlace)+ " ("+meetupPlace+")");
			} else {
				System.out.println("No suitable meet up point found");
			}
		} else if ("5".equalsIgnoreCase(option.trim())){
			
			System.out.println("Please enter the Airport Code[3 letter code] of Person 1:");
			String aiportCode1 = in.nextLine();
			System.out.println("Please enter the Airport Code[3 letter code] of Person 2:");
			String aiportCode2 = in.nextLine();
			System.out.println("Please enter the starting time in hhmm format:");
			String startingTime = in.nextLine();
			String meetupPlace = iFlightItinerary.leastTimeMeetUp(aiportCode1.toUpperCase(), aiportCode2.toUpperCase(),startingTime);
			if(meetupPlace!=null){
				System.out.println("Least Time Meet Up city for two people starting from "+aiportCode1+" and "+aiportCode2 +" starting at "+ startingTime +" is "+FlightsReader.airportsMap.get(meetupPlace)+ " ("+meetupPlace+")");
			} else {
				System.out.println("No suitable meet up point found");
			}
			
		} else {
				throw new FlightItineraryException("Wrong option entered !!!");
		}
		
		
	}
}
