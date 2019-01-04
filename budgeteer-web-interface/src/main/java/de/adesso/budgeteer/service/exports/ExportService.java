package de.adesso.budgeteer.service.exports;


import de.adesso.budgeteer.CSVUtils;
import de.adesso.budgeteer.service.record.WorkRecord;
import org.apache.wicket.markup.repeater.data.DataView;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

@Service
public class ExportService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public File generateCSVFileFromRecords(DataView<WorkRecord> records) {
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
                Iterator<? extends WorkRecord> it = records.getDataProvider().iterator(0, records.getItemCount());
                while (it.hasNext()) {
                    WorkRecord record = it.next();
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
