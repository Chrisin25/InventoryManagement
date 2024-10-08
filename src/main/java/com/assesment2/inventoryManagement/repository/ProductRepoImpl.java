//package com.assesment2.inventoryManagement.repository;
//
//import com.assesment2.inventoryManagement.model.Product;
//import com.assesment2.inventoryManagement.service.InventoryManagementService;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import jakarta.persistence.TypedQuery;
//import org.apache.log4j.Logger;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//import org.springframework.util.StopWatch;
//
//import java.util.List;
//
//@Repository
//public  class ProductRepoImpl implements ProductRepoCustom {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//    private final Logger logger = Logger.getLogger(InventoryManagementService.class);
//    @Override
//    public Page<Product> findAll(Pageable pageable) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p", Product.class);
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//        List<Product> products = query.getResultList();
//        stopWatch.stop();
//        logger.info("Query 'findAll' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return new PageImpl<>(products, pageable, products.size());
//    }
//
//    @Override
//    public List<Product> findAllByProductId(Integer productId) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.productId = :productId", Product.class);
//        query.setParameter("productId", productId);
//        List<Product> products = query.getResultList();
//        stopWatch.stop();
//        logger.info("Query 'findAllByProductId' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return products;
//    }
//
//    @Override
//    public Page<Product> findAllByCategoryId(Integer categoryId, Pageable pageable) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.categoryId = :categoryId", Product.class);
//        query.setParameter("categoryId", categoryId);
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//        List<Product> products = query.getResultList();
//        stopWatch.stop();
//        logger.info("Query 'findAllByCategoryId' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return new PageImpl<>(products, pageable, products.size());
//    }
//
//    @Override
//    public List<Product> findAllByProductIdAndCategoryId(Integer productId, Integer categoryId) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.productId = :productId AND p.categoryId = :categoryId", Product.class);
//        query.setParameter("productId", productId);
//        query.setParameter("categoryId", categoryId);
//        List<Product> products = query.getResultList();
//        stopWatch.stop();
//        logger.info("Query 'findAllByProductIdAndCategoryId' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return products;
//    }
//
//    @Override
//    public void deleteByProductId(Integer productId) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        Query query = entityManager.createQuery("DELETE FROM Product p WHERE p.productId = :productId");
//        query.setParameter("productId", productId);
//        query.executeUpdate();
//        stopWatch.stop();
//        logger.info("Query 'deleteByProductId' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//    }
//
//    @Override
//    public Product findByProductId(Integer productId) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p WHERE p.productId = :productId", Product.class);
//        query.setParameter("productId", productId);
//        Product product = query.getSingleResult();
//        stopWatch.stop();
//        logger.info("Query 'findByProductId' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return product;
//    }
//
//    @Override
//    public boolean existsByProductName(String productName) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.productName = :productName", Long.class);
//        query.setParameter("productName", productName);
//        long count = query.getSingleResult();
//        stopWatch.stop();
//        logger.info("Query 'existsByProductName' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return count > 0;
//    }
//
//    @Override
//    public boolean existsAllByCategoryId(Integer categoryId) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.categoryId = :categoryId", Long.class);
//        query.setParameter("categoryId", categoryId);
//        long count = query.getSingleResult();
//        stopWatch.stop();
//        logger.info("Query 'existsAllByCategoryId' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return count > 0;
//    }
//
//    @ Override
//    public boolean existsByProductId(Integer productId) {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Product p WHERE p.productId = :productId", Long.class);
//        query.setParameter("productId", productId);
//        long count = query.getSingleResult();
//        stopWatch.stop();
//        logger.info("Query 'existsByProductId' executed in " + stopWatch.getTotalTimeMillis() + " ms");
//        return count > 0;
//    }
//
//    @Override
//    public void save(Product product) {
//
//    }
//}