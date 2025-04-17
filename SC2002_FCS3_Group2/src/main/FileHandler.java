package main;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import interfaces.IFileHandler;

public class FileHandler implements IFileHandler{

	/**
     * Writes a list of strings to a file.
     * If the file's parent directories do not exist, they will be created.
     *
     * @param filename The path of the file to write to.
     * @param data The list of strings to be written to the file.
     */
	@Override
    public void writeToFile(String filename, List<String> data) {
        try {
            File file = new File(filename);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
            	directory.mkdirs();
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String line : data) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

	/**
     * Reads the contents of a file into a list of strings. Each line in the file becomes one element in the list.
     *
     * @param filename The path of the file to read from.
     * @return A list of strings
     */
	@Override
    public List<String> readFromFile(String filename) {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
        return data;
    }
}