package LethalLeagueModLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Menu {
    final private static Scanner scan = new Scanner(System.in);
    private static Queue<LLMod> modQueue = new LinkedList<>();
    private static LinkedList<LLMod> modList = new LinkedList<>();

    final private static String menu_message =
            "\n-------------- MAIN MENU --------------" +
            "\n  (0) Quit" +
            "\n  (1) Add mod" +
            "\n  (2) Drop mod" +
            "\n  (3) Restore all original files" +
            "\n  (4) View selected mods" +
            "\n  (5) Load mods (overwrites files)" +
            "\n---------------------------------------" +
            "\nPlease enter the action's number: ";

    static void run() throws FileNotFoundException {
        int menu_option;


        do{
            loadModList();
            menu_option = printMenu();
            System.out.println();

            handle_request(menu_option);


            try {
                Thread.sleep(150);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }while(menu_option != Option.QUIT);

        scan.close();
    }

    private static int printMenu(){
        String input = prompt_for(menu_message);

        return process_input(input);
    }

    private static String prompt_for(String msg){
        System.out.print(msg);

        return scan.nextLine();
    }

    private static void loadModList() throws FileNotFoundException {
        File f = new File(Loader.getModFolder());

        if(f.isDirectory()){
            File[] mods = f.listFiles();

            if(mods != null) {
                LLMod mod;
                for (int i = 0; i < mods.length; i++) {
                    mod = new LLMod(mods[i]);
                    if(!containsMod(modList, mod) && mods[i].isDirectory()) {
                        modList.add(new LLMod(mods[i]));
                    }
                }
            }else{
                System.out.println("No mods: " + Loader.getModFolder());
            }

        }else{
            throw new FileNotFoundException("Mod folder is not a directory");
        }
    }

    private static void handle_request(int menu_option) {
        int queue_size;
        switch(menu_option){
            case Option.QUIT:
                System.out.println("Program will exit.");
                break;
            case Option.ADD:
                queue_size = modQueue.size();
                if(modList.size() > 0) {
                    Collection<LLMod> adds = selectModsFromObject(modList);
                    if(adds.size() > 0){
                        modQueue.addAll(adds);
                    }else{
                        System.out.println("No mods selected.");
                    }
                }else{
                    System.out.println("No mods available. Add mods to " + Loader.getModFolder());
                }
                System.out.println((modQueue.size() - queue_size) + " mod(s) added to queue.");
                break;
            case Option.DROP:
                queue_size = modQueue.size();
                if(modQueue.size() > 0) {
                    Collection<LLMod> drops = selectModsFromObject(modQueue);
                    if(drops.size() > 0) {
                        modQueue.removeAll(drops);
                    }else{
                        System.out.println("No mods dropped.");
                    }
                }else{
                    System.out.println("No mods are in the queue.");
                }
                System.out.println((queue_size - modQueue.size()) + " mod(s) removed from queue.");
                break;
            case Option.RESTORE:
                if (restoreOriginalFiles()) {
                    System.out.println("Original files have been restored.");
                }
                break;
            case Option.VIEW:
                printQueue();
                break;
            case Option.LOAD:
                if (executeModQueue()) {
                    System.out.println("Files have been overwritten; why are you still here? Go play!");
                }
                break;
            case -1:
                break;
            default:
                System.out.println("Don't know that action: " + menu_option);
                break;
        }
    }

    private static LinkedList<LLMod> selectModsFromObject(Collection<LLMod> ll) {
        LinkedList<LLMod> selection = new LinkedList<>();
        Object[] mods = ll.toArray();
        StringBuilder sb = new StringBuilder();

        sb.append("\n-------- MOD SELECTION --------");

        for (int i = 0; i < mods.length; i++) {
            sb.append("\n (").append(i).append(") ").append(mods[i].toString());
        }

        sb.append("\n-------------------------------" +
                "\nIf you enter the numbers comma separated, they will all be added in left-to-right order." +
                "\nPlease enter the number of the mod(s):");


        String input = prompt_for(sb.toString()).trim();

        String[] ins = input.split(",");

        for (int i = 0; i < ins.length; i++) {
            if(ins[i].trim().length() > 0) {
                if (parseableInteger(ins[i]) && Integer.parseInt(ins[i]) < mods.length && Integer.parseInt(ins[i]) >= 0) {
                    selection.add((LLMod) mods[Integer.parseInt(ins[i])]);
                } else {
                    System.out.println("Not an available number: " + ins[i]);
                }
            }
        }

        return selection;
    }

    private static boolean executeModQueue() {
        if(modQueue.size() > 0) {
            try {
                Object[] mods = modQueue.toArray();

                File from, to = new File(Loader.getLethalLeagueRoot() + File.separator + "resources");

                for (int i = 0; i < mods.length; i++) {
                    from = new File(((LLMod) mods[i]).dir + File.separator + "resources");

                    overwriteFiles(from, to);
                }
                return true;
            } catch (Exception e) {
                System.out.println("Failed to load mods.");
                e.printStackTrace();
            }
            return false;
        }else{
            System.out.println("There are no mods in the queue.");
            return false;
        }
    }

    private static boolean restoreOriginalFiles() {
        try {
            File src = new File(Loader.getRestoreFolder() + File.separator + "resources");
            File dest = new File(Loader.getLethalLeagueRoot() + File.separator + "resources");

            overwriteFiles(src, dest);
            return true;
        }catch (Exception e){
            System.out.println("Failed to re-load original files.");
            e.printStackTrace();
        }
        return false;
    }

    private static void printQueue() {
        if(modQueue.size() > 0) {
            Object[] q = modQueue.toArray();
            System.out.println("Load order:");
            for (int i = 0; i < q.length; i++) {
                System.out.format("%d %s\n", i, q[i].toString());
            }
        }else{
            System.out.println("There are no mods in the queue.");
        }
    }

    private static int process_input(String input) {
        input = input.toLowerCase();
        if(input.length() == 1){
            if(Character.isDigit(input.charAt(0))){
                int in = Integer.parseInt(input);
                if(in == Option.QUIT){
                    return Option.QUIT;
                }else if(in == Option.ADD){
                    return Option.ADD;
                }else if(in == Option.DROP){
                    return Option.DROP;
                }else if(in == Option.RESTORE){
                    return Option.RESTORE;
                }else if(in == Option.VIEW){
                    return Option.VIEW;
                }else if(in == Option.LOAD){
                    return Option.LOAD;
                }
            }else{
                if("q".equals(input)){
                    return Option.QUIT;
                }else if("a".equals(input)){
                    return Option.ADD;
                }else if("d".equals(input)){
                    return Option.DROP;
                }else if("r".equals(input)){
                    return Option.RESTORE;
                }else if("v".equals(input)){
                    return Option.VIEW;
                }else if("l".equals(input)){
                    return Option.LOAD;
                }
            }

        }else{
            if("quit".equals(input)){
                return Option.QUIT;
            }else if("add".equals(input)){
                return Option.ADD;
            }else if("drop".equals(input)){
                return Option.DROP;
            }else if("restore".equals(input)){
                return Option.RESTORE;
            }else if("view".equals(input)){
                return Option.VIEW;
            }else if("load".equals(input)){
                return Option.LOAD;
            }
        }

        System.out.print("Please enter the indicated number, or the first letter of the first word.");
        return -1;
    }

    private static boolean parseableInteger(String s){
        try {
            Integer.parseInt(s);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private static void overwriteFiles(File src, File dest) {
        if(src.isDirectory()){
            File[] f = src.listFiles();
            if(f != null){
                File newDest;
                for (int i = 0; i < f.length; i++) {
                    src = f[i];
                    newDest = new File(dest.getAbsolutePath() + File.separator + f[i].getName());

                    if(newDest.exists()) {
                        overwriteFiles(src, newDest);
                    }else{
                        if(src.isDirectory()) {
                            String modName = src.getAbsolutePath().substring(0, src.getAbsolutePath().lastIndexOf(File.separator + "resources" + File.separator));
                            modName = modName.substring(modName.lastIndexOf(File.separator)+1);
                            System.out.println("There are options available for: " + modName);
                            System.out.println("\tCheck them out here: " + src.getAbsolutePath());
                        }
                    }
                }
            }
            //else - There are no files to copy in this subtree!
        }else{
            try {
//                System.out.println("WILL COPY:\n" + src.getAbsolutePath() + "\n" + dest.getAbsolutePath());
                Files.copy(
                        src.toPath(),
                        dest.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }catch (IOException io){
                io.printStackTrace();
            }
        }
    }

    private static boolean containsMod(Collection<LLMod> collection, String modName){
        return collection.stream().anyMatch(m -> modName.equals(m.name));
    }
    private static boolean containsMod(Collection<LLMod> collection, LLMod mod){
        return containsMod(collection, mod.name);
    }
}
