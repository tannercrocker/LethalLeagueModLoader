package LethalLeagueModLoader;

import java.io.File;

public class LLMod {
    File dir;
    String name;

    LLMod(File f){
        this.dir = f;
        this.name = f.getName();
    }
    LLMod(String fileName){
        this(new File(fileName));
    }

    @Override
    public String toString() {
        return name;
    }
}
