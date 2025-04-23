package interfaces;

import java.util.List;

/**
 * Handles generic file read and write operations.
 */
public interface IFileHandler {
	
	/**
     * Writes a list of string data into the specified file.
     *
     * @param filename the name (path) of the file to write to
     * @param data the lines of data to write
     */
	void writeToFile(String filename, List<String> data);
	
	/**
     * Reads all lines from the specified file.
     *
     * @param filename the name (path) of the file to read from
     * @return a list of strings, each representing a line from the file
     */
	List<String> readFromFile(String filename);
}