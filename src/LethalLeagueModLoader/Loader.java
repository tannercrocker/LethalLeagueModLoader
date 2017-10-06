package LethalLeagueModLoader;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

/**
 * LLML is originally created by Tanner Crocker, for the Lethal League community.
 *
 * Intended use: to easily add/remove community-created, graphical mods
 *      for the video game 'Lethal League', created by Team Reptile.
 *
 * Lethal League website: http://lethalleague.team-reptile.com/
 *
 * It requires a Java runtime environment. (Java SE)
 *
 */

public class Loader {
    final private static double VERSION = 1.0;
    final private static String disclaimer =
            "\nThanks for using LLML v" + VERSION + "! Please read the disclaimer before use." +
            "\n\n*************************** DISCLAIMER ***************************" +
            "\n\tThis program is considered a service to the Lethal" +
            "\n\tLeague community. Any unintended use is due to" +
            "\n\timproper use of the program. The author of this" +
            "\n\tprogram claims no responsibility or liability to" +
            "\n\tany repercussions resulting from use of this program." +
            "\n\tThis program is made without checking any legal" +
            "\n\tinformation from Team Reptile and any other affiliates." +
            "\n\n\t\tYou use this program at your own risk." +
            "\n******************************************************************" +
            "\nPress enter twice to continue.";

    private static String SteamRoot = "SteamRoot";
    private static String LethalLeagueRoot = "LethalLeagueRoot";
    private static String ModFolder = "ModsFolder";
    private static String RestoreFolder = "RestoreFolder";

    private static ResourceBundle rsc = new ResourceBundle() {
        final String name = "LLML.properties";
        Reader r;
        Properties prop = new Properties();

        {
            try{
                r = new FileReader(name);

                prop.load(r);
            }
            catch (FileNotFoundException f){
                f.printStackTrace();
                System.out.println("'LLML.properties' file seems to be missing.");
                System.exit(1);
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Error with 'LLML.properties'. Please fix.");
                System.exit(1);
            }
        }

        @Override
        protected Object handleGetObject(String key) {
            return prop.getProperty(key);
        }

        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(prop.stringPropertyNames());
        }
    };


    public static void main(String[] args) {
        //Ensure program runs from command line
        if(args.length == 0) {
            try {
                String jarName = Loader.class.getProtectionDomain().getCodeSource().getLocation().toString();
                jarName = jarName.substring(0, jarName.length()-4);
                jarName = jarName.substring(jarName.lastIndexOf('/')+1);

                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    //Runs this command:
                    //  cmd.exe /c cd <Path to Jar> & start cmd.exe /k java -jar <jarName>.jar <some arbitrary cl-arg> & exit
                    Runtime.getRuntime().exec("cmd.exe /c cd \"" + System.getProperty("user.dir") + "\" " +
                            "& start cmd.exe /k \"java -jar " + jarName + ".jar cl & exit\""
                    );
                }else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    //Opens terminal to the jar file
                    //I don't know if this behaves correctly. I don't own a mac.
                    Runtime.getRuntime().exec("/usr/bin/open -a Terminal " +
                            System.getProperty("user.dir") + File.separator + jarName + ".jar"
                    );
                }else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                    JOptionPane.showMessageDialog(null, "Lethal League doesn't run on your OS.");
                }else{
                    JOptionPane.showMessageDialog(null, "Run this program from the command line, with at least one argument.");
                    throw new Exception("Not running from command line, with at least one argument.");
                }
                return;
            } catch (Exception e) {
                return;
            }
        }

        //Print a disclaimer which has to be accepted.
        try {
            System.out.print(disclaimer);
            Scanner s = new Scanner(System.in);
            s.nextLine();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("This program doesn't store any of your information.");
        System.out.println("Please send this information if you experience any strange crashes.");
        System.out.println("----- Potentially Helpful Debugging Info -----");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        printSpecs();
        loadProperties();
        verifyProperties();
        validateProperties();
        System.out.println("------------------- End ----------------------\n\n\n");

        try {
            Menu.run();
        }catch (Exception e){
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    private static void printSpecs() {
        System.out.println("=====================\nENVIRONMENT\n=====================");

        String[] env = new String[]{"OS", "PROCESSOR_ARCHITECTURE", "PROCESSOR_REVISION"};
        for (String e : env) {
            System.out.println(e + ": " + System.getenv(e));
        }

        System.out.println("=====================\nPROPERTIES\n=====================");

        String[] props = new String[]{"java.version", "java.runtime.name", "java.runtime.version",
                                        "java.vm.version", "os.arch", "os.name", "file.separator"};
        for (String p : props) {
            System.out.println(p + ": " + System.getProperty(p));
        }
        System.out.println("=====================");
    }

    private static void loadProperties() {
        try {
            SteamRoot = rsc.getString("SteamRoot");
            LethalLeagueRoot = rsc.getString("LethalLeagueRoot");
            ModFolder = rsc.getString("ModsFolder");
            RestoreFolder = rsc.getString("RestoreFolder");

            System.out.println(
                    "SteamRoot: " + SteamRoot +
                    "\nLethalLeagueRoot: " + LethalLeagueRoot +
                    "\nModFolder: " + ModFolder +
                    "\nRestoreFolder: " + RestoreFolder  );


            if(SteamRoot.contains(",")) {
                SteamRoot = SteamRoot.replace(",", File.separator);
            }
            if(LethalLeagueRoot.contains(",")) {
                LethalLeagueRoot = LethalLeagueRoot.replace(",", File.separator);
            }
            if(ModFolder.contains(",")) {
                ModFolder = ModFolder.replace(",", File.separator);
            }
            if(RestoreFolder.contains(",")) {
                RestoreFolder = RestoreFolder.replace(",", File.separator);
            }


            System.out.println();


            if(LethalLeagueRoot.contains("**Steam**")) {
                LethalLeagueRoot = LethalLeagueRoot.replace("**Steam**", SteamRoot);
            }
            if(ModFolder.contains("**LethalLeague**")) {
                ModFolder = ModFolder.replace("**LethalLeague**", LethalLeagueRoot);
            }
            if(RestoreFolder.contains("**LethalLeague**")) {
                RestoreFolder = RestoreFolder.replace("**LethalLeague**", LethalLeagueRoot);
            }

            System.out.println(
                    "SteamRoot: " + SteamRoot +
                    "\nLethalLeagueRoot: " + LethalLeagueRoot +
                    "\nModFolder: " + ModFolder +
                    "\nRestoreFolder: " + RestoreFolder  );

        }
            catch (MissingResourceException m){
                System.out.println("'LLML.properties' file is missing a property.");
                m.printStackTrace();
            }

            catch (Exception e){
                e.printStackTrace();
                System.out.println("A fatal error occurred during parameter load. Please check the properties file.");
            }
    }

    private static void verifyProperties() {
        try {
            if ("SteamRoot".equals(SteamRoot) ||
                    "LethalLeagueRoot".equals(LethalLeagueRoot) ||
                    "ModFolder".equals(ModFolder) ||
                    "RestoreFolder".equals(RestoreFolder)) {
                if (SteamRoot.isEmpty() ||
                        LethalLeagueRoot.isEmpty() ||
                        ModFolder.isEmpty() ||
                        RestoreFolder.isEmpty()) {
                    System.out.println("Properties file has empty properties.");
                    throw new Exception("The properties file is improperly configured.");
                }

                System.out.println("Properties file is improperly configured.");
                throw new Exception("The properties file is improperly configured.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("A fatal error occurred during verification. Please check the properties file.");
            System.exit(1);
        }


    }

    private static void validateProperties(){
        if(!SteamRoot.toLowerCase().contains("steam")){
            System.out.println("The Steam folder path does not contain 'Steam'.\nI assume that this incorrect. Please fix the properties file.");
            System.exit(1);
        }

        try{
            String[] p = new String[]{SteamRoot, LethalLeagueRoot, ModFolder, RestoreFolder};
            File f;
            boolean b = false;
            for (int i = 0; i < p.length; i++) {
                f = new File(p[i]);

                if (!f.exists() && i < 2) {
                    System.out.println("The path specified in the properties file does not exist: '" + p[i] + "'");
                    b = true;
                }else if(!b && !f.exists() && i < 4){
                    f = new File(p[i] + File.separator + ".");
                    if(!f.mkdirs()){
                        System.out.println("Please change the property or create this path: '" + p[i] + "'");
                    }
                }
            }

            if(b){
                throw new FileNotFoundException("Bad path specified in properties.");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("A fatal occurred during validation.");
            System.exit(1);
        }
    }

    public static String getSteamRoot(){return SteamRoot;}
    public static String getLethalLeagueRoot(){return LethalLeagueRoot;}
    public static String getModFolder(){return ModFolder;}
    public static String getRestoreFolder(){return RestoreFolder;}
}
