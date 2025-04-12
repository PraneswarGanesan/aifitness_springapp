package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody  ActivityRequest request){
        return ResponseEntity.ok(activityService.trackActivit(request));

    }


}
