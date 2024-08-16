package InternshipProj.api.dummy;

import InternshipProj.api.dto.EventResponseDto;
import InternshipProj.api.events.CSVService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CSVServiceTests {

    @InjectMocks
    private CSVService csvService;

    @Mock
    private MultipartFile mockFile;

    @Test
    public void readDoc_shouldReturnListOfStringArrays() throws IOException, CsvException {
        // Arrange
        String csvContent = "1,Sample Event,08-16-2024:12:00,Music\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        when(mockFile.getInputStream()).thenReturn(inputStream);

        // Act
        List<String[]> result = csvService.readDoc(mockFile);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertArrayEquals(new String[]{"1", "Sample Event", "08-16-2024:12:00", "Music"}, result.get(0));
    }

    @Test
    public void validateCsv_shouldReturnErrorsForInvalidData() throws IOException, CsvException {
        // Arrange
        String csvContent = "invalid,Sample Event,08-16-2024:12:00,Music\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        when(mockFile.getInputStream()).thenReturn(inputStream);

        // Act
        List<String> errors = csvService.validateCsv(mockFile);

        // Assert
        assertNotNull(errors);
        assertTrue(errors.contains("Line 1: Invalid User ID."));
    }

    @Test
    public void writeDoc_shouldWriteCsvCorrectly() throws IOException {
        // Arrange
        List<EventResponseDto> events = new ArrayList<>();
        EventResponseDto event = new EventResponseDto();
        event.setEventName("Sample Event");
        event.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy:HH:mm")));
        event.setCategory("Music");
        event.setType("user created");
        events.add(event);

        StringWriter stringWriter = new StringWriter();

        // Act
        csvService.writeDoc(events, stringWriter);

        // Assert
        String csvContent = stringWriter.toString();
        assertTrue(csvContent.contains("Sample Event"));
        assertTrue(csvContent.contains("Music"));
        assertTrue(csvContent.contains("user created"));
    }

    @Test
    public void validateCsv_shouldReturnNoErrorsForValidData() throws IOException, CsvException {
        // Arrange
        String csvContent = "1,Sample Event,08-16-2024:12:00,Music\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
        when(mockFile.getInputStream()).thenReturn(inputStream);

        // Act
        List<String> errors = csvService.validateCsv(mockFile);

        // Assert
        assertNotNull(errors);
        assertTrue(errors.isEmpty());
    }
}
