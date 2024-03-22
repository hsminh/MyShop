package com.example.myshopdaknong.controller.products;

import com.example.myshopdaknong.entity.Product;
import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.ProductException;
import com.example.myshopdaknong.service.ProductSerVice;
import com.example.myshopdaknong.util.FileUploadUltil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductSerVice productSerVice;
    @GetMapping("/products")
    public String listProduct(Model model) {
        model.addAttribute("pageTitle","Products");
        model.addAttribute("ListProduct",this.productSerVice.findAll());
        return "products/products";
    }

    @GetMapping("/products/add")
    public String formAddProduct(Model model) {
        model.addAttribute("pageTitle","Add Product");
        model.addAttribute("TitleForm", "Add Product");
        model.addAttribute("ListProductCategory",this.productSerVice.findAllCategory());
        model.addAttribute("Product",new Product());
        return "products/add-form-products";
    }
    @GetMapping("/products/delete/{id}")
    public String deleteProducts(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes) {
        try{
            this.productSerVice.delete(id);
            redirectAttributes.addFlashAttribute("Message", "Delete Successful Product With Id " + id);
        }catch (ProductException ex)
        {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProducts(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes,Model model) {
        try{

            model.addAttribute("pageTitle","Edit Product | ID "+id);
            model.addAttribute("ListProductCategory",this.productSerVice.findAllCategory());
            model.addAttribute("Product",this.productSerVice.findByid(id).get());
            model.addAttribute("pageTitle","Products");
            return "products/add-form-products";
        }catch (ProductException ex)
        {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/products";
        }

    }

    public Product setProductCategory( Product Product, MultipartFile multipartFile)
    {
        Product.setImage(multipartFile.getOriginalFilename());
        return Product;
    }
    @PostMapping("/products/save")
    public String saveProduts(RedirectAttributes redirectAttributes,  @RequestParam("images")MultipartFile multipartFile, Product Product, Model model) {
          try{
              String fileName=multipartFile.getOriginalFilename();
              Product = this.setProductCategory(Product,multipartFile);
              Product=this.productSerVice.save(Product);
              String udir="public/images/"+Product.getId();
              FileUploadUltil.saveFile(udir,fileName,multipartFile,null);
          }catch (IOException ex)
          {
              redirectAttributes.addFlashAttribute("errorMessage",ex.getMessage());
          }
          return "redirect:/products";
    }

}
