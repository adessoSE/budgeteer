package org.wickedsource.budgeteer.imports.api;



public class InvalidFileFormatException extends Exception {
    private String fileName;

    public InvalidFileFormatException(String message, String fileName){
        super(message);
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;
    }
}
