package F28DA_CW2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsUnweightedDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class IFlightItineraryImpl implements IFlightItinerary {
	private SimpleDirectedWeightedGraph<String, Route>  flightGraph;
	DateFormat dateFormat = new SimpleDateFormat("hhmm");

	
	public IFlightItineraryImpl() {
		super();
		flightGraph = new SimpleDirectedWeightedGraph<String, Route>(Route.class);
	}


	@Override
	public boolean populate(HashSet<String[]> airlines, HashSet<String[]> airports, HashSet<String[]> routes) {
		System.out.println("The following airports are used:");
		try {
			for(String[] airport:airports){
				flightGraph.addVertex(airport[0]);
				System.out.println(airport[1]);
			}
			
			//Adding the routes as custom edges and the cost of the route as edge weight to the graph
			for(String[] route:routes){
				if(route[1]!=null && route[3]!=null && route[5]!=null){
					Route e= new Route(route);
					try {
						flightGraph.addEdge(route[1],route[3],e);
						flightGraph.setEdgeWeight(e, Double.valueOf(route[5]));
					} catch (Exception e1){
						
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public IItinerary leastCost(String to, String from) throws FlightItineraryException {
		IItinerary iItinerary = null;
		if(flightGraph.containsVertex(from) && flightGraph.containsVertex(to)){
				GraphPath<String, Route> path= DijkstraShortestPath.findPathBetween(flightGraph, from, to);
				if(path!=null){
					iItinerary = new IItineraryImpl(path);
				}
			}	
		
		return iItinerary;
	}

	@Override
	public IItinerary leastHop(String to, String from) throws FlightItineraryException {
		IItinerary iItinerary = null;		
		Graph<String, Route> unweightedGraph = new AsUnweightedDirectedGraph<>(flightGraph);
		
		if(unweightedGraph.containsVertex(from) && unweightedGraph.containsVertex(to)){
			GraphPath<String, Route> path= DijkstraShortestPath.findPathBetween(unweightedGraph, from, to);
			if(path!=null)			{
				iItinerary = new IItineraryImpl(path);
			}
		}
		
		return iItinerary;
	}

	@Override
	public IItinerary leastCost(String to, String from, List<String> excluding) throws FlightItineraryException {
		flightGraph.removeAllVertices(excluding);
		return leastCost(to,from);
	}

	@Override
	public IItinerary leastHop(String to, String from, List<String> excluding) throws FlightItineraryException {
		flightGraph.removeAllVertices(excluding);
		return leastHop(to,from);
	}

	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlightItineraryException {
		Set<String> airports=flightGraph.vertexSet();
		String meetPoint = null;
		IItinerary iItinerary1;
		IItinerary iItinerary2;
		int leastCost = Integer.MAX_VALUE;
		for(String destination:airports){
			try{
				iItinerary1 = leastCost(destination, at1) ;
				if(iItinerary1!=null){
					iItinerary2 = leastCost(destination, at2) ;
					if(iItinerary2!=null){
						if(leastCost>iItinerary1.totalCost()+iItinerary2.totalCost()){
							meetPoint = destination;
							leastCost= iItinerary1.totalCost()+iItinerary2.totalCost();
						}
					}
				}
			} catch (FlightItineraryException e){
				
			}
		}
		return meetPoint;
	}

	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlightItineraryException {
		Set<String> airports=flightGraph.vertexSet();
		String meetPoint = null;
		IItinerary iItinerary1;
		IItinerary iItinerary2;
		int leastHop = Integer.MAX_VALUE;
		
		for(String destination:airports){
			try{
				iItinerary1 = leastHop(destination, at1) ;
				if(iItinerary1!=null){
					iItinerary2 = leastHop(destination, at2) ;
					if(iItinerary2!=null){
						if(leastHop>iItinerary1.totalHop()+iItinerary2.totalHop()){
							meetPoint = destination;
							leastHop = iItinerary1.totalHop()+iItinerary2.totalHop();
						}
					}
				}
			} catch (FlightItineraryException e){
				
			}
		}
		return meetPoint;
	}

	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTm) throws FlightItineraryException {
		AllDirectedPaths<String, Route> allDirectedPaths = new AllDirectedPaths<String, Route>(flightGraph);
		String meetupPlace = null;
		
		try {
			Date startTime = dateFormat.parse(startTm);
			Set<String> airports=flightGraph.vertexSet();
			long minTime=Long.MAX_VALUE;
			
			
			for(String destination:airports){
				List<GraphPath<String, Route>> paths1=allDirectedPaths.getAllPaths(at1, destination, true,4);
				List<GraphPath<String, Route>> paths2=allDirectedPaths.getAllPaths(at2, destination, true,4);
				long totalTime1=Long.MAX_VALUE;
				long leastTime1=Long.MAX_VALUE;
				for(GraphPath<String, Route> path1:paths1){
					IItinerary iItinerary1=new IItineraryImpl(path1);
					if(!iItinerary1.getRoutes().isEmpty()){
						Date departTime = dateFormat.parse(iItinerary1.getRoutes().get(0).getDepartureTime());
						totalTime1 = timeDifference(startTime,departTime) + iItinerary1.totalTime();
						if(totalTime1<leastTime1){
							leastTime1=totalTime1;
						}
					} else {
						//When the destination is same as the vertex
						totalTime1 = 0;
						leastTime1 = 0;
					}
				}
				
				long totalTime2=Long.MAX_VALUE;
				long leastTime2=Long.MAX_VALUE;
				for(GraphPath<String, Route> path2:paths2){
					IItinerary iItinerary2=new IItineraryImpl(path2);
					if(!iItinerary2.getRoutes().isEmpty()){
						Date departTime = dateFormat.parse(iItinerary2.getRoutes().get(0).getDepartureTime());
						totalTime2 = timeDifference(startTime,departTime) + iItinerary2.totalTime();
						if(totalTime2<leastTime2){
							leastTime2=totalTime2;
						}
					} else { 
						//When the destination is same as the vertex
						totalTime2 = 0;
						leastTime2 = 0;
					}
				}
				if(Math.max(leastTime1, leastTime2) < minTime){
					meetupPlace = destination;
					minTime = Math.max(leastTime1, leastTime2);
				}
			}
		}catch(ParseException e){
			throw new FlightItineraryException(e.getMessage());
		}

		return meetupPlace;
	}

	/*
	 * Returns the difference in minutes between start and end time of a journey
	 */
	private static int timeDifference(Date startTime, Date endTime) {
		long difference = endTime.getTime() - startTime.getTime(); // milliseconds
		if(difference<0){
			difference += 24*60*60*1000;
		}
		int differenceInMins = (int)difference/(60*1000);
		return differenceInMins;
	}
}
