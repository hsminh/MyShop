package com.example.myshopdaknong.Controller.productCategory;

import com.example.myshopdaknong.Entity.Product_categories;
import com.example.myshopdaknong.Entity.Users;
import com.example.myshopdaknong.Exception.Product_categoriesNotFoundException;
import com.example.myshopdaknong.Service.ProductCategoriesSerVice;
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
    public String ListProduct(Model model) {
        model.addAttribute("pageTitle","Category");
        model.addAttribute("ListCate",this.productCategoriesSerVice.FindAll());
        return "category/category";
    }
    @GetMapping("/category/add")
    public String FormAddCategory(Model model) {
        model.addAttribute("pageTitle","Category");
        model.addAttribute("TitleForm", "Add Category");
        model.addAttribute("isNewUser", true);
        model.addAttribute("Category",new Product_categories());
        return "category/add-form";
    }
    public Product_categories SetEditCategory(Product_categories productCategories,Product_categories EditCategory)
    {
        productCategories.setName(EditCategory.getName());
        productCategories.setSlug(EditCategory.getSlug());
        productCategories.setDescription(EditCategory.getDescription());
        productCategories.setUpdatedAt(new Date());
        return productCategories;
    }
    public Product_categories SetCreateNewCategory(Product_categories productCategories)
    {
        productCategories.setCreatedAt(new Date());
        return productCategories;
    }
    @PostMapping("/category/save")
    public String saveCategory(Product_categories productCategories,Model model) throws Product_categoriesNotFoundException {
        if(productCategories.getId()==null||productCategories.getId()==0)
        {
            System.out.println("come this");
            productCategories=this.SetCreateNewCategory(productCategories);
        }else
        {
            System.out.println("come thiss");
            Product_categories productCategory=this.productCategoriesSerVice.FindById(productCategories.getId());
            productCategories=this.SetEditCategory(productCategory,productCategories);
        }
        this.productCategoriesSerVice.save(productCategories);
        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String DeleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            this.productCategoriesSerVice.DeleteCategory(id);
            redirectAttributes.addFlashAttribute("Message", "Delete Successful User With Id " + id);
        } catch (Product_categoriesNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category With Id " + id + " Not Found");
        }
        return "redirect:/category";
    }

    @GetMapping("/category/edit/{id}")
    public String update(@PathVariable Integer id, Model model,RedirectAttributes redirectAttributes) {
        try {
            Product_categories Category=this.productCategoriesSerVice.FindById(id);
            model.addAttribute("pageTitle","Category");
            model.addAttribute("TitleForm", "Edit Category");
            model.addAttribute("Category",Category);
            return "category/add-form";
        } catch (Product_categoriesNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Category With Id " + id + " Not Found");
        }
        return "redirect:/category";
    }
}
