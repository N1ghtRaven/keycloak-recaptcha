<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
    <#if section = "header">
        Подтвердите что вы человек
    <#elseif section = "form">
        <script src="https://www.google.com/recaptcha/api.js" async defer></script>

        <#if _err??>
            <div class="alert-error pf-c-alert pf-m-inline pf-m-danger">
                <div class="pf-c-alert__icon">
                    <span class="fa fa-fw fa-exclamation-circle"></span>
                </div>
                <span class="pf-c-alert__title kc-feedback-text">Некорректная каптча</span>
            </div>
        </#if>

        <form id="kc-sms-code-login-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
            <div class="${properties.kcFormGroupClass!}">
                <div align="center" class="g-recaptcha" data-sitekey="${site_key}"></div>
            </div>
            <div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
                <div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
                    <div class="${properties.kcFormOptionsWrapperClass!}">
                        <span><a href="${url.loginUrl}">${kcSanitize(msg("backToLogin"))?no_esc}</a></span>
                    </div>
                </div>

                <div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
                    <input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" name="accept" type="submit" value="${msg("doSubmit")}"/>
                </div>
            </div>
        </form>
    </#if>
</@layout.registrationLayout>
