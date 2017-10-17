# Lethal League Mod Loader
A command line mod loader for Lethal League. Only for visual/audio mods (any mod that does not change game functionality).

**Note:** I don't have a mac, so I can't test for it, or confidently write any information about using the program on mac - there should be mac-equivalent guides for stuff somewhere out there.

**Note:** You must set up the backup/restore point folder. It should be set up like the mods folder, but the only item within the folder should be a resouces folder.

## What the program does
Allows a user to select mod files that they want. The user will add mods to the queue, and then deploy the changes. The program will forget what mods were loaded previously when running the program a second time. All that this program does is overwrite image files that are used in Lethal League.

I'm expecting you to place the backup resources folder in the you designate as the RestoreFolder in the properties file.


## Requirements
1. Java 1.8+ (to run the jar)
2. Steam
3. Lethal League
4. LethalLeagueModLoader.jar and LLML.properties must be in the same folder. Shouldn't matter which folder. As long as the properties file has the correct paths, the program will work.


## How To / Instructions
### What you need from the respoitory
 1. LLML.properties
 1. LethalLeaugeModLoader.jar
 
### Set up the LLML.properties file:  
 1. Open with a text editor, and replace the paths that are there with the paths for your machine. (They are set for the windows default paths for Steam/LethalLeague)

### Have Java in your Path environment vairable  
This step isn't required, but makes the program easier to run from command line.  
(These steps get you to System Properties; this is just the way that I know of getting there.)
  1. Left click windows icon on the left part of the taskbar  
  1. Click on Settings  
  1. Click on System  
  1. Click on About  
  1. Click on System info  
  1. Click on Advanced system settings (on the left of the pop-up window)  
  1. Click on the Environment Variables button at the bottom of the second pop-up window  
  1. Under System variables, double-click on 'Path'  
  1. Click on the 'New' button  
  1. Type (or paste in) the path to the bin folder of java.  
  (Probably going to be something like: C:\Program Files\Java\jre1.8.0_144\bin )  
  1. Click on the OK button  
  1. Click on the OK button (again)

### Running the program
Ideally, you added Java to the path. Regardless, the jar should have an icon that looks like a coffee cup.  
 - **IF** you added Java to the path **AND** have set up the properties file with the correct paths, then you should be able to just double click the jar file.
 - **IF** you did not add Java to the path, you will have to run the command yourself:
   1. Open Command Prompt (WIN + R and then "cmd")
   1. In command prompt, naviagte to the folder that contains the jar file. (cd {next folder};  You can use 'dir' to look at what is in the folder)
   1. In command prompt, type "{path to java in the bin folder} -jar LethalLeagueModLoader.jar j" (Replace {} with your path, the j is an argument that tells the program to operate a certain way. It doesn't matter what it is, just that it is there.)
   From there, the program will telll you what to do.
 
 ### Adding to the Mods Folder
 Using NeonUI as an example, this is what the folder structure should look like:  
 {ModFolder}/NeonUI/resources/{mod files}  
 Each mod should have its own resources folder, which should share the structure of the resources folder in the Lethal League folder - In other words, the mod's resources folder has the images hidden away in more folders.

Using Invisible Ball as another example,
The Mods Folder would look like this:

<html>
  <table>
    <tr>
      <td>{Mod Folder}/</td>
      <td>NeonUI</td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td></td>
      <td>Invisible Ball/</td>
      <td>resoruces/</td>
      <td>graphics/</td>
      <td>palettes/</td>
      <td>ball/</td>
      <td>0.png</td>
    </tr>
    <tr>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td>1.png</td>
    </tr>
    <tr>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td></td>
      <td>etc..</td>
    </tr>
  </table>
</html>
