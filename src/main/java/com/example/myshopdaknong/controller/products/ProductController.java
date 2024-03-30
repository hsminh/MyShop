package com.example.myshopdaknong.controller.products;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.services.ProductService;
import com.example.myshopdaknong.util.FileUploadUltil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;

@Controller
public class ProductController {

    @Autowired
    private ProductService productSerVice;
    @Autowired
    private EntityManager entityManager;
    @GetMapping("/products")
    public String listProduct(@RequestParam(value = "search" ,required = false)String keyWord, Model model) {
        model.addAttribute("pageTitle", "Products");
        model.addAttribute("isChoice", "Products");
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
            model.addAttribute("pageTitle", "Add Product");
            model.addAttribute("TitleForm", "Add Product");
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
    public String saveProducts(Model model,@Valid @ModelAttribute("Product") Product Product, BindingResult bindingResult, @RequestParam("images") MultipartFile multipartFile, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("pageTitle", "Add Product");
            model.addAttribute("TitleForm", "Add Product");
            model.addAttribute("ListProductCategory", productSerVice.findAllCategory());
//            model.addAttribute("Product", Product);
            for(FieldError error : bindingResult.getFieldErrors())
            {
                System.out.println("come : "+error.getField());
                System.out.println("come : "+error.getDefaultMessage());
            }
            return "products/add-form-products";
        }
        try {
            if (Product.getId() != null) {
                Product productInDb = productSerVice.findByid(Product.getId()).orElseThrow(() -> new ProductException("Product not found"));
                productInDb.setUpdatedAt(new Date());
                productInDb.setContent(Product.getContent());
                productInDb.setSku(Product.getSku());
                productInDb.setListProductCategories(Product.getListProductCategories());
                productInDb.setPrice(Product.getPrice());
                productInDb.setName(Product.getName());
                productInDb.setDiscount_price(Product.getDiscount_price());
                productInDb.setTax(Product.getTax());
                productInDb.setIsActive(Product.getIsActive());
                if (!multipartFile.isEmpty()) {
                    String fileName = multipartFile.getOriginalFilename();
                    fileName=fileName.replace(" ","_");
                    productInDb.setImage(fileName);

                    String directory = "public/images/" + productInDb.getId();
                    FileUploadUltil.saveFile(directory, fileName, multipartFile, null);
                }

                this.productSerVice.save(productInDb);

                redirectAttributes.addFlashAttribute("Message", "Update Successful");
            } else {
                Product.setCreatedAt(new Date());
                if (!multipartFile.isEmpty()) {
                    String fileName = multipartFile.getOriginalFilename();
                    fileName=fileName.replace(" ","_");
                    Product.setImage(fileName);
                    Product = productSerVice.save(Product);

                    String directory = "public/images/" + Product.getId();
                    FileUploadUltil.saveFile(directory, fileName, multipartFile, null);
                } else {
                    Product = productSerVice.save(Product);
                }

                redirectAttributes.addFlashAttribute("Message", "Save Successful");
            }
        } catch (IOException | ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/products";
    }




}
