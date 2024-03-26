package com.example.myshopdaknong.controller.productcategory;

import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.ProductCategoriesException;
import com.example.myshopdaknong.services.ProductCategoriesSerVice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class ProductCategoryController {
    @Autowired
    private ProductCategoriesSerVice productCategoriesSerVice;
    @GetMapping("/category")
    public String listProduct(Model model) {
        model.addAttribute("pageTitle","Category");
        model.addAttribute("ListCate",this.productCategoriesSerVice.FindAll());
        return "category/category";
    }


    @GetMapping("/category/add")
    public String formAddCategory(Model model) {
        model.addAttribute("pageTitle","Category");
        model.addAttribute("TitleForm", "Add Category");
        model.addAttribute("isNewUser", true);
        model.addAttribute("Category",new ProductCategory());
        return "category/add-form-category";
    }
    public ProductCategory SetEditCategory(ProductCategory productCategories, ProductCategory EditCategory)
    {
        productCategories.setName(EditCategory.getName());
        productCategories.setSlug(EditCategory.getSlug());
        productCategories.setDescription(EditCategory.getDescription());
        productCategories.setUpdatedAt(new Date());
        return productCategories;
    }
    public ProductCategory SetCreateNewCategory(ProductCategory productCategories)
    {
        productCategories.setCreatedAt(new Date());
        return productCategories;
    }
    @PostMapping("/category/save")
    public String saveCategory(ProductCategory productCategories, Model model) throws ProductCategoriesException {
        if(productCategories.getId()==null||productCategories.getId()==0)
        {
            productCategories=this.SetCreateNewCategory(productCategories);
        }else
        {
            ProductCategory productCategory=this.productCategoriesSerVice.FindById(productCategories.getId());
            productCategories=this.SetEditCategory(productCategory,productCategories);
        }
        this.productCategoriesSerVice.save(productCategories);
        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String DeleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            this.productCategoriesSerVice.DeleteCategory(id);
            redirectAttributes.addFlashAttribute("Message", "Delete Successful Categoty With Id " + id);
        } catch (ProductCategoriesException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/category";
    }

    @GetMapping("/category/edit/{id}")
    public String update(@PathVariable Integer id, Model model,RedirectAttributes redirectAttributes) {
        try {
            ProductCategory Category=this.productCategoriesSerVice.FindById(id);
            model.addAttribute("pageTitle","Category");
            model.addAttribute("TitleForm", "Edit Category");
            model.addAttribute("Category",Category);
            return "category/add-form-category";
        } catch (ProductCategoriesException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category With Id " + id + " Not Found");
        }
        return "redirect:/category";
    }
}
