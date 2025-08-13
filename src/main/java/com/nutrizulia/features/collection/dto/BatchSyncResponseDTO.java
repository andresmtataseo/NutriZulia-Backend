package com.nutrizulia.features.collection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchSyncResponseDTO {

    @Builder.Default
    private List<String> success = new ArrayList<>();
    
    @Builder.Default
    private List<FailedRecordDTO> failed = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FailedRecordDTO {
        private String uuid;
        private String reason;
    }
}