package com.example.myshopdaknong.Controllers.products;

import com.example.myshopdaknong.Entity.Product;
import com.example.myshopdaknong.Entity.Product_categories;
import com.example.myshopdaknong.Exception.ProductException;
import com.example.myshopdaknong.Exception.UpLoadPhotoNotSuccessfullt;
import com.example.myshopdaknong.Services.ProductSerVice;
import com.example.myshopdaknong.Util.FileUploadUltil;
import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Date;
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
    public String deleteProducts(@PathVariable("id")Integer id, RedirectAttributes redirectAttributes,Model model) {
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

    public Product setProductCategory(List<Product_categories> productCategoriesList,Product Product,MultipartFile multipartFile)
    {
        for(Product_categories productCategories: productCategoriesList)
        {
            Product.addProductCate(productCategories);
        }
        Product.setImage(multipartFile.getOriginalFilename());
        return Product;
    }
    @PostMapping("/products/save")
    public String saveProduts(RedirectAttributes redirectAttributes, @RequestParam("productCategories") List<Product_categories> productCategoriesList, @RequestParam("images")MultipartFile multipartFile, Product Product, Model model) {
          try{
              String fileName=multipartFile.getOriginalFilename();
              Product = this.setProductCategory(productCategoriesList,Product,multipartFile);
              Product=this.productSerVice.save(Product);
              String udir="products-Photos/"+Product.getId();
              FileUploadUltil.saveFile(udir,fileName,multipartFile,null);
          }catch (IOException ex)
          {
              redirectAttributes.addFlashAttribute("errorMessage",ex.getMessage());
          }
          return "products/products";
    }

}
