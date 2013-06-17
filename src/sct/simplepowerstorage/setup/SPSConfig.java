/*
 * SimplePowerStorage
 * Copyright (C) 2013  Ryan Cohen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package sct.simplepowerstorage.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import sct.simplepowerstorage.SimplePowerStorage;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class SPSConfig {
	
	public static Property idStart;
	
	public static Property makeshiftBatteryId;
	public static Property conductanceCoilId;
	public static Property makeshiftUpgradeId;
	
	public static File configFolder;
	
	public static void loadCommonConfig(FMLPreInitializationEvent evt)
	{
		Configuration c = new Configuration(evt.getSuggestedConfigurationFile());
		try {
			c.load();
			
			makeshiftBatteryId = c.getBlock("ID.MakeshiftBattery", 2400);
			conductanceCoilId = c.getItem(Configuration.CATEGORY_ITEM, "ID.ConductanceCoil", 23200);
			makeshiftUpgradeId = c.getItem(Configuration.CATEGORY_ITEM, "ID.MakeshiftUpgrade", 23201);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			c.save();
		}
	}
	
	public static String getConfigBaseFolder()
	{
		return "sct";
	}
	
	public static void setConfigFolderBase(File folder)
    {
        configFolder = new File(folder.getAbsolutePath() + "/" + getConfigBaseFolder() + "/"
                + SimplePowerStorage.modId + "/");
    }
	
	public static void extractLang(String[] languages)
    {
        String langResourceBase = "/sct/" + SimplePowerStorage.modId + "/lang/";
        for (String lang : languages)
        {
            InputStream is = SimplePowerStorage.instance.getClass().getResourceAsStream(langResourceBase + lang + ".lang");
            try
            {
                File f = new File(configFolder.getAbsolutePath() + "/lang/"
                        + lang + ".lang");
                if (!f.exists())
                    f.getParentFile().mkdirs();
                OutputStream os = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = is.read(buffer)) != -1)
                {
                    os.write(buffer, 0, read);
                }
                is.close();
                os.flush();
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
	
	public static void loadLang()
    {
        File f = new File(configFolder.getAbsolutePath() + "/lang/");
        for (File langFile : f.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".lang");
            }
        }))
        {
            try
            {
                Properties langPack = new Properties();
                langPack.load(new FileInputStream(langFile));
                String lang = langFile.getName().replace(".lang", "");
                LanguageRegistry.instance().addStringLocalization(langPack,
                        lang);
            }
            catch (FileNotFoundException x)
            {
                x.printStackTrace();
            }
            catch (IOException x)
            {
                x.printStackTrace();
            }
        }
    }

}
