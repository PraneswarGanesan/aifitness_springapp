package com.fitness.aiservice.model;
import  java.util.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "recommendations")
@Data
@Builder
public class Recommendation {
    @Id
    private String id;
    private String activityId;
    private String userId;
    private String acitivityType;
    private String recommendations;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;
    private LocalDateTime createdAt;
}
