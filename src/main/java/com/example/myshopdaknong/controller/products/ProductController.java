package com.example.myshopdaknong.controller.products;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.services.ProductService;
import com.example.myshopdaknong.util.FileUploadUltil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
        System.out.println("cc +"+ Product);
        if(bindingResult.hasErrors())
        {
            model.addAttribute("pageTitle", "Add Product");
            model.addAttribute("TitleForm", "Add Product");
            model.addAttribute("ListProductCategory", productSerVice.findAllCategory());
            return "products/add-form-products";
        }
        try {
            if (Product.getId() != null) {
                Product productInDb = productSerVice.findByid(Product.getId()).orElseThrow(() -> new ProductException("Product not found"));
                productInDb=this.setData(productInDb,Product);
                productInDb=this.setImage(productInDb,multipartFile);
                this.setImage(productInDb,multipartFile);
                redirectAttributes.addFlashAttribute("Message", "Update Successful");
            } else {
                Product.setCreatedAt(new Date());
                this.setImage(Product,multipartFile);
                redirectAttributes.addFlashAttribute("Message", "Save Successful");
            }
        } catch (IOException | ProductException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/products";
    }

    public Product setData(Product product,Product productInForm) throws IOException {
        product.setUpdatedAt(new Date());
        product.setContent(productInForm.getContent());
        product.setSku(productInForm.getSku());
        product.setListProductCategories(productInForm.getListProductCategories());
        product.setPrice(productInForm.getPrice());
        product.setName(productInForm.getName());
        product.setDiscount_price(productInForm.getDiscount_price());
        product.setTax(productInForm.getTax());
        product.setIsActive(productInForm.getIsActive());
        return product;
    }

    public Product setImage(Product product,MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = multipartFile.getOriginalFilename();
            fileName=fileName.replace(" ","_");
            product.setImage(fileName);
            this.productSerVice.save(product);

//            String directory = "public/images/" + product.getId();
            String directory = "src/main/resources/static/images/" + product.getId();
            FileUploadUltil.saveFile(directory, fileName, multipartFile, null);
        }else
        {
            this.productSerVice.save(product);
        }
        return product;
    }
}
