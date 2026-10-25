package com.inventory.api.controller;

import com.inventory.api.dto.CreateSaleRequest;
import com.inventory.api.model.Sale;
import com.inventory.api.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @GetMapping("/{id}")
    public Sale getSaleById(@PathVariable("id") int id) {
        return saleService.getSaleById(id);
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody CreateSaleRequest request) {
        Sale created = saleService.createSale(request);
        return new ResponseEntity<Sale>(created, HttpStatus.CREATED);
    }
}
