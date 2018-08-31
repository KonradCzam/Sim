package com.example.Sim.Girls;

import com.example.Sim.Utilities.FileUtility;
import com.example.Sim.Model.Girl;
import com.example.Sim.screens.Gallery.model.TableGirl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GirlService {

    @Resource
    GirlCreator girlCreator;
    @Resource
    FileUtility fileUtility;


    String [] files ;
    List<Girl> normalGirls = new ArrayList<Girl>();
    List<Girl> randomGirls = new ArrayList<Girl>();

    String girlName;
    String folderPresent;


    public void createGirls( ){
        normalGirls = new ArrayList<Girl>();
        randomGirls = new ArrayList<Girl>();
        files = fileUtility.getFileArray();
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].endsWith(".girlsx")) {
                addToNormalList(files[i]);
            }
            if(files[i].endsWith(".rgirlsx")) {
                addToRandomList(files[i]);
            }
        }
    }
    private void addToNormalList(String filePath){
        girlName = filePath.substring(0, filePath.length() - 7);
        folderPresent = setFolder( files,   girlName, true);
        Girl girl = girlCreator.createGirl(filePath);
        girl.setName(girlName);
        girl.setFolder(folderPresent);
        normalGirls.add(girl);
    }
    private void addToRandomList(String filePath){
        girlName = filePath.substring(0, filePath.length() - 8);
        folderPresent = setFolder( files,   girlName, true);
        Girl rgirl = girlCreator.createRandomGirl( filePath);
        rgirl.setName(girlName);
        rgirl.setFolder(folderPresent);
        randomGirls.add(rgirl);

    }
    public List<TableGirl> getNormalTableGirls(){
        List<TableGirl> tableGirls = new ArrayList<TableGirl>();
        String girlPath;
        for (int i = 0; i < normalGirls.size(); i++){
            girlName = normalGirls.get(i).getName();
            folderPresent = normalGirls.get(i).getFolder();
            girlPath = normalGirls.get(i).getPath();
            TableGirl tableGirl = new TableGirl(girlName,girlPath,folderPresent);
            tableGirls.add(tableGirl);
        }
        return tableGirls;
    }
    public List<TableGirl> getRandomTableGirls(){
        List<TableGirl> tableGirls = new ArrayList<TableGirl>();
        String girlPath;
        for (int i = 0; i < randomGirls.size(); i++){
            girlName = randomGirls.get(i).getName();
            folderPresent = randomGirls.get(i).getFolder();
            girlPath = randomGirls.get(i).getPath();
            TableGirl tableGirl = new TableGirl(girlName,girlPath,folderPresent);
            tableGirls.add(tableGirl);
        }
        return tableGirls;
    }

    private String setFolder(String[] files,  String girlName,Boolean random){
        if (contains(files,girlName,true))
            return  "Present";
        else
            return  "Missing";
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
