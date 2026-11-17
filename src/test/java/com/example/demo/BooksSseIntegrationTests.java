package com.example.demo;

import com.example.demo.observer.AllBooksSubject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // in-memory database, see application-test.properties
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BooksSseIntegrationTests {

    @Autowired
    private Environment environment;

    @Autowired
    private AllBooksSubject allBooksSubject;

    private final HttpClient client = HttpClient.newHttpClient();

    private URI uri(String path) {
        return URI.create("http://localhost:" + environment.getProperty("local.server.port") + path);
    }

    @Test
    void newBookIsPushedToSseObservers() throws Exception {
        int observersBefore = allBooksSubject.getObserversCount();

        // subscribe to the server-sent events stream (as index.html does); asynchronously, because
        // the response headers are only flushed together with the first event
        CompletableFuture<HttpResponse<InputStream>> sseResponse = client.sendAsync(
                HttpRequest.newBuilder(uri("/books-sse")).header("Accept", "text/event-stream").GET().build(),
                HttpResponse.BodyHandlers.ofInputStream());

        // wait until the server has attached the SseObserver to the subject
        long deadline = System.currentTimeMillis() + 10_000;
        while (allBooksSubject.getObserversCount() <= observersBefore) {
            assertTrue(System.currentTimeMillis() < deadline, "SseObserver was not attached");
            Thread.sleep(20);
        }

        CompletableFuture<String> firstEvent = sseResponse.thenApply(sse -> {
            assertEquals(200, sse.statusCode());
            assertTrue(sse.headers().firstValue("Content-Type").orElse("").startsWith("text/event-stream"));
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(sse.body(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data:")) {
                        return line.substring("data:".length());
                    }
                }
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // change the state of the subject through POST /books
        HttpResponse<String> created = client.send(
                HttpRequest.newBuilder(uri("/books"))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(10))
                        .POST(HttpRequest.BodyPublishers.ofString(
                                "{\"title\":\"SSE Book\",\"authors\":[{\"name\":\"SSE Author\"}]}"))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(201, created.statusCode(), created.body());
        String location = created.headers().firstValue("Location").orElseThrow();
        String bookId = location.substring(location.lastIndexOf('/') + 1);

        String data = firstEvent.get(10, TimeUnit.SECONDS);
        assertNotNull(data);
        assertTrue(data.contains("\"id\":" + bookId), data);
        assertTrue(data.contains("\"title\":\"SSE Book\""), data);
        assertTrue(data.contains("\"name\":\"SSE Author\""), data);
    }

    @Test
    void indexPageIsServed() throws Exception {
        HttpResponse<String> index = client.send(HttpRequest.newBuilder(uri("/index.html")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, index.statusCode());
        assertTrue(index.body().contains("EventSource"), index.body());
        assertTrue(index.body().contains("/books-sse"), index.body());
    }
}
