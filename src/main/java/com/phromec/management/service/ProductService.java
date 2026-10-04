package com.phromec.management.service;

import java.util.List;

import com.phromec.management.exception.ProductAlreadyExistsException;
import com.phromec.management.model.Product;

public interface ProductService {
	
	public List<Product> getAllProducts();
//	public List<Product> getProductsByCategory(String categoryName);
	public Product addProduct(Product productDetails) throws ProductAlreadyExistsException;
	public Product updateProduct(Product productDetails, String productId);
	public void deleteProduct(String productId);
}
