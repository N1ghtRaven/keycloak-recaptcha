package works.red_eye.keycloak.service.captcha;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.regex.Pattern;

public class ReCaptchaService implements CaptchaService {

    private static final Logger LOG = Logger.getLogger(ReCaptchaService.class);

    private final static String API_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    private final static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private final String secretKey;

    public ReCaptchaService(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean verifyCaptcha(String captchaResponse) {
        if(!responseSanityCheck(captchaResponse)) {
            LOG.info("Response contains invalid characters");
            return false;
        }

        String response = makeRequest(captchaResponse);
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(response);
            return ((Boolean) json.get("success"));
        } catch (ParseException e) { return false; }
    }

    private String makeRequest(String captchaResponse) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(makeUrl(captchaResponse, secretKey));

        try {
            HttpEntity response = client.execute(post).getEntity();
            return response != null ? EntityUtils.toString(response) : null;
        } catch (IOException e) { return null; }
    }

    private static String makeUrl(String captchaResponse, String secretKey) {
        return String.format(API_URL, secretKey, captchaResponse);
    }

    private boolean responseSanityCheck(String response) {
        return response != null && response.length() > 0 && RESPONSE_PATTERN.matcher(response).matches();
    }

}
