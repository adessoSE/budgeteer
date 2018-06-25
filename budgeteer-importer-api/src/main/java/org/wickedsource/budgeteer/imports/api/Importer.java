package org.wickedsource.budgeteer.imports.api;

import java.io.Serializable;
import java.util.List;

public interface Importer extends Serializable {

	/**
	* Returns the name of this importer as it should be displayed in the UI.
	*
	* @return the human readable name of this importer.
	*/
	String getDisplayName();

	/**
	* Returns the file extensions of import file that this importer understands.
	*
	* @return list of file extensions (with leading ".").
	*/
	List<String> getSupportedFileExtensions();

	/**
	* Returns an example file that contains data in the format that this importer understands.
	* This file can be downloaded by the user to learn the import format.
	*
	* @return example file with the data format that this importer understands.
	*/
	ExampleFile getExampleFile();

	/**
	* During the import-process it's possible to skipp some data sets (according to internal rules).
	* The skipped sets are returned by this method. The values of each data set was converted to Strings
	* @return List of skipped data sets or null if there weren't any because the importer didn't skipp data sets
	*/
	List<List<String>> getSkippedRecords();


}
