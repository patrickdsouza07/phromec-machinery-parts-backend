package com.phromec.machinery.service;

import java.util.List;

import com.phromec.machinery.exception.ProductAlreadyExistsException;
import com.phromec.machinery.model.Product;

public interface ProductService {
	
	public List<Product> getAllProducts();
//	public List<Product> getProductsByCategory(String categoryName);
	public Product addProduct(Product productDetails) throws ProductAlreadyExistsException;
	public Product updateProduct(Product productDetails, String productId);
	public void deleteProduct(String productId);
}
