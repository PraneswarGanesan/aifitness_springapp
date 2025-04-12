package com.fitness.activityservice.dto;

import com.fitness.activityservice.model.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest {
    private  String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime startTime;
    private LocalDateTime updatedTime;
}
