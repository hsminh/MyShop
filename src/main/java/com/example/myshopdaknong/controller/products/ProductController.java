package com.example.myshopdaknong.controller.products;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.services.ProductSerVice;
import com.example.myshopdaknong.util.FileUploadUltil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductSerVice productSerVice;
    @Autowired
    private EntityManager entityManager;
    @GetMapping("/products")
    public String listProduct(@RequestParam(value = "search" ,required = false)String keyWord, Model model) {
        model.addAttribute("pageTitle", "Products");
        model.addAttribute("ListProduct", productSerVice.findAll(null, keyWord));
        return "products/products";
    }

    @GetMapping("/products/add")
    public String formAddProduct(Model model) {
        model.addAttribute("pageTitle", "Add Product");
        model.addAttribute("TitleForm", "Add Product");
        model.addAttribute("ListProductCategory", productSerVice.findAllCategory());
        model.addAttribute("Product", new Product());
        return "products/add-form-products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProducts(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productSerVice.delete(id);
            redirectAttributes.addFlashAttribute("Message", "Delete Successful Product With Id " + id);
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProducts(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            model.addAttribute("pageTitle", "Edit Product | ID " + id);
            model.addAttribute("ListProductCategory", productSerVice.findAllCategory());
            model.addAttribute("Product", productSerVice.findByid(id).orElseThrow(() -> new ProductException("Product not found")));
            return "products/add-form-products";
        } catch (ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        }
    }

    @Transactional
    @PostMapping("/products/save")
    public String saveProducts(@ModelAttribute Product product, @RequestParam("images") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
        try {
            if (product.getId() != null) {
                // Nếu sản phẩm đã có id, lấy thông tin sản phẩm từ DB để cập nhật
                Product productInDb = productSerVice.findByid(product.getId()).orElseThrow(() -> new ProductException("Product not found"));
                productInDb.setUpdatedAt(new Date());
                productInDb.setContent(product.getContent());
                productInDb.setSku(product.getSku());
                productInDb.setListProductCategories(product.getListProductCategories());
                productInDb.setPrice(product.getPrice());
                productInDb.setName(product.getName());
                productInDb.setDiscount_price(product.getDiscount_price());
                productInDb.setTax(product.getTax());

                if (!multipartFile.isEmpty()) {
                    String fileName = multipartFile.getOriginalFilename();
                    productInDb.setImage(fileName);

                    String directory = "public/images/" + productInDb.getId();
                    FileUploadUltil.saveFile(directory, fileName, multipartFile, null);
                }

                entityManager.merge(productInDb); // Cập nhật thông tin sản phẩm

                redirectAttributes.addFlashAttribute("Message", "Update Successful");
            } else {
                // Nếu sản phẩm chưa có id, đây là sản phẩm mới
                if (!multipartFile.isEmpty()) {
                    String fileName = multipartFile.getOriginalFilename();
                    product.setImage(fileName);

                    product.setCreatedAt(new Date());
                    product = productSerVice.save(product); // Lưu sản phẩm mới vào DB

                    String directory = "public/images/" + product.getId();
                    FileUploadUltil.saveFile(directory, fileName, multipartFile, null); // Lưu file ảnh
                } else {
                    product = productSerVice.save(product); // Lưu sản phẩm mới vào DB
                }

                redirectAttributes.addFlashAttribute("Message", "Save Successful");
            }
        } catch (IOException | ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/products";
    }




}
