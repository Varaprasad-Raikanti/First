package Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import TestLib.Driver;


public class BackupUtils {
	private static String backUppath = "Regression Results Backup";
	private static String resultsPath = "Regression Results";
	private static int numberOfDaysToConsiderForFileDeletion = 30;
	private static final String RegressionHistoryDir = "Regression Results";
	private static List<String> executionDataDirectories = Arrays.asList("TestLogs/logs", "TestLogs/Compare Results", "TestLogs/screenShots", "test-output", "TestLogs/videos", "TestLogs/buildlogs", "TestLogs/Listener txt files", "TestLogs/IETraceLogs", "TestLogs/DebugLogs");

	public static void cleanAutomationEnvironment() {
		executionDataDirectories.forEach(eachDirectory -> deleteFilesInDirectory(new File(eachDirectory)));
	}

	private static void pack(String sourceDirPath, String zipFilePath) throws IOException {
		File file = new File(resultsPath);
		if (file.exists()) {
			Path p = Files.createFile(Paths.get(zipFilePath));
			try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
				Path pp = Paths.get(sourceDirPath);
				Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
					ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
					try {
						zs.putNextEntry(zipEntry);
						Files.copy(path, zs);
						zs.closeEntry();
					} catch (IOException e) {
						Driver.getLogger().error(e);
					}
				});
			}
			deleteFilesInDirectory(new File(resultsPath));
			Files.delete(Paths.get(resultsPath));
		}
	}

	private static void regularBackup(String backupFile) {
		try {
			Files.createDirectories(Paths.get(backUppath));
			pack(resultsPath, backupFile);
			deleteOlderFiles();
		} catch (IOException e) {
			Driver.getLogger().error(e);
		}
	}

	private static void deleteOlderFiles() {
		File dir = new File(backUppath);
		if (dir.exists()) {
			for (File file : dir.listFiles()) {
				deleteOldFileByNdays(file, numberOfDaysToConsiderForFileDeletion);
			}
		}
	}

	private static void deleteOldFileByNdays(File file, int numberOfDays) {
		java.util.Calendar time = java.util.Calendar.getInstance();
		time.add(java.util.Calendar.DAY_OF_YEAR, -numberOfDays);
		Date lastModified = new Date(file.lastModified());
		if (lastModified.before(time.getTime())) {
			try {
				file.delete();
			} catch (Exception e) {
				Driver.getLogger().error("Unable to delete the file: " + e.getMessage());
			}
		}
	}

	private static void deleteFilesInDirectory(File directoryPath) {
		try {
			FileUtils.cleanDirectory(directoryPath);
		} catch (IOException e) {
			Driver.getLogger().error("Unable to delete files in directory. Continuing.. \n Error: ");
		}
	}

	public static void runRegressionBackup() throws IOException {
		String sFolderName = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
		String sTime = new SimpleDateFormat("HH_mm_ss").format(new Date());
		Path destDir = Paths.get(RegressionHistoryDir, sFolderName, sTime);
		Files.createDirectories(destDir);
		executionDataDirectories.forEach(eachDirectory -> copyFoldersWithFiles(eachDirectory, destDir.toString()));
		Path failedTestNG = Paths.get("FailedTestNG.xml");
		if (failedTestNG.toFile().exists())
			Files.copy(failedTestNG, destDir.resolve(failedTestNG.getFileName()));
		regularBackup(backUppath + File.separator + sFolderName + " " + sTime + ".zip");

	}

	public static void copyFoldersWithFiles(String sSourceFolder, String sDescFolder) {
		File srcFolder = new File(sSourceFolder);
		String destFolderName = Paths.get(sSourceFolder).getFileName().toString();
		if (srcFolder.exists()) {
			try {
				FileUtils.copyDirectory(srcFolder, Paths.get(sDescFolder, destFolderName).toFile(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		cleanAutomationEnvironment();
	}
}
