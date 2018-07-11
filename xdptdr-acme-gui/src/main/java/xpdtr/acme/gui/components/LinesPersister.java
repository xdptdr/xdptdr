package xpdtr.acme.gui.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LinesPersister {

	public static boolean saveLines(String file, List<String> previousLines, String possiblyNew) {
		for (String contact : previousLines) {
			if (contact.equals(possiblyNew)) {
				return false;
			}
		}

		previousLines.add(possiblyNew);
		Collections.sort(previousLines);

		try (PrintWriter w = new PrintWriter(new FileWriter(getPath(file).toFile()))) {
			for (String contact : previousLines) {
				w.println(contact);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static List<String> getLines(String file) {

		List<String> knownContacts = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(getPath(file).toFile()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				knownContacts.add(line);
			}

		} catch (IOException e) {
			if (!(e instanceof FileNotFoundException)) {
				System.out.println(e.getClass().getName() + " : " + e.getMessage());
			}
		}

		Collections.sort(knownContacts);

		return knownContacts;

	}

	private static Path getPath(String file) throws IOException {
		String userDirectory = System.getProperty("user.home");
		Path path = FileSystems.getDefault().getPath(userDirectory, ".acme-gui");
		File pathFile = path.toFile();
		if (pathFile.exists()) {
			if (!pathFile.isDirectory()) {
				throw new IOException(pathFile.getAbsolutePath() + " is not a directory");
			}
		} else {
			pathFile.mkdirs();
		}
		return path.resolve(file);
	}
	
}
