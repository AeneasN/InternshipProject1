package InternshipProj.api.csv;

import InternshipProj.api.dto.EventResponseDto;
import InternshipProj.api.events.EventCategory;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {
    public List<String[]> readDoc(MultipartFile up) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(up.getInputStream()))) {
            return reader.readAll();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
    public List<String> validateCsv(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = reader.readAll();
            for (int i = 0; i < records.size(); i++) {
                String[] record = records.get(i);
                validateRecord(record, i + 1, errors);
            }
        } catch (CsvException e) {
            errors.add("Error reading CSV file.");
        }
        return errors;
    }

    private void validateRecord(String[] record, int lineNumber, List<String> errors) {
        if (record.length != 4) {
            errors.add("Line " + lineNumber + ": Incorrect number of columns.");
            return;
        }

        try {
            Long userId = Long.valueOf(record[0]);
        } catch (NumberFormatException e) {
            errors.add("Line " + lineNumber + ": Invalid User ID.");
        }

        String title = record[1];
        if (title.trim().isEmpty()) {
            errors.add("Line " + lineNumber + ": Title cannot be empty.");
        }

        try {
            LocalDate.parse(record[2], DateTimeFormatter.ofPattern("MM-dd-yyyy:HH:mm"));
        } catch (DateTimeParseException e) {
            errors.add("Line " + lineNumber + ": Invalid date format.");
        }

        try {
            EventCategory.valueOf(record[3].toUpperCase().replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            errors.add("Line " + lineNumber + ": Invalid category.");
        }
    }
    public void writeDoc(List<EventResponseDto> events, Writer writer) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Title", "Date", "Category", "Type"}); //Columns just to show the format
            for (EventResponseDto event : events) {
                data.add(new String[]{
                        event.getEventName(),
                        event.getDate(),
                        event.getCategory(),
                        event.getType()
                });
            }
            csvWriter.writeAll(data);
        }
    }
}
