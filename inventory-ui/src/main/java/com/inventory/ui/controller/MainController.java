package com.inventory.ui.controller;

import com.inventory.ui.model.Category;
import com.inventory.ui.model.CreateSaleItemRequest;
import com.inventory.ui.model.CreateSaleRequest;
import com.inventory.ui.model.DashboardStats;
import com.inventory.ui.model.Product;
import com.inventory.ui.model.Sale;
import com.inventory.ui.model.SaleItem;
import com.inventory.ui.service.InventoryApiClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final InventoryApiClient apiClient = new InventoryApiClient();

    // Nav Buttons
    @FXML private Button navDashboardBtn;
    @FXML private Button navProductsBtn;
    @FXML private Button navCategoriesBtn;
    @FXML private Button navSalesBtn;
    @FXML private Button navLowStockBtn;

    // View Containers
    @FXML private VBox dashboardView;
    @FXML private VBox productsView;
    @FXML private VBox categoriesView;
    @FXML private VBox salesView;
    @FXML private VBox lowStockView;

    // Dashboard Elements
    @FXML private Label cardTotalProducts;
    @FXML private Label cardTotalCategories;
    @FXML private Label cardTotalSales;
    @FXML private Label cardTotalRevenue;
    @FXML private Label cardLowStock;
    @FXML private TableView<Product> dashboardLowStockTable;
    @FXML private TableColumn<Product, Integer> dashColId;
    @FXML private TableColumn<Product, String> dashColName;
    @FXML private TableColumn<Product, String> dashColSku;
    @FXML private TableColumn<Product, String> dashColCategory;
    @FXML private TableColumn<Product, BigDecimal> dashColPrice;
    @FXML private TableColumn<Product, Integer> dashColQuantity;
    @FXML private TableColumn<Product, Integer> dashColThreshold;

    // Products View Elements
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> prodColId;
    @FXML private TableColumn<Product, String> prodColName;
    @FXML private TableColumn<Product, String> prodColSku;
    @FXML private TableColumn<Product, String> prodColCategory;
    @FXML private TableColumn<Product, BigDecimal> prodColPrice;
    @FXML private TableColumn<Product, Integer> prodColQuantity;
    @FXML private TableColumn<Product, Integer> prodColThreshold;

    // Categories View Elements
    @FXML private TableView<Category> categoriesTable;
    @FXML private TableColumn<Category, Integer> catColId;
    @FXML private TableColumn<Category, String> catColName;
    @FXML private TableColumn<Category, String> catColDescription;

    // Sales View Elements
    @FXML private ComboBox<Product> saleProductCombo;
    @FXML private TextField saleQtyInput;
    @FXML private Label cartTotalLabel;
    @FXML private TableView<SaleItem> saleCartTable;
    @FXML private TableColumn<SaleItem, String> cartColProduct;
    @FXML private TableColumn<SaleItem, BigDecimal> cartColPrice;
    @FXML private TableColumn<SaleItem, Integer> cartColQty;
    @FXML private TableColumn<SaleItem, BigDecimal> cartColSubtotal;
    @FXML private TableView<Sale> salesHistoryTable;
    @FXML private TableColumn<Sale, Integer> saleColId;
    @FXML private TableColumn<Sale, String> saleColNumber;
    @FXML private TableColumn<Sale, BigDecimal> saleColTotal;
    @FXML private TableColumn<Sale, String> saleColDate;

    // Low Stock View Elements
    @FXML private TableView<Product> lowStockTable;
    @FXML private TableColumn<Product, Integer> lowColId;
    @FXML private TableColumn<Product, String> lowColName;
    @FXML private TableColumn<Product, String> lowColSku;
    @FXML private TableColumn<Product, String> lowColCategory;
    @FXML private TableColumn<Product, BigDecimal> lowColPrice;
    @FXML private TableColumn<Product, Integer> lowColQuantity;
    @FXML private TableColumn<Product, Integer> lowColThreshold;

    private final ObservableList<SaleItem> cartItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        showDashboardView();
    }

    private void setupTableColumns() {
        // Dashboard table
        dashColId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        dashColName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        dashColSku.setCellValueFactory(new PropertyValueFactory<Product, String>("sku"));
        dashColCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
        dashColPrice.setCellValueFactory(new PropertyValueFactory<Product, BigDecimal>("price"));
        dashColQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        dashColThreshold.setCellValueFactory(new PropertyValueFactory<Product, Integer>("lowStockThreshold"));

        // Products table
        prodColId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        prodColName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        prodColSku.setCellValueFactory(new PropertyValueFactory<Product, String>("sku"));
        prodColCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
        prodColPrice.setCellValueFactory(new PropertyValueFactory<Product, BigDecimal>("price"));
        prodColQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        prodColThreshold.setCellValueFactory(new PropertyValueFactory<Product, Integer>("lowStockThreshold"));

        // Categories table
        catColId.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        catColName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        catColDescription.setCellValueFactory(new PropertyValueFactory<Category, String>("description"));

        // Sales cart table
        cartColProduct.setCellValueFactory(new PropertyValueFactory<SaleItem, String>("productName"));
        cartColPrice.setCellValueFactory(new PropertyValueFactory<SaleItem, BigDecimal>("unitPrice"));
        cartColQty.setCellValueFactory(new PropertyValueFactory<SaleItem, Integer>("quantity"));
        cartColSubtotal.setCellValueFactory(new PropertyValueFactory<SaleItem, BigDecimal>("subtotal"));
        saleCartTable.setItems(cartItems);

        // Sales history table
        saleColId.setCellValueFactory(new PropertyValueFactory<Sale, Integer>("id"));
        saleColNumber.setCellValueFactory(new PropertyValueFactory<Sale, String>("saleNumber"));
        saleColTotal.setCellValueFactory(new PropertyValueFactory<Sale, BigDecimal>("totalAmount"));
        saleColDate.setCellValueFactory(new PropertyValueFactory<Sale, String>("saleDate"));

        // Low stock table
        lowColId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        lowColName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        lowColSku.setCellValueFactory(new PropertyValueFactory<Product, String>("sku"));
        lowColCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("categoryName"));
        lowColPrice.setCellValueFactory(new PropertyValueFactory<Product, BigDecimal>("price"));
        lowColQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        lowColThreshold.setCellValueFactory(new PropertyValueFactory<Product, Integer>("lowStockThreshold"));
    }

    // Navigation handlers
    @FXML
    public void showDashboardView() {
        switchView(dashboardView, navDashboardBtn);
        loadDashboardData();
    }

    @FXML
    public void showProductsView() {
        switchView(productsView, navProductsBtn);
        loadProductsData();
    }

    @FXML
    public void showCategoriesView() {
        switchView(categoriesView, navCategoriesBtn);
        loadCategoriesData();
    }

    @FXML
    public void showSalesView() {
        switchView(salesView, navSalesBtn);
        loadSalesData();
    }

    @FXML
    public void showLowStockView() {
        switchView(lowStockView, navLowStockBtn);
        loadLowStockData();
    }

    private void switchView(VBox targetView, Button targetBtn) {
        dashboardView.setVisible(false);
        productsView.setVisible(false);
        categoriesView.setVisible(false);
        salesView.setVisible(false);
        lowStockView.setVisible(false);

        navDashboardBtn.getStyleClass().remove("active");
        navProductsBtn.getStyleClass().remove("active");
        navCategoriesBtn.getStyleClass().remove("active");
        navSalesBtn.getStyleClass().remove("active");
        navLowStockBtn.getStyleClass().remove("active");

        targetView.setVisible(true);
        if (!targetBtn.getStyleClass().contains("active")) {
            targetBtn.getStyleClass().add("active");
        }
    }

    // Dashboard Logic
    @FXML
    public void loadDashboardData() {
        try {
            DashboardStats stats = apiClient.getDashboardStats();
            cardTotalProducts.setText(String.valueOf(stats.getTotalProducts()));
            cardTotalCategories.setText(String.valueOf(stats.getTotalCategories()));
            cardTotalSales.setText(String.valueOf(stats.getTotalSalesCount()));
            cardTotalRevenue.setText("$" + stats.getTotalRevenue().toString());
            cardLowStock.setText(String.valueOf(stats.getLowStockCount()));

            List<Product> lowStockList = apiClient.getLowStockProducts();
            dashboardLowStockTable.setItems(FXCollections.observableArrayList(lowStockList));
        } catch (Exception e) {
            showError("Connection Error", "Failed to load dashboard: " + e.getMessage());
        }
    }

    // Products Logic
    @FXML
    public void loadProductsData() {
        try {
            List<Product> products = apiClient.getProducts();
            productsTable.setItems(FXCollections.observableArrayList(products));
        } catch (Exception e) {
            showError("Load Error", "Failed to load products: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddProduct() {
        showProductDialog(null);
    }

    @FXML
    public void handleEditProduct() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("No Selection", "Please select a product from the table to edit.");
            return;
        }
        showProductDialog(selected);
    }

    @FXML
    public void handleDeleteProduct() {
        Product selected = productsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("No Selection", "Please select a product from the table to delete.");
            return;
        }
        if (showConfirm("Confirm Delete", "Are you sure you want to delete product '" + selected.getName() + "'?")) {
            try {
                apiClient.deleteProduct(selected.getId());
                showInfo("Deleted", "Product deleted successfully.");
                loadProductsData();
            } catch (Exception e) {
                showError("Delete Error", e.getMessage());
            }
        }
    }

    private void showProductDialog(Product existing) {
        Dialog<Product> dialog = new Dialog<Product>();
        dialog.setTitle(existing == null ? "Add New Product" : "Edit Product");
        dialog.setHeaderText(existing == null ? "Enter product details" : "Update product details");

        ButtonType saveButtonType = new ButtonType(existing == null ? "Create" : "Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");
        TextField skuField = new TextField();
        skuField.setPromptText("SKU (e.g. SKU-1001)");
        ComboBox<Category> categoryCombo = new ComboBox<Category>();
        TextField priceField = new TextField();
        priceField.setPromptText("Price (e.g. 19.99)");
        TextField qtyField = new TextField();
        qtyField.setPromptText("Initial Quantity");
        TextField thresholdField = new TextField();
        thresholdField.setPromptText("Low Stock Threshold");
        thresholdField.setText("10");

        try {
            List<Category> categories = apiClient.getCategories();
            categoryCombo.setItems(FXCollections.observableArrayList(categories));
        } catch (Exception e) {
            showError("Error", "Could not load categories for dropdown: " + e.getMessage());
        }

        if (existing != null) {
            nameField.setText(existing.getName());
            skuField.setText(existing.getSku());
            priceField.setText(existing.getPrice().toString());
            qtyField.setText(String.valueOf(existing.getQuantity()));
            thresholdField.setText(String.valueOf(existing.getLowStockThreshold()));

            for (int i = 0; i < categoryCombo.getItems().size(); i++) {
                Category c = categoryCombo.getItems().get(i);
                if (c.getId().equals(existing.getCategoryId())) {
                    categoryCombo.setValue(c);
                    break;
                }
            }
        }

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("SKU:"), 0, 1);
        grid.add(skuField, 1, 1);
        grid.add(new Label("Category:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Price ($):"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Stock Quantity:"), 0, 4);
        grid.add(qtyField, 1, 4);
        grid.add(new Label("Low Stock Threshold:"), 0, 5);
        grid.add(thresholdField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(new javafx.util.Callback<ButtonType, Product>() {
            @Override
            public Product call(ButtonType buttonType) {
                if (buttonType == saveButtonType) {
                    Product p = existing == null ? new Product() : existing;
                    p.setName(nameField.getText());
                    p.setSku(skuField.getText());
                    Category selCat = categoryCombo.getValue();
                    if (selCat != null) {
                        p.setCategoryId(selCat.getId());
                    }
                    try {
                        p.setPrice(new BigDecimal(priceField.getText().trim()));
                    } catch (Exception ignored) {}
                    try {
                        p.setQuantity(Integer.parseInt(qtyField.getText().trim()));
                    } catch (Exception ignored) {}
                    try {
                        p.setLowStockThreshold(Integer.parseInt(thresholdField.getText().trim()));
                    } catch (Exception ignored) {}
                    return p;
                }
                return null;
            }
        });

        Optional<Product> result = dialog.showAndWait();
        if (result.isPresent()) {
            Product p = result.get();
            try {
                if (existing == null) {
                    apiClient.createProduct(p);
                    showInfo("Success", "Product created successfully.");
                } else {
                    apiClient.updateProduct(p.getId(), p);
                    showInfo("Success", "Product updated successfully.");
                }
                loadProductsData();
            } catch (Exception e) {
                showError("Save Error", e.getMessage());
            }
        }
    }

    // Categories Logic
    @FXML
    public void loadCategoriesData() {
        try {
            List<Category> categories = apiClient.getCategories();
            categoriesTable.setItems(FXCollections.observableArrayList(categories));
        } catch (Exception e) {
            showError("Load Error", "Failed to load categories: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddCategory() {
        showCategoryDialog(null);
    }

    @FXML
    public void handleEditCategory() {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("No Selection", "Please select a category to edit.");
            return;
        }
        showCategoryDialog(selected);
    }

    @FXML
    public void handleDeleteCategory() {
        Category selected = categoriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("No Selection", "Please select a category to delete.");
            return;
        }
        if (showConfirm("Confirm Delete", "Are you sure you want to delete category '" + selected.getName() + "'?")) {
            try {
                apiClient.deleteCategory(selected.getId());
                showInfo("Deleted", "Category deleted successfully.");
                loadCategoriesData();
            } catch (Exception e) {
                showError("Delete Error", e.getMessage());
            }
        }
    }

    private void showCategoryDialog(Category existing) {
        Dialog<Category> dialog = new Dialog<Category>();
        dialog.setTitle(existing == null ? "Add Category" : "Edit Category");
        dialog.setHeaderText(existing == null ? "Enter new category details" : "Update category details");

        ButtonType saveBtn = new ButtonType(existing == null ? "Create" : "Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Category Name");
        TextField descField = new TextField();
        descField.setPromptText("Description (Optional)");

        if (existing != null) {
            nameField.setText(existing.getName());
            descField.setText(existing.getDescription());
        }

        grid.add(new Label("Category Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(new javafx.util.Callback<ButtonType, Category>() {
            @Override
            public Category call(ButtonType buttonType) {
                if (buttonType == saveBtn) {
                    Category c = existing == null ? new Category() : existing;
                    c.setName(nameField.getText());
                    c.setDescription(descField.getText());
                    return c;
                }
                return null;
            }
        });

        Optional<Category> result = dialog.showAndWait();
        if (result.isPresent()) {
            Category c = result.get();
            try {
                if (existing == null) {
                    apiClient.createCategory(c);
                    showInfo("Success", "Category created successfully.");
                } else {
                    apiClient.updateCategory(c.getId(), c);
                    showInfo("Success", "Category updated successfully.");
                }
                loadCategoriesData();
            } catch (Exception e) {
                showError("Save Error", e.getMessage());
            }
        }
    }

    // Sales Logic
    @FXML
    public void loadSalesData() {
        try {
            List<Sale> sales = apiClient.getSales();
            salesHistoryTable.setItems(FXCollections.observableArrayList(sales));

            List<Product> products = apiClient.getProducts();
            saleProductCombo.setItems(FXCollections.observableArrayList(products));
        } catch (Exception e) {
            showError("Load Error", "Failed to load sales: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddItemToSale() {
        Product selectedProduct = saleProductCombo.getValue();
        if (selectedProduct == null) {
            showInfo("Validation", "Please select a product first.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(saleQtyInput.getText().trim());
            if (qty <= 0) {
                showError("Validation", "Quantity must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Validation", "Please enter a valid numeric quantity.");
            return;
        }

        if (qty > selectedProduct.getQuantity()) {
            showError("Insufficient Stock", "Requested quantity (" + qty + ") exceeds available stock (" +
                    selectedProduct.getQuantity() + ") for " + selectedProduct.getName());
            return;
        }

        // Check if already in cart
        boolean found = false;
        for (int i = 0; i < cartItems.size(); i++) {
            SaleItem existing = cartItems.get(i);
            if (existing.getProductId().equals(selectedProduct.getId())) {
                int newQty = existing.getQuantity() + qty;
                if (newQty > selectedProduct.getQuantity()) {
                    showError("Insufficient Stock", "Total cart quantity (" + newQty +
                            ") exceeds available stock (" + selectedProduct.getQuantity() + ")");
                    return;
                }
                existing.setQuantity(newQty);
                existing.setSubtotal(existing.getUnitPrice().multiply(new BigDecimal(newQty)));
                found = true;
                break;
            }
        }

        if (!found) {
            BigDecimal subtotal = selectedProduct.getPrice().multiply(new BigDecimal(qty));
            SaleItem item = new SaleItem(selectedProduct.getId(), selectedProduct.getName(),
                    qty, selectedProduct.getPrice(), subtotal);
            cartItems.add(item);
        }

        saleCartTable.refresh();
        updateCartTotal();
    }

    private void updateCartTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < cartItems.size(); i++) {
            total = total.add(cartItems.get(i).getSubtotal());
        }
        cartTotalLabel.setText("Grand Total: $" + total.toString());
    }

    @FXML
    public void handleClearSaleCart() {
        cartItems.clear();
        updateCartTotal();
    }

    @FXML
    public void handleCompleteSale() {
        if (cartItems.isEmpty()) {
            showInfo("Empty Cart", "Please add items to the cart before completing the sale.");
            return;
        }

        CreateSaleRequest req = new CreateSaleRequest();
        for (int i = 0; i < cartItems.size(); i++) {
            SaleItem item = cartItems.get(i);
            req.getItems().add(new CreateSaleItemRequest(item.getProductId(), item.getQuantity()));
        }

        try {
            Sale completed = apiClient.createSale(req);
            showInfo("Sale Completed", "Sale #" + completed.getSaleNumber() + " completed! Total: $" + completed.getTotalAmount());
            cartItems.clear();
            updateCartTotal();
            loadSalesData();
        } catch (Exception e) {
            showError("Sale Error", "Failed to complete sale: " + e.getMessage());
        }
    }

    // Low Stock Logic
    @FXML
    public void loadLowStockData() {
        try {
            List<Product> list = apiClient.getLowStockProducts();
            lowStockTable.setItems(FXCollections.observableArrayList(list));
        } catch (Exception e) {
            showError("Load Error", "Failed to load low stock items: " + e.getMessage());
        }
    }

    @FXML
    public void handleRestockProduct() {
        Product selected = lowStockTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("No Selection", "Please select a product to restock.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("10");
        dialog.setTitle("Restock Product");
        dialog.setHeaderText("Add stock for: " + selected.getName() + " (Current stock: " + selected.getQuantity() + ")");
        dialog.setContentText("Quantity to add:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int addQty = Integer.parseInt(result.get().trim());
                if (addQty <= 0) {
                    showError("Invalid Quantity", "Quantity to add must be positive.");
                    return;
                }
                selected.setQuantity(selected.getQuantity() + addQty);
                apiClient.updateProduct(selected.getId(), selected);
                showInfo("Restocked", "Product restocked successfully! New quantity: " + selected.getQuantity());
                loadLowStockData();
            } catch (NumberFormatException e) {
                showError("Invalid Input", "Please enter a valid number.");
            } catch (Exception e) {
                showError("Update Error", e.getMessage());
            }
        }
    }

    // UI Helpers
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Action Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
