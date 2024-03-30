package com.example.myshopdaknong.controller.productcategory;

import com.example.myshopdaknong.entity.ProductCategory;
import com.example.myshopdaknong.exception.ProductCategoriesException;
import com.example.myshopdaknong.services.ProductCategoriesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class ProductCategoryController {
    @Autowired
    private ProductCategoriesService productCategoriesSerVice;
    @GetMapping("/category")
    public String listProduct(@RequestParam(value = "search",required = false) String serachValue, Model model) {
        model.addAttribute("pageTitle","Category");
        model.addAttribute("isChoice", "Category");
        model.addAttribute("ListCate",this.productCategoriesSerVice.findAll(serachValue));

        return "category/category";
    }

    public void setCategoryAddForm(Model model)
    {
        model.addAttribute("pageTitle","Category");
        model.addAttribute("TitleForm", "Add Category");
        model.addAttribute("isNewUser", true);
    }
    @GetMapping("/category/add")
    public String formAddCategory(Model model) {

        model.addAttribute("category",new ProductCategory());
        this.setCategoryAddForm(model);
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
    public String saveCategory(@Valid  @ModelAttribute("category")  ProductCategory category, BindingResult bindingResult, Model model) throws ProductCategoriesException {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("pageTitle","Category");
            model.addAttribute("TitleForm", "Add Category");
            model.addAttribute("isNewUser", true);
            return "category/add-form-category";
        }
        if(category.getId()==null||category.getId()==0)
        {
            category=this.SetCreateNewCategory(category);
        }else
        {
            ProductCategory productCategory=this.productCategoriesSerVice.FindById(category.getId());
            category=this.SetEditCategory(productCategory,category);
        }
        this.productCategoriesSerVice.save(category);
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
            model.addAttribute("category",Category);
            return "category/add-form-category";
        } catch (ProductCategoriesException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category With Id " + id + " Not Found");
        }
        return "redirect:/category";
    }
}