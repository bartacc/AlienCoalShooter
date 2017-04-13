package com.gamejam.czest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.gamejam.czest.JamGame;

import java.io.File;
import java.io.IOException;

public class DesktopLauncher {
		private static final String TEXTURE_INPUT = "rawTextures/";
		private static final String TEXTURE_OUTPUT = "assets/";
		private static final String TEXTURE_OUTPUT_NAME = "textures.pack";
		private static final TexturePacker.Settings texturePackerSettings = new TexturePacker.Settings();

		public static void main(String[] arg)
		{
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.height = 600;//768;
			config.width = 1000;//1024;
			//config.fullscreen = true;


			texturePackerSettings.maxWidth = 2048;
			texturePackerSettings.maxHeight = 2048;

			//Here repackage the textures if something has changed
			if(TexturePacker.isModified(TEXTURE_INPUT, TEXTURE_OUTPUT, TEXTURE_OUTPUT_NAME, texturePackerSettings))
			{
				File file = new File("assets/");
				try
				{
					delete(file);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					System.exit(0);
				}

				TexturePacker.process(texturePackerSettings, TEXTURE_INPUT, TEXTURE_OUTPUT, TEXTURE_OUTPUT_NAME);
			}


			new LwjglApplication(new JamGame(), config);
		}

		public static void delete(File file)
            throws IOException
		{

			if(file.isDirectory()){

				//directory is empty, then delete it
				if(file.list().length==0){

					file.delete();
					System.out.println("Directory is deleted : "
							+ file.getAbsolutePath());

				}else{

					//list all the directory contents
					String files[] = file.list();

					for (String temp : files) {
						//construct the file structure
						File fileDelete = new File(file, temp);

						//recursive delete
						delete(fileDelete);
					}

					//check the directory again, if empty then delete it
					if(file.list().length==0){
						file.delete();
						System.out.println("Directory is deleted : "
								+ file.getAbsolutePath());
					}
				}

			}else{
				//if file, then delete it
				file.delete();
				System.out.println("File is deleted : " + file.getAbsolutePath());
			}
		}
}
