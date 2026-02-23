package library.controllers;

import jakarta.validation.Valid;
import library.models.Book;
import library.models.Person;
import library.services.BooksService;
import library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") boolean sort_by_year)
    {
        if(page != null && booksPerPage != null){
            if(sort_by_year){
                model.addAttribute("books", booksService.index(page, booksPerPage, true));
            }else {
                model.addAttribute("books", booksService.index(page, booksPerPage));
            }
        } else if (sort_by_year) {
            model.addAttribute("books", booksService.index(true));
        } else {
            model.addAttribute("books", booksService.index());
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.show(id));
        model.addAttribute("person", booksService.findOwnerByBookId(id));
        model.addAttribute("cleanPerson", new Person());
        model.addAttribute("people", peopleService.index());
        return "books/show";
    }

    @GetMapping("/new")
    public String createPage(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.create(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/edit";
        }
        booksService.edit(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String edit(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String deleteBookFromPerson(@PathVariable("id") int id){
        booksService.clearOwnerByBookId(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBookToPerson(@PathVariable("id") int id, @ModelAttribute("assignedPerson") Person assignedPerson){
        booksService.setOwnerByBookId(id, assignedPerson);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("name") String name, Model model){
        model.addAttribute("books", booksService.searchAllBooksByName(name));
        return "books/search";
    }
}