package works.red_eye.keycloak.spi;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

public class RecaptchaExecutionFactory implements AuthenticatorFactory {

	@Override
	public String getId() {
		return "recaptcha-execution";
	}

	@Override
	public String getDisplayType() {
		return "ReCaptcha";
	}

	@Override
	public String getHelpText() {
		return "Проверка капчи";
	}

	@Override
	public String getReferenceCategory() {
		return "captcha";
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}

	@Override
	public boolean isUserSetupAllowed() {
		return false;
	}

	@Override
	public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
		return new AuthenticationExecutionModel.Requirement[] {
			AuthenticationExecutionModel.Requirement.REQUIRED,
			AuthenticationExecutionModel.Requirement.DISABLED
		};
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return Arrays.asList(
			new ProviderConfigProperty("public", "Сайт ключ", "Публичный ключ", ProviderConfigProperty.STRING_TYPE, ""),
			new ProviderConfigProperty("secret", "Секретный ключ", "Приватный ключ", ProviderConfigProperty.STRING_TYPE, "")
		);
	}

	@Override
	public Authenticator create(KeycloakSession session) {
		return new RecaptchaExecution();
	}

	@Override
	public void init(Config.Scope config) {
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {
	}

	@Override
	public void close() {
	}

}
