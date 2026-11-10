package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // in-memory database, see application-test.properties
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksControllerIntegrationTests {

    @Autowired
    private Environment environment;

    private final HttpClient client = HttpClient.newHttpClient();

    private HttpResponse<String> send(String method, String path, String body) throws Exception {
        String port = environment.getProperty("local.server.port");
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json");
        builder.method(method, body == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(body));
        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void fullCrudOnBooks() throws Exception {
        String newBook = """
                {
                  "title": "Noapte buna, copii!",
                  "authors": [ { "name": "Radu Pavel Gheo" } ],
                  "children": [
                    { "type": "section", "title": "Capitolul 1", "children": [
                        { "type": "paragraph", "text": "Moto capitol" },
                        { "type": "imageProxy", "url": "ImageOne" },
                        { "type": "table", "title": "Table 1" }
                    ]},
                    { "type": "tableOfContents" },
                    { "type": "image", "url": "ImageTwo" }
                  ]
                }
                """;

        // CREATE
        HttpResponse<String> created = send("POST", "/books", newBook);
        assertEquals(201, created.statusCode(), created.body());
        String createdJson = created.body();
        assertTrue(createdJson.contains("\"title\":\"Noapte buna, copii!\""), createdJson);
        assertTrue(createdJson.contains("\"name\":\"Radu Pavel Gheo\""), createdJson);
        assertTrue(createdJson.contains("\"type\":\"paragraph\""), createdJson);
        assertTrue(createdJson.contains("\"text\":\"Moto capitol\""), createdJson);
        assertTrue(createdJson.contains("\"type\":\"imageProxy\""), createdJson);
        assertTrue(createdJson.contains("\"type\":\"table\""), createdJson);
        assertFalse(createdJson.contains("realImage"), createdJson);
        String location = created.headers().firstValue("Location").orElseThrow();
        int bookId = Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));

        // READ ALL
        HttpResponse<String> all = send("GET", "/books", null);
        assertEquals(200, all.statusCode());
        assertTrue(all.body().contains("Noapte buna, copii!"), all.body());

        // READ ONE (fresh from the database)
        HttpResponse<String> one = send("GET", "/books/" + bookId, null);
        assertEquals(200, one.statusCode(), one.body());
        assertTrue(one.body().contains("\"text\":\"Moto capitol\""), one.body());
        assertTrue(one.body().contains("\"url\":\"ImageTwo\""), one.body());
        assertFalse(one.body().contains("hibernateLazyInitializer"), one.body());

        // find the persisted author's id, then UPDATE reusing the existing author
        Matcher authorId = Pattern.compile("\"authors\":\\[\\{[^}]*?\"id\":(\\d+)").matcher(one.body());
        assertTrue(authorId.find(), one.body());
        String updatedBook = """
                {
                  "title": "Noapte buna, copii! (ed. 2)",
                  "authors": [ { "id": %s }, { "name": "Co Author" } ],
                  "children": [ { "type": "paragraph", "text": "Only paragraph" } ]
                }
                """.formatted(authorId.group(1));
        HttpResponse<String> updated = send("PUT", "/books/" + bookId, updatedBook);
        assertEquals(200, updated.statusCode(), updated.body());
        HttpResponse<String> afterUpdate = send("GET", "/books/" + bookId, null);
        assertTrue(afterUpdate.body().contains("(ed. 2)"), afterUpdate.body());
        assertTrue(afterUpdate.body().contains("\"name\":\"Radu Pavel Gheo\""), afterUpdate.body());
        assertTrue(afterUpdate.body().contains("\"name\":\"Co Author\""), afterUpdate.body());
        assertTrue(afterUpdate.body().contains("Only paragraph"), afterUpdate.body());
        assertFalse(afterUpdate.body().contains("Capitolul 1"), afterUpdate.body());

        // unknown author id -> 400
        HttpResponse<String> badAuthor = send("PUT", "/books/" + bookId,
                "{\"title\":\"x\",\"authors\":[{\"id\":999999}]}");
        assertEquals(400, badAuthor.statusCode(), badAuthor.body());

        // DELETE
        assertEquals(204, send("DELETE", "/books/" + bookId, null).statusCode());
        assertEquals(404, send("GET", "/books/" + bookId, null).statusCode());
        assertEquals(404, send("DELETE", "/books/" + bookId, null).statusCode());
        assertEquals(404, send("PUT", "/books/" + bookId, updatedBook).statusCode());
    }

    @Test
    void h2ConsoleIsAvailable() throws Exception {
        HttpResponse<String> console = send("GET", "/h2-console/", null);
        assertEquals(200, console.statusCode());
    }
}
