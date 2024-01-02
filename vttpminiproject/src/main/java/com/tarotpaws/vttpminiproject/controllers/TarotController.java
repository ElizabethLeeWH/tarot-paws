package com.tarotpaws.vttpminiproject.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.tarotpaws.vttpminiproject.models.FavouriteCard;
import com.tarotpaws.vttpminiproject.models.Tarot;
import com.tarotpaws.vttpminiproject.models.User;
import com.tarotpaws.vttpminiproject.service.AuthenticationService;
import com.tarotpaws.vttpminiproject.service.FavouriteCardsService;
import com.tarotpaws.vttpminiproject.service.TarotService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class TarotController {
    @Autowired
    TarotService tarotService;

    @Autowired
    AuthenticationService authService;

    @Autowired
    FavouriteCardsService favCardsService;

    @GetMapping("/threecards")
    public ModelAndView getThreeTarotCards(HttpSession sess) throws IOException{
        ModelAndView mav = new ModelAndView();
        Integer numOfCardsDrawn = 3;
        List<Tarot> tarotCards = tarotService.getRandCards(numOfCardsDrawn);
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        mav.setStatus(HttpStatus.OK);
        mav.addObject("tarotCards", tarotCards);
        mav.setViewName("threecards");
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView searchCards(@RequestParam String name, HttpSession sess) throws IOException {
        ModelAndView mav = new ModelAndView();
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        List<Tarot> searchResults = tarotService.searchCardsByName(name);
        mav.setStatus(HttpStatus.OK);
        mav.addObject("searchResults", searchResults);
        mav.setViewName("search");
        return mav;
    }

    @GetMapping(path="/")
    public ModelAndView getLogin(HttpSession sess){
        ModelAndView mav= new ModelAndView();
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        mav.setViewName("index");
        return mav;
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute("user") User user, Model model, HttpSession sess) {
        boolean isAuthenticated = authService.authenticate(user.getUsername(), user.getPassword());
        if (isAuthenticated) {
            sess.setAttribute("user", user);
            sess.setMaxInactiveInterval(60 * 60);
            model.addAttribute("username", user.getUsername());
            return "redirect:/";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "index";
        }
    }

    @PostMapping("/signout")
    public String signOut(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/favourite")
    public ModelAndView getFavCard(HttpSession sess, RedirectAttributes redirectAttributes) throws IOException {
        ModelAndView mav = new ModelAndView();
        User user = (User) sess.getAttribute("user");
        if (user != null) {
            // User is logged in
            List<Tarot> allCards = tarotService.getAllCards();
            mav.addObject("cards", allCards);
            List<Tarot> userFavouriteCards = favCardsService.getFavoriteCardsService(user.getUsername());
            mav.addObject("username", user.getUsername());
            mav.addObject("favouriteCards", userFavouriteCards);
        } else {
            // User is not logged in
            RedirectView redirectView = new RedirectView("/");
            redirectView.setExposeModelAttributes(false);
            mav.setView(redirectView);
            redirectAttributes.addFlashAttribute("alert", "Please log in to access favourites.");
            return mav;
        }
        mav.addObject("favouriteCard", new FavouriteCard());
        mav.setViewName("favourite");
        return mav;
    }

    @PostMapping("/favourite")
    public String postFavCard(@ModelAttribute FavouriteCard favouriteCard, RedirectAttributes redirectAttributes){

        try {
            favCardsService.addFavoriteCardService(favouriteCard.getUsername(), favouriteCard.getCardName());
            redirectAttributes.addFlashAttribute("message", "Card favourited");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Card not favourited: " + e.getMessage());
        }
        return "redirect:/favourite";
    }

    @PostMapping("/unfavourite")
    public String deleteFavouriteCard(@ModelAttribute FavouriteCard favouriteCard, RedirectAttributes redirectAttributes) {
        try {
            boolean isDeleted = favCardsService.deleteFavouriteCard(favouriteCard.getUsername(), favouriteCard.getCardName());
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("message", "Card unfavourited successfully");
            } else {
                redirectAttributes.addFlashAttribute("message", "Card failed to be unfavourited");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error processing request: " + e.getMessage());
        }
        return "redirect:/favourite";
    }

    @GetMapping("/allcardsmeaning")
    public ModelAndView allCardsMeaning(HttpSession sess) throws IOException{
        ModelAndView mav = new ModelAndView();
        List<Tarot> allTarotCards = tarotService.getAllCards();
        User user = (User) sess.getAttribute("user");
        if(user == null){
            user = new User();
        }
        mav.addObject("user", user);
        mav.addObject("allTarotCards", allTarotCards);
        mav.setViewName("allcardsmeaning");
        return mav;
    }
}
