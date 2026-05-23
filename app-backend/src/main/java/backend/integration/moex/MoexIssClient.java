package backend.integration.moex;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MoexIssClient {

    private static final String SECURITY_URL_TEMPLATE =
            "%s/iss/engines/stock/markets/%s/boards/%s/securities/%s.json";

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSecurityRawJson(String baseUrl, String market, String board, String ticker) {
        String url = SECURITY_URL_TEMPLATE.formatted(
                baseUrl, market, board, ticker);
        return restTemplate.getForObject(url, String.class);
    }
}
