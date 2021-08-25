package de.adesso.budgeteer.core.common;

import lombok.Value;

@Value
public class Attachment {
    String fileName;
    String link;
    byte[] file;
}
