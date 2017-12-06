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
import tip.service.validators.PodcastValidator;

@Controller
public class PodcastController extends SuperController {

    @Autowired
    PodcastValidator podcastValidator;

    @GetMapping("/podcast")
    public String addForm(Model model) {
        return "addPodcast";
    }

    @PostMapping("/newPodcast")
    public String addPodcast(
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

        Tip tip = new Tip(name, "podcast");
        tip.setRead(false);

        super.makeDetail(url, "url", tip);
        super.makeDetail(artist, "artist", tip);
        super.makeDetail(date, "date", tip);
        super.makeDetail(kommentti, "kommentti", tip);

        errors.addAll(podcastValidator.validate(tip));
        super.saveTip(errors, tip, attributes, DEFAUL_ADD_SUCC_MSG);

        return "redirect:/";
    }

    @PostMapping("/podcast/{tipId}")
    public String editPodcast(Model model,
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
        errors.addAll(handleDetail(url, "url", tip, podcastValidator.getNotNullDetailKeys()));
        errors.addAll(handleDetail(artist, "artist", tip, podcastValidator.getNotNullDetailKeys()));
        errors.addAll(handleDetail(date, "date", tip, podcastValidator.getNotNullDetailKeys()));
        errors.addAll(handleDetail(comment, "kommentti", tip, podcastValidator.getNotNullDetailKeys()));
        errors.addAll(podcastValidator.validate(tip));
        if (saveTip(errors, tip, attributes, DEFAUL_MODE_SUCC_MSG)) {
            return "redirect:/";
        }
        return "redirect:/podcast/" + tipId;

    }

}
