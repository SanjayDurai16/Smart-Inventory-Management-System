package com.inventory.api.repository;

import com.inventory.api.model.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Category> rowMapper = new RowMapper<Category>() {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            category.setDescription(rs.getString("description"));
            category.setCreatedAt(rs.getTimestamp("created_at"));
            return category;
        }
    };

    public List<Category> findAll() {
        String sql = "SELECT id, name, description, created_at FROM categories ORDER BY name ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Category findById(int id) {
        String sql = "SELECT id, name, description, created_at FROM categories WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean existsByName(String name, Integer excludeId) {
        String sql;
        Integer count;
        if (excludeId != null) {
            sql = "SELECT COUNT(*) FROM categories WHERE LOWER(name) = LOWER(?) AND id <> ?";
            count = jdbcTemplate.queryForObject(sql, Integer.class, name, excludeId);
        } else {
            sql = "SELECT COUNT(*) FROM categories WHERE LOWER(name) = LOWER(?)";
            count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        }
        return count != null && count > 0;
    }

    public int save(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new org.springframework.jdbc.core.PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(java.sql.Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, category.getName());
                ps.setString(2, category.getDescription());
                return ps;
            }
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            int newId = keyHolder.getKey().intValue();
            category.setId(newId);
            return newId;
        }
        return 0;
    }

    public int update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        return jdbcTemplate.update(sql, category.getName(), category.getDescription(), category.getId());
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int countProductsByCategoryId(int categoryId) {
        String sql = "SELECT COUNT(*) FROM products WHERE category_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        if (count != null) {
            return count;
        }
        return 0;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM categories";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        if (count != null) {
            return count;
        }
        return 0;
    }
}
