import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Conversor {
    public static double getConversionRate(String fromCurrency, String toCurrency) {
        String respBody = callAPI(fromCurrency);

        JsonObject jsonObject = new Gson().fromJson(respBody, JsonObject.class);
        JsonObject convRates = jsonObject.getAsJsonObject("conversion_rates");

        if (!convRates.has(toCurrency)) {
            throw new IllegalArgumentException("Moeda de destino inválida: " + toCurrency);
        }

        return convRates.get(toCurrency).getAsDouble();
    }

    private static String callAPI(String fromCurrency) {
        String endPoint = "https://v6.exchangerate-api.com/v6/6e56a55f83ce147e31e060d5/latest/" + fromCurrency;

        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(endPoint))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro ao obter a taxa de conversão: " + response.body());
            }

            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar a API: " + e.getMessage());
        }
    }
}
