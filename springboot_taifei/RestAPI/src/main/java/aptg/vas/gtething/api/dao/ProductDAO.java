package aptg.vas.gtething.api.dao;

import aptg.vas.gtething.api.model.Product;

import java.util.List;

public interface ProductDAO {
	public List<Product> findAll();
	public Product find(int id);

}
