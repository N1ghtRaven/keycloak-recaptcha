package works.red_eye.keycloak.service.captcha;

public interface CaptchaService {

    boolean verifyCaptcha(String captchaResponse);

}
