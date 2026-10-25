package com.inventory.api.repository;

import com.inventory.api.model.Sale;
import com.inventory.api.model.SaleItem;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class SaleRepository {

    private final JdbcTemplate jdbcTemplate;

    public SaleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Sale> saleRowMapper = new RowMapper<Sale>() {
        @Override
        public Sale mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sale sale = new Sale();
            sale.setId(rs.getInt("id"));
            sale.setSaleNumber(rs.getString("sale_number"));
            sale.setTotalAmount(rs.getBigDecimal("total_amount"));
            sale.setSaleDate(rs.getTimestamp("sale_date"));
            return sale;
        }
    };

    private final RowMapper<SaleItem> itemRowMapper = new RowMapper<SaleItem>() {
        @Override
        public SaleItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            SaleItem item = new SaleItem();
            item.setId(rs.getInt("id"));
            item.setSaleId(rs.getInt("sale_id"));
            item.setProductId(rs.getInt("product_id"));
            item.setProductName(rs.getString("product_name"));
            item.setQuantity(rs.getInt("quantity"));
            item.setUnitPrice(rs.getBigDecimal("unit_price"));
            item.setSubtotal(rs.getBigDecimal("subtotal"));
            return item;
        }
    };

    public List<Sale> findAll() {
        String sql = "SELECT id, sale_number, total_amount, sale_date FROM sales ORDER BY sale_date DESC";
        return jdbcTemplate.query(sql, saleRowMapper);
    }

    public Sale findById(int id) {
        String sql = "SELECT id, sale_number, total_amount, sale_date FROM sales WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, saleRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<SaleItem> findItemsBySaleId(int saleId) {
        String sql = "SELECT si.id, si.sale_id, si.product_id, p.name AS product_name, " +
                     "si.quantity, si.unit_price, si.subtotal " +
                     "FROM sale_items si " +
                     "LEFT JOIN products p ON si.product_id = p.id " +
                     "WHERE si.sale_id = ?";
        return jdbcTemplate.query(sql, itemRowMapper, saleId);
    }

    public int saveSale(Sale sale) {
        String sql = "INSERT INTO sales (sale_number, total_amount) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, sale.getSaleNumber());
                ps.setBigDecimal(2, sale.getTotalAmount());
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            int newId = keyHolder.getKey().intValue();
            sale.setId(newId);
            return newId;
        }
        return 0;
    }

    public void saveSaleItem(SaleItem item) {
        String sql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                item.getSaleId(),
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal());
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM sales";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count != null) {
            return count;
        }
        return 0;
    }

    public BigDecimal sumTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0.00) FROM sales";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class);
    }
}
