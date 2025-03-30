package com.products.products.service;

import com.products.products.entity.Product;
import com.products.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Long id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> filterProductsByPrice(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findByQuantityLessThan(10);
    }

    public void bulkUpdateQuantities(List<Product> products) {
        productRepository.saveAll(products);
    }

    public List<Product> getTopSellingProducts() {
        // Implement logic as per business criteria
        return productRepository.findTop10ByOrderByPurchaseQuantityDesc();
    }

    public void markProductFeatured(Long id) {
        Product product = getProductById(id);
        if (product != null) {
            product.setFeatured(true);
            productRepository.save(product);
        }
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findByIsFeaturedTrue();
    }

    public void deactivateProduct(Long id) {
        Product product = getProductById(id);
        if (product != null) {
            product.setActive(false);
            productRepository.save(product);
        }
    }

    public void reactivateProduct(Long id) {
        Product product = getProductById(id);
        if (product != null) {
            product.setActive(true);
            productRepository.save(product);
        }
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getRecentlyAddedProducts() {
        return productRepository.findTop10ByOrderByCreatedDateDesc();
    }

    public void bulkDeleteProducts(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    public void addProductImage(Long id, String imageUrl) {
        Product product = getProductById(id);
        if (product != null) {
            product.setImageUrls(imageUrl);
            productRepository.save(product);
        }
    }


    public List<Product> getActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public List<Product> getInactiveProducts() {
        return productRepository.findByIsActiveFalse();
    }
}

