package com.fitness.aiservice.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumeMessageService {

    public void consumeMessage(String messageBody) {
        log.info("Consumed Message: " + messageBody);
    }
}