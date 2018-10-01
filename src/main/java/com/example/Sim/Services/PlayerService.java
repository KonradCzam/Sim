package com.example.Sim.Services;

import com.example.Sim.Model.Item;
import com.example.Sim.Model.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerService {
    private Player player = new Player();

    public void addItemToInvetory(Item item) {
        player.getInventory().add(item);
    }

    public void removeItemFromInvetory(Item item) {
        player.getInventory().remove(item);
    }
}
