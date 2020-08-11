package F28DA_CW2;

public class Route {
	private String flightRouteCode;
	private String sourceAirportCode;
	private String departureTime;
	private String destinationAirportCode;
	private String arrivalTime;
	private String routeCost;
	
	
	
	public Route() {
		super();
	}


	public Route(String flightRouteCode, String sourceAirportCode, String departureTime, String destinationAirportCode,
			String arrivalTime, String routeCost) {
		super();
		this.flightRouteCode = flightRouteCode;
		this.sourceAirportCode = sourceAirportCode;
		this.departureTime = String.format("%4s", departureTime).replace(' ', '0');;
		this.destinationAirportCode = destinationAirportCode;
		this.arrivalTime = String.format("%4s",arrivalTime).replace(' ', '0');;
		this.routeCost = routeCost;
	}
	
	
	
	public Route(String sourceAirportCode, String destinationAirportCode) {
		super();
		this.sourceAirportCode = sourceAirportCode;
		this.destinationAirportCode = destinationAirportCode;
	}


	
	public Route(String sourceAirportCode, String destinationAirportCode, String routeCost) {
		super();
		this.sourceAirportCode = sourceAirportCode;
		this.destinationAirportCode = destinationAirportCode;
		this.routeCost = routeCost;
	}


	public Route(String[] route) {
		if(route.length==6){
			this.flightRouteCode = route[0];
			this.sourceAirportCode = route[1];
			this.departureTime = String.format("%4s",route[2]).replace(' ', '0'); // To get the time in HHMM format
			this.destinationAirportCode = route[3];
			this.arrivalTime = String.format("%4s",route[4]).replace(' ', '0');
			this.routeCost = route[5];
		}
	}

	// Overriding toString to print the Route in desired format
	/*@Override
	public String toString() {
		return  FlightsReader.airportsMap.get(sourceAirportCode) + " -> " + FlightsReader.airportsMap.get(destinationAirportCode);
	}*/
	
	@Override
	public String toString() {
		return  sourceAirportCode + " -> " + destinationAirportCode;
	}
	
	public String getFlightRouteCode() {
		return flightRouteCode;
	}
	public String getSourceAirportCode() {
		return sourceAirportCode;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public String getDestinationAirportCode() {
		return destinationAirportCode;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public String getRouteCost() {
		return routeCost;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivalTime == null) ? 0 : arrivalTime.hashCode());
		result = prime * result + ((departureTime == null) ? 0 : departureTime.hashCode());
		result = prime * result + ((destinationAirportCode == null) ? 0 : destinationAirportCode.hashCode());
		result = prime * result + ((flightRouteCode == null) ? 0 : flightRouteCode.hashCode());
		result = prime * result + ((routeCost == null) ? 0 : routeCost.hashCode());
		result = prime * result + ((sourceAirportCode == null) ? 0 : sourceAirportCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Route other = (Route) obj;
		
		if (destinationAirportCode == null) {
			if (other.destinationAirportCode != null)
				return false;
		} else if (!destinationAirportCode.equals(other.destinationAirportCode))
			return false;
		
		if (sourceAirportCode == null) {
			if (other.sourceAirportCode != null)
				return false;
		} else if (!sourceAirportCode.equals(other.sourceAirportCode))
			return false;
		return true;
	}
	
	
}
