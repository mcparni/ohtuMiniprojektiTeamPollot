package tip.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tip.domain.Tip;
import tip.service.validators.BlogpostValidator;

@Controller
public class BlogpostController extends SuperController {

    @Autowired
    BlogpostValidator blogpostValidator;

    @GetMapping("/blogpost")
    public String addForm(Model model) {
        return "addBlogpost";
    }

    @PostMapping("/newBlogpost")
    public String addBlogpost(
            @RequestParam String name,
            @RequestParam String artist,
            @RequestParam String url,         
            @RequestParam String date,
            @RequestParam String kommentti,
            RedirectAttributes attributes) {
        
        if (artist == null || artist.trim().isEmpty()) {
            artist = "tuntematon";
        }
        List<String> errors = super.tipNameIsUnique(name);

        Tip tip = new Tip(name, "blogpost");
        tip.setRead(false);

        super.makeDetail(url, "url", tip);
        super.makeDetail(artist, "artist", tip);
        super.makeDetail(date, "date", tip);
        super.makeDetail(kommentti, "kommentti", tip);

        errors.addAll(blogpostValidator.validate(tip));
        super.saveTip(errors, tip, attributes, DEFAUL_ADD_SUCC_MSG);

        return "redirect:/";
    }

    @PostMapping("/blogpost/{tipId}")
    public String editBlogpost(Model model, 
            @PathVariable Long tipId, 
            @RequestParam String artist,
            @RequestParam int read, 
            @RequestParam String name, 
            @RequestParam String url, 
            @RequestParam String date,
            @RequestParam String comment,
            RedirectAttributes attributes) {

        Tip tip = tipRepository.findOne(tipId);
        tip.setName(name);
        
        List<String> errors = new ArrayList<>();
        setTipRead(tip, read);
        errors.addAll(handleDetail(url, "url", tip, blogpostValidator.getNotNullDetailKeys()));
        errors.addAll(handleDetail(artist, "artist", tip, blogpostValidator.getNotNullDetailKeys()));
        errors.addAll(handleDetail(date, "date", tip, blogpostValidator.getNotNullDetailKeys()));
        errors.addAll(handleDetail(comment, "kommentti", tip, blogpostValidator.getNotNullDetailKeys()));
        errors.addAll(blogpostValidator.validate(tip));

        if (saveTip(errors, tip, attributes, DEFAUL_MODE_SUCC_MSG)) {
            return "redirect:/";
        }
        return "redirect:/blogpost/" + tipId;

    }

}
