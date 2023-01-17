package Utilities;

import java.io.File;

import org.apache.commons.io.FileUtils;

import TestLib.Driver;



public class Deletefiles {

	public static void startingwith(String path,String name)
	{

		//		File directory = new File("C:/Users/yrram/Downloads");
		File directory = new File(path);
		File[] files = directory.listFiles();
		if(files!=null)
		{
			for (File f : files)
			{
				if (f.getName().startsWith(name))
				{
					if(f.delete())
					{
						System.out.println(f.getName()+"  file deleted  "+f.getAbsolutePath());
						Driver.getLogger().info(f.getName()+"  file deleted  "+f.getAbsolutePath());
					}
					else
					{
						System.out.println("Unable to delete file.....");
					}
				}
			}
		}
		/*Driver.logger.info("Below are Files after deletion in directory");
		for (File f : files)
		{
			Driver.logger.info(f.getAbsolutePath());
		}*/
	}

	public static void deleteFiles(String directoryLocation){
		try{
			File dir = new File(directoryLocation);	
			File[] directoryListing = dir.listFiles();
			Driver.getLogger().info("No. of files in the directory Location: "+directoryLocation+" are: "+directoryListing.length);
			for(File file: directoryListing){
				try{
					if(file.isDirectory()){

						FileUtils.deleteDirectory(file);
						Driver.getLogger().info("Successfully deleted filer: "+file);
					}else{
						FileUtils.forceDelete(file); 
					}}catch(Exception e){

					}	
			}	
			Driver.getLogger().info("Successfully deleted files in the folder: "+directoryLocation);

		}catch(Exception  e){
			Driver.getLogger().info("Exception while deleting files in the folder: "+directoryLocation);
			e.printStackTrace();
		}
	}

	//Do Not Delete this main method.. It is used to delte temp files in the machine
	public static void main(String[] args){
		Deletefiles.deleteFiles(System.getProperty("java.io.tmpdir"));
	}
}
