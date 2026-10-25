package com.inventory.ui.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.ui.model.Category;
import com.inventory.ui.model.CreateSaleRequest;
import com.inventory.ui.model.DashboardStats;
import com.inventory.ui.model.Product;
import com.inventory.ui.model.Sale;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class InventoryApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public InventoryApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    // Dashboard
    public DashboardStats getDashboardStats() throws IOException, InterruptedException {
        String json = sendGet(BASE_URL + "/dashboard");
        return objectMapper.readValue(json, DashboardStats.class);
    }

    // Categories
    public List<Category> getCategories() throws IOException, InterruptedException {
        String json = sendGet(BASE_URL + "/categories");
        return objectMapper.readValue(json, new TypeReference<List<Category>>() {});
    }

    public Category createCategory(Category category) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(category);
        String json = sendPost(BASE_URL + "/categories", requestBody);
        return objectMapper.readValue(json, Category.class);
    }

    public Category updateCategory(int id, Category category) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(category);
        String json = sendPut(BASE_URL + "/categories/" + id, requestBody);
        return objectMapper.readValue(json, Category.class);
    }

    public void deleteCategory(int id) throws IOException, InterruptedException {
        sendDelete(BASE_URL + "/categories/" + id);
    }

    // Products
    public List<Product> getProducts() throws IOException, InterruptedException {
        String json = sendGet(BASE_URL + "/products");
        return objectMapper.readValue(json, new TypeReference<List<Product>>() {});
    }

    public List<Product> getLowStockProducts() throws IOException, InterruptedException {
        String json = sendGet(BASE_URL + "/products/low-stock");
        return objectMapper.readValue(json, new TypeReference<List<Product>>() {});
    }

    public Product createProduct(Product product) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(product);
        String json = sendPost(BASE_URL + "/products", requestBody);
        return objectMapper.readValue(json, Product.class);
    }

    public Product updateProduct(int id, Product product) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(product);
        String json = sendPut(BASE_URL + "/products/" + id, requestBody);
        return objectMapper.readValue(json, Product.class);
    }

    public void deleteProduct(int id) throws IOException, InterruptedException {
        sendDelete(BASE_URL + "/products/" + id);
    }

    // Sales
    public List<Sale> getSales() throws IOException, InterruptedException {
        String json = sendGet(BASE_URL + "/sales");
        return objectMapper.readValue(json, new TypeReference<List<Sale>>() {});
    }

    public Sale createSale(CreateSaleRequest request) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(request);
        String json = sendPost(BASE_URL + "/sales", requestBody);
        return objectMapper.readValue(json, Sale.class);
    }

    // HTTP Helper methods
    private String sendGet(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        checkError(response);
        return response.body();
    }

    private String sendPost(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        checkError(response);
        return response.body();
    }

    private String sendPut(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        checkError(response);
        return response.body();
    }

    private void sendDelete(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .DELETE()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        checkError(response);
    }

    private void checkError(HttpResponse<String> response) throws IOException {
        int code = response.statusCode();
        if (code >= 400) {
            String errorMsg = "HTTP error " + code;
            try {
                JsonNode root = objectMapper.readTree(response.body());
                if (root.has("message")) {
                    errorMsg = root.get("message").asText();
                }
            } catch (Exception ignored) {
            }
            throw new IOException(errorMsg);
        }
    }
}
