package de.adesso.budgeteer.web.components.fileUpload;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileUploadModel implements Serializable{
    private String fileName;
    private byte[] file;
    private boolean changed;
    private String link;

    public FileUploadModel(){
    }

    public FileUploadModel(String fileName, byte[] file, String link) {
        this.fileName = fileName;
        this.file = file;
        this.link = link;
    }

    public String getLink() {
        if(link != null) {
            if (!link.matches("^https?://.*")) {
                link = "http://" + link;
            }
        }
        return link;
    }
}
