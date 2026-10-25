package com.inventory.api.service;

import com.inventory.api.dto.CreateSaleItemRequest;
import com.inventory.api.dto.CreateSaleRequest;
import com.inventory.api.exception.AppException;
import com.inventory.api.model.Product;
import com.inventory.api.model.Sale;
import com.inventory.api.model.SaleItem;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    public List<Sale> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        for (int i = 0; i < sales.size(); i++) {
            Sale s = sales.get(i);
            s.setItems(saleRepository.findItemsBySaleId(s.getId()));
        }
        return sales;
    }

    public Sale getSaleById(int id) {
        Sale sale = saleRepository.findById(id);
        if (sale == null) {
            throw new AppException("Sale not found with id: " + id);
        }
        sale.setItems(saleRepository.findItemsBySaleId(id));
        return sale;
    }

    @Transactional
    public Sale createSale(CreateSaleRequest request) {
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new AppException("Sale must contain at least one item.");
        }

        List<CreateSaleItemRequest> itemRequests = request.getItems();
        List<SaleItem> preparedItems = new ArrayList<SaleItem>();
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (int i = 0; i < itemRequests.size(); i++) {
            CreateSaleItemRequest itemReq = itemRequests.get(i);
            if (itemReq.getProductId() == null) {
                throw new AppException("Product id is required for sale item.");
            }
            if (itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                throw new AppException("Quantity must be greater than zero.");
            }

            Product product = productRepository.findById(itemReq.getProductId());
            if (product == null) {
                throw new AppException("Product not found with id: " + itemReq.getProductId());
            }

            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new AppException("Insufficient stock for product '" + product.getName() +
                        "'. Available: " + product.getQuantity() + ", Requested: " + itemReq.getQuantity());
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(new BigDecimal(itemReq.getQuantity()));
            grandTotal = grandTotal.add(subtotal);

            SaleItem saleItem = new SaleItem();
            saleItem.setProductId(product.getId());
            saleItem.setProductName(product.getName());
            saleItem.setQuantity(itemReq.getQuantity());
            saleItem.setUnitPrice(unitPrice);
            saleItem.setSubtotal(subtotal);
            preparedItems.add(saleItem);
        }

        // Generate sale number: e.g. SAL-YYYYMMDD-HHMMSS
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String saleNumber = "SAL-" + sdf.format(new Date()) + "-" + (int)(Math.random() * 900 + 100);

        Sale sale = new Sale();
        sale.setSaleNumber(saleNumber);
        sale.setTotalAmount(grandTotal);

        int saleId = saleRepository.saveSale(sale);
        sale.setId(saleId);

        for (int i = 0; i < preparedItems.size(); i++) {
            SaleItem item = preparedItems.get(i);
            item.setSaleId(saleId);
            saleRepository.saveSaleItem(item);

            // Reduce stock
            Product product = productRepository.findById(item.getProductId());
            int newQuantity = product.getQuantity() - item.getQuantity();
            productRepository.updateStock(product.getId(), newQuantity);
        }

        sale.setItems(preparedItems);
        return sale;
    }
}
