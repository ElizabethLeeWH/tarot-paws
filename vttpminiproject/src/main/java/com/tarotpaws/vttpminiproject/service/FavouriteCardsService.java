package com.tarotpaws.vttpminiproject.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tarotpaws.vttpminiproject.models.Tarot;
import com.tarotpaws.vttpminiproject.repos.RedisRepo;

@Service
public class FavouriteCardsService {
    
    @Autowired
    RedisRepo redisRepo;

    @Autowired
    TarotService tService;
    
    public void addFavoriteCardService(String username, String cardName) throws IOException {
        Tarot card = new Tarot();
        List<Tarot> tarots = tService.searchCardsByName(cardName.replace("\"", ""));
        for(Tarot tarot: tarots){
            String normalizedCardName = cardName.toLowerCase().replace("\"", "");
            String normalizedTarotName = tarot.getName().toLowerCase().replace("\"", "");
        
            if (normalizedCardName.equals(normalizedTarotName)) {
                card = tarot;
                break;
            }
        }
        System.out.println(card.getName());

        if (card == null || card.getName() == null) {
            throw new RuntimeException("Card not found.");
        }
        // Check if the card is already a favorite
        List<Tarot> existingCards = redisRepo.getFavouriteCards(username);

        if (existingCards == null) {
            existingCards = new ArrayList<>();
        }
        
        Tarot finalCard = card;
        boolean isDuplicate = existingCards.stream().anyMatch(existingCard -> existingCard.equals(finalCard));
        if (isDuplicate) {
            throw new RuntimeException("Card is already in favorites.");
        }

        existingCards.add(card);
        // Create and save the new favorite card
        redisRepo.setFavouriteCards(username, existingCards);
    }

    public List<Tarot> getFavoriteCardsService(String username) {
        return redisRepo.getFavouriteCards(username);
    }

    public boolean deleteFavouriteCard(String username, String cardName) {
        List<Tarot> favourites = redisRepo.getFavouriteCards(username);
        if (favourites != null) {
            System.out.println("favourites before removal:" + favourites);
            boolean isRemoved = favourites.removeIf(card -> cardName != null && cardName.equalsIgnoreCase(card.getName()));
            if (isRemoved) {
                redisRepo.setFavouriteCards(username, favourites);
            }
            return isRemoved;
        }
        return false;
    }
}
