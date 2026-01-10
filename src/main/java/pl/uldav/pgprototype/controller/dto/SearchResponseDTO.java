package pl.uldav.pgprototype.controller.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SearchResponseDTO {
    private Long id;
    private Long patientId;
    private String eventType;
    private OffsetDateTime eventTime;
    private String value;
}
