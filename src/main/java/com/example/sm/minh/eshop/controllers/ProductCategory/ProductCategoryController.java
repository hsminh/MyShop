package com.example.sm.minh.eshop.controllers.ProductCategory;

import com.example.sm.minh.eshop.models.ProductCategory;
import com.example.sm.minh.eshop.exceptions.ProductCategoryException;
import com.example.sm.minh.eshop.exceptions.ProductException;
import com.example.sm.minh.eshop.mappers.CategoryRequestMapper;
import com.example.sm.minh.eshop.services.ProductCategoryService;
import com.example.sm.minh.eshop.validators.CategoryRequest;
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
    private ProductCategoryService productCategoriesSerVice;
    @GetMapping("/category")
    public String viewListProduct(@RequestParam(value = "search",required = false) String serachValue,
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
    public String viewAddCategory(Model model) {

        model.addAttribute("categoryRequest",new CategoryRequest());
        model.addAttribute("pageTitle","Category");
        model.addAttribute("TitleForm", "Add Category");
        model.addAttribute("isNewUser", true);
        return "category/add-category-form";
    }


    @PostMapping("/category/save")
    public String saveCategory(@Valid  @ModelAttribute("categoryRequest")  CategoryRequest categoryRequest, BindingResult bindingResult, Model model,RedirectAttributes redirectAttributes) throws ProductCategoryException {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("pageTitle","Category");
            model.addAttribute("TitleForm", "Add Category");
            model.addAttribute("isNewUser", true);
            return "category/add-category-form";
        }
        ProductCategory savedCategory=CategoryRequestMapper.toCategory(categoryRequest);
        if(savedCategory.getId()==null||savedCategory.getId()==0)
        {
            redirectAttributes.addFlashAttribute("Message","Save Successfully Category "+savedCategory.getName());
            savedCategory.setCreatedAt(new Date());
        }else
        {
            redirectAttributes.addFlashAttribute("Message","Updated Successfully Category "+savedCategory.getName());
            ProductCategory productCategory=this.productCategoriesSerVice.findById(savedCategory.getId(),null);
            savedCategory=this.productCategoriesSerVice.setDataForProductCategory(productCategory,savedCategory);
        }
        this.productCategoriesSerVice.save(savedCategory);
        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String DeleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            this.productCategoriesSerVice.deleteCategory(id);
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
            ProductCategory editCategory=this.productCategoriesSerVice.findById(id,true);

            model.addAttribute("pageTitle","Category");
            model.addAttribute("TitleForm", "Edit Category");
            model.addAttribute("categoryRequest",CategoryRequestMapper.toCategoryRequest(editCategory));
            return "category/add-category-form";
        } catch (ProductCategoryException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category With Id " + id + " Not Found");
        }
        return "redirect:/category";
    }
}