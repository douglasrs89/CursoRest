package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.junit.Test;

public class EnvioDadosTeste {

    @Test
    public void deveEnviarValorViaQuery() {
        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/v2/users?format=json")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void deveEnviarValorViaQueryViaParam() {
        given()
            .log().all()
            .queryParam("format", "xml")
            .queryParam("outra", "coisa")
        .when()
            .get("https://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML)
            .contentType(containsString("utf-8"))
        ;
    }

    @Test
    public void deveEnviarValorViaHeader() {
        given()
            .log().all()
            .accept(ContentType.XML)
        .when()
            .get("https://restapi.wcaquino.me/v2/users")
        .then()
            .log().all()
            .statusCode(200)
            .contentType(ContentType.XML)
        ;
    }
}