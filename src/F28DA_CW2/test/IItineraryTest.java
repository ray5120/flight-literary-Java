package F28DA_CW2.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import F28DA_CW2.FlightItineraryException;
import F28DA_CW2.IFlightItinerary;
import F28DA_CW2.IFlightItineraryImpl;
import F28DA_CW2.IItinerary;

public class IItineraryTest {
	private IItinerary iItinerary;
	@Before
	public void setUp() throws Exception {
		IFlightItinerary iFlightItinerary=new IFlightItineraryImpl();
		iFlightItinerary.populate(getAirlinesForTest(), getAirportsForTest(), getRoutesForTest());
		iItinerary=iFlightItinerary.leastCost("DXB", "SYD");
	}



	@Test
	public void testGetStops() {
		List<String> stops = new ArrayList<String>();
		stops.add("SYD");
		stops.add("KUL");
		stops.add("DXB");
		assertEquals(iItinerary.getStops(), stops);
	}

	@Test
	public void testGetFlights() {
		List<String> flights = new ArrayList<String>();
		flights.add("EK4243");
		flights.add("EK2326");
		
		assertEquals(iItinerary.getFlights(), flights);
	}

	@Test
	public void testTotalHop() {
		assertEquals(iItinerary.totalHop(), 2);
	}

	@Test
	public void testTotalCost() {
		assertEquals(iItinerary.totalCost(),320);
	}

	@Test
	public void testAirTime() {
		try {
			assertEquals(iItinerary.airTime(),521);
		} catch (FlightItineraryException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testConnectingTime() {
		try {
			assertEquals(iItinerary.connectingTime(),1099);
		} catch (FlightItineraryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTotalTime() {
		try {
			System.out.println(iItinerary.totalTime());
			assertEquals(iItinerary.totalTime(),1620);
		} catch (FlightItineraryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns a hashset of hardcoded airports to load data for Test Case
	 */
	public HashSet<String[]> getAirportsForTest() {
		HashSet<String[]> airportsForTest = new HashSet<String[]>();
		String airport1[]= {"EDI","Edinburgh"};
		String airport2[]= {"LHR","London"};
		String airport3[]= {"DXB","Dubai"};
		String airport4[]= {"SYD","Sydney"};
		String airport5[]= {"KUL","Kuala Lumpur"};

		airportsForTest.add(airport1);
		airportsForTest.add(airport2);
		airportsForTest.add(airport3);
		airportsForTest.add(airport4);
		airportsForTest.add(airport5);
		
		return airportsForTest;
	}

	
	/**
	 * Returns a hashset of hardcoded routes to load data for Test Case
	 */
	public HashSet<String[]> getRoutesForTest() {
		HashSet<String[]> routesForTest = new HashSet<String[]>();
		String[] r1 = new String[]{"BA1234","EDI","1230","LHR","1630","80"};
		String[] r2 = new String[]{"EK1235","LHR","0915","DXB","1428","130"};
		String[] r3 = new String[]{"BA7867","LHR","2215","SYD","0825","570"};
		String[] r4 = new String[]{"EK2325","DXB","0400","KUL","0623","170"};
		String[] r5 = new String[]{"BA4512","DXB","1328","EDI","1818","190"};
		String[] r6 = new String[]{"EK4242","KUL","1900","SYD","0100","150"};
		
		//Assuming that flights operate in both directions for PartA
		String[] r7 = new String[]{"BA1235","LHR","1820","EDI","2220","80"};
		String[] r8 = new String[]{"EK1236","DXB","0500","LHR","1010","130"};
		String[] r9 = new String[]{"BA7868","SYD","1245","LHR","2300","570"};
		String[] r10= new String[]{"EK2326","KUL","1414","DXB","1645","170"};
		String[] r11= new String[]{"BA4513","EDI","1200","DXB","1730","190"};
		String[] r12= new String[]{"EK4243","SYD","1345","KUL","1955","150"};
		
		routesForTest.add(r1);
		routesForTest.add(r2);
		routesForTest.add(r3);
		routesForTest.add(r4);
		routesForTest.add(r5);
		routesForTest.add(r6);
		
		routesForTest.add(r7);
		routesForTest.add(r8);
		routesForTest.add(r9);
		routesForTest.add(r10);
		routesForTest.add(r11);
		routesForTest.add(r12);
		
		return routesForTest;
	}
	
	public HashSet<String[]> getAirlinesForTest() {
		HashSet<String[]> airlinesForTest = new HashSet<String[]>();
		String[] a1 = new String[]{"BA1234"};
		String[] a2 = new String[]{"EK1235"};
		String[] a3 = new String[]{"BA7867"};
		String[] a4 = new String[]{"EK2325"};
		String[] a5 = new String[]{"BA4512"};
		String[] a6 = new String[]{"EK4242"};
		
		String[] a7 = new String[]{"BA1235"};
		String[] a8 = new String[]{"EK1236"};
		String[] a9 = new String[]{"BA7868"};
		String[] a10= new String[]{"EK2326"};
		String[] a11= new String[]{"BA4513"};
		String[] a12= new String[]{"EK4243"};
		
		airlinesForTest.add(a1);
		airlinesForTest.add(a2);
		airlinesForTest.add(a3);
		airlinesForTest.add(a4);
		airlinesForTest.add(a5);
		airlinesForTest.add(a6);
		
		airlinesForTest.add(a7);
		airlinesForTest.add(a8);
		airlinesForTest.add(a9);
		airlinesForTest.add(a10);
		airlinesForTest.add(a11);
		airlinesForTest.add(a12);
		return airlinesForTest;
	}
	
}
