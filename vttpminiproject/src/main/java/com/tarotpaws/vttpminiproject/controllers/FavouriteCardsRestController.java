package com.tarotpaws.vttpminiproject.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tarotpaws.vttpminiproject.models.FavouriteCard;
import com.tarotpaws.vttpminiproject.models.Tarot;
import com.tarotpaws.vttpminiproject.service.FavouriteCardsService;
import com.tarotpaws.vttpminiproject.service.TarotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class FavouriteCardsRestController {
    @Autowired
    FavouriteCardsService favCardsService;

    @Autowired
    TarotService tarotService;

    @PostMapping("/favourite")
    public ResponseEntity<?> addFavoriteCard(@Valid @RequestBody FavouriteCard favouriteCard) throws IOException {
        try {
            System.out.println(favouriteCard.getCardName().toString());
            List<Tarot> cardsFound = tarotService.searchCardsByName(favouriteCard.getCardName());
            if(cardsFound.size()!=1){
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid cardName");
            }
            favCardsService.addFavoriteCardService(favouriteCard.getUsername().toString(), favouriteCard.getCardName().toString());
            return ResponseEntity.ok("Card added to favourites");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/unfavourite")
    public ResponseEntity<?> deleteFavouriteCard(@Valid @RequestBody FavouriteCard favouriteCard) {
        try {
            String cardNameWithQuotes = "\"" + favouriteCard.getCardName() + "\"";
            boolean isDeleted = favCardsService.deleteFavouriteCard(favouriteCard.getUsername(), cardNameWithQuotes);
            if (isDeleted) {
                return ResponseEntity.ok("Card unfavourited successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found or could not be unfavourited");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/favourite/{userName}")
    public ResponseEntity<List<Tarot>> getFavoriteCards(@PathVariable String userName) {
        List<Tarot> cards = favCardsService.getFavoriteCardsService(userName);
        if (cards == null || cards.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cards);
    }
}
