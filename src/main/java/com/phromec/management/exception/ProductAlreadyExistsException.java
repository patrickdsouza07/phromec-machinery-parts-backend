package com.phromec.management.exception;

import java.io.Serial;

public class ProductAlreadyExistsException extends Exception{
	
	@Serial
    private static final long serialVersionUID = 1L;

	public ProductAlreadyExistsException(String message) {
		super(message);
	}


}
