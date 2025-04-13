package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import  java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {
    private  final GeminiService geminiService;

    public Recommendation generaterecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("response from ai: {}",aiResponse);

        return processAiResponse(activity,aiResponse);
    }

    private Recommendation processAiResponse(Activity activity,String aiResponse){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String textNodeValue = textNode.asText();


            String jsonContent = textNodeValue
                    .replaceAll("(?i)```json\\s*", "")
                    .replaceAll("(?i)```", "")
                    .trim();

//            log.info("Parsed response from ai: {}", jsonContent);
            JsonNode analysisJson = mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnallysis = new StringBuilder();
            addAnalysisSection(fullAnallysis,analysisNode,"overall","OverAll:");
            addAnalysisSection(fullAnallysis,analysisNode,"pace","Pace:");
            addAnalysisSection(fullAnallysis,analysisNode,"heartrate","HeartRate:");
            addAnalysisSection(fullAnallysis,analysisNode,"caloriesBurned","CaloriesBurned:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestion = extractSuggestion(analysisJson.path("suggestions"));
            List<String> safety = extractSafety(analysisJson.path("safety"));
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .acitivityType(activity.getType())
                    .recommendations(fullAnallysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestion)
                    .safety(safety)
                    .createdAt(LocalDateTime.now()).build();
        }
        catch (Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }

    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        List<String> defaultImprovements = List.of(
                "Keep up the consistent effort in your workouts.",
                "Track your runs regularly to monitor your progress.",
                "Maintain a balanced diet to support your fitness goals."
        );

        List<String> defaultSuggestions = List.of(
                "Try a 20-minute light jog to stay active.",
                "Include dynamic stretches before each run to prevent injury."
        );

        List<String> defaultSafetyTips = List.of(
                "Stay hydrated before and after exercise.",
                "Avoid overtraining – rest days are essential."
        );

        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .acitivityType(activity.getType())
                .recommendations("No AI analysis available. This is a default recommendation based on your activity type.")
                .improvements(defaultImprovements)
                .suggestions(defaultSuggestions)
                .safety(defaultSafetyTips)
                .createdAt(LocalDateTime.now())
                .build();
    }


    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(item -> safety.add(item.asText()));
        }
        return safety.isEmpty() ?
                Collections.singletonList("No safety guidelines") :
                safety;
    }

    private List<String> extractSuggestion(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(improvement ->{
                String workout_name = improvement.path("workout_name").asText();
                String description = improvement.path("description").asText();
                suggestions.add(String.format("%s: %s",workout_name,description));
            });
        }
        return suggestions.isEmpty() ?
                Collections.singletonList("No specific suggestions provided ") :
                suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement ->{
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s",area,detail));

            });
        }
        return improvements.isEmpty() ?
                Collections.singletonList("No specific improvements provided ") :
                improvements ;
    }

    private void addAnalysisSection(StringBuilder fullAnallysis, JsonNode analysisNode, String key, String prefix) {
            if(!analysisNode.path(key).isMissingNode()){
                fullAnallysis.append(prefix)
                        .append(analysisNode.path(key).asText())
                        .append("\n\n");
            }
    }


    private String createPromptForActivity(Activity activity) {

        return String.format("""
Analyze this fitness Activity and provide the detailed recommendation in this exact JSON format:
{
    "analysis": {
        "overall": "Overall analysis here",
        "pace": "Pace analysis here",
        "heartrate": "Heart rate analysis here",
        "caloriesBurned": "Calories analysis here"
    },
    "improvements": [
        {
            "area": "Area name",
            "recommendation": "Detailed Recommendation"
        }
    ],
    "suggestions": [{
        "workout_name": "Name of the suggested workout",
        "description": "Description of the workout and its benefits",
    }],
     "safety": [
            "Safety point 1",
            "Safety point 2"
        ]
}

Analyze this activity:
Activity Type: %s
Duration: %d minutes
Calories Burned: %d
Additonal Metrics: %s

Provide detailed analysis focusing on performance, improvements, next workout suggestion, and safety tips.
Ensure the response follows the Exact JSON format shown above.
""",
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );



    }
}
