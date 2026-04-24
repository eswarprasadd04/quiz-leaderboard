import java.net.URI;
import java.net.http.*;
import java.util.*;
public class QuizLeaderboard {

    static final String REG_NO = "RA2311050010018";
    static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();
        Set<String> seen = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();
        for (int poll = 0; poll <= 9; poll++) {
            System.out.println("\n--- Poll " + poll + " ---");
            String url = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + poll;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            System.out.println("Raw Response: " + body);
            List<String[]> events = parseEvents(body);
            for (String[] event : events) {
                String roundId = event[0];
                String participant = event[1];
                int score = Integer.parseInt(event[2]);
                String uniqueKey = roundId + "_" + participant;
                if (seen.contains(uniqueKey)) {
                    System.out.println("Skipping duplicate: " + uniqueKey);
                    continue;
                }
                seen.add(uniqueKey);
                scores.put(participant, scores.getOrDefault(participant, 0) + score);
                System.out.println("Added: " + participant + " +" + score);
            }
            if (poll < 9) {
                System.out.println("Waiting 5 seconds...");
                Thread.sleep(5000);
            }
        }
        List<Map.Entry<String, Integer>> leaderboard = new ArrayList<>(scores.entrySet());
        leaderboard.sort((a, b) -> b.getValue() - a.getValue());
        StringBuilder sb = new StringBuilder();
        sb.append("{\"regNo\":\"").append(REG_NO).append("\",\"leaderboard\":[");

        int total = 0;
        for (int i = 0; i < leaderboard.size(); i++) {
            String name = leaderboard.get(i).getKey();
            int score = leaderboard.get(i).getValue();
            total += score;
            sb.append("{\"participant\":\"").append(name)
              .append("\",\"totalScore\":").append(score).append("}");
            if (i < leaderboard.size() - 1) sb.append(",");
        }
        sb.append("]}");
        System.out.println("\n--- Final Leaderboard ---");
        System.out.println(sb);
        System.out.println("Total Score: " + total);
        HttpRequest submitRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/quiz/submit"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(sb.toString()))
                .build();

        HttpResponse<String> submitResponse = client.send(submitRequest,
                HttpResponse.BodyHandlers.ofString());
        System.out.println("\n--- Submit Response ---");
        System.out.println(submitResponse.body());
    }
    static List<String[]> parseEvents(String json) {
        List<String[]> events = new ArrayList<>();
        int eventsStart = json.indexOf("\"events\"");
        if (eventsStart == -1) return events;
        int arrayStart = json.indexOf("[", eventsStart);
        int arrayEnd = json.lastIndexOf("]");
        if (arrayStart == -1 || arrayEnd == -1) return events;
        String eventsArray = json.substring(arrayStart + 1, arrayEnd);
        String[] parts = eventsArray.split("\\},\\s*\\{");
        for (String part : parts) {
            try {
                String roundId = extractValue(part, "roundId");
                String participant = extractValue(part, "participant");
                String score = extractValue(part, "score");
                if (roundId != null && participant != null && score != null) {
                    events.add(new String[]{roundId, participant, score});
                }
            } catch (Exception e) {
                System.out.println("Error parsing: " + part);
            }
        }
        return events;
    }
    static String extractValue(String json, String key) {
        String search = "\"" + key + "\"";
        int keyIndex = json.indexOf(search);
        if (keyIndex == -1) return null;

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) return null;

        String rest = json.substring(colonIndex + 1).trim();

        if (rest.startsWith("\"")) {
            int end = rest.indexOf("\"", 1);
            return rest.substring(1, end);
        } else {
            int end = rest.indexOf(",");
            if (end == -1) end = rest.indexOf("}");
            if (end == -1) end = rest.length();
            return rest.substring(0, end).trim();
        }
    }
}
