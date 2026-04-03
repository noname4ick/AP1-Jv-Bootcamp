package com.rogue.data.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rogue.data.save.SaveData;

import java.io.File;
import java.io.IOException;

public class SaveRepository {

    private static final String FILE;

    static {
        FILE = "save.json";
    }

    private final ObjectMapper mapper =
            new ObjectMapper();

    public void save(SaveData data){

        try{

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(FILE),
                            data);

        }catch(IOException e){

            e.printStackTrace();
        }
    }

    public SaveData load(){

        try{

            File file =
                    new File(FILE);

            if(!file.exists()){

                return new SaveData();
            }

            return mapper.readValue(
                    file,
                    SaveData.class);

        }catch(Exception e){

            return new SaveData();
        }
    }

}