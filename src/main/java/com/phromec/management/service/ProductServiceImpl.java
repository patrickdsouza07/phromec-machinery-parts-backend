package com.phromec.management.service;

import java.util.List;

import com.phromec.management.repository.ProductServiceRepository;

import com.phromec.management.exception.ProductAlreadyExistsException;
import com.phromec.management.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductServiceRepository productServiceRepository;

	public ProductServiceImpl(ProductServiceRepository productServiceRepository) {
		this.productServiceRepository = productServiceRepository;
	}
	
	@Override
	public List<Product> getAllProducts() {
		return productServiceRepository.findAll();
	}

	/*@Override
	public List<Product> getProductsByCategory(String categoryName) {
		// TODO Auto-generated method stub
		List<Product> result=new ArrayList<>();

		try {
			result= productServiceRepository.getProductsByCategory(categoryName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}*/

	@Override
	public Product addProduct(Product productDetails) throws ProductAlreadyExistsException {
		return productServiceRepository.save(productDetails);
	}

	@Override
	public Product updateProduct(Product productDetails, String productId) {
		Product existing = productServiceRepository.findById(Long.valueOf(productId)).orElseThrow();
		existing.setProductName(productDetails.getProductName());
		existing.setPriceInr(productDetails.getPriceInr());
		existing.setStockQuantity(productDetails.getStockQuantity());
		return productServiceRepository.save(existing);
	}

	@Override
	public void deleteProduct(String productId) {
		productServiceRepository.deleteById(Long.valueOf(productId));
	}
	

}
