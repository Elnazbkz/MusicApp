import java.util.Scanner;

public class Main {
	private static Artist artist;
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		// Create a new artist or update an existing artist
		System.out.print("Do you want to add a new artist (1) or update an existing artist (2)? ");
		int choice = scanner.nextInt();
		if (choice == 1) {
			artist.addArtist(null);
		} else if (choice == 2) {
			artist.updateArtist();
		} else {
			System.out.print("Invalid input, please select (1) to add new artist or (2) to update an artist: ");
		}

	}
}
