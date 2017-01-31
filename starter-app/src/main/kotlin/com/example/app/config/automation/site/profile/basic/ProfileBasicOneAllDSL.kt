/*
 * Copyright (c) Interactive Information R & D (I2RD) LLC.
 * All Rights Reserved.
 *
 * This software is confidential and proprietary information of
 * I2RD LLC ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with I2RD.
 */

package com.example.app.config.automation.site.profile.basic

import com.example.app.config.ProjectInformation
import com.example.app.config.automation.site.profile.basic.ProfileBasicDSL.Companion.SITE_FRONTEND
import com.example.app.login.oneall.service.OneAllLoginService
import com.example.app.login.social.ui.SocialLogin
import com.example.app.login.social.ui.SocialLoginMode
import com.example.app.profile.ui.ApplicationFunctions
import experimental.cms.dsl.AppDefinition
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.net.URL

private val appName = ProjectInformation.APPLICATION_NAME

@Profile("automation")
@Component
open class ProfileBasicOneAllDSL :
    AppDefinition(DEFINITION_NAME, version = 1, siteId = SITE_FRONTEND,
        dependency = ProfileBasicDSL.DEFINITION_NAME, init = {
    page("Login", "/login") {
        template("Login")
        content("Body", SocialLogin("OneAll Social Login")) {
            htmlClass = "oneall-social-login"
            landingPage("Dashboard")
            scriptedRedirect("StarterSiteRedirectScript.groovy")
            scriptedRedirectParam("Default Redirect Page", "/dashboard")
            loginService(OneAllLoginService.SERVICE_IDENTIFIER)
            provider("google")
            mode(SocialLoginMode.Login)
            //You can enable SSO by uncommenting the line below:
//            additionalProperty(OneAllLoginService.PROP_SSO_ENABLED, "true")
        }
    }

    page(ApplicationFunctions.User.MY_ACCOUNT_VIEW, "/account/my-profile") {
        template("Frontend")
        content("Body", SocialLogin("OneAll Link")) {
            htmlClass = "oneall-link"
            loginService(OneAllLoginService.SERVICE_IDENTIFIER)
            provider("google")
            mode(SocialLoginMode.Link)
        }
    }
}) {
    companion object {
        internal const val DEFINITION_NAME = "${ProfileBasicDSL.DEFINITION_NAME} - OneAll"
    }
}

@Profile("automation")
@Component
open class ProteusBackendOneAllDSL :
    AppDefinition(DEFINITION_NAME, version = 1, siteId = SITE_BACKEND, init = {

    libraryResources(URL("https://repo.venturetech.net/artifactory/vt-snapshot-local/" +
        "com/example/starter-app/\${LATEST}/starter-app-\${LATEST}-libraries.zip"))

    page("Dashboard", "/config/dashboard") { }

    page("Login", "/account/login") {
        template("Login")
        content("Primary Content", SocialLogin("OneAll Social Login")) {
            htmlClass = "oneall-social-login"
            landingPage("Dashboard")
            scriptedRedirect("StarterSiteRedirectScript.groovy")
            scriptedRedirectParam("Default Redirect Page", "/config/dashboard")
            loginService(OneAllLoginService.SERVICE_IDENTIFIER)
            provider("google")
            mode(SocialLoginMode.Login)
            //You can enable SSO by uncommenting the line below:
            additionalProperty(OneAllLoginService.PROP_SSO_ENABLED, "true")
        }
    }

    page("My Preferences", "/account/preferences") {
        template("Neptune - Protected - No Primary Content Styles")
        content("Primary Content", SocialLogin("OneAll Social Link")) {
            htmlClass = "oneall-link"
            loginService(OneAllLoginService.SERVICE_IDENTIFIER)
            provider("google")
            mode(SocialLoginMode.Link)
        }
    }

    hostname("\${proteus.install.name}-admin.\${base.domain}", "Dashboard")
}) {
    companion object {
        internal const val DEFINITION_NAME = "Proteus Backend - OneAll"
        internal const val SITE_BACKEND = "Administration site, backend, " +
            "for managing content for this installation of Proteus Framework."
    }
}