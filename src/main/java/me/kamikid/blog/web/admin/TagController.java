package me.kamikid.blog.web.admin;

import me.kamikid.blog.entity.Tag;
import me.kamikid.blog.service.TagService;
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
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tag")
    public String tags(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model){
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tag";
    }

    @GetMapping("/tag/edit")
    public String directToEdit(Model model){
        model.addAttribute("tag", new Tag());
        return "admin/edit-tag";
    }

    @PostMapping("/tag")
    public String editTag(Tag tag, RedirectAttributes attributes) {
        Tag check = tagService.getTagByName(tag.getName());
        if (check != null) {
            attributes.addFlashAttribute("message", "Redundant Tag");
            return "redirect:/admin/tag";
        }
        Tag success = tagService.saveTag(tag);
        if (success == null) {
            attributes.addFlashAttribute("message", "Failed to Edit Tag");
        }
        return "redirect:/admin/tag";
    }

    @PostMapping("/tag/{id}")
    public String editTagById(@Valid Tag tag, RedirectAttributes attributes, @PathVariable Long id){
        Tag check = tagService.getTagByName(tag.getName());
        if(check!=null){
            attributes.addFlashAttribute("message","Redundant Tag");
            return "redirect:/admin/tag";
        }
        Tag success;
        if(id != null){
            success = tagService.updateTag(id, tag);
        }else{
            success = tagService.saveTag(tag);
        }
        if(success == null){
            attributes.addFlashAttribute("message", "Failed to Edit Tag");
        }
        return "redirect:/admin/tag";
    }

    @GetMapping("/tag/{id}/edit")
    public String directToEditTag(@PathVariable Long id, Model model){
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/edit-tag";
    }

    @GetMapping("/tag/{id}/delete")
    public String deleteTag(@PathVariable Long id, RedirectAttributes attributes){
        Tag temp = tagService.getTag(id);
        if(temp != null){
            String message = temp.getName() + " - Tag Deleted...";
            attributes.addFlashAttribute("message", message);
            tagService.deleteTag(id);
        }
        return "redirect:/admin/tag";
    }
}
