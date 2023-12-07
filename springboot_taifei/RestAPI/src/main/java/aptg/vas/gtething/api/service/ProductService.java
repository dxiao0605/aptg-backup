package aptg.vas.gtething.api.service;

import java.util.List;

import aptg.vas.gtething.api.model.Product;

public interface ProductService {
	public List<Product> findAll();
	public Product find(int id);
}
