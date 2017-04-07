package org.wickedsource.budgeteer.service.exports;

import org.apache.wicket.markup.html.list.ListView;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.CSVUtils;
import org.wickedsource.budgeteer.service.record.WorkRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

@Service
public class ExportService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public File generateCSVFileFromRecords(ListView<WorkRecord> records) {
        File csvFile = null;
        try {
            csvFile = new File("export.csv");
            csvFile.createNewFile();
            FileWriter writer = new FileWriter(csvFile);

            if (writer != null) {

                CSVUtils.writeLine(writer, Arrays.asList(
                        "Budget",
                        "Name",
                        "Daily Rate",
                        "Date",
                        "Hours",
                        "Budget"));

                for (WorkRecord record : records.getList()) {
                    CSVUtils.writeLine(writer, Arrays.asList(
                            record.getBudgetName(),
                            record.getPersonName(),
                            record.getDailyRate().getCurrencyUnit().toString() + " " + record.getDailyRate().getAmount().toString(),
                            record.getDate().toString(),
                            Double.toString(record.getHours()),
                            record.getBudgetBurned().getAmount().toString()));
                }
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.err.println("Error while exporting to csv");
            e.printStackTrace();
        }
        return csvFile;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
