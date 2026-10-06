package com.phromec.machinery.controller;

import java.util.List;

import com.phromec.machinery.model.Permission;
import com.phromec.machinery.model.Product;
import com.phromec.machinery.model.Role;
import com.phromec.machinery.dto.UserResponse;
import com.phromec.machinery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phromec.machinery.exception.ProductAlreadyExistsException;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final ProductService productService;
	private final UserService userService;
	private final PermissionService permissionService;
	private final RoleService roleService;

	@GetMapping()
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public List<UserResponse> getAllUsers(){
		return userService.getAllUsers();
	}

	@GetMapping("/roles")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public List<Role> getAllRoles(){
		return roleService.getAllRoles();
	}

	@GetMapping("/roles/permissions")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public List<Permission> getAllPermissions(){ return permissionService.getAllPermissions(); }

	
	/*@GetMapping("/getProductsByCategory/{key}")
	public Object getProductsByCategory(@PathVariable("key") String categoryName) throws JsonProcessingException{

		List<Product> result= productService.getProductsByCategory(categoryName);
		String listToJson = objectMapper.writeValueAsString(result);
		return listToJson;
	}*/

	@PostMapping("/addProduct")
	@PreAuthorize("hasAuthority('PRODUCT_CREATE')")
	public Product addProduct(@RequestBody Product productDetails) throws ProductAlreadyExistsException{
		return productService.addProduct(productDetails);
	}

	@PostMapping("updateProduct/{productId}")
	@PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    public Product updateProduct(@RequestBody Product productDetails,@PathVariable("productId") String productId) {
        return productService.updateProduct(productDetails, productId);
    }

	@DeleteMapping("deleteProduct/{productId}")
	@PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    public void deleteProduct(@PathVariable("productId") String productId) {
        productService.deleteProduct(productId);
    }
	
}
