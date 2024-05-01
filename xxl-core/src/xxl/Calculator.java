package xxl;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import xxl.exceptions.ImportFileException;
import xxl.exceptions.MissingFileAssociationException;
import xxl.exceptions.UnavailableFileException;
import xxl.exceptions.UnrecognizedEntryException;

/**
 * Class representing a spreadsheet application.
 */
public class Calculator {

    /** The calculator. */
    private String _filename = "";

    /** The current spreadsheet. */
    private Spreadsheet _spreadsheet = null;

    /** The current user. */
    private User _user = new User();

    /**
     * Saves the serialized application's state into the file associated to the current network.
     *
     * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
        if (_filename == null || _filename.equals(""))
            throw new MissingFileAssociationException();

        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)))) {
            oos.writeObject(_spreadsheet);
            _spreadsheet.setChanged(false);
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current network is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current network does not have a file.
     * @throws IOException if there is some error while serializing the state of the network to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _filename = filename;
        save();
    }

    /**
     * @param filename name of the file containing the serialized application's state
     *        to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *         an error while processing this file.
     */
    public void load(String filename) throws UnavailableFileException {
        _filename = filename;

        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _spreadsheet = (Spreadsheet) ois.readObject();
            _spreadsheet.setChanged(false);
        } catch (IOException | ClassNotFoundException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file and create domain entities..
     *
     * @param filename name of the text input file
     * @throws ImportFileException
     */
    public void importFile(String filename) throws ImportFileException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int lines = Integer.parseInt(reader.readLine().split("=")[1]);
            int columns = Integer.parseInt(reader.readLine().split("=")[1]);
            _spreadsheet = new Spreadsheet(lines, columns);

            String entry;
            while ((entry = reader.readLine()) != null) {
                String[] fields = entry.split("\\|");
                if (fields.length == 2)
	                _spreadsheet.insertContents(fields[0], fields[1]);
            }
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename, e);
        }
    }

    /**
     * @return filename.
     */
    public String getFilename() {
        return _filename;
    }

    /**
     * @param filename name of the file containing the serialized application's state.
     */
    public void setFilename(String filename) {
        _filename = filename;
    }

    /**
     * @return spreadsheet.
     */
    public Spreadsheet getSpreadsheet() {
        return _spreadsheet;
    }

    /**
     * @return changed?
     */
    public boolean hasChanged() {
        return _spreadsheet.hasChanged();
    }

    /**
     * @return if the application has already started.
     */
    public boolean hasStarted() {
        return _spreadsheet != null;
    }

    /**
     * Reset the calculator.
     */
    public void reset(int lines, int columns) {
        _spreadsheet = new Spreadsheet(lines, columns);
    }

}
