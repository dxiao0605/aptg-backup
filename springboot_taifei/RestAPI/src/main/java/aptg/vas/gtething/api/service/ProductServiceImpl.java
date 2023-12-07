package aptg.vas.gtething.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aptg.vas.gtething.api.dao.ProductDAO;
import aptg.vas.gtething.api.model.Product;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDAO productDAO;

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return productDAO.findAll();
	}

	@Override
	public Product find(int id) {
		// TODO Auto-generated method stub
		return productDAO.find(id);
	}

}
