import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Artist {

	private String ID;
	private String Name;
	private String Address;
	private String Birthdate;
	private String Bio;
	private ArrayList<String> Occupations;
	private ArrayList<String> Genres;
	private ArrayList<String> Awards;
	Map<String, String> ArtistInfo = new HashMap<String, String>();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public Artist(String id, String name, String address, String birthdate, String bio, ArrayList<String> occupations,
			ArrayList<String> genres, ArrayList<String> awards) {
		ID = id;
		Name = name;
		Address = address;
		Birthdate = birthdate;
		Bio = bio;
		Occupations = occupations;
		Genres = genres;
		Awards = awards;

	}

	///// Getters
	public String getID() {
		return ID;
	}

	public String getName() {
		return Name;
	}

	public String getAddress() {
		return Address;
	}

	public String getBirthdate() {
		return Birthdate;
	}

	public String getBio() {
		return Bio;
	}

	public ArrayList<String> getOccupations() {
		return Occupations;
	}

	public ArrayList<String> getGenres() {
		return Genres;
	}

	public ArrayList<String> getAwards() {
		return Awards;
	}

	public static Artist validateArtistID(String artistID) {
		try (BufferedReader reader = new BufferedReader(new FileReader("Artists-list.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");
				String currentArtistID = parts[0];

				if (currentArtistID.equals(artistID)) {
					// Assuming the order of parts is ID, Name, Address, Birthdate, Bio,
					// Occupations, Genres, Awards
					ArrayList<String> occupations = new ArrayList<>(Arrays.asList(parts[5].split(",")));
					ArrayList<String> genres = new ArrayList<>(Arrays.asList(parts[6].split(",")));
					ArrayList<String> awards = new ArrayList<>(Arrays.asList(parts[7].split(",")));

					return new Artist(parts[0], // ID
							parts[1], // Name
							parts[2], // Address
							parts[3], // Birthdate
							parts[4], // Bio
							occupations, genres, awards);
				}
			}
		} catch (IOException e) {
			System.err.println("An error occurred while reading the file: " + e.getMessage());
		}
		return null; // Artist with the specified ID not found
	}

	public static boolean addArtistToTxtFile(Map<String, String> artist) {
		String newArtistID = artist.get("ID");
		Artist FoundArtist = Artist.validateArtistID(newArtistID);
		if (FoundArtist != null) {
			return false;
		} else {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("Artists-list.txt", true))) {
				// Format: ID|Name|Address|Birthdate|Bio|Occupations|Genres|Awards
				writer.write(artist.get("ID") + ";" + artist.get("Name") + ";" + artist.get("Address") + ";"
						+ artist.get("Birthdate") + ";" + artist.get("Bio") + ";"
						+ String.join(",", artist.get("Occupations")) + ";" + String.join(",", artist.get("Genres"))
						+ ";" + String.join(",", artist.get("Awards")));
				writer.newLine();
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	public static void updateArtistInTxtFile(String artistID, Map<String, String> updatedArtistInfo) {
		try {
			File inputFile = new File("Artists-list.txt");
			File tempFile = new File("TempArtists-list.txt");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");
				String currentArtistID = parts[0];

				if (currentArtistID.equals(artistID)) {
					// Replace the existing line with the updated artist information
					StringBuilder updatedLine = new StringBuilder();
					updatedLine.append(updatedArtistInfo.get("ID")).append(";").append(updatedArtistInfo.get("Name"))
							.append(";").append(updatedArtistInfo.get("Address")).append(";")
							.append(updatedArtistInfo.get("Birthdate")).append(";").append(updatedArtistInfo.get("Bio"))
							.append(";").append(updatedArtistInfo.get("Occupations")).append(";")
							.append(updatedArtistInfo.get("Genres")).append(";")
							.append(updatedArtistInfo.get("Awards"));
					writer.write(updatedLine.toString());
					writer.newLine();
				} else {
					writer.write(line);
					writer.newLine();
				}
			}

			writer.close();
			reader.close();

			// Rename the temporary file to the original file
			tempFile.renameTo(inputFile);
			System.out.println("Artist information updated in artists.txt.");
		} catch (IOException e) {
			System.err.println("An error occurred while updating artist information: " + e.getMessage());
		}
	}

	public static ArrayList<String> getAwards(Scanner scanner) {
		ArrayList<String> awards;

		do {
			System.out.println("Enter Awards (Comma-separated. 1 to 5 awards allowed): ");
			String awardsString = scanner.nextLine().trim();
			awards = new ArrayList<>(Arrays.asList(awardsString.split(",")));

			boolean validAwards = true;
			for (String award : awards) {
				String[] parts = award.trim().split("-");
				if (parts.length != 2 || !parts[0].trim().matches("\\d{4}") || parts[1].trim().split("\\s+").length < 2
						|| parts[1].trim().split("\\s+").length > 10) {
					validAwards = false;
					break;
				}
			}

			if (!validAwards) {
				System.out.println("Invalid awards format. Please enter awards in the format: YYYY - Award");
			}
		} while (awards.size() < 1 || awards.size() > 5);

		return awards;
	}

	public static boolean addArtist(Map<String, String> ArtistInfo) {
		Scanner scanner = new Scanner(System.in);
		if (ArtistInfo == null) {
			ArtistInfo = new HashMap<>();

			// Get Artist ID
			String artistID;
			do {
				System.out.println("Enter Artist ID (format: 569MMMRR_%):");
				artistID = scanner.nextLine().trim();
				try {
					// Check if the input date string matches the desired format
					if (!artistID.matches("[5-9]{3}[A-Z]{5}[%_]{2}")) {
						throw new Exception(); // Invalid format, throw an exception
					}
				} catch (Exception e) {
					System.out.println("Invalid format. Please enter valid Artist ID");
				}
			} while (artistID == null);

			ArtistInfo.put("ID", artistID);
			System.out.println("Artist ID format is correct.");

			// Get Name (NO VALIDATION HERE)
			String Name;
			System.out.println("Enter name of the artist:");
			Name = scanner.nextLine();
			ArtistInfo.put("Name", Name);

			// Get Birthdate
			LocalDate parsedBirthdate = null;
			String birthdate;
			do {
				System.out.println("Enter Birth Date (format: dd/MM/yyyy):");
				birthdate = scanner.nextLine().trim();

				try {
					// Check if the input date string matches the desired format
					if (birthdate.matches("\\d{2}/\\d{2}/\\d{4}")) {
						parsedBirthdate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					} else {
						throw new Exception(); // Invalid format, throw an exception
					}
				} catch (Exception e) {
					System.out.println("Invalid format. Please enter the date in dd/MM/yyyy format:");
				}
			} while (parsedBirthdate == null);

			ArtistInfo.put("Birthdate", birthdate);
			System.out.println("Birthdate format is correct.");

			// Get Address
			String address;
			do {
				System.out.println("Enter Address:");
				address = scanner.nextLine().trim();
				try {
					// Check if the input date string matches the desired format
					if (!address.matches("[a-zA-Z]+\\|[a-zA-Z]+\\|[a-zA-Z]+")) {
						throw new Exception(); // Invalid format, throw an exception
					}
				} catch (Exception e) {
					System.out.println("Invalid Address. Please enter valid address:");
				}
			} while (address == null);
			ArtistInfo.put("Address", address);
			System.out.println("Address format is correct.");

			// GetBio
			String bio;
			int wordCount;
			do {
				System.out.println("Enter Biography (10 to 30 words allowed): ");
				bio = scanner.nextLine().trim();
				wordCount = bio.split("\\s+").length;
			} while (wordCount < 10 || wordCount > 30);
			ArtistInfo.put("Bio", bio);
			System.out.println("Bio format is correct.");

			// Get Occupations
			ArrayList<String> occupations;
			String occupationsString;

			do {
				System.out.println("Enter Occupations (Comma-separated. 1 to 5 occupations allowed): ");
				occupationsString = scanner.nextLine().trim();
				occupations = new ArrayList<>(Arrays.asList(occupationsString.split(",")));
			} while (occupations.isEmpty() || occupations.size() > 5);
			ArtistInfo.put("Occupations", occupationsString);
			System.out.println("Occupations format is correct.");

			// Get Genres
			ArrayList<String> genres;
			String genresString;

			do {
				System.out.println("Enter Genres (Comma-separated - 1 to 5 allowed): ");
				genresString = scanner.nextLine().trim();
				genres = new ArrayList<>(Arrays.asList(genresString.split(",")));
			} while (genres.size() < 2 || genres.size() > 5 || (genres.contains("pop") && genres.contains("rock")));
			ArtistInfo.put("Genres", genresString);
			System.out.println("Genres format is correct.");

			// Get awards
			ArrayList<String> awards = getAwards(scanner);
			String awardsString = String.join(",", awards);
			ArtistInfo.put("Awards", awardsString);
			System.out.println("Awards format is correct.");
		}
		boolean result = addArtistToTxtFile(ArtistInfo);
		if (result) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean updateArtist() {
		Scanner scanner = new Scanner(System.in);

		// Get Artist ID
		String artistID;
		do {
			System.out.println("Enter Artist ID you would like to update:");
			artistID = scanner.nextLine().trim();
			try {
				// Check if the input Artist ID matches the desired format
				if (validateArtistID(artistID) == null) {
					throw new Exception(); // Invalid format, throw an exception
				}
			} catch (Exception e) {
				System.out.println("Artist ID is invalid. Try again:");
			}
		} while (validateArtistID(artistID) == null);
		Artist artistInfo = validateArtistID(artistID);

		String Oldname = artistInfo.Name;
		String Oldbirthdate = artistInfo.Birthdate;
		String Oldaddress = artistInfo.Address;
		String Oldbio = artistInfo.Bio;
		String[] Oldoccupations = artistInfo.Occupations.toArray(new String[0]);
		String[] Oldgenres = artistInfo.Genres.toArray(new String[0]);
		String[] Oldawards = artistInfo.Awards.toArray(new String[0]);

		String name;
		String birthdate;
		String address;
		String bio;
		String[] occupations;
		String[] genres;
		String[] awards;

		do {
			try {
				System.out.println(
						"Provide new information('fullname', 'birthdate', 'address', 'bio', 'occupation1,occupation2,...', 'award1,award2,...')");
				String artistNewInfo = scanner.nextLine().trim();
				String[] infoParts = artistNewInfo.split(";");

				if (infoParts.length >= 7) {
					name = infoParts[0].trim();
					birthdate = infoParts[1].trim();
					address = infoParts[2].trim();
					bio = infoParts[3].trim();
					occupations = infoParts[4].split(",");
					genres = infoParts[5].split(",");
					awards = infoParts[6].split(",");
					break;
				} else {
					throw new Exception(); // Invalid format, throw an exception
				}
			} catch (Exception e) {
				System.out.println("Invalid input format. Please provide all required information.");
			}
		} while (true);

		// Check if the input date string matches the desired format (dd-MM-yyyy)
		String[] birthdateParts = birthdate.split("/");
		System.out.println(birthdateParts);
		if (birthdateParts.length != 3) {
			System.out.println("Invalid birthdate format. Please enter the date in dd-MM-yyyy format.");
			return false;
		}

		try {
			int birthYear = Integer.parseInt(birthdateParts[2]);
			try {
				if (birthYear < 2000 && Arrays.equals(Oldoccupations, occupations)) {
					System.out.println("Cannot update occupations for artists born before 2000.");
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid birth year format: " + birthdateParts[2]);
			return false;
		}

		// Condition 3: Check awards before 2000
		if (awards != null) {
			for (String award : awards) {
				String[] parts = award.split("-");
				if (parts.length == 2) {
					try {
						int awardYear = Integer.parseInt(parts[0].trim());
						if (awardYear < 2000) {
							System.out.println("Cannot update awards before 2000.");
							return false;
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid award year format: " + parts[0].trim());
						return false;
					}
				} else {
					System.out.println("Invalid award format: " + award);
					return false;
				}
			}
		}

		// Create a Map with the updated artist information
		Map<String, String> updatedArtistInfo = new HashMap<>();
		updatedArtistInfo.put("ID", artistID);
		updatedArtistInfo.put("Name", name);
		updatedArtistInfo.put("Birthdate", birthdate);
		updatedArtistInfo.put("Address", address);
		updatedArtistInfo.put("Bio", bio);
		updatedArtistInfo.put("Occupations", Arrays.toString(occupations));
		updatedArtistInfo.put("Genres", Arrays.toString(genres));

		if (awards != null) {
			updatedArtistInfo.put("Awards", Arrays.toString(awards));
		} else {
			updatedArtistInfo.put("Awards", "");
		}

		updateArtistInTxtFile(artistID, updatedArtistInfo);
		return true;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Create a new artist or update an existing artist
		System.out.print("Do you want to add a new artist (1) or update an existing artist (2)? ");
		int choice = scanner.nextInt();
		if (choice == 1) {
			addArtist(null);
		} else if (choice == 2) {
			updateArtist();
		} else {
			System.out.print("Invalid input, please select (1) to add new artist or (2) to update an artist: ");
		}

	}

}
