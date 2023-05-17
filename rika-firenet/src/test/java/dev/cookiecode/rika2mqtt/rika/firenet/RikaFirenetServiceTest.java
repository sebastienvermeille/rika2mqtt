/*
 * Copyright (c) 2023 Sebastien Vermeille and contributors.
 *
 * Use of this source code is governed by an MIT
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package dev.cookiecode.rika2mqtt.rika.firenet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.MediaType.APPLICATION_JSON;
import static org.mockserver.model.MediaType.HTML_UTF_8;

import dev.cookiecode.rika2mqtt.rika.firenet.api.RetrofitConfiguration;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.CouldNotAuthenticateToRikaFirenetException;
import dev.cookiecode.rika2mqtt.rika.firenet.exception.InvalidStoveIdException;
import dev.cookiecode.rika2mqtt.rika.firenet.model.StoveId;
import java.util.List;
import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Test class
 *
 * @author Sebastien Vermeille
 */
@SpringBootTest(classes = {
    RikaFirenetServiceImpl.class,
    RetrofitConfiguration.class
})
@Testcontainers
class RikaFirenetServiceTest {

  @Container
  static MockServerContainer mockServer = new MockServerContainer(
      DockerImageName.parse("mockserver/mockserver:5.15.0"));
  @Autowired
  private RikaFirenetServiceImpl rikaFirenetService;

  @DynamicPropertySource
  static void registerMockServerProperties(final DynamicPropertyRegistry registry) {
    registry.add("rika.url",
        () -> "http://" + mockServer.getHost() + ":" + mockServer.getServerPort());
  }

  @Test
  void isAuthenticatedShouldReturnTrueGivenValidMockResponseIsProvided() throws Exception {
    // GIVEN
    initSuccessLoginMock();

    // WHEN
    this.rikaFirenetService.authenticate();

    // THEN
    assertThat(this.rikaFirenetService.isAuthenticated()).isTrue();
  }

  private void initSuccessLoginMock() {
    new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        .when(
            request()
                .withMethod("POST")
                .withPath("/web/login"),
            Times.once()
        )
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(HTML_UTF_8)
                .withBody(
                    // Valid credentials response contains a link to logout <a href="/web/logout"
                    """
                            <!DOCTYPE html>
                            <html lang="en" dir="ltr">
                                <head>
                                    <title>RIKA firenet</title>
                                    <meta name="viewport" content="width=device-width,initial-scale=1">
                                    <meta name="apple-mobile-web-app-capable" content="yes">
                                    <meta name="apple-mobile-web-app-status-bar-style" content="black">
                                    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Source+Sans+Pro:300,200">
                                    <link rel="stylesheet" href="/assets/rika-firenet.css?version=7">
                                    <script src="/assets/rika-firenet-en.js?version=4"></script>
                                    <link rel="shortcut icon" href="/images/favicon/favicon.ico">
                                    <link rel="icon" sizes="16x16 32x32 64x64" href="/images/favicon/favicon.ico">
                                    <link rel="icon" type="image/png" sizes="196x196" href="/images/favicon/favicon-192.png">
                                    <link rel="icon" type="image/png" sizes="160x160" href="/images/favicon/favicon-160.png">
                                    <link rel="icon" type="image/png" sizes="96x96" href="/images/favicon/favicon-96.png">
                                    <link rel="icon" type="image/png" sizes="64x64" href="/images/favicon/favicon-64.png">
                                    <link rel="icon" type="image/png" sizes="32x32" href="/images/favicon/favicon-32.png">
                                    <link rel="icon" type="image/png" sizes="16x16" href="/images/favicon/favicon-16.png">
                                    <link rel="apple-touch-icon" href="/images/favicon/favicon-57.png">
                                    <link rel="apple-touch-icon" sizes="114x114" href="/images/favicon/favicon-114.png">
                                    <link rel="apple-touch-icon" sizes="72x72" href="/images/favicon/favicon-72.png">
                                    <link rel="apple-touch-icon" sizes="144x144" href="/images/favicon/favicon-144.png">
                                    <link rel="apple-touch-icon" sizes="60x60" href="/images/favicon/favicon-60.png">
                                    <link rel="apple-touch-icon" sizes="120x120" href="/images/favicon/favicon-120.png">
                                    <link rel="apple-touch-icon" sizes="76x76" href="/images/favicon/favicon-76.png">
                                    <link rel="apple-touch-icon" sizes="152x152" href="/images/favicon/favicon-152.png">
                                    <link rel="apple-touch-icon" sizes="180x180" href="/images/favicon/favicon-180.png">
                                    <meta name="msapplication-TileColor" content="#FFFFFF">
                                    <meta name="msapplication-TileImage" content="/images/favicon/favicon-144.png">
                                    <meta name="msapplication-config" content="/images/favicon/browserconfig.xml">
                                </head>
                                <body id="rika-body">
                                    <div data-role="page">
                                        <div id="cookieMessage">
                                            <div>This site uses cookies. By continuing to browse the site you agree to our use of cookies. To learn more read our <a href=/web/privacy target=_blank> cookie policy </a>.</div>
                                            <button id="acceptCookies" data-icon="fa-check" data-iconpos="notext" data-mini="true" data-inline="true"></button>
                                        </div>
                                        <div id="sidePanel" data-role="panel" data-display="overlay" data-position="right" data-position-fixed="true" data-theme="a">
                                            <a id="sidePanelCloseButton" href="#sidePanel"></a>
                                            <div data-role="controlgroup">
                                                <h3>Settings</h3>
                                                <ul id="sidePanelButtons" data-role="listview" data-inset="true" data-theme="a">
                                                    <li> <a href="/web/summary" data-theme="a" data-role="button" data-icon="fa-th-list" data-iconpos="left" class="sidePanelButton">Summary</a></li>
                                                    <li> <a href="/web/add" data-theme="a" data-role="button" data-icon="fa-plus" data-iconpos="left" class="sidePanelButton">Add stove</a></li>
                                                    <li> <a href="/web/profile" data-theme="a" data-role="button" data-icon="fa-user" data-iconpos="left" class="sidePanelButton">Manage account</a></li>
                                                    <li> <a href="/web/password" data-theme="a" data-role="button" data-icon="fa-lock" data-iconpos="left" class="sidePanelButton">Change password</a></li>
                                                    <li><a href="/web/about" data-theme="a" data-role="button" data-icon="fa-question-circle" data-iconpos="left" class="sidePanelButton">Imprint, privacy</a></li>
                                                    <li> <a href="/web/logout" data-theme="a" data-role="button" data-icon="fa-sign-out" data-iconpos="left" class="sidePanelButton">Log out</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div id="rika-header" data-role="header" data-position="fixed" data-tap-toggle="false">
                                            <div class="rika-header-wrapper">
                                                <a href="/web/" style="float:left"><img src="/images/RIKA-flame.svg" style="height:2.8em; padding: 0.1em;"></a><a id="sidePanelButton" href="#sidePanel"></a>
                                                <div class="rika-title"><span style="line-height: 1em">Welcome </span><span style="line-height: 1em" class="hide-medium">to RIKA firenet, </span><span style="line-height: 1em"><br>john.doe@gmail.com</span></div>
                                            </div>
                                        </div>
                                        <div role="main" class="ui-content">
                                            <div data-role="controlgroup">
                                                <h3>You have access to the following stoves</h3>
                                                <ul id="stoveList" data-role="listview" data-inset="true" data-theme="a" data-split-theme="a" data-split-icon="fa-pencil">
                                                    <li><a href="/web/stove/12345678" data-ajax="false">Living Room</a><a href="/web/edit/12345678" data-ajax="false"></a></li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div id="gadiv">
                                            <script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                                                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                                                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                                                })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
                                                ga('create', 'UA-71662097-1', 'auto');
                                                ga('send', 'pageview');
                                            </script>
                                        </div>
                                    </div>
                                </body>
                            </html>               
                        """
                )
        );
  }

  @Test
  void isAuthenticatedShouldReturnFalseGivenInvalidMockResponseIsProvided() {
    // GIVEN
    initFailureLoginMock();

    // THEN
    assertThrows(CouldNotAuthenticateToRikaFirenetException.class, () -> {
      // WHEN
      this.rikaFirenetService.authenticate();
    });
    assertThat(this.rikaFirenetService.isAuthenticated()).isFalse();
  }

  private void initFailureLoginMock() {
    new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        .when(
            request()
                .withMethod("POST")
                .withPath("/web/login"),
            Times.once()
        )
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(HTML_UTF_8)
                .withBody(
                    // Invalid credentials response
                    """
                        <!DOCTYPE html>
                        <html lang="en" dir="ltr">
                            <head>
                                <title>RIKA firenet</title>
                                <meta name="viewport" content="width=device-width,initial-scale=1">
                                <meta name="apple-mobile-web-app-capable" content="yes">
                                <meta name="apple-mobile-web-app-status-bar-style" content="black">
                                <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Source+Sans+Pro:300,200">
                                <link rel="stylesheet" href="/assets/rika-firenet.css?version=7">
                                <script src="/assets/rika-firenet-en.js?version=4"></script>
                                <link rel="shortcut icon" href="/images/favicon/favicon.ico">
                                <link rel="icon" sizes="16x16 32x32 64x64" href="/images/favicon/favicon.ico">
                                <link rel="icon" type="image/png" sizes="196x196" href="/images/favicon/favicon-192.png">
                                <link rel="icon" type="image/png" sizes="160x160" href="/images/favicon/favicon-160.png">
                                <link rel="icon" type="image/png" sizes="96x96" href="/images/favicon/favicon-96.png">
                                <link rel="icon" type="image/png" sizes="64x64" href="/images/favicon/favicon-64.png">
                                <link rel="icon" type="image/png" sizes="32x32" href="/images/favicon/favicon-32.png">
                                <link rel="icon" type="image/png" sizes="16x16" href="/images/favicon/favicon-16.png">
                                <link rel="apple-touch-icon" href="/images/favicon/favicon-57.png">
                                <link rel="apple-touch-icon" sizes="114x114" href="/images/favicon/favicon-114.png">
                                <link rel="apple-touch-icon" sizes="72x72" href="/images/favicon/favicon-72.png">
                                <link rel="apple-touch-icon" sizes="144x144" href="/images/favicon/favicon-144.png">
                                <link rel="apple-touch-icon" sizes="60x60" href="/images/favicon/favicon-60.png">
                                <link rel="apple-touch-icon" sizes="120x120" href="/images/favicon/favicon-120.png">
                                <link rel="apple-touch-icon" sizes="76x76" href="/images/favicon/favicon-76.png">
                                <link rel="apple-touch-icon" sizes="152x152" href="/images/favicon/favicon-152.png">
                                <link rel="apple-touch-icon" sizes="180x180" href="/images/favicon/favicon-180.png">
                                <meta name="msapplication-TileColor" content="#FFFFFF">
                                <meta name="msapplication-TileImage" content="/images/favicon/favicon-144.png">
                                <meta name="msapplication-config" content="/images/favicon/browserconfig.xml">
                            </head>
                            <body id="rika-body">
                                <div data-role="page">
                                    <div id="cookieMessage">
                                        <div>This site uses cookies. By continuing to browse the site you agree to our use of cookies. To learn more read our <a href=/web/privacy target=_blank> cookie policy </a>.</div>
                                        <button id="acceptCookies" data-icon="fa-check" data-iconpos="notext" data-mini="true" data-inline="true"></button>
                                    </div>
                                    <div id="rika-header" data-role="header" data-position="fixed" data-tap-toggle="false">
                                        <div class="rika-header-wrapper">
                                            <div id="deploymentStage"></div>
                                            <a href="/web/" style="float:left"><img src="/images/RIKA-flame.svg" style="height:2.8em; padding: 0.1em;"></a>
                                            <div style="height:3em; padding:0px 0px; position: relative" class="ui-title"> <span style="vertical-align: middle; line-height: 3em">RIKA firenet</span></div>
                                        </div>
                                    </div>
                                    <div role="main" class="ui-content">
                                        <p id="warningbox" onclick="$('#warningbox').fadeOut(500)">Invalid password</p>
                                        <form id="login" method="POST" action="/web/login" data-ajax="false">
                                            <h3>Please sign in</h3>
                                            <label for="email">E-mail address</label><input type="text" data-theme="b" name="email" value="" placeholder="email@example.com"><label for="password">Password</label><input type="password" data-theme="b" name="password" value="" placeholder="password"><button type="submit" data-theme="a" data-icon="fa-sign-in" data-iconpos="right">Sign in</button><a href="/web/" data-role="button" data-iconpos="right" data-icon="fa-times" data-theme="a">Cancel</a>
                                            <p>Need an account? Go <a href='/web/signup'> here </a> to sign up.</p>
                                            <p>Forgot your password? Go <a href='/web/recover'> here </a> to reset your password.</p>
                                        </form>
                                        <script src="/assets/login.js"></script>
                                    </div>
                                    <div id="about-footer"><a href="/web/imprint">Imprint</a><a href="/web/terms">Terms and conditions</a><a href="/web/privacy">Privacy policy</a></div>
                                </div>
                            </body>
                        </html>
                        """
                )
        );
  }

  @Test
  void getStovesShouldRetrieveTwoStovesDeclaredInSummaryPageGivenTwoStovesAreMocked() {

    // GIVEN
    final var mainStoveId = new StoveId(111111L);
    final var studioStoveId = new StoveId(222222L);
    final var stoveIds = List.of(mainStoveId, studioStoveId);
    initSummaryPageMock(stoveIds);

    // WHEN
    final var actualStoves = this.rikaFirenetService.getStoves();

    // THEN
    Assertions.assertThat(actualStoves).hasSize(stoveIds.size())
        .containsExactly(mainStoveId, studioStoveId);
  }

  private void initSummaryPageMock(@NonNull final List<StoveId> stoveIds) {

    final var stoveHtmlLinksBuilder = new StringBuilder();
    for (final var stoveId : stoveIds) {
      stoveHtmlLinksBuilder.append("<li>\n")
          .append("<a href=\"/web/stove/").append(stoveId.id()).append("\" data-ajax=\"false\">")
          .append("Stove#").append(stoveId).append("</a>\n")
          .append("<a href=\"/web/edit/").append(stoveId.id()).append("\" data-ajax=\"false\">")
          .append("Stove#").append(stoveId).append(
              "</a>\n")
          .append("</li>\n");
    }

    new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        .when(
            request()
                .withMethod("GET")
                .withPath("/web/summary"),
            Times.once()
        )
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.HTML_UTF_8)
                .withBody(String.format(
                    """
                            <!DOCTYPE html>
                            <html lang="en" dir="ltr">
                                <head>
                                    <title>RIKA firenet</title>
                                    <meta name="viewport" content="width=device-width,initial-scale=1">
                                    <meta name="apple-mobile-web-app-capable" content="yes">
                                    <meta name="apple-mobile-web-app-status-bar-style" content="black">
                                    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Source+Sans+Pro:300,200">
                                    <link rel="stylesheet" href="/assets/rika-firenet.css?version=7">
                                    <script src="/assets/rika-firenet-en.js?version=4"></script>
                                    <link rel="shortcut icon" href="/images/favicon/favicon.ico">
                                    <link rel="icon" sizes="16x16 32x32 64x64" href="/images/favicon/favicon.ico">
                                    <link rel="icon" type="image/png" sizes="196x196" href="/images/favicon/favicon-192.png">
                                    <link rel="icon" type="image/png" sizes="160x160" href="/images/favicon/favicon-160.png">
                                    <link rel="icon" type="image/png" sizes="96x96" href="/images/favicon/favicon-96.png">
                                    <link rel="icon" type="image/png" sizes="64x64" href="/images/favicon/favicon-64.png">
                                    <link rel="icon" type="image/png" sizes="32x32" href="/images/favicon/favicon-32.png">
                                    <link rel="icon" type="image/png" sizes="16x16" href="/images/favicon/favicon-16.png">
                                    <link rel="apple-touch-icon" href="/images/favicon/favicon-57.png">
                                    <link rel="apple-touch-icon" sizes="114x114" href="/images/favicon/favicon-114.png">
                                    <link rel="apple-touch-icon" sizes="72x72" href="/images/favicon/favicon-72.png">
                                    <link rel="apple-touch-icon" sizes="144x144" href="/images/favicon/favicon-144.png">
                                    <link rel="apple-touch-icon" sizes="60x60" href="/images/favicon/favicon-60.png">
                                    <link rel="apple-touch-icon" sizes="120x120" href="/images/favicon/favicon-120.png">
                                    <link rel="apple-touch-icon" sizes="76x76" href="/images/favicon/favicon-76.png">
                                    <link rel="apple-touch-icon" sizes="152x152" href="/images/favicon/favicon-152.png">
                                    <link rel="apple-touch-icon" sizes="180x180" href="/images/favicon/favicon-180.png">
                                    <meta name="msapplication-TileColor" content="#FFFFFF">
                                    <meta name="msapplication-TileImage" content="/images/favicon/favicon-144.png">
                                    <meta name="msapplication-config" content="/images/favicon/browserconfig.xml">
                                </head>
                                <body id="rika-body">
                                    <div data-role="page">
                                        <div id="cookieMessage">
                                            <div>This site uses cookies. By continuing to browse the site you agree to our use of cookies. To learn more read our <a href=/web/privacy target=_blank> cookie policy </a>.</div>
                                            <button id="acceptCookies" data-icon="fa-check" data-iconpos="notext" data-mini="true" data-inline="true"></button>
                                        </div>
                                        <div id="sidePanel" data-role="panel" data-display="overlay" data-position="right" data-position-fixed="true" data-theme="a">
                                            <a id="sidePanelCloseButton" href="#sidePanel"></a>
                                            <div data-role="controlgroup">
                                                <h3>Settings</h3>
                                                <ul id="sidePanelButtons" data-role="listview" data-inset="true" data-theme="a">
                                                    <li> <a href="/web/summary" data-theme="a" data-role="button" data-icon="fa-th-list" data-iconpos="left" class="sidePanelButton">Summary</a></li>
                                                    <li> <a href="/web/add" data-theme="a" data-role="button" data-icon="fa-plus" data-iconpos="left" class="sidePanelButton">Add stove</a></li>
                                                    <li> <a href="/web/profile" data-theme="a" data-role="button" data-icon="fa-user" data-iconpos="left" class="sidePanelButton">Manage account</a></li>
                                                    <li> <a href="/web/password" data-theme="a" data-role="button" data-icon="fa-lock" data-iconpos="left" class="sidePanelButton">Change password</a></li>
                                                    <li><a href="/web/about" data-theme="a" data-role="button" data-icon="fa-question-circle" data-iconpos="left" class="sidePanelButton">Imprint, privacy</a></li>
                                                    <li> <a href="/web/logout" data-theme="a" data-role="button" data-icon="fa-sign-out" data-iconpos="left" class="sidePanelButton">Log out</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div id="rika-header" data-role="header" data-position="fixed" data-tap-toggle="false">
                                            <div class="rika-header-wrapper">
                                                <a href="/web/" style="float:left"><img src="/images/RIKA-flame.svg" style="height:2.8em; padding: 0.1em;"></a><a id="sidePanelButton" href="#sidePanel"></a>
                                                <div class="rika-title"><span style="line-height: 1em">Welcome </span><span style="line-height: 1em" class="hide-medium">to RIKA firenet, </span><span style="line-height: 1em"><br>john.doe@gmail.com</span></div>
                                            </div>
                                        </div>
                                        <div role="main" class="ui-content">
                                            <div data-role="controlgroup">
                                                <h3>You have access to the following stoves</h3>
                                                <ul id="stoveList" data-role="listview" data-inset="true" data-theme="a" data-split-theme="a" data-split-icon="fa-pencil">
                                                    %s
                                                </ul>
                                            </div>
                                        </div>
                                        <div id="gadiv">
                                            <script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                                                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                                                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                                                })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
                                                ga('create', 'UA-71662097-1', 'auto');
                                                ga('send', 'pageview');
                                            </script>
                                        </div>
                                    </div>
                                </body>
                            </html>        
                        """, stoveHtmlLinksBuilder)
                )
        );
  }

  @Test
  void getStatusShouldRetrieveStatusGivenStoveIdExists() throws Exception {
    // GIVEN
    final var mainStoveId = new StoveId(111111L);
    initStoveStatusMock(mainStoveId);

    // WHEN
    final var actualStoveStatus = this.rikaFirenetService.getStatus(mainStoveId);

    // THEN
    Assertions.assertThat(actualStoveStatus).isNotNull();
    Assertions.assertThat(actualStoveStatus.getStoveId()).isEqualTo(mainStoveId.id());
  }

  @Test
  void getStatusShouldRetrieveStatusGivenUserDoesntHavePermissionToSeeThatStove() {
    // GIVEN
    final var invalidStoveId = new StoveId(111111L);
    initStoveNotOwnedStatusMock(invalidStoveId);

    // THEN
    assertThrows(InvalidStoveIdException.class, () -> {
      // WHEN
      this.rikaFirenetService.getStatus(invalidStoveId);
    });
  }

  @Test
  void isAuthenticatedShouldReturnTrueGivenValidCredentials() {
    // GIVEN
    final var validEmail = "valid-test-user@gmail.com";
    final var validPassword = "s3cr3t1234567890!";

    initRikaFirenetWebLoginSuccessMock();

    // WHEN
    final var result = this.rikaFirenetService.isValidCredentials(validEmail, validPassword);

    // THEN
    assertThat(result).isTrue();
  }

  @Test
  void isAuthenticatedShouldReturnFalseGivenInvalidCredentials() {
    // GIVEN
    final var invalidEmail = "invalid-test-user@gmail.com";
    final var invalidPassword = "nopassword";

    initRikaFirenetWebLoginFailureMock();

    // WHEN
    final var result = this.rikaFirenetService.isValidCredentials(invalidEmail, invalidPassword);

    // THEN
    assertThat(result).isFalse();
  }

  private void initStoveStatusMock(final StoveId stoveId) {
    new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        .when(
            request()
                .withMethod("GET")
                .withPath("/api/client/" + stoveId.id() + "/status"),
            Times.once()
        )
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(APPLICATION_JSON)
                .withBody(String.format(
                    """
                            {
                              "name": "Stove name",
                              "stoveID": "%s",
                              "lastSeenMinutes": 0,
                              "lastConfirmedRevision": 1664524372,
                              "controls": {
                                "revision": 1664524372,
                                "onOff": false,
                                "operatingMode": 2,
                                "heatingPower": 100,
                                "targetTemperature": "19",
                                "bakeTemperature": "340",
                                "ecoMode": false,
                                "heatingTimeMon1": "05552200",
                                "heatingTimeMon2": "12302230",
                                "heatingTimeTue1": "05552200",
                                "heatingTimeTue2": "12302230",
                                "heatingTimeWed1": "05552200",
                                "heatingTimeWed2": "12302230",
                                "heatingTimeThu1": "05552200",
                                "heatingTimeThu2": "12302230",
                                "heatingTimeFri1": "05552200",
                                "heatingTimeFri2": "16452000",
                                "heatingTimeSat1": "05552200",
                                "heatingTimeSat2": "16452000",
                                "heatingTimeSun1": "05552200",
                                "heatingTimeSun2": "00000016",
                                "heatingTimesActiveForComfort": false,
                                "setBackTemperature": "13",
                                "convectionFan1Active": false,
                                "convectionFan1Level": 0,
                                "convectionFan1Area": 15,
                                "convectionFan2Active": false,
                                "convectionFan2Level": 0,
                                "convectionFan2Area": 7,
                                "frostProtectionActive": true,
                                "frostProtectionTemperature": "4",
                                "temperatureOffset": "0",
                                "RoomPowerRequest": 3,
                                "debug0": 0,
                                "debug1": 0,
                                "debug2": 0,
                                "debug3": 0,
                                "debug4": 0
                              },
                              "sensors": {
                                "inputRoomTemperature": "18",
                                "inputFlameTemperature": 33,
                                "inputBakeTemperature": "1024",
                                "statusError": 0,
                                "statusSubError": 0,
                                "statusWarning": 0,
                                "statusService": 0,
                                "outputDischargeMotor": 0,
                                "outputDischargeCurrent": 0,
                                "outputIDFan": 0,
                                "outputIDFanTarget": 0,
                                "outputInsertionMotor": 0,
                                "outputInsertionCurrent": 0,
                                "outputAirFlaps": 50,
                                "outputAirFlapsTargetPosition": 50,
                                "outputBurnBackFlapMagnet": true,
                                "outputGridMotor": false,
                                "outputIgnition": false,
                                "inputUpperTemperatureLimiter": true,
                                "inputPressureSwitch": false,
                                "inputPressureSensor": 0,
                                "inputGridContact": true,
                                "inputDoor": true,
                                "inputCover": true,
                                "inputExternalRequest": true,
                                "inputBurnBackFlapSwitch": true,
                                "inputFlueGasFlapSwitch": true,
                                "inputBoardTemperature": "2.9",
                                "inputCurrentStage": 90,
                                "inputTargetStagePID": 90,
                                "inputCurrentStagePID": 90,
                                "statusMainState": 1,
                                "statusSubState": 0,
                                "statusWifiStrength": -64,
                                "parameterEcoModePossible": false,
                                "parameterFabricationNumber": 1,
                                "parameterStoveTypeNumber": 17,
                                "parameterLanguageNumber": 2,
                                "parameterVersionMainBoard": 227,
                                "parameterVersionTFT": 227,
                                "parameterVersionWiFi": 111,
                                "parameterVersionMainBoardBootLoader": 160,
                                "parameterVersionTFTBootLoader": 150,
                                "parameterVersionWiFiBootLoader": 101,
                                "parameterVersionMainBoardSub": 44501,
                                "parameterVersionTFTSub": 42901,
                                "parameterVersionWiFiSub": 12701,
                                "parameterRuntimePellets": 2320,
                                "parameterRuntimeLogs": 539,
                                "parameterFeedRateTotal": 3176,
                                "parameterFeedRateService": 616,
                                "parameterServiceCountdownKg": 79,
                                "parameterServiceCountdownTime": 158,
                                "parameterIgnitionCount": 619,
                                "parameterOnOffCycleCount": 32,
                                "parameterFlameSensorOffset": 14,
                                "parameterPressureSensorOffset": 0,
                                "parameterErrorCount0": 42,
                                "parameterErrorCount1": 7,
                                "parameterErrorCount2": 0,
                                "parameterErrorCount3": 18,
                                "parameterErrorCount4": 8,
                                "parameterErrorCount5": 1,
                                "parameterErrorCount6": 0,
                                "parameterErrorCount7": 0,
                                "parameterErrorCount8": 0,
                                "parameterErrorCount9": 2,
                                "parameterErrorCount10": 2,
                                "parameterErrorCount11": 0,
                                "parameterErrorCount12": 2,
                                "parameterErrorCount13": 0,
                                "parameterErrorCount14": 0,
                                "parameterErrorCount15": 0,
                                "parameterErrorCount16": 0,
                                "parameterErrorCount17": 0,
                                "parameterErrorCount18": 0,
                                "parameterErrorCount19": 0,
                                "statusHeatingTimesNotProgrammed": false,
                                "statusFrostStarted": false,
                                "parameterSpiralMotorsTuning": 0,
                                "parameterIDFanTuning": 0,
                                "parameterCleanIntervalBig": 240,
                                "parameterKgTillCleaning": 700,
                                "parameterDebug0": 0,
                                "parameterDebug1": 0,
                                "parameterDebug2": 0,
                                "parameterDebug3": 0,
                                "parameterDebug4": 0
                              },
                              "stoveType": "PARO",
                              "stoveFeatures": {
                                "multiAir1": true,
                                "multiAir2": true,
                                "insertionMotor": true,
                                "airFlaps": true,
                                "logRuntime": true,
                                "bakeMode": false
                              },
                              "oem": "RIKA"
                            }                   
                        """, stoveId.id())
                )
        );
  }

  private void initStoveNotOwnedStatusMock(final StoveId stoveId) {
    new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        .when(
            request()
                .withMethod("GET")
                .withPath("/api/client/" + stoveId.id() + "/status"),
            Times.once()
        )
        .respond(
            response()
                .withStatusCode(500)
                .withContentType(APPLICATION_JSON)
                .withBody(
                    String.format(
                        "Stove %s is not registered for user XYZ",
                        stoveId.id()
                    )
                )
        );
  }

  private void initRikaFirenetWebLoginSuccessMock() {
    new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        .when(
            request()
                .withMethod("POST")
                .withPath("/web/login"),
            Times.once()
        )
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(HTML_UTF_8)
                .withBody(
                    // Valid credentials response contains a link to logout <a href="/web/logout"
                    """
                            <!DOCTYPE html>
                            <html lang="en" dir="ltr">
                                <head>
                                    <title>RIKA firenet</title>
                                    <meta name="viewport" content="width=device-width,initial-scale=1">
                                    <meta name="apple-mobile-web-app-capable" content="yes">
                                    <meta name="apple-mobile-web-app-status-bar-style" content="black">
                                    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Source+Sans+Pro:300,200">
                                    <link rel="stylesheet" href="/assets/rika-firenet.css?version=7">
                                    <script src="/assets/rika-firenet-en.js?version=4"></script>
                                    <link rel="shortcut icon" href="/images/favicon/favicon.ico">
                                    <link rel="icon" sizes="16x16 32x32 64x64" href="/images/favicon/favicon.ico">
                                    <link rel="icon" type="image/png" sizes="196x196" href="/images/favicon/favicon-192.png">
                                    <link rel="icon" type="image/png" sizes="160x160" href="/images/favicon/favicon-160.png">
                                    <link rel="icon" type="image/png" sizes="96x96" href="/images/favicon/favicon-96.png">
                                    <link rel="icon" type="image/png" sizes="64x64" href="/images/favicon/favicon-64.png">
                                    <link rel="icon" type="image/png" sizes="32x32" href="/images/favicon/favicon-32.png">
                                    <link rel="icon" type="image/png" sizes="16x16" href="/images/favicon/favicon-16.png">
                                    <link rel="apple-touch-icon" href="/images/favicon/favicon-57.png">
                                    <link rel="apple-touch-icon" sizes="114x114" href="/images/favicon/favicon-114.png">
                                    <link rel="apple-touch-icon" sizes="72x72" href="/images/favicon/favicon-72.png">
                                    <link rel="apple-touch-icon" sizes="144x144" href="/images/favicon/favicon-144.png">
                                    <link rel="apple-touch-icon" sizes="60x60" href="/images/favicon/favicon-60.png">
                                    <link rel="apple-touch-icon" sizes="120x120" href="/images/favicon/favicon-120.png">
                                    <link rel="apple-touch-icon" sizes="76x76" href="/images/favicon/favicon-76.png">
                                    <link rel="apple-touch-icon" sizes="152x152" href="/images/favicon/favicon-152.png">
                                    <link rel="apple-touch-icon" sizes="180x180" href="/images/favicon/favicon-180.png">
                                    <meta name="msapplication-TileColor" content="#FFFFFF">
                                    <meta name="msapplication-TileImage" content="/images/favicon/favicon-144.png">
                                    <meta name="msapplication-config" content="/images/favicon/browserconfig.xml">
                                </head>
                                <body id="rika-body">
                                    <div data-role="page">
                                        <div id="cookieMessage">
                                            <div>This site uses cookies. By continuing to browse the site you agree to our use of cookies. To learn more read our <a href=/web/privacy target=_blank> cookie policy </a>.</div>
                                            <button id="acceptCookies" data-icon="fa-check" data-iconpos="notext" data-mini="true" data-inline="true"></button>
                                        </div>
                                        <div id="sidePanel" data-role="panel" data-display="overlay" data-position="right" data-position-fixed="true" data-theme="a">
                                            <a id="sidePanelCloseButton" href="#sidePanel"></a>
                                            <div data-role="controlgroup">
                                                <h3>Settings</h3>
                                                <ul id="sidePanelButtons" data-role="listview" data-inset="true" data-theme="a">
                                                    <li> <a href="/web/summary" data-theme="a" data-role="button" data-icon="fa-th-list" data-iconpos="left" class="sidePanelButton">Summary</a></li>
                                                    <li> <a href="/web/add" data-theme="a" data-role="button" data-icon="fa-plus" data-iconpos="left" class="sidePanelButton">Add stove</a></li>
                                                    <li> <a href="/web/profile" data-theme="a" data-role="button" data-icon="fa-user" data-iconpos="left" class="sidePanelButton">Manage account</a></li>
                                                    <li> <a href="/web/password" data-theme="a" data-role="button" data-icon="fa-lock" data-iconpos="left" class="sidePanelButton">Change password</a></li>
                                                    <li><a href="/web/about" data-theme="a" data-role="button" data-icon="fa-question-circle" data-iconpos="left" class="sidePanelButton">Imprint, privacy</a></li>
                                                    <li> <a href="/web/logout" data-theme="a" data-role="button" data-icon="fa-sign-out" data-iconpos="left" class="sidePanelButton">Log out</a></li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div id="rika-header" data-role="header" data-position="fixed" data-tap-toggle="false">
                                            <div class="rika-header-wrapper">
                                                <a href="/web/" style="float:left"><img src="/images/RIKA-flame.svg" style="height:2.8em; padding: 0.1em;"></a><a id="sidePanelButton" href="#sidePanel"></a>
                                                <div class="rika-title"><span style="line-height: 1em">Welcome </span><span style="line-height: 1em" class="hide-medium">to RIKA firenet, </span><span style="line-height: 1em"><br>john.doe@gmail.com</span></div>
                                            </div>
                                        </div>
                                        <div role="main" class="ui-content">
                                            <div data-role="controlgroup">
                                                <h3>You have access to the following stoves</h3>
                                                <ul id="stoveList" data-role="listview" data-inset="true" data-theme="a" data-split-theme="a" data-split-icon="fa-pencil">
                                                    <li><a href="/web/stove/12345678" data-ajax="false">Living Room</a><a href="/web/edit/12345678" data-ajax="false"></a></li>
                                                </ul>
                                            </div>
                                        </div>
                                        <div id="gadiv">
                                            <script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                                                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                                                m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                                                })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
                                                ga('create', 'UA-71662097-1', 'auto');
                                                ga('send', 'pageview');
                                            </script>
                                        </div>
                                    </div>
                                </body>
                            </html>               
                        """
                )
        );
  }

  private void initRikaFirenetWebLoginFailureMock() {
    new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
        .when(
            request()
                .withMethod("POST")
                .withPath("/web/login"),
            Times.once()
        )
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(HTML_UTF_8)
                .withBody("""
                    <!DOCTYPE html><html lang="en" dir="ltr"><head><title>RIKA firenet</title><meta name="viewport" content="width=device-width,initial-scale=1"><meta name="apple-mobile-web-app-capable" content="yes"><meta name="apple-mobile-web-app-status-bar-style" content="black"><link rel="stylesheet" href="//fonts.googleapis.com/css?family=Source+Sans+Pro:300,200"><link rel="stylesheet" href="/assets/rika-firenet.css?version=7"><script src="/assets/rika-firenet-en.js?version=4"></script><link rel="shortcut icon" href="/images/favicon/favicon.ico"><link rel="icon" sizes="16x16 32x32 64x64" href="/images/favicon/favicon.ico"><link rel="icon" type="image/png" sizes="196x196" href="/images/favicon/favicon-192.png"><link rel="icon" type="image/png" sizes="160x160" href="/images/favicon/favicon-160.png"><link rel="icon" type="image/png" sizes="96x96" href="/images/favicon/favicon-96.png"><link rel="icon" type="image/png" sizes="64x64" href="/images/favicon/favicon-64.png"><link rel="icon" type="image/png" sizes="32x32" href="/images/favicon/favicon-32.png"><link rel="icon" type="image/png" sizes="16x16" href="/images/favicon/favicon-16.png"><link rel="apple-touch-icon" href="/images/favicon/favicon-57.png"><link rel="apple-touch-icon" sizes="114x114" href="/images/favicon/favicon-114.png"><link rel="apple-touch-icon" sizes="72x72" href="/images/favicon/favicon-72.png"><link rel="apple-touch-icon" sizes="144x144" href="/images/favicon/favicon-144.png"><link rel="apple-touch-icon" sizes="60x60" href="/images/favicon/favicon-60.png"><link rel="apple-touch-icon" sizes="120x120" href="/images/favicon/favicon-120.png"><link rel="apple-touch-icon" sizes="76x76" href="/images/favicon/favicon-76.png"><link rel="apple-touch-icon" sizes="152x152" href="/images/favicon/favicon-152.png"><link rel="apple-touch-icon" sizes="180x180" href="/images/favicon/favicon-180.png"><meta name="msapplication-TileColor" content="#FFFFFF"><meta name="msapplication-TileImage" content="/images/favicon/favicon-144.png"><meta name="msapplication-config" content="/images/favicon/browserconfig.xml"></head><body id="rika-body"><div data-role="page"><div id="cookieMessage"><div>This site uses cookies. By continuing to browse the site you agree to our use of cookies. To learn more read our <a href=/web/privacy target=_blank> cookie policy </a>.</div><button id="acceptCookies" data-icon="fa-check" data-iconpos="notext" data-mini="true" data-inline="true"></button></div><div id="rika-header" data-role="header" data-position="fixed" data-tap-toggle="false"><div class="rika-header-wrapper"><div id="deploymentStage"></div><a href="/web/" style="float:left"><img src="/images/RIKA-flame.svg" style="height:2.8em; padding: 0.1em;"></a><div style="height:3em; padding:0px 0px; position: relative" class="ui-title"> <span style="vertical-align: middle; line-height: 3em">RIKA firenet</span></div></div></div><div role="main" class="ui-content"><form id="login" method="POST" action="/web/login" data-ajax="false"><h3>Please sign in</h3><label for="email">E-mail address</label><input type="text" data-theme="b" name="email" value="" placeholder="email@example.com"><label for="password">Password</label><input type="password" data-theme="b" name="password" value="" placeholder="password"><button type="submit" data-theme="a" data-icon="fa-sign-in" data-iconpos="right">Sign in</button><a href="/web/" data-role="button" data-iconpos="right" data-icon="fa-times" data-theme="a">Cancel</a><p>Need an account? Go <a href='/web/signup'> here </a> to sign up.</p><p>Forgot your password? Go <a href='/web/recover'> here </a> to reset your password.</p></form><script src="/assets/login.js"></script></div><div id="about-footer"><a href="/web/imprint">Imprint</a><a href="/web/terms">Terms and conditions</a><a href="/web/privacy">Privacy policy</a></div></div></body></html>
                                                        """)
        );

  }
}
