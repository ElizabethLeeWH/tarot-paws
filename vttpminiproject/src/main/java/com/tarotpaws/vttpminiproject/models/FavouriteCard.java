package com.tarotpaws.vttpminiproject.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavouriteCard {
    @NotEmpty(message = "Username is required")
    @NotNull
    private String username;

    @NotEmpty(message = "Card name is required")
    @NotNull
    private String cardName;
}
