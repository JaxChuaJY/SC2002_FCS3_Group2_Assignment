package interfaces;

import java.util.List;

public interface IFileHandler {
	void writeToFile(String filename, List<String> data);
	List<String> readFromFile(String filename);
}
