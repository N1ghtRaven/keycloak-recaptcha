package works.red_eye.keycloak.spi;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import works.red_eye.keycloak.service.captcha.CaptchaService;
import works.red_eye.keycloak.service.captcha.ReCaptchaService;

import javax.ws.rs.core.Response;
import java.util.Map;

import static works.red_eye.keycloak.spi.RecaptchaExecution.FormKit.CAPTCHA_CHALLENGE;
import static works.red_eye.keycloak.spi.RecaptchaExecution.FormKit.CAPTCHA_INVALID;

public class RecaptchaExecution implements Authenticator {

	private static final String TPL_CAPTCHA = "captcha.ftl";
	enum FormKit {
		CAPTCHA_CHALLENGE,
		CAPTCHA_INVALID;

		Response getResponse(AuthenticationFlowContext context) {
			Map<String, String> config = context.getAuthenticatorConfig().getConfig();
			String siteKey = config.get("public");

			LoginFormsProvider form = context.form().setAttribute("realm", context.getRealm());
			switch (this) {
				case CAPTCHA_CHALLENGE:
					return form.setAttribute("site_key", siteKey).createForm(TPL_CAPTCHA);
				case CAPTCHA_INVALID:
					return form.setAttribute("_err", true).setAttribute("site_key", siteKey).createForm(TPL_CAPTCHA);
				default:
					return form.createErrorPage(Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

	}

	@Override
	public void authenticate(AuthenticationFlowContext context) {
		context.challenge(CAPTCHA_CHALLENGE.getResponse(context));
	}

	@Override
	public void action(AuthenticationFlowContext context) {
		Map<String, String> config = context.getAuthenticatorConfig().getConfig();
		String secretKey = config.get("secret");

		String captchaResponse = context.getHttpRequest().getDecodedFormParameters().getFirst("g-recaptcha-response");
		if (captchaResponse == null) {
			context.failureChallenge(AuthenticationFlowError.EXPIRED_CODE, CAPTCHA_INVALID.getResponse(context));
			return;
		}

		CaptchaService captchaService = new ReCaptchaService(secretKey);
		if (!captchaService.verifyCaptcha(captchaResponse)) {
			context.failureChallenge(AuthenticationFlowError.EXPIRED_CODE, CAPTCHA_INVALID.getResponse(context));
			return;
		}

		context.success();
	}

	@Override
	public boolean requiresUser() {
		return true;
	}

	@Override
	public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
		return true;
	}

	@Override
	public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

	}

	@Override
	public void close() {

	}
}
