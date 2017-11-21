
package tip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tip.domain.Book;
import tip.repository.BookRepository;

@Controller
public class BookController {
    
    @Autowired
    private BookRepository bookRepository;
    
    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("tips", bookRepository.findAll());
        return "index";
    }
    
    @PostMapping("/")
    public String add(@RequestParam String name, @RequestParam String isbn) {
        Book book = new Book(name, isbn);
        this.bookRepository.save(book);
        return "redirect:/";
    }
}
