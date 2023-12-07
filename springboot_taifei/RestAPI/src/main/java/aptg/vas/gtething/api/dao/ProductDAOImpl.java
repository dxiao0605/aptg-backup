package aptg.vas.gtething.api.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import aptg.vas.gtething.api.model.Product;

@Repository
public class ProductDAOImpl implements ProductDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Product> findAll() {		
		return this.jdbcTemplate.query("select * from product", 
				new BeanPropertyRowMapper<Product>(Product.class));
	}

	@Override
	public Product find(int id) {
		return this.jdbcTemplate.queryForObject("select * from product where id = ?", 
				new BeanPropertyRowMapper<Product>(Product.class),id);
	}

}
