package F28DA_CW2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jgrapht.GraphPath;

public class IItineraryImpl implements IItinerary {
	
	GraphPath<String, Route> path = null;
	DateFormat dateFormat = new SimpleDateFormat("hhmm");
	
	
	public IItineraryImpl(GraphPath<String, Route> path) {
		super();
		this.path = path;
	}

	@Override
	public List<String> getStops() {
		List<String> stops = new ArrayList<String>();
		List<Route> routeList = path.getEdgeList();
		if(routeList!=null){
			stops.add(routeList.get(0).getSourceAirportCode());
		}
		for(Route r:routeList){
			stops.add(r.getDestinationAirportCode());
		}
		return stops;
	}

	@Override
	public List<String> getFlights() {
		List<String> flights = new ArrayList<String>();
		List<Route> routeList = path.getEdgeList();
		for(Route r:routeList){
			flights.add(r.getFlightRouteCode());
		}
		return flights;
	}

	@Override
	public int totalHop() {
		return path.getEdgeList().size();
	}

	@Override
	public int totalCost() {
		int totalCost = 0;
		List<Route> routeList = path.getEdgeList();
		for(Route r:routeList){
			totalCost += Integer.valueOf(r.getRouteCost());
		}
		return totalCost;
	}

	@Override
	public int airTime() throws FlightItineraryException {
		List<Route> routeList = path.getEdgeList();
		int airTimeInMins = 0;
		for(Route r:routeList){
			if(r.getDepartureTime()!=null && r.getArrivalTime()!=null){
				try {
					Date departTime = dateFormat.parse(r.getDepartureTime());
					Date arriveTime = dateFormat.parse(r.getArrivalTime());
					airTimeInMins += timeDifference(departTime,arriveTime);
				} catch (ParseException e) {
					throw new FlightItineraryException(e.getMessage());
				}
			}
		}
		return airTimeInMins;
	} 
	

	@Override
	public int connectingTime() throws FlightItineraryException {
		List<Route> routeList = path.getEdgeList();
		int connectingTimeInMins = 0;
		Date prevArrivalTime = null;
		try {
			if(!routeList.isEmpty() && routeList.get(0).getDepartureTime()!=null)
				prevArrivalTime = dateFormat.parse(routeList.get(0).getDepartureTime());

			for(Route r:routeList){
				Date currentDepartTime = dateFormat.parse(r.getDepartureTime());
				connectingTimeInMins += timeDifference(prevArrivalTime,currentDepartTime);
				prevArrivalTime = dateFormat.parse(r.getArrivalTime());
			}
		} catch (ParseException e) {
			throw new FlightItineraryException(e.getMessage());
		}
		return connectingTimeInMins;
	}

	@Override
	public int totalTime() throws FlightItineraryException {
		return airTime()+connectingTime();
	}

	@Override
	public List<Route> getRoutes() {
		return this.path.getEdgeList();
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
