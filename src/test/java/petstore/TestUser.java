package petstore;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class TestUser {
    static String uri = "https://petstore.swagger.io/v2/user";
    static String ct = "application/json"; // content-type
    static String token;                  // token de autenticação

    @Test
    public static String loginUser() {
        String username = "calves";
        String password = "123456";

        String resultadoEsperado = "logged in user session:";

        Response resposta = (Response) given()        // Dado
            .contentType(ct)                        // Tipo de conteúdo
            .log().all()

        .when()                                     // Quando
            .get(uri + "/login?username=" + username + "&password=" + password) // Endpoint
        .then()                                     // Então
            .log().all()
            .statusCode(200)                       // Valida o status code
            .body("message", containsString(resultadoEsperado)) // Valida parte da mensagem
            .extract();                            // Extrai a resposta

            // estração do token de autenticação
            token = resposta.jsonPath().getString("message").substring(23);
            System.out.println("Conteudo do Token/API Key" + token);
            return token;
        
    }

            
} // Final loginUser




