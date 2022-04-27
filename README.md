# Plugin installation
1. Run `mvn package` at the root of the project
2. Copy the artifact from `./target/keycloak-recaptcha-.*.jar` to `/opt/jboss/keycloak/standalone/deployments/` on the KeyCloak server

# How to activate in KeyCloak
1. Login to the control panel
2. Tab `Authentication` -> `Flows`
3. Select `Reset Credentials` and copy
4. Go to the copy and immediately after `Choose user` add `ReCapthca` and configure it
5. After that, on the `Bindings` tab, you need to set the password reset flow to a new