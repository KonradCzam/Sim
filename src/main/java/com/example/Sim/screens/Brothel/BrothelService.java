package com.example.Sim.screens.Brothel;

import com.example.Sim.screens.Brothel.model.BrothelTableGirl;
import com.example.Sim.Model.Girl;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BrothelService {
    List<Girl> brothelGirls = new ArrayList<Girl>();
    List<BrothelTableGirl> brothelTableGirlsList = new ArrayList<>();

    public void addGirlToBrothel(Girl girl){
        brothelGirls.add(girl);
    }
    public void removeGirlFromBrothel(Girl girl){
        brothelGirls.remove(girl);
    }
    public List<BrothelTableGirl> addGirlToBrothelAndRefresh(Girl girl){
        addGirlToBrothel(girl);
        return getBrothelTableGirls();
    }
    public List<BrothelTableGirl> removeGirlFromBrothelAndRefresh(Girl girl){
        removeGirlFromBrothel(girl);
        return getBrothelTableGirls();
    }
    public List<BrothelTableGirl>  getBrothelTableGirls(){

        for (Girl girl:brothelGirls
             ) {
            brothelTableGirlsList.add(new BrothelTableGirl(girl));
        }
        return brothelTableGirlsList;
    }
}
