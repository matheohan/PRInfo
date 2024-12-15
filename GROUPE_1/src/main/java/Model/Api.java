package Model;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class Api {
    private static final String API_BASE_URL = "https://comicvine.gamespot.com/api";
    private static final String API_KEY = "1a582c946b0b328eea2d5389e530355572e95a27";

    // Méthode pour rechercher les issues associées à un personnage
    public String searchComicsByCharacter(String characterName, int resultLimit) throws Exception {
        StringBuilder resultBuilder = new StringBuilder();

        // Encodage du nom du personnage
        String encodedCharacterName = URLEncoder.encode(characterName, StandardCharsets.UTF_8);

        // Construire l'URL
        String url = API_BASE_URL + "/search/?api_key=" + API_KEY +
                     "&format=json" +
                     "&query=" + encodedCharacterName +
                     "&resources=character" +
                     "&limit=" + resultLimit;

        // Log de l'URL
        System.out.println("Request URL: " + url);

        // Créer le client HTTP et envoyer la requête
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(new URI(url))
                                         .GET()
                                         .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray results = jsonResponse.getJSONArray("results");
            System.out.println(results);

            if (results.length() > 0) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject issue = results.getJSONObject(i);
                    System.out.println(issue);

                    String issueName = issue.optString("name", "Unknown Comic");                   

                    resultBuilder.append(i + 1).append(". ").append(issueName).append("\n");
                }
            } else {
                resultBuilder.append("No issues found featuring the character: ").append(characterName);
            }
        } else {
            throw new ApiException("HTTP Error: " + response.statusCode());
        }

        return resultBuilder.toString();
    }

    // Exception personnalisée
    public class ApiException extends Exception {
        public ApiException(String message) {
            super(message);
        }
    }


    public String searchByTitle(String title, int resultLimit) throws Exception {
        StringBuilder resultBuilder = new StringBuilder();
    
        // Encodage du titre pour les caractères spéciaux et les espaces
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.name());
    
        // Construire l'URL avec le titre encodé
        String url = API_BASE_URL + "/issues/?api_key=" + API_KEY +
                "&format=json" +
                "&filter=name:" + encodedTitle + 
                "&limit=" + resultLimit;
        // Log de l'URL (utile pour le débogage)
        System.out.println("Request URL: " + url);
    
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(new URI(url))
                                         .GET()
                                         .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    

        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray results = jsonResponse.getJSONArray("results");

            if (results.length() > 0) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject issue = results.getJSONObject(i);
                    String issueTitle = issue.optString("name", "Unknown Comic");
                    JSONObject volume = issue.optJSONObject("volume");
                    String volumeName = (volume != null) ? volume.optString("name", "Unknown Volume") : "Unknown Volume";

                    resultBuilder.append(i + 1).append(". ").append(issueTitle)
                                 .append(" (Volume: ").append(volumeName).append(")\n");
                }
            } else {
                resultBuilder.append("No issues found with the title: ").append(title);
            }
        } else {
            throw new Exception("HTTP Error: " + response.statusCode());
        }

        return resultBuilder.toString();
    }


    public static void main(String[] args) {
        Api api = new Api();
        
        try {
            // Test the searchComicsByCharacter method
            System.out.println("Testing searchComicsByCharacter...");
            String characterSearchResults = api.searchComicsByCharacter("Spider-Man",20);
            System.out.println(characterSearchResults);

            // Test the searchByTitle method
            System.out.println("\nTesting searchByTitle...");
            api.searchByTitle("Blutrote",20);
        } catch (Exception e) {
            System.out.println("An error occurred while testing the API:");
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
}





   


