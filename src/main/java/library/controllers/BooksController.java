package library.controllers;

import library.dao.BookDAO;
import library.dao.PersonDAO;
import library.models.Book;
import library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", bookDAO.index());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookDAO.show(id));
        model.addAttribute("person", bookDAO.showPersonWhoTookTheBook(id));
        model.addAttribute("cleanPerson", new Person());
        model.addAttribute("people", personDAO.index());
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
        bookDAO.create(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") int id, Model model){
        model.addAttribute("book", bookDAO.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/edit";
        }
        bookDAO.edit(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String edit(@PathVariable("id") int id){
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String deleteBookFromPerson(@PathVariable("id") int id){
        bookDAO.deleteBookFromPerson(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBookToPerson(@PathVariable("id") int id, @ModelAttribute("assignedPerson") Person assignedPerson){
        bookDAO.assignBook(id, assignedPerson.getPersonId());
        return "redirect:/books/" + id;
    }
}
