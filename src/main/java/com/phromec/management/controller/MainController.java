package com.phromec.management.controller;

import java.util.List;

import com.phromec.management.model.*;
import com.phromec.management.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.phromec.management.exception.ProductAlreadyExistsException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/phromecManagement/api/v1")
public class MainController {

	private final ProductService productService;

	private final UserService userService;

	private final MachineService machineService;

	private final PartService partService;
	private final RoleService roleService;

	public MainController(ProductService productService, UserService userService,
                          MachineService machineService, PartService partService, RoleService roleService) {
		this.productService = productService;
		this.userService = userService;
		this.machineService = machineService;
		this.partService = partService;
        this.roleService = roleService;
    }
	
	@GetMapping("/getAllProducts")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public List<Product> getAllProducts(){
		return productService.getAllProducts();
	}

	@GetMapping("/getAllUsers")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public Object getAllUsers(){
		return userService.getAllUsers();
	}

	@GetMapping("/getAllRoles")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public Object getAllRoles(){
		return roleService.getAllRoles();
	}

	@GetMapping("/getAllMachines")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public Object getAllMachines(){
		return machineService.getAllMachines();
	}

	@GetMapping("/getAllParts")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public Object getAllParts(){
		return partService.getAllParts();
	}
	
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
