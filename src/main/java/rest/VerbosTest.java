package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VerbosTest {

    @Test
    public void deveSalvarUsuario() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Douglas Rosa\", \"age\": 30 }")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Douglas Rosa"))
            .body("age", is(30))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoMap() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Usuario via map");
        params.put("age", 25);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(params)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Usuario via map"))
            .body("age", is(25))
        ;
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto() {
        User user = new User("Usuario via objeto", 35);

        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", is(notNullValue()))
            .body("name", is("Usuario via objeto"))
            .body("age", is(35))
        ;
    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuario() {
        User user = new User("Usuario deserializado", 35);

        User usuarioInserido =
        given()
            .log().all()
            .contentType(ContentType.JSON)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class)
        ;

        System.out.println(usuarioInserido);
        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(35));

    }

    @Test
    public void naoDeveSalvarUsuarioSemNome() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"age\": 30 }")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(400)
            .body("id", is(nullValue()))
            .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveSalvarUsuarioViaXML() {
        given()
            .log().all()
//            .contentType("application/xml")
            .contentType(ContentType.XML)
            .body("<user><name>Douglas Rosa</name><age>30</age></user>")
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", is(notNullValue()))
            .body("user.name", is("Douglas Rosa"))
            .body("user.age", is("30"))
        ;
    }

    @Test
    public void deveSalvarUsuarioViaXMLUsandoObjeto() {
        User user = new User("Usuario XML", 40);

        given()
            .log().all()
//            .contentType("application/xml")
            .contentType(ContentType.XML)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", is(notNullValue()))
            .body("user.name", is("Usuario XML"))
            .body("user.age", is("40"))
        ;
    }

    @Test
    public void deveDeserializarXMLAoSalvarUsuario() {
        User user = new User("Usuario XML", 40);

        User usuarioXML =
        given()
            .log().all()
            .contentType(ContentType.XML)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class)
        ;

        Assert.assertThat(usuarioXML.getId(), notNullValue());
        Assert.assertEquals("Usuario XML", usuarioXML.getName());
        Assert.assertThat(usuarioXML.getAge(), is(40));
        Assert.assertThat(usuarioXML.getSalary(), is(nullValue()));

    }

    @Test
    public void devoAlterarUsuario() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Douglas Rosa 2\", \"age\": 60 }")
        .when()
            .put("https://restapi.wcaquino.me/users/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Douglas Rosa 2"))
            .body("age", is(60))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void devoCustomizarURL() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Douglas Rosa 2\", \"age\": 60 }")
        .when()
            .put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Douglas Rosa 2"))
            .body("age", is(60))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void devoCustomizarURLParte2() {
        given()
            .log().all()
            .contentType("application/json")
            .body("{ \"name\": \"Douglas Rosa 2\", \"age\": 60 }")
            .pathParam("entidade", "users")
            .pathParam("userId", 1)
        .when()
            .put("https://restapi.wcaquino.me/{entidade}/{userId}")
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(1))
            .body("name", is("Douglas Rosa 2"))
            .body("age", is(60))
            .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void deveRemoverUsuario() {
        given()
                .log().all()
                .when()
                .delete("https://restapi.wcaquino.me/users/1")
                .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test
    public void naoDeveRemoverUsuarioInexistente() {
        given()
            .log().all()
        .when()
            .delete("https://restapi.wcaquino.me/users/1000")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", is("Registro inexistente"))
        ;
    }
}



