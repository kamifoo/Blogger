package me.kamikid.blog.web.admin;

import me.kamikid.blog.entity.Category;
import me.kamikid.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public String categories(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                     Pageable pageable, Model model) {
        model.addAttribute("page", categoryService.listCategory(pageable));
        return "admin/category";
    }

    @GetMapping("/category/edit")
    public String directToEdit(Model model) {
        model.addAttribute("category", new Category());
        return "admin/edit-category";
    }

    @PostMapping("/category")
    public String editCategory(Category category, RedirectAttributes attributes) {
        Category check = categoryService.getCategoryByName(category.getName());
        if (check != null) {
            attributes.addFlashAttribute("message", "Redundant Category");
            return "redirect:/admin/category";
        }
        Category success = categoryService.saveCategory(category);
        if (success == null) {
            attributes.addFlashAttribute("message", "Failed to Create New Category");
        }
        return "redirect:/admin/category";
    }

    @PostMapping("/category/{id}")
    public String editCategoryById(@Valid Category category, RedirectAttributes attributes, @PathVariable Long id) {
        Category check = categoryService.getCategoryByName(category.getName());
        if (check != null) {
            attributes.addFlashAttribute("message", "Redundant Category");
            return "redirect:/admin/category";
        }
        Category success;
        if(id !=null) {
            success  = categoryService.updateCategory(id, category);
        }else{
            success = categoryService.saveCategory(category);
        }
        if (success == null) {
            attributes.addFlashAttribute("message", "Failed to Edit Category");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/category/{id}/edit")
    public String directToEditCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.getCategory(id));
        return "admin/edit-category";
    }

    @GetMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes attributes) {
        Category temp = categoryService.getCategory(id);
        if (temp != null) {
            String message = temp.getName() + " - Category Deleted...";
            attributes.addFlashAttribute("message", message);
            categoryService.deleteCategory(id);
        }
        return "redirect:/admin/category";
    }

}
