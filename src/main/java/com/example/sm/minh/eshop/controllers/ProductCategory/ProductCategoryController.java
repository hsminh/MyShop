package com.example.sm.minh.eshop.controllers.ProductCategory;

import com.example.sm.minh.eshop.entities.ProductCategory;
import com.example.sm.minh.eshop.exceptions.ProductCategoryException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.services.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoriesSerVice;
    @GetMapping("/category")
    public String listProduct(@RequestParam(value = "search",required = false) String serachValue,
                              @RequestParam(value = "isHide" ,required = false)Boolean isHide,
                              Model model) {

        if(isHide==null)
        {
            isHide=true;
        }
        if(isHide==true)
        {
            model.addAttribute("hideAndShowButton", "Show Deleted Category");
            model.addAttribute("ProductTitle", "Product Category");
        }else
        {
            model.addAttribute("hideAndShowButton", "Hide Deleted Category");
            model.addAttribute("ProductTitle", "Deleted Product Category");
        }
        model.addAttribute("pageTitle","Category");
        model.addAttribute("isChoice", "Category");
        model.addAttribute("ListCate",this.productCategoriesSerVice.findAll(serachValue,isHide));
        isHide=(isHide==false)?true:false;
        model.addAttribute("isHide", isHide);
        return "category/category";
    }


    @GetMapping("/category/add")
    public String formAddCategory(Model model) {

        model.addAttribute("category",new ProductCategory());
        this.productCategoriesSerVice.setCategoryAddForm(model);
        return "category/add-category-form";
    }


    @PostMapping("/category/save")
    public String saveCategory(@Valid  @ModelAttribute("category")  ProductCategory category, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes) throws ProductCategoryException {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("pageTitle","Category");
            model.addAttribute("TitleForm", "Add Category");
            model.addAttribute("isNewUser", true);
            return "category/add-category-form";
        }
        if(category.getId()==null||category.getId()==0)
        {
            redirectAttributes.addFlashAttribute("Message","Save Successfully Category "+category.getName());
            category=this.productCategoriesSerVice.SetCreateNewCategory(category);
        }else
        {
            redirectAttributes.addFlashAttribute("Message","Updated Successfully Category "+category.getName());
            ProductCategory productCategory=this.productCategoriesSerVice.FindById(category.getId(),null);
            category=this.productCategoriesSerVice.SetEditCategory(productCategory,category);
        }
        this.productCategoriesSerVice.save(category);
        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String DeleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            this.productCategoriesSerVice.DeleteCategory(id);
            redirectAttributes.addFlashAttribute("Message", "Delete Successfully Category With Id " + id);
        } catch (ProductCategoryException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (ProductException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/category";
    }
    @GetMapping("/category/restore/{id}")
    public String restoreCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            this.productCategoriesSerVice.restoreCategory(id);
            redirectAttributes.addFlashAttribute("Message", "Restore Successful Category With Id " + id);
        } catch (ProductCategoryException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/category";
    }

    @GetMapping("/category/edit/{id}")
    public String update(@PathVariable Integer id, Model model,RedirectAttributes redirectAttributes) {
        try {
            ProductCategory Category=this.productCategoriesSerVice.FindById(id,true);
            model.addAttribute("pageTitle","Category");
            model.addAttribute("TitleForm", "Edit Category");
            model.addAttribute("category",Category);
            return "category/add-category-form";
        } catch (ProductCategoryException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category With Id " + id + " Not Found");
        }
        return "redirect:/category";
    }
}