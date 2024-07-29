package TaxiBookingSystem;

import java.util.*;

public class Booking {
	public static void bookTaxi(int customerID, char pickupPoint, char dropPoint, int pickupTime,
			List<Taxi> freeTaxis) {
		int minDistance = Integer.MAX_VALUE;
		int distanceBetweenPickupAndDrop = 0;
		int earning = 0;
		int nextFreeTime = 0;
		char nextSpot = 'Z';
		Taxi bookedTaxi = null;
		String tripDetail = "";

		for (Taxi t : freeTaxis) {
			int distanceBetweenCustomerAndTaxi = Math.abs(t.currentSpot - pickupPoint) * 15;
			if (distanceBetweenCustomerAndTaxi < minDistance) {
				bookedTaxi = t;
				distanceBetweenPickupAndDrop = Math.abs(dropPoint - pickupPoint) * 15;
				earning = (distanceBetweenPickupAndDrop - 5) * 10 + 100;
				int dropTime = pickupTime + distanceBetweenPickupAndDrop / 15;
				nextFreeTime = dropTime;
				nextSpot = dropPoint;
				tripDetail = String.format("%-10d%-15d%-10c%-10c%-15d%-10d%-10d", customerID, customerID, pickupPoint,
						dropPoint, pickupTime, dropTime, earning);
				minDistance = distanceBetweenCustomerAndTaxi;
			}
		}

		if (bookedTaxi != null) {
			bookedTaxi.setDetails(true, nextSpot, nextFreeTime, bookedTaxi.totalEarnings + earning, tripDetail);
			System.out.println("Taxi " + bookedTaxi.id + " booked successfully.");
		} else {
			System.out.println("No taxi available for booking.");
		}
	}

	public static List<Taxi> createTaxis(int n) {
		List<Taxi> taxis = new ArrayList<>();
		for (int i = 1; i <= n; i++) {
			Taxi t = new Taxi();
			taxis.add(t);
		}
		return taxis;
	}

	public static List<Taxi> getFreeTaxis(List<Taxi> taxis, int pickupTime, char pickupPoint) {
		List<Taxi> freeTaxis = new ArrayList<>();
		for (Taxi t : taxis) {
			if (t.freeTime <= pickupTime && Math.abs(t.currentSpot - pickupPoint) <= pickupTime - t.freeTime) {
				freeTaxis.add(t);
			}
		}
		return freeTaxis;
	}

	public static void main(String[] args) {
		List<Taxi> taxis = createTaxis(4);
		Scanner scanner = new Scanner(System.in);
		int id = 1;

		while (true) {
			System.out.println("0 -> Book Taxi");
			System.out.println("1 -> Print Taxi details");
			System.out.println("2 -> Exit");
			int choice = scanner.nextInt();

			switch (choice) {
			case 0:
				bookTaxiFlow(scanner, taxis, id);
				id++;
				break;
			case 1:
				printAllTaxiDetails(taxis);
				break;
			case 2:
				System.out.println("Exiting the system. Thank you!");
				scanner.close();
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private static void bookTaxiFlow(Scanner scanner, List<Taxi> taxis, int customerID) {
		System.out.println("Enter Pickup point (A-F):");
		char pickupPoint = Character.toUpperCase(scanner.next().charAt(0));
		System.out.println("Enter Drop point (A-F):");
		char dropPoint = Character.toUpperCase(scanner.next().charAt(0));
		System.out.println("Enter Pickup time (0-23):");
		int pickupTime = scanner.nextInt();

		if (!isValidPoint(pickupPoint) || !isValidPoint(dropPoint) || pickupTime < 0 || pickupTime > 23) {
			System.out.println("Invalid input. Please enter valid pickup/drop points (A-F) and pickup time (0-23).");
			return;
		}

		List<Taxi> freeTaxis = getFreeTaxis(taxis, pickupTime, pickupPoint);
		if (freeTaxis.isEmpty()) {
			System.out.println("No Taxi can be allotted. Please try again later.");
			return;
		}

		Collections.sort(freeTaxis, Comparator.comparingInt(a -> a.totalEarnings));
		bookTaxi(customerID, pickupPoint, dropPoint, pickupTime, freeTaxis);
	}

	private static boolean isValidPoint(char point) {
		return point >= 'A' && point <= 'F';
	}

	private static void printAllTaxiDetails(List<Taxi> taxis) {
		for (Taxi t : taxis) {
			t.printTaxiDetails();
			t.printDetails();
		}
	}
}
