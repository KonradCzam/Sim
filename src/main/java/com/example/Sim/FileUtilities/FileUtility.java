package com.example.Sim.FileUtilities;

import com.example.Sim.Model.TableGirl;
import com.example.Sim.Model.GirlsLists;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Setter
public class FileUtility {
    public void openImage() {

    }
    @Value( "${girls.directory:./New folder/}" )
    private String directory  ;

    public GirlsLists getGirlList( ){
        String girlName;
        String folderPresent;

        File dir = new File(directory);
        String[] files = dir.list();
        List<TableGirl> normalTableGirls = new ArrayList<TableGirl>();
        List<TableGirl> randomTableGirls = new ArrayList<TableGirl>();
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].endsWith(".girlsx")) {
                girlName = files[i].substring(0, files[i].length() - 7);
                if (contains(files,girlName,false))
                    folderPresent = "Present";
                else
                    folderPresent = "Missing";
                normalTableGirls.add(new TableGirl(null,girlName,folderPresent));
            }
            if(files[i].endsWith(".rgirlsx")) {
                girlName = files[i].substring(0, files[i].length() - 8);

                if (contains(files,girlName,true))
                    folderPresent = "Present";
                else
                    folderPresent = "Missing";
                randomTableGirls.add(new TableGirl(girlName,null,folderPresent));
            }
        }
        GirlsLists girlsLists = new GirlsLists(normalTableGirls, randomTableGirls);
        return girlsLists;
    }
    public boolean removeFile(String filename){
        File dir = new File(directory+filename);
        return  dir.delete();
    }
    private boolean contains( String[] array,  String v,Boolean random) {
        if (random) {
            for (String e : array)
                if (v != null && v.startsWith(e))
                    return true;

            return false;
        } else{
            for (String e : array)
                if (v != null && v.endsWith(e))
                    return true;
            return false;
        }
    }
}