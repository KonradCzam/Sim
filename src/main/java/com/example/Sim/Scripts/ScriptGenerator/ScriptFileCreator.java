package com.example.Sim.Scripts.ScriptGenerator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class ScriptFileCreator {

    public static void saveToXml(Script script){
        try{
            //Create JAXB Context
        JAXBContext jaxbContext = JAXBContext.newInstance(Script.class);

        //Create Marshaller
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        //Required formatting??
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);


        //Write XML to StringWriter
        jaxbMarshaller.marshal(script, new File("product.xml"));


        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
