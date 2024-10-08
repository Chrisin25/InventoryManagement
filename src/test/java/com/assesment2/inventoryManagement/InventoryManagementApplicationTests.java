package com.assesment2.inventoryManagement;

import com.assesment2.inventoryManagement.InMemoryCache.InMemoryCache;
import com.assesment2.inventoryManagement.handlers.GlobalExceptionHandler;
import com.assesment2.inventoryManagement.model.Category;
import com.assesment2.inventoryManagement.model.OrderTable;
import com.assesment2.inventoryManagement.model.Product;
import com.assesment2.inventoryManagement.model.User;
import com.assesment2.inventoryManagement.repository.CategoryRepo;
import com.assesment2.inventoryManagement.repository.OrderRepo;
import com.assesment2.inventoryManagement.repository.ProductRepo;
import com.assesment2.inventoryManagement.repository.UserRepo;
import com.assesment2.inventoryManagement.response.ResponseMessage;
import com.assesment2.inventoryManagement.service.InventoryManagementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class InventoryManagementApplicationTests {


	@InjectMocks
	private InventoryManagementService inventoryManagementService;

	@Mock
	private ProductRepo productRepo;

	@Mock
	private CategoryRepo categoryRepo;

	@Mock
	private UserRepo userRepo;

	@Mock
	private OrderRepo orderRepo;

	@Mock
	private InMemoryCache inMemoryCache;

	private GlobalExceptionHandler globalExceptionHandler;
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this); // Initializes the mocks
		globalExceptionHandler = new GlobalExceptionHandler();
	}

	@Test
	void testAddProduct_Invalid_Price() {
		Product product = new Product();
		product.setProductName("Existing Product");
		product.setCategoryId(1);
		product.setPrice(-1.0);
		product.setQuantity(10);

		when(categoryRepo.existsByCategoryId(product.getCategoryId())).thenReturn(true);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
		assertEquals("Enter a valid price", exception.getMessage());
	}

	@Test
	void testAddProduct_Invalid_Quantity() {
		Product product = new Product();
		product.setProductName("Existing Product");
		product.setCategoryId(1);
		product.setPrice(1.0);
		product.setQuantity(-1);

		when(categoryRepo.existsByCategoryId(product.getCategoryId())).thenReturn(true);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
		assertEquals("Enter a valid quantity", exception.getMessage());
	}

	@Test
	void testAddProduct_NullProductName_ThrowsException() {
		Product product = new Product();
		product.setProductName(null); // Null name
		product.setCategoryId(1);
		product.setPrice(10.0);
		product.setQuantity(10);

		// Assert that an IllegalArgumentException is thrown with the expected message
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
		assertEquals("Product name is required", exception.getMessage());
	}

	@Test
	void testAddProduct_EmptyProductName_ThrowsException() {
		Product product = new Product();
		product.setProductName(""); // Empty name
		product.setCategoryId(1);
		product.setPrice(10.0);
		product.setQuantity(10);

		// Assert that an IllegalArgumentException is thrown with the expected message
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
		assertEquals("Product name is required", exception.getMessage());
	}




	@Test
	void testAddProduct_ExistingProductName_ThrowsException() {
		Product product = new Product();
		product.setProductName("Existing Product");
		product.setCategoryId(1);
		product.setPrice(10.0);
		product.setQuantity(10);


		when(productRepo.existsByProductName(product.getProductName())).thenReturn(true);


		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}


	@Test
	void testAddProduct_InvalidCategoryId_ThrowsException() {
		Product product = new Product();
		product.setProductName("New Product");
		product.setCategoryId(1);
		product.setPrice(10.0);
		product.setQuantity(10);


		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}

	@Test
	void testAddProduct_InvalidPrice_ThrowsException() {
		Product product = new Product();
		product.setProductName("New Product");
		product.setCategoryId(1);
		product.setPrice(0.0); // Invalid price
		product.setQuantity(10);


		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}

	@Test
	void testAddProduct_InvalidQuantity_ThrowsException() {
		Product product = new Product();
		product.setProductName("New Product");
		product.setCategoryId(1);
		product.setPrice(10.0);
		product.setQuantity(0); // Invalid quantity

		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}
	@Test
	void testAddProduct_ValidProduct_SavesProduct() {

		Product product = new Product();
		product.setProductName("New Product");
		product.setCategoryId(1);
		product.setPrice(10.0);
		product.setQuantity(10);

		when(productRepo.existsByProductName(product.getProductName())).thenReturn(false);
		when(categoryRepo.existsByCategoryId(product.getCategoryId())).thenReturn(true);

		when(productRepo.save(any(Product.class))).thenAnswer(invocation -> {
			Product savedProduct = invocation.getArgument(0);
			savedProduct.setProductId(1); // Simulate the saved product ID
			return savedProduct;
		});
		int savedProductId = inventoryManagementService.addProduct(product);

		verify(productRepo).save(product);
		assertEquals(1, savedProductId);
	}


	@Test
	void testAddProduct_ProductWithNullName_ThrowsException() {
		Product product = new Product();
		product.setCategoryId(1);
		product.setPrice(10.0);
		product.setQuantity(10);

		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}

	@Test
	void testAddProduct_ProductWithNullCategoryId_ThrowsException() {
		Product product = new Product();
		product.setProductName("New Product");
		product.setPrice(10.0);
		product.setQuantity(10);

		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}

	@Test
	void testAddProduct_ProductWithNullPrice_ThrowsException() {
		Product product = new Product();
		product.setProductName("New Product");
		product.setCategoryId(1);
		product.setQuantity(10);

		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}

	@Test
	void testAddProduct_ProductWithNullQuantity_ThrowsException() {
		Product product = new Product();
		product.setProductName("New Product");
		product.setCategoryId(1);
		product.setPrice(10.0);

		assertThrows(IllegalArgumentException.class, () -> inventoryManagementService.addProduct(product));
	}


	//add a category
	@Test
	void testAddCategory_NullCategoryName_ThrowsException() {
		Category category = new Category();
		category.setCategoryName(null); // Null name

		// Assert that a NoSuchElementException is thrown
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.addCategory(category));
		assertEquals("Category name must be valid and not null", exception.getMessage());
	}

	@Test
	void testAddCategory_EmptyCategoryName_ThrowsException() {
		Category category = new Category();
		category.setCategoryName(""); // Empty name

		// Assert that a NoSuchElementException is thrown
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.addCategory(category));
		assertEquals("Category name must be valid and not null", exception.getMessage());
	}

	@Test
	void testAddCategory_ExistingCategoryName_ThrowsException() {
		Category category = new Category();
		category.setCategoryName("Existing Category"); // Name that already exists

		// Mocking the behavior of the category repository
		when(categoryRepo.existsByCategoryName(category.getCategoryName())).thenReturn(true);

		// Assert that a NoSuchElementException is thrown
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.addCategory(category));
		assertEquals("Category already exists", exception.getMessage());
	}

	@Test
	void testAddCategory_ValidCategoryName_SavesCategory() {
		Category category = new Category();
		category.setCategoryName("New Category"); // Valid name
		category.setCategoryId(1); // Set an ID for mock return

		// Mocking the behavior of the category repository
		when(categoryRepo.existsByCategoryName(category.getCategoryName())).thenReturn(false);
		when(categoryRepo.save(category)).thenReturn(category); // Mock save to return the category itself

		// Calling the method and asserting the returned ID
		int savedCategoryId = inventoryManagementService.addCategory(category);
		assertEquals(1, savedCategoryId); // Check that the returned ID matches the mocked ID
	}

	@Test
	void testGetCategory_NullCategoryId_ReturnsAllCategories() {
		// Create categories using setters
		List<Category> categories = new ArrayList<>();
		Category category1 = new Category();
		category1.setCategoryId(1);
		category1.setCategoryName("Category 1");
		categories.add(category1);

		Category category2 = new Category();
		category2.setCategoryId(2);
		category2.setCategoryName("Category 2");
		categories.add(category2);

		// Mocking the behavior of the category repository to return a list of categories
		when(categoryRepo.findAll()).thenReturn(categories);

		List<Category> result = inventoryManagementService.getCategory(null);

		assertEquals(2, result.size()); // Expecting two categories
		assertEquals("Category 1", result.get(0).getCategoryName());
		assertEquals("Category 2", result.get(1).getCategoryName());
		verify(categoryRepo).findAll(); // Ensure findAll was called
	}

	@Test
	void testGetCategory_ValidCategoryId_Cached() {
		Integer categoryId = 1;
		Category cachedCategory = new Category();
		cachedCategory.setCategoryId(categoryId);
		cachedCategory.setCategoryName("Category 1");

		// Mocking the InMemoryCache to return the cached category
		InMemoryCache.putCategory(cachedCategory);
		Category result = inventoryManagementService.getCategory(categoryId).get(0);

		assertEquals(cachedCategory, result); // Check that the returned category is the cached one
		verify(categoryRepo, never()).findByCategoryId(anyInt()); // Ensure findByCategoryId is never called
	}
	@Test
	void testGetCategory_ValidCategoryId_NotCached_ExistsInRepo() {
		Integer categoryId = 2;
		Category categoryFromRepo = new Category();
		categoryFromRepo.setCategoryId(categoryId);
		categoryFromRepo.setCategoryName("Category 2");

		// Clear the cache before the test
		InMemoryCache.deleteCategory(categoryId); // Assuming this method exists to clear the cache

		// Mocking the behavior of the category repository
		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(categoryRepo.findByCategoryId(categoryId)).thenReturn(categoryFromRepo);

		// Invoke the method you are testing
		List<Category> result = inventoryManagementService.getCategory(categoryId);



		// Assertions
		assertEquals(1, result.size()); // Expecting one category
		assertEquals(categoryFromRepo, result.get(0)); // Check that the returned category is the one from the repo
		verify(categoryRepo).findByCategoryId(categoryId); // Ensure findByCategoryId was called

		// Check that the category was cached
		assertEquals(categoryFromRepo, InMemoryCache.getCategory(categoryId)); // Ensure the cached category is the one from the repo
	}




	@Test
	void testGetCategory_InvalidCategoryId_ThrowsException() {
		Integer categoryId = 3;

		// Mocking the behavior of the category repository
		when(categoryRepo.existsById(categoryId)).thenReturn(false);

		// Assert that a NoSuchElementException is thrown
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.getCategory(categoryId));
		assertEquals("Invalid Category ID", exception.getMessage());
	}


	//get products
	@Test
	void testGetProducts_ValidPageNumber_NoFilters() {
		int pageNo = 1;
		int pageSize = 25;

		Product product1 = new Product();
		product1.setProductId(1);
		product1.setCategoryId(1);
		product1.setProductName("Product 1");

		Product product2 = new Product();
		product2.setProductId(2);
		product2.setCategoryId(1);
		product2.setProductName("Product 2");

		List<Product> productList = List.of(product1, product2);
		Page<Product> page = new PageImpl<>(productList, PageRequest.of(0, pageSize), productList.size());

		when(productRepo.findAll(PageRequest.of(0, pageSize))).thenReturn(page);

		Page<Product> result = inventoryManagementService.getProducts(null, null, pageNo);

		assertEquals(2, result.getTotalElements());
		assertEquals(2, result.getContent().size());
		verify(productRepo).findAll(PageRequest.of(0, pageSize));
	}

	@Test
	void testGetProducts_InvalidPageNumber() {
		int pageNo = -1;

		IllegalArgumentException result = assertThrows(IllegalArgumentException.class, () ->
				inventoryManagementService.getProducts(null, null, pageNo)
		);

		assertEquals("Page number must be a positive integer", result.getMessage());
	}


	@Test
	void testGetProducts_ValidProductId_NoCategory() {
		Integer productId = 1;
		Product product = new Product();
		product.setProductId(productId);
		product.setProductName("Product 1");

		when(productRepo.findByProductId(productId)).thenReturn(product);

		Page<Product> result = inventoryManagementService.getProducts(productId, null, 1);

		assertEquals(1, result.getTotalElements());

		assertEquals(product.getProductId(), result.getContent().get(0).getProductId());


	}

	@Test
	void testGetProducts_InvalidProductId() {
		Integer productId = 1;

		when(productRepo.findByProductId(productId)).thenReturn(null);

		assertThrows(NoSuchElementException.class, () -> inventoryManagementService.getProducts(productId, null, 1));
		verify(productRepo).findByProductId(productId);
	}

	@Test
	void testGetProducts_ValidCategoryId_NoProduct() {
		Integer categoryId = 1;
		int pageNo = 1;

		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(productRepo.existsAllByCategoryId(categoryId)).thenReturn(false);

		assertThrows(NoSuchElementException.class, () -> inventoryManagementService.getProducts(null, categoryId, pageNo));
	}

	@Test
	void testGetProducts_InvalidCategoryId() {
		Integer categoryId = 999;
		int pageNo = 1;

		when(categoryRepo.existsById(categoryId)).thenReturn(false);

		assertThrows(NoSuchElementException.class, () -> inventoryManagementService.getProducts(null, categoryId, pageNo));
	}

	@Test
	void testGetProducts_ValidCategoryId_WithProducts() {
		Integer categoryId = 1;
		int pageNo = 1;
		int pageSize = 25;

		Product product = new Product();
		product.setProductId(1);
		product.setCategoryId(categoryId);
		product.setProductName("Product 1");

		List<Product> productList = Collections.singletonList(product);
		Page<Product> page = new PageImpl<>(productList, PageRequest.of(0, pageSize), productList.size());

		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(productRepo.existsAllByCategoryId(categoryId)).thenReturn(true);
		when(productRepo.findAllByCategoryId(categoryId, PageRequest.of(0, pageSize))).thenReturn(page);

		Page<Product> result = inventoryManagementService.getProducts(null, categoryId, pageNo);

		assertEquals(1, result.getTotalElements());
		assertEquals(product, result.getContent().get(0));
		verify(productRepo).findAllByCategoryId(categoryId, PageRequest.of(0, pageSize));
	}

	@Test
	void testGetProducts_ProductIdAndCategoryId() {
		Integer productId = 1;
		Integer categoryId = 1;

		Product product = new Product();
		product.setProductId(productId);
		product.setCategoryId(categoryId);
		product.setProductName("Product 1");

		when(productRepo.findAllByProductIdAndCategoryId(productId, categoryId)).thenReturn(List.of(product));

		Page<Product> result = inventoryManagementService.getProducts(productId, categoryId, 1);

		assertEquals(1, result.getTotalElements());
		assertEquals(product, result.getContent().get(0));
		verify(productRepo).findAllByProductIdAndCategoryId(productId, categoryId);
	}

	@Test
	void testDeleteProduct_InvalidProductId() {
		Integer productId = 100;

		when(productRepo.existsByProductId(productId)).thenReturn(false);

		NoSuchElementException result = assertThrows(NoSuchElementException.class, () ->
				inventoryManagementService.deleteProduct(productId)
		);

		assertEquals("Invalid product ID", result.getMessage());
		verify(productRepo, never()).deleteByProductId(productId);

	}
	@Test
	void testDeleteProduct_ValidProductId_FoundInCache() {
		Integer productId = 1;
		Product product = new Product();
		product.setProductId(productId);

		when(productRepo.existsByProductId(productId)).thenReturn(true);

		try (MockedStatic<InMemoryCache> mockedCache = Mockito.mockStatic(InMemoryCache.class)) {
			mockedCache.when(() -> InMemoryCache.getProductById(productId)).thenReturn(product);

			inventoryManagementService.deleteProduct(productId);

			verify(productRepo).deleteByProductId(productId);
		}
	}

	@Test
	void testDeleteProduct_ValidProductId_NotFoundInCache() {
		Integer productId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		try (MockedStatic<InMemoryCache> mockedCache = Mockito.mockStatic(InMemoryCache.class)) {
			mockedCache.when(() -> InMemoryCache.getProductById(productId)).thenReturn(null);

			inventoryManagementService.deleteProduct(productId);

			verify(productRepo).deleteByProductId(productId);
		}
	}

	@Test
	void testDeleteProduct_ExceptionThrownDuringDeletion() {
		Integer productId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		doThrow(new RuntimeException("Deletion error")).when(productRepo).deleteByProductId(productId);

		assertDoesNotThrow(() -> inventoryManagementService.deleteProduct(productId));
	}

	//update product
	@Test
	void testUpdateProduct_ValidInputs() {
		Integer productId = 1;
		String productName = "New Product";
		Integer categoryId = 2;
		Double price = 50.0;
		Integer quantity = 10;

		Product existingProduct = new Product();
		existingProduct.setProductId(productId);
		existingProduct.setProductName("Old Product");
		existingProduct.setCategoryId(1);
		existingProduct.setPrice(40.0);
		existingProduct.setQuantity(5);

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(existingProduct);
		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(productRepo.existsByProductName(productName)).thenReturn(false);

		inventoryManagementService.updateProduct(productId, productName, categoryId, price, quantity);

		verify(productRepo).save(existingProduct);

		assertEquals(productName, existingProduct.getProductName());
		assertEquals(categoryId, existingProduct.getCategoryId());
		assertEquals(price, existingProduct.getPrice());
		assertEquals(15, existingProduct.getQuantity()); // 5 + 10 = 15
	}

	@Test
	void testUpdateProduct_InvalidProductId() {
		Integer productId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(false);

		NoSuchElementException exception = assertThrows(
				NoSuchElementException.class,
				() -> inventoryManagementService.updateProduct(productId, "Product", 2, 50.0, 10)
		);

		assertEquals("Invalid Product ID", exception.getMessage());
	}

	@Test
	void testUpdateProduct_InvalidProductIdNull() {



		NoSuchElementException exception = assertThrows(
				NoSuchElementException.class,
				() -> inventoryManagementService.updateProduct(null, "Product", 2, 50.0, 10)
		);

		assertEquals("Invalid Product ID", exception.getMessage());
	}

	@Test
	void testUpdateProduct_NoUpdateParameters() {
		Integer productId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(true);

		NoSuchElementException exception = assertThrows(
				NoSuchElementException.class,
				() -> inventoryManagementService.updateProduct(productId, null, null, null, null)
		);

		assertEquals("Enter the details to be updated", exception.getMessage());
	}

	@Test
	void testUpdateProduct_DuplicateProductName() {
		Integer productId = 1;
		String productName = "Existing Product";

		Product existingProduct = new Product();
		existingProduct.setProductId(productId);

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(existingProduct);
		when(productRepo.existsByProductName(productName)).thenReturn(true);

		NoSuchElementException exception = assertThrows(
				NoSuchElementException.class,
				() -> inventoryManagementService.updateProduct(productId, productName, 2, 50.0, 10)
		);

		assertEquals("Product with name " + productName + " exists", exception.getMessage());
	}

	@Test
	void testUpdateProduct_InvalidCategoryId() {
		Integer productId = 1;
		Integer categoryId = 2;

		Product existingProduct = new Product();
		existingProduct.setProductId(productId);
		existingProduct.setCategoryId(1);

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(existingProduct);
		when(categoryRepo.existsById(categoryId)).thenReturn(false);

		NoSuchElementException exception = assertThrows(
				NoSuchElementException.class,
				() -> inventoryManagementService.updateProduct(productId, "Product", categoryId, 50.0, 10)
		);

		assertEquals("Invalid Category ID", exception.getMessage());
	}

	@Test
	void testUpdateProduct_SameCategoryId() {
		Integer productId = 1;
		Integer categoryId = 1; // Same as current category

		Product existingProduct = new Product();
		existingProduct.setProductId(productId);
		existingProduct.setCategoryId(categoryId);

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(existingProduct);
		when(categoryRepo.existsById(categoryId)).thenReturn(true);

		NoSuchElementException exception = assertThrows(
				NoSuchElementException.class,
				() -> inventoryManagementService.updateProduct(productId, "Product", categoryId, 50.0, 10)
		);

		assertEquals("Product is under the same category", exception.getMessage());
	}

	@Test
	void testUpdateProduct_UpdateOnlyPrice() {
		Integer productId = 1;
		Double newPrice = 60.0;

		Product existingProduct = new Product();
		existingProduct.setProductId(productId);
		existingProduct.setPrice(50.0);

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(existingProduct);

		inventoryManagementService.updateProduct(productId, null, null, newPrice, null);

		verify(productRepo).save(existingProduct);


		assertEquals(newPrice, existingProduct.getPrice());
	}


	@Test
	void testUpdateProduct_UpdateOnlyQuantity() {
		Integer productId = 1;
		Integer additionalQuantity = 5;

		Product existingProduct = new Product();
		existingProduct.setProductId(productId);
		existingProduct.setQuantity(10);

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(existingProduct);

		inventoryManagementService.updateProduct(productId, null, null, null, additionalQuantity);

		verify(productRepo).save(existingProduct);


		assertEquals(15, existingProduct.getQuantity()); // 10 + 5 = 15
	}

	//update category


	@Test
	void testUpdateCategory_InvalidCategoryId() {
		assertThrows(NoSuchElementException.class, () -> inventoryManagementService.updateCategory(null, "New Category"));

		when(categoryRepo.existsById(99)).thenReturn(false);
		assertThrows(NoSuchElementException.class, () -> inventoryManagementService.updateCategory(99, "New Category"));
	}

	@Test
	void testUpdateCategory_CategoryAlreadyExists() {
		Integer categoryId = 1;
		String existingCategoryName = "Existing Category";
		Category category  = new Category();
		category.setCategoryId(1);
		category.setCategoryName("category1");
		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(categoryRepo.findByCategoryName(existingCategoryName)).thenReturn(category);

		assertThrows(NoSuchElementException.class, () -> inventoryManagementService.updateCategory(categoryId, existingCategoryName));
	}

	@Test
	void testUpdateCategory_Success() {
		Integer categoryId = 1;
		String newCategoryName = "Updated Category";
		Category existingCategory  = new Category();
		existingCategory.setCategoryId(1);
		existingCategory.setCategoryName("category1");
		existingCategory.setCategoryId(categoryId);

		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(categoryRepo.findByCategoryName(newCategoryName)).thenReturn(null);
		when(categoryRepo.findAllByCategoryId(categoryId)).thenReturn(List.of(existingCategory));



		inventoryManagementService.updateCategory(categoryId, newCategoryName);

		verify(categoryRepo).save(existingCategory);
		assertEquals(newCategoryName, existingCategory.getCategoryName());
	}


	@Test
	void testUpdateCategory_CacheUpdate() {

		Integer productId = 1;
		Product product = new Product(); // Create and set up your Product instance

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		try (MockedStatic<InMemoryCache> mockedCache = Mockito.mockStatic(InMemoryCache.class)) {
			mockedCache.when(() -> InMemoryCache.getProductById(productId)).thenReturn(product);

			inventoryManagementService.deleteProduct(productId);

			verify(productRepo).deleteByProductId(productId);
		}
	}



	//delete

	@Test
	void testDeleteCategory_ValidCategoryId() {
		Integer categoryId = 1;
		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(productRepo.existsAllByCategoryId(categoryId)).thenReturn(false);

		inventoryManagementService.deleteCategory(categoryId);

		verify(categoryRepo, times(1)).deleteById(categoryId);
		verify(productRepo, times(1)).existsAllByCategoryId(categoryId);
	}

	@Test
	void testDeleteCategory_CategoryIdNotFound() {
		Integer categoryId = 1;
		when(categoryRepo.existsById(categoryId)).thenReturn(false);

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.deleteCategory(categoryId));
		assertEquals("Invalid category ID", exception.getMessage());
		verify(categoryRepo, never()).deleteById(anyInt());
	}

	@Test
	void testDeleteCategory_CategoryIdNull() {
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.deleteCategory(null));
		assertEquals("Invalid category ID", exception.getMessage());
		verify(categoryRepo, never()).deleteById(anyInt());
	}

	@Test
	void testDeleteCategory_CategoryHasProducts() {
		Integer categoryId = 1;
		when(categoryRepo.existsById(categoryId)).thenReturn(true);
		when(productRepo.existsAllByCategoryId(categoryId)).thenReturn(true);

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.deleteCategory(categoryId));
		assertEquals("Cannot delete category as there are products associated to this category", exception.getMessage());
		verify(categoryRepo, never()).deleteById(anyInt());
	}

	//order a product
	@Test
	void testOrderProduct_ValidOrder() {
		Integer productId = 1;
		Integer quantity = 2;
		Integer userId = 1;

		Product product = new Product();
		product.setProductId(productId); // Ensure product ID is set
		product.setQuantity(10);

		User user = new User();
		user.setRole("buyer");

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(product);
		when(userRepo.findById(userId)).thenReturn(Optional.of(user));


		inventoryManagementService.orderProduct(productId, quantity, userId);


		verify(orderRepo, times(1)).save(any(OrderTable.class));
		verify(productRepo, times(1)).save(product);
		assertEquals(8, product.getQuantity()); // Validate the quantity is updated
	}


	@Test
	void testOrderProduct_NullProductId() {
		Integer quantity = 2;
		Integer userId = 2;



		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.orderProduct(null, quantity, userId));
		assertEquals("Invalid Product ID", exception.getMessage());
		verify(orderRepo, never()).save(any(OrderTable.class));
		verify(productRepo, never()).save(any(Product.class));
	}


	@Test
	void testOrderProduct_InvalidProductId() {
		Integer productId = 1;
		Integer quantity = 2;
		Integer userId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(false);

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.orderProduct(productId, quantity, userId));
		assertEquals("Invalid Product ID", exception.getMessage());
		verify(orderRepo, never()).save(any(OrderTable.class));
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testOrderProduct_NegativeQuantity() {
		Integer productId = 1;
		Integer quantity = -1;
		Integer userId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(true);

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.orderProduct(productId, quantity, userId));
		assertEquals("Quantity should be greater than 0", exception.getMessage());
		verify(orderRepo, never()).save(any(OrderTable.class));
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testOrderProduct_InvalidUserId() {
		Integer productId = 1;
		Integer quantity = 2;
		Integer userId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(userRepo.findById(userId)).thenReturn(Optional.empty());

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.orderProduct(productId, quantity, userId));
		assertEquals("Invalid User ID", exception.getMessage());
		verify(orderRepo, never()).save(any(OrderTable.class));
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testOrderProduct_UserNotBuyer() {
		Integer productId = 1;
		Integer quantity = 2;
		Integer userId = 1;

		User user = new User();
		user.setRole("admin");

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(userRepo.findById(userId)).thenReturn(Optional.of(user));

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.orderProduct(productId, quantity, userId));
		assertEquals("Not a valid user", exception.getMessage());
		verify(orderRepo, never()).save(any(OrderTable.class));
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testOrderProduct_InsufficientQuantity() {
		Integer productId = 1;
		Integer quantity = 5;
		Integer userId = 1;

		Product product = new Product();
		product.setQuantity(3);

		User user = new User();
		user.setRole("buyer");

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(product);
		when(userRepo.findById(userId)).thenReturn(Optional.of(user));

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.orderProduct(productId, quantity, userId));
		assertEquals("Insufficient product quantity. Available quantity: 3", exception.getMessage());
		verify(orderRepo, never()).save(any(OrderTable.class));
		verify(productRepo, never()).save(any(Product.class));
	}



	//restock a product

	@Test
	void testReStockProduct_ValidReStock() {
		Integer productId = 1;
		Integer quantity = 5;
		Integer userId = 1;

		Product product = new Product();
		product.setProductId(productId);
		product.setQuantity(10);

		User user = new User();
		user.setRole("seller");

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(product);
		when(userRepo.findById(userId)).thenReturn(Optional.of(user));

		inventoryManagementService.reStockProduct(productId, quantity, userId);

		verify(productRepo, times(1)).save(product);
		assertEquals(15, product.getQuantity());
	}

	@Test
	void testReStockProduct_NullProductId() {
		Integer quantity = 5;
		Integer userId = 1;

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.reStockProduct(null, quantity, userId));
		assertEquals("Invalid Product ID", exception.getMessage());
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testReStockProduct_InvalidProductId() {
		Integer productId = 1;
		Integer quantity = 5;
		Integer userId = 1;

		when(productRepo.existsByProductId(productId)).thenReturn(false);

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.reStockProduct(productId, quantity, userId));
		assertEquals("Invalid Product ID", exception.getMessage());
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testReStockProduct_NegativeQuantity() {
		Integer productId = 1;
		Integer quantity = -1;
		Integer userId = 1;
		when(productRepo.existsByProductId(productId)).thenReturn(true);
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.reStockProduct(productId, quantity, userId));
		assertEquals("Quantity should be greater than 0", exception.getMessage());
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testReStockProduct_InvalidUser() {
		Integer productId = 1;
		Integer quantity = 5;
		Integer userId = 1;

		Product product = new Product();
		product.setProductId(productId);
		product.setQuantity(10);

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(product);
		when(userRepo.findById(userId)).thenReturn(Optional.empty());

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.reStockProduct(productId, quantity, userId));
		assertEquals("Invalid User ID", exception.getMessage());
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	void testReStockProduct_UserNotSeller() {
		Integer productId = 1;
		Integer quantity = 5;
		Integer userId = 1;

		Product product = new Product();
		product.setProductId(productId);
		product.setQuantity(10);

		User user = new User();
		user.setRole("buyer");

		when(productRepo.existsByProductId(productId)).thenReturn(true);
		when(productRepo.findByProductId(productId)).thenReturn(product);
		when(userRepo.findById(userId)).thenReturn(Optional.of(user));

		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> inventoryManagementService.reStockProduct(productId, quantity, userId));
		assertEquals("Not a valid user", exception.getMessage());
		verify(productRepo, never()).save(any(Product.class));
	}

	@Test
	public void testHandleNoSuchElementException() throws Exception {
		String errorMessage = "Invalid Product ID";
		NoSuchElementException exception = new NoSuchElementException(errorMessage);

		ResponseEntity<ResponseMessage> responseEntity = globalExceptionHandler.handleNoSuchElementException(exception);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid Product ID", Objects.requireNonNull(responseEntity.getBody()).getMessage());
	}

	@Test
	public void testHandleIllegalArgumentException() {
		String errorMessage = "Page number must be a positive integer";
		IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

		ResponseEntity<ResponseMessage> responseEntity = globalExceptionHandler.handleIllegalArgumentException(exception);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Page number must be a positive integer", responseEntity.getBody().getMessage());
	}

	@Test
	public void testHandleGenericException() {
		String errorMessage = "An unexpected error occurred";
		Exception exception = new Exception(errorMessage);

		ResponseEntity<ResponseMessage> responseEntity = globalExceptionHandler.handleGenericException(exception);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals("An unexpected error occurred: An unexpected error occurred", responseEntity.getBody().getMessage());
	}


	@Test
	public void testHandleArgumentMismatchExceptions() {
		MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(null, Integer.class, null, null, null);

		ResponseEntity<ResponseMessage> responseEntity = globalExceptionHandler.handleArgumentMismatchExceptions(exception);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Input cannot be empty", responseEntity.getBody().getMessage());
	}

	@AfterEach
	void resetMocks() {
		Mockito.reset(productRepo, categoryRepo,inMemoryCache);
	}
}
