package de.adesso.budgeteer.core.common;

import lombok.Value;

@Value
public class FileAttachment {
    String fileName;
    byte[] file;
}
