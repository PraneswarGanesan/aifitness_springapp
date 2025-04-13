package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private  final ActivityAIService aiService;
    private  final RecommendationRepository recommendationRepository;
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processActivity(Activity activity) {
        log.info("Received Activity for processing: {}", activity.getId(),activity.getUserId(),activity.getType());
        log.info("Received Activity for processing: {}",activity.getType());
        log.info("Received Activity for processing: {}", activity.getDuration());
//        log.info("Generated Recommendation: {}", aiService.generaterecommendation(activity) );
        Recommendation recommendation =  aiService.generaterecommendation(activity);
        recommendationRepository.save(recommendation);

    }
}
