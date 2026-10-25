package com.inventory.api.repository;

import com.inventory.api.model.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> rowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setSku(rs.getString("sku"));
            product.setCategoryId(rs.getInt("category_id"));
            product.setCategoryName(rs.getString("category_name"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setQuantity(rs.getInt("quantity"));
            product.setLowStockThreshold(rs.getInt("low_stock_threshold"));
            product.setCreatedAt(rs.getTimestamp("created_at"));
            product.setUpdatedAt(rs.getTimestamp("updated_at"));
            return product;
        }
    };

    public List<Product> findAll() {
        String sql = "SELECT p.id, p.name, p.sku, p.category_id, c.name AS category_name, " +
                     "p.price, p.quantity, p.low_stock_threshold, p.created_at, p.updated_at " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "ORDER BY p.name ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Product findById(int id) {
        String sql = "SELECT p.id, p.name, p.sku, p.category_id, c.name AS category_name, " +
                     "p.price, p.quantity, p.low_stock_threshold, p.created_at, p.updated_at " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Product> findLowStock() {
        String sql = "SELECT p.id, p.name, p.sku, p.category_id, c.name AS category_name, " +
                     "p.price, p.quantity, p.low_stock_threshold, p.created_at, p.updated_at " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.quantity <= p.low_stock_threshold " +
                     "ORDER BY p.quantity ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public boolean existsBySku(String sku, Integer excludeId) {
        String sql;
        Integer count;
        if (excludeId != null) {
            sql = "SELECT COUNT(*) FROM products WHERE LOWER(sku) = LOWER(?) AND id <> ?";
            count = jdbcTemplate.queryForObject(sql, Integer.class, sku, excludeId);
        } else {
            sql = "SELECT COUNT(*) FROM products WHERE LOWER(sku) = LOWER(?)";
            count = jdbcTemplate.queryForObject(sql, Integer.class, sku);
        }
        return count != null && count > 0;
    }

    public int save(Product product) {
        String sql = "INSERT INTO products (name, sku, category_id, price, quantity, low_stock_threshold) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, product.getName());
                ps.setString(2, product.getSku());
                ps.setInt(3, product.getCategoryId());
                ps.setBigDecimal(4, product.getPrice());
                ps.setInt(5, product.getQuantity());
                ps.setInt(6, product.getLowStockThreshold());
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            int newId = keyHolder.getKey().intValue();
            product.setId(newId);
            return newId;
        }
        return 0;
    }

    public int update(Product product) {
        String sql = "UPDATE products SET name = ?, sku = ?, category_id = ?, price = ?, " +
                     "quantity = ?, low_stock_threshold = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                product.getName(),
                product.getSku(),
                product.getCategoryId(),
                product.getPrice(),
                product.getQuantity(),
                product.getLowStockThreshold(),
                product.getId());
    }

    public int updateStock(int productId, int newQuantity) {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newQuantity, productId);
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int countSaleItemsByProductId(int productId) {
        String sql = "SELECT COUNT(*) FROM sale_items WHERE product_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count != null ? count : 0;
    }

    public int deleteSaleItemsByProductId(int productId) {
        String sql = "DELETE FROM sale_items WHERE product_id = ?";
        return jdbcTemplate.update(sql, productId);
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM products";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count != null) {
            return count;
        }
        return 0;
    }

    public int countLowStock() {
        String sql = "SELECT COUNT(*) FROM products WHERE quantity <= low_stock_threshold";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count != null) {
            return count;
        }
        return 0;
    }
}
