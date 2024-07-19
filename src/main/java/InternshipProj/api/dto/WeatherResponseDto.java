package InternshipProj.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WeatherResponseDto {
    private List<DataPoint> data;

    @Data
    public static class DataPoint {
        private String parameter;
        private List<Coordinate> coordinates;

        @Data
        public static class Coordinate {
            private double lat;
            private double lon;
            private List<DateValue> dates;

            @Data
            public static class DateValue {
                private String date;
                private Double value;
            }
        }
    }

    @JsonProperty("temperature")
    public Double getTemperature() {
        if (data != null && !data.isEmpty()) {
            List<DataPoint.Coordinate> coordinates = data.get(0).getCoordinates();
            if (coordinates != null && !coordinates.isEmpty()) {
                List<DataPoint.Coordinate.DateValue> dates = coordinates.get(0).getDates();
                if (dates != null && !dates.isEmpty()) {
                    return dates.get(0).getValue();
                }
            }
        }
        return null;
    }
}
