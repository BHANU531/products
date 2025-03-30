package com.products.products.controller;

import com.products.products.entity.Product;
import com.products.products.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // Display all products (main page)
    @GetMapping("/products")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    // Create a new product
    @PostMapping("/products")
    public String createProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            // Set default values if they're not provided in the form
            if (product.getActive() == null) {
                product.setActive(true);
            }
            if (product.getFeatured() == null) {
                product.setFeatured(false);
            }

            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Get product by ID (for AJAX)
    @GetMapping("/products/{id}/json")
    @ResponseBody
    public ResponseEntity<Product> getProductJson(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing product
    @PostMapping("/products/{id}/update")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            // Ensure the ID is set correctly
            product.setId(id);

            // Update the product
            productService.updateProduct(id,product);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Delete a product
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Feature/unfeature a product
    @PostMapping("/products/{id}/feature")
    public String featureProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            if (product != null) {
                product.setFeatured(true);
                productService.updateProduct(id,product);
                redirectAttributes.addFlashAttribute("successMessage", "Product marked as featured!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to feature product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Deactivate a product
    @PostMapping("/products/{id}/deactivate")
    public String deactivateProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            if (product != null) {
                product.setActive(false);
                productService.updateProduct(id,product);
                redirectAttributes.addFlashAttribute("successMessage", "Product deactivated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to deactivate product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Reactivate a product
    @PostMapping("/products/{id}/reactivate")
    public String reactivateProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.getProductById(id);
            if (product != null) {
                product.setActive(true);
                productService.updateProduct(id,product);
                redirectAttributes.addFlashAttribute("successMessage", "Product reactivated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to reactivate product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Search products by name
    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String name, Model model) {
        try {
            List<Product> products = productService.searchProductsByName(name);
            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error searching products: " + e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products";
    }

    // Filter products by price range
    @GetMapping("/products/filter")
    public String filterProducts(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {
        try {
            // Default values if parameters are not provided
            double min = minPrice != null ? minPrice : 0.0;
            double max = maxPrice != null ? maxPrice : Double.MAX_VALUE;

            List<Product> products = productService.filterProductsByPrice(min, max);
            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error filtering products: " + e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products";
    }

    // Get active products
    @GetMapping("/products/active")
    public String getActiveProducts(Model model) {
        try {
            List<Product> products = productService.getActiveProducts();
            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving active products: " + e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products";
    }

    // Get inactive products
    @GetMapping("/products/inactive")
    public String getInactiveProducts(Model model) {
        try {
            List<Product> products = productService.getInactiveProducts();
            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error retrieving inactive products: " + e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
        }
        return "products";
    }

    // Get low stock products
    @GetMapping("/products/low-stock")
    public String getLowStockProducts(Model model) {
        List<Product> products = productService.getLowStockProducts();
        model.addAttribute("products", products);
        return "products";
    }

    // Get top selling products
    @GetMapping("/products/top-selling")
    public String getTopSellingProducts(Model model) {
        List<Product> products = productService.getTopSellingProducts();
        model.addAttribute("products", products);
        return "products";
    }

    // Get featured products
    @GetMapping("/products/featured")
    public String getFeaturedProducts(Model model) {
        List<Product> products = productService.getFeaturedProducts();
        model.addAttribute("products", products);
        return "products";
    }

    // Get recently added products
    @GetMapping("/products/recent")
    public String getRecentProducts(Model model) {
        List<Product> products = productService.getRecentlyAddedProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @PostMapping("/products/delete-bulk")
    public String deleteBulkProducts(@RequestParam(value = "productIds", required = false) List<Long> productIds, // Get list of IDs from form checkboxes
                                     RedirectAttributes redirectAttributes) {

        if (CollectionUtils.isEmpty(productIds)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select at least one product to delete.");
            return "redirect:/products"; // Redirect back if nothing is selected
        }

        try {
            productService.bulkDeleteProducts(productIds); // Call the service method
            redirectAttributes.addFlashAttribute("successMessage", "Successfully deleted " + productIds.size() + " product(s)."); // Use size of input list for message
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting products: " + e.getMessage());
        }

        return "redirect:/products"; // Redirect back to the product list page
    }

}