package rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class FileTest {

    @Test
    public void deveObrigarEnvioArquivo() {
        given()
            .log().all()
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .statusCode(404)  //deveria ser 400
            .body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    public void deveFazerUploadArquivo() {
        given()
            .log().all()
            .multiPart("arquivo", new File("src/main/resources/users.pdf"))
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("users.pdf"))
        ;
    }

    @Test
    public void naoDeveFazerUploadArquivoGrande() {
        given()
            .log().all()
            .multiPart("arquivo", new File("src/main/resources/fileplus1mb.pdf"))
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .time(lessThan(6000L))
            .statusCode(413)
        ;
    }

    @Test
    public void deveBaixarArquivo() throws IOException {

        byte[] image = given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/download")
        .then()
//            .log().all()
            .statusCode(200)
            .extract().asByteArray()
        ;

        File imagem = new File("src/main/resources/file.jpg");
        OutputStream outputStream = new FileOutputStream(imagem);
        outputStream.write(image);
        outputStream.close();

        Assert.assertThat(imagem.length(), lessThan(100000L));
    }
}
