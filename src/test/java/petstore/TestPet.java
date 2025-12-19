// 1 - Pacote (Grupo de Classes Reunidas)
package petstore;
import java.io.IOException;
import java.nio.file.Files; // Cuida de operações com pastas e arquivos
import java.nio.file.Paths; // Cuida de direcionar pastas e arquivos

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;

import static io.restassured.RestAssured.given;
 
// 2 - Imports / Framework e Bibliotecas
 
// 3 - Classe
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ordenar os testes por ordem numérica
 
public class TestPet {
 // 3.1 - Atributos
    static String uri = "https://petstore.swagger.io/v2/pet";
    static String ct = "application/json"; // content-type
    static int petId = 413661;      // id do animal
    static String petName = "Flor";
    static String categoryName = "dog";
    static String tag0Name = "vacinado";

    public TestPet() {
    }
       
 
    // 3.2 - Funções de Apoio
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    } // Final lerJson
 
    
    // 3.3 - Métodos e Funções em sim
 
    // Incluir - Create - Post
    // visibilidade Tipo
    // public       String = Texto
    // private      int    = número inteiro
    
    @Test @Order(1)
    public void postPet() throws IOException {
        String jsonbody = lerJson("data/pet.json");
        // Grande linha de comando para realizar
        // um metodo de uma API
        given()                                                 // Dado
            .contentType(ct)                                   // Tipo de conteúdo / formato transmissão
            .log().all()                                       // Exibir o envio (requisição)
            .body(jsonbody)                                   // Conteúdo mensagem
        .when()                                              // Quando
            .post(uri)                                      // Método Post (endpoint)
        .then()                                            // Então
            .log().all()                                  // Exibir resposta
            .statusCode(200)          // Validar status code (comunicação)
            .body("name", is("Flor"));       // Validar conteúdo da resposta
           
        
    } // Final postPet
    @Test @Order(2)
    public void getPet() {
         given()                                          // Dado
            .contentType(ct)                            // Tipo de conteúdo
            .log().all()
            // Passar o token de autenticação no header
            .header("", "api_key" + TestUser.loginUser())
        .when()                                           // Quando
            .get(uri + "/" + petId)                    // Consulta URL/pet/petid
           
        .then()                                       // Então
            .log().all()
            .statusCode(200)
            .body("name", is(petName))
            .body("id", is(petId))
            .body("category.name", is(categoryName))
            .body("tags[0].name", is(tag0Name));
    } // final getPet

    @Test @Order(3)
    public void PutPet() throws IOException {
        String jsonbody = lerJson("data/petPut.json");

        given()
            .contentType(ct)
            .log().all()
            //.header("", "api_key" + TestUser.loginUser())
            .body(jsonbody)
        .when()
            .put(uri)
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is(petName))
            .body("id", is(petId))
            .body("category.name", is(categoryName))
            .body("tags[0].name", is(tag0Name));

    } // final PutPet

    @Test @Order(4)
    public void deletePet() {
        given()
            .contentType(ct)
            .log().all()
            
        .when()
            .delete(uri + "/" + petId)
        .then()
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(petId)));

    } // final deletePet

    // DDT - Data Driven Testing
    // Teste com Json (body) parametrizado

    @ParameterizedTest @Order(5)
    @CsvFileSource(resources = "/pets.csv", numLinesToSkip = 1, delimiter = ',')
    public void PostPetDDT(
        int petId,
        String petName,
        int catId,
        String catName,
        String status1,
        String status2 // para um futuro uso Put
    ) // Fim dos campos contidos no csv

    { // inicio do código do postPetDDt
        // Criar a classe pet para receber os dados (funciona como um template)
        Pet pet = new Pet(); // instanciar a classe Pet a partir da classe pet
        Pet.Category category = pet.new Category();
        Pet.Tag[] tags = new Pet.Tag[2]; // instancia a subclasse tag com 2 espaços        
        pet.id = petId;
        pet.category = category;
        pet.category.id = catId;
        pet.category.name = catName;
        pet.name = petName;
        pet.tags = tags;
        tags[0] = pet.new Tag();
        tags[1] = pet.new Tag();
        pet.tags[0].id = 1;
        pet.tags[0].name = "vacinado";
        pet.tags[1].id = 2;
        pet.tags[1].name = "vermifugado";
        pet.status = status1;

        // Criar um Json a partir do objeto pet preenchido com os dados do csv
        Gson gson = new Gson();
        String jsonbody = gson.toJson(pet);        

        
        given()                                                 // Dado
            .contentType(ct)                                   // Tipo de conteúdo / formato transmissão
            .log().all()                                       // Exibir o envio (requisição)
            .body(jsonbody)                                   // Conteúdo mensagem
        .when()                                              // Quando
            .post(uri)                                      // Método Post (endpoint)
        .then()                                            // Então
            .log().all()                                  // Exibir resposta
            .statusCode(200) 
            .body("id", is(petId))                            // Validar status code (comunicação)
            .body("name", is(petName))                  // Validar conteúdo da resposta
            .body("category.id", is(catId))
            .body("category.name", is(catName))
            .body("status", is(status1))

    
        ;

        
            


    } // Final do PostPetDDT


} // Final Classe Pet
 