package InternshipProj.api.dto;

import lombok.Data;

@Data
public class CompWeatherResponseDto {
    private Double temperature;
    private String location;
    private String timestamp;
}

