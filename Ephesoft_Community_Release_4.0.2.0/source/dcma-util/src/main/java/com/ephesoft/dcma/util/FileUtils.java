/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * This class is a utility file consisting of various APIs related to different functions that can be performed with a file.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class FileUtils implements IUtilCommonConstants {

	/**
	 * Logger for logging the messages.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * DEFAULT_LOCALE {@link Locale} the default locale.
	 */
	private static final Locale DEFAULT_LOCALE = Locale.getDefault();

	/**
	 * BUFFER_SIZE the buffer size.
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * One minute time interval constant
	 */
	private static final long ONE_MINUTE = 60000L;

	/**
	 * Ten Seconds time interval constant
	 */
	private static final long TEN_SECONDS = 10000L;

	/**
	 * Constant for time period of one seconds.
	 */
	private static final int ONE_SECOND = 1000;

	/**
	 * Constant for time period of one minute.
	 */
	private static final long WAITING_TIME = 60000L;
	
	/**
	 * Constant for Zip file retry count.
	 */
	private static final int RETRY_COUNT = 3;

	/**
	 * Constant for Sleep period
	 */
	private static final long SLEEP_PERIOD = 200l;

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 * @return true if successful false other wise.
	 */
	public static boolean deleteDirectoryAndContents(final File srcPath) {

		final String files[] = srcPath.list();

		boolean folderDelete = true;

		if (files != null) {
			for (int index = 0; index < files.length; index++) {

				final String sFilePath = srcPath.getPath() + File.separator + files[index];
				final File fFilePath = new File(sFilePath);
				folderDelete = folderDelete & fFilePath.delete();
			}
		}
		folderDelete = folderDelete & srcPath.delete();
		return folderDelete;
	}

	/**
	 * This method zips the contents of Directory specified into a zip file whose name is provided.
	 * 
	 * @param dir
	 * @param zipfile
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static void zipDirectory(final String dir, final String zipfile, final boolean excludeBatchXml) throws IOException,
			IllegalArgumentException {
		// Check that the directory is a directory, and get its contents
		final File directory = new File(dir);
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("Not a directory:  " + dir);
		}
		final String[] entries = directory.list();
		final byte[] buffer = new byte[4096]; // Create a buffer for copying
		int bytesRead;

		final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));

		for (int index = 0; index < entries.length; index++) {
			if (excludeBatchXml && entries[index].contains(IUtilCommonConstants.BATCH_XML)) {
				continue;
			}
			final File file = new File(directory, entries[index]);
			if (file.isDirectory()) {
				continue;// Ignore directory
			}
			final FileInputStream input = new FileInputStream(file); // Stream to read file
			final ZipEntry entry = new ZipEntry(file.getName()); // Make a ZipEntry
			out.putNextEntry(entry); // Store entry
			bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				out.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
			if (input != null) {
				input.close();
			}
		}
		if (out != null) {
			out.close();
		}
	}

	/**
	 * API to zip list of files to a desired file. Operation aborted if any file is invalid or a directory.
	 * 
	 * @param filePaths {@link List}< {@link String}>
	 * @param outputFilePath {@link String}
	 * @throws IOException
	 */
	public static void zipMultipleFiles(final List<String> filePaths, final String outputFilePath) throws IOException {
		LOGGER.info("Zipping files to " + outputFilePath + ".zip file");
		final File outputFile = new File(outputFilePath);

		if (outputFile.exists()) {
			LOGGER.info(outputFilePath + " file already exists. Deleting existing and creating a new file.");
			outputFile.delete();
		}

		final byte[] buffer = new byte[4096]; // Create a buffer for copying
		int bytesRead;
		ZipOutputStream out = null;
		FileInputStream input = null;
		try {
			out = new ZipOutputStream(new FileOutputStream(outputFilePath));
			for (final String filePath : filePaths) {
				LOGGER.info("Writing file " + filePath + " into zip file.");

				final File file = new File(filePath);
				if (!file.exists() || file.isDirectory()) {
					throw new Exception("Invalid file: " + file.getAbsolutePath()
							+ ". Either file does not exists or it is a directory.");
				}
				input = new FileInputStream(file); // Stream to read file
				final ZipEntry entry = new ZipEntry(file.getName()); // Make a ZipEntry
				out.putNextEntry(entry); // Store entry
				bytesRead = input.read(buffer);
				while (bytesRead != -1) {
					out.write(buffer, 0, bytesRead);
					bytesRead = input.read(buffer);
				}

			}

		} catch (final Exception e) {
			LOGGER.error("Exception occured while zipping file." + e.getMessage(), e);
		} finally {
			if (input != null) {
				input.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 * @return true if successful false other wise.
	 */
	public static boolean deleteDirectoryAndContents(final String sSrcPath) {
		return deleteContents(sSrcPath, true);
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 * @return true if successful false other wise.
	 */
	public static boolean deleteContents(final String sSrcPath, final boolean folderDelete) {
		final File srcPath = new File(sSrcPath);
		boolean returnVal = folderDelete;
		if (null == srcPath || !srcPath.exists()) {
			returnVal = false;
		} else {
			final String files[] = srcPath.list();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					final String sFilePath = srcPath.getPath() + File.separator + files[index];
					final File fFilePath = new File(sFilePath);
					returnVal = returnVal & fFilePath.delete();
				}
				returnVal = returnVal & srcPath.delete();
			}
		}
		return returnVal;
	}

	public static boolean deleteContentsOnly(final String srcPath) {
		boolean folderDelete = true;
		final File srcPathFile = new File(srcPath);
		if (null == srcPathFile || !srcPathFile.exists()) {
			folderDelete = false;
		} else {
			final String files[] = srcPathFile.list();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					final String sFilePath = srcPathFile.getPath() + File.separator + files[index];
					final File fFilePath = new File(sFilePath);
					folderDelete = folderDelete & fFilePath.delete();
				}
			}
		}
		return folderDelete;
	}

	/**
	 * This method copies the src file to dest file.
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(final File srcFile, final File destFile) throws IOException {
		InputStream input = null;
		input = new FileInputStream(srcFile);
		// Check if destination file's parent directory exist, if not, try creating it, if unsuccessful throw an exception.
		File parentFile = destFile.getParentFile();
		if ((null != parentFile) && (!parentFile.exists()) && (!parentFile.mkdirs())) {
			if (null != input) {
				input.close();
			}
			throw new IOException("Destination '" + destFile + "' parent directory cannot be created");
		}
		OutputStream out = null;
		out = new FileOutputStream(destFile);
		final byte[] buf = new byte[1024];
		int len = input.read(buf);
		while (len > 0) {
			out.write(buf, 0, len);
			len = input.read(buf);
		}
		if (input != null) {
			input.close();
		}
		if (out != null) {
			out.close();
		}

	}

	/**
	 * This methods copies a directory with all its files.
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	public static void copyDirectoryWithContents(final File srcPath, final File dstPath) throws IOException {

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			final String[] files = srcPath.list();
			if (files.length > 0) {
				Arrays.sort(files);

				for (int index = 0; index < files.length; index++) {
					copyDirectoryWithContents(new File(srcPath, files[index]), new File(dstPath, files[index]));
				}
			}

		} else {
			if (srcPath.exists()) {
				final InputStream input = new FileInputStream(srcPath);
				final OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				final byte[] buf = new byte[1024];
				int len = input.read(buf);
				while (len > 0) {
					out.write(buf, 0, len);
					len = input.read(buf);
				}
				if (input != null) {
					input.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}

	}

	/**
	 * This methods copies a directory with all its files.
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	public static void copyDirectoryWithContents(final String sSrcPath, final String sDstPath) throws IOException {
		final File srcPath = new File(sSrcPath);
		final File dstPath = new File(sDstPath);

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			final String[] files = srcPath.list();
			if (files.length > 0) {
				// Arrays.sort(files);

				for (int index = 0; index < files.length; index++) {

					copyDirectoryWithContents(new File(srcPath, files[index]), new File(dstPath, files[index]));
				}
			}

		} else {
			if (srcPath.exists()) {
				final InputStream input = new FileInputStream(srcPath);
				final OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				final byte[] buf = new byte[1024];
				int len = input.read(buf);
				while (len > 0) {
					out.write(buf, 0, len);
					len = input.read(buf);
				}
				if (input != null) {
					input.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}

	}

	public static void deleteAllXMLs(final String folderName) {
		final File file = new File(folderName);
		if (file.isDirectory()) {
			final File[] allFiles = file.listFiles();
			for (int index = 0; index < allFiles.length; index++) {
				if (allFiles[index].getName().endsWith(EXTENSION_XML)) {
					allFiles[index].delete();
				}
			}
		}
	}

	public static void deleteAllHocrFiles(final String folderName) {
		final File file = new File(folderName);
		if (file.isDirectory()) {
			final File[] allFiles = file.listFiles();
			for (int index = 0; index < allFiles.length; index++) {
				if (allFiles[index].getName().endsWith(EXTENSION_HTML)) {
					allFiles[index].delete();
				}
			}
		}
	}

	public static void copyAllXMLFiles(final String fromLoc, final String toLoc) {
		final File inputFolder = new File(fromLoc);
		final File outputFolder = new File(toLoc);
		final File[] inputFiles = inputFolder.listFiles();
		for (int index = 0; index < inputFiles.length; index++) {
			if (inputFiles[index].getName().endsWith(EXTENSION_XML)) {
				FileReader input;
				FileWriter out;
				int character;
				try {
					input = new FileReader(inputFiles[index]);
					out = new FileWriter(outputFolder + File.separator + inputFiles[index].getName());
					character = input.read();
					while (character != -1) {
						out.write(character);
						character = input.read();
					}
					if (input != null) {
						input.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (final FileNotFoundException e) {
					LOGGER.error("Exception while reading files:" + e);
				} catch (final IOException e) {
					LOGGER.error("Exception while copying files:" + e);
				}
			}
		}

	}

	public static boolean checkHocrFileExist(final String folderLocation) {
		boolean returnValue = false;
		final File folderLoc = new File(folderLocation);
		final File[] allFiles = folderLoc.listFiles();
		for (int index = 0; index < allFiles.length; index++) {
			if (allFiles[index].getName().endsWith(EXTENSION_HTML)) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	/**
	 * An utility method to update the properties file.
	 * 
	 * @param propertyFile File
	 * @param propertyMap Map<String, String>
	 * @param comments String
	 * @throws IOException If any of the parameter is null or input property file is not found.
	 */
	public static void updateProperty(final File propertyFile, final Map<String, String> propertyMap, final String comments)
			throws IOException {
		if (null == propertyFile || null == propertyMap || propertyMap.isEmpty()) {
			throw new IOException("propertyFile/propertyMap is null or empty.");
		}
		final String commentsToAdd = HASH_STRING + comments;
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		List<String> propertiesToWrite = null;
		try {
			fileInputStream = new FileInputStream(propertyFile);
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			propertiesToWrite = new ArrayList<String>();
			propertiesToWrite.add(commentsToAdd);
			processPropertyFile(propertyMap, bufferedReader, propertiesToWrite);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (final IOException exception) {
					LOGGER.error("Exception occured while closing bufferedReader :" + exception);
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (final IOException exception) {
					LOGGER.error("Exception while closing input stream :" + exception);
				}
			}
		}
		writeToPropertyFile(propertyFile, propertiesToWrite);
	}

	/**
	 * API to write a list of Strings to a property file.
	 * 
	 * @param propertyFile {@link File}
	 * @param propertiesToWrite {@link List}
	 * @throws IOException
	 */
	private static void writeToPropertyFile(final File propertyFile, final List<String> propertiesToWrite) throws IOException {

		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(propertyFile, false);
			bufferedWriter = new BufferedWriter(fileWriter);
			for (final String lineToWrite : propertiesToWrite) {
				bufferedWriter.write(lineToWrite);
				bufferedWriter.newLine();
			}
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (final IOException exception) {
					LOGGER.error("Exception occured while closing bufferedWriter : " + exception);
				}
			}
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (final IOException exception) {
					LOGGER.error("Exception occured while closing fileWriter : " + exception);
				}
			}

		}

	}

	/**
	 * API to process A property file and add all properties along with comment to list.
	 * 
	 * @param propertyMap {@link Map}
	 * @param bufferedReader {@link BufferedReader}
	 * @param propertiesToWrite {@link List}
	 * @throws IOException
	 */
	private static void processPropertyFile(final Map<String, String> propertyMap, final BufferedReader bufferedReader,
			final List<String> propertiesToWrite) throws IOException {
		String strLine = null;
		String key = null;
		String value = null;
		while ((strLine = bufferedReader.readLine()) != null) {
			strLine = strLine.trim();
			if (strLine.startsWith(HASH_STRING)) {
				propertiesToWrite.add(strLine);
			} else {
				final int indexOfDelimeter = strLine.indexOf(EQUAL_TO);
				if (indexOfDelimeter > 0) {
					key = strLine.substring(0, indexOfDelimeter).trim();
					final StringBuilder lineToWrite = new StringBuilder(key);
					lineToWrite.append(EQUAL_TO);
					value = propertyMap.get(key);
					if (value != null) {
						lineToWrite.append(value);
					} else {
						lineToWrite.append(strLine.substring(indexOfDelimeter + 1));
					}
					propertiesToWrite.add(lineToWrite.toString());
				}
			}
		}
	}

	public static String getAbsoluteFilePath(final String pathname) {
		assert pathname != null : "Path name is Null, pathname : " + pathname;
		final File file = new File(pathname);
		return file.getAbsolutePath();
	}

	public static String changeFileExtension(final String fileName, final String extension) {
		LOGGER.info("Changing extension of file " + fileName + " to " + extension);
		final int indexOfDot = fileName.lastIndexOf(IUtilCommonConstants.DOT);
		final String existingFileName = fileName.substring(0, indexOfDot);
		final StringBuilder fileNameBuilder = new StringBuilder();
		fileNameBuilder.append(existingFileName);
		fileNameBuilder.append(IUtilCommonConstants.DOT);
		fileNameBuilder.append(extension);

		return fileNameBuilder.toString();
	}

	/**
	 * This method zips the contents of Directory specified into a zip file whose name is provided.
	 * 
	 * @param dir2zip {@link String} directory to be zipped
	 * @param zout {@link ZipOutputStream} zip output stream for the zip file
	 * @param dir2zipName {@link String} directory name for the zip file
	 * @throws IOException throws {@link IOException} if error occurs while creating zip file
	 */
	public static void zipDirectory(final String dir2zip, final ZipOutputStream zout, final String dir2zipName) throws IOException {
		final File srcDir = new File(dir2zip);
		final List<String> fileList = listDirectory(srcDir);
		for (final String fileName : fileList) {
			final File file = new File(srcDir.getParent(), fileName);
			String zipName = fileName;
			if (File.separatorChar != FORWARD_SLASH) {
				zipName = fileName.replace(File.separatorChar, FORWARD_SLASH);
			}
			zipName = zipName.substring(zipName.indexOf(dir2zipName + BACKWARD_SLASH) + 1 + (dir2zipName + BACKWARD_SLASH).length());

			ZipEntry zipEntry;
			if (file.isFile()) {
				zipEntry = new ZipEntry(zipName);
				zipEntry.setTime(file.lastModified());
				zout.putNextEntry(zipEntry);
				final FileInputStream fin = new FileInputStream(file);
				final byte[] buffer = new byte[4096];
				for (int n; (n = fin.read(buffer)) > 0;) {
					zout.write(buffer, 0, n);
				}
				if (fin != null) {
					fin.close();
				}
			} else {
				zipEntry = new ZipEntry(zipName + FORWARD_SLASH);
				zipEntry.setTime(file.lastModified());
				zout.putNextEntry(zipEntry);
			}
		}
		if (zout != null) {
			zout.close();
		}
	}

	public static List<String> listDirectory(final File directory) throws IOException {
		return listDirectory(directory, true);
	}

	public static List<String> listDirectory(final File directory, final boolean includingDirectory) throws IOException {

		final Stack<String> stack = new Stack<String>();
		final List<String> list = new ArrayList<String>();

		// If it's a file, just return itself
		if (directory.isFile()) {
			if (directory.canRead()) {
				list.add(directory.getName());
			}
		} else {

			// Traverse the directory in width-first manner, no-recursively
			final String root = directory.getParent();
			stack.push(directory.getName());
			while (!stack.empty()) {
				final String current = (String) stack.pop();
				final File curDir = new File(root, current);
				final String[] fileList = curDir.list();
				if (fileList != null) {
					for (final String entry : fileList) {
						final File file = new File(curDir, entry);
						if (file.isFile()) {
							if (file.canRead()) {
								list.add(current + File.separator + entry);
							} else {
								throw new IOException("Can't read file: " + file.getPath());
							}
						} else if (file.isDirectory()) {
							if (includingDirectory) {
								list.add(current + File.separator + entry);
							}
							stack.push(current + File.separator + file.getName());
						} else {
							throw new IOException("Unknown entry: " + file.getPath());
						}
					}
				}
			}
		}
		return list;
	}

	/**
	 * This method deletes a given directory with its content.
	 * 
	 * @param srcPath
	 */
	public static void deleteDirectoryAndContentsRecursive(final File srcPath, final boolean deleteSrcDir) {
		if (srcPath.exists()) {
			final File[] files = srcPath.listFiles();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					if (files[index].isDirectory()) {
						deleteDirectoryAndContentsRecursive(files[index], true);
					}
					files[index].delete();
				}
			}
		}
		if (deleteSrcDir) {
			srcPath.delete();
		}
	}

	public static void deleteDirectoryAndContentsRecursive(final File srcPath) {
		deleteDirectoryAndContentsRecursive(srcPath, true);
	}

	/**
	 * This method unzips a given directory with its content.
	 * 
	 * @param zipFilepath
	 * @param destinationDir
	 */
	public static void unzip(final File zipFile, final String destinationDir) {
		final File destinationFile = new File(destinationDir);
		if (destinationFile.exists()) {
			destinationFile.delete();
		}
		final class Expander extends Expand {

			private static final String UNZIP = "unzip";

			public Expander() {
				super();
				setProject(new Project());
				getProject().init();
				setTaskType(UNZIP);
				setTaskName(UNZIP);
			}
		}
		final Expander expander = new Expander();
		expander.setSrc(zipFile);
		expander.setDest(destinationFile);
		expander.execute();
	}

	public static String getFileNameOfTypeFromFolder(final String dirLocation, final String fileExtOrFolderName) {
		String fileOrFolderName = EMPTY_STRING;
		final File[] listFiles = new File(dirLocation).listFiles();
		if (listFiles != null) {
			for (int index = 0; index < listFiles.length; index++) {
				if (listFiles[index].getName().toLowerCase(Locale.getDefault())
						.indexOf(fileExtOrFolderName.toLowerCase(Locale.getDefault())) > -1) {
					fileOrFolderName = listFiles[index].getPath();
					break;
				}
			}
		}
		return fileOrFolderName;
	}

	public static void createThreadPoolLockFile(final String batchInstanceIdentifier, final String lockFolderPath,
			final String pluginFolderName) throws IOException {
		final File lockFolder = new File(lockFolderPath);
		if (!lockFolder.exists()) {
			lockFolder.mkdir();
		}
		final File _lockFile = new File(lockFolderPath + File.separator + pluginFolderName);
		final boolean isCreateSuccess = _lockFile.createNewFile();
		if (!isCreateSuccess) {
			LOGGER.error("Unable to create lock file for threadpool for pluginName:" + pluginFolderName);
		}
	}

	public static void deleteThreadPoolLockFile(final String batchInstanceIdentifier, final String lockFolderPath,
			final String pluginFolderName) throws IOException {
		final File _lockFile = new File(lockFolderPath + File.separator + pluginFolderName);
		final boolean isDeleteSuccess = _lockFile.delete();
		if (!isDeleteSuccess) {
			LOGGER.error("Unable to delete lock file for threadpool for pluginName:" + pluginFolderName);
		}
	}

	public static boolean moveDirectoryAndContents(final String sourceDirPath, final String destDirPath) {
		boolean success = true;
		final File sourceDir = new File(sourceDirPath);
		final File destDir = new File(destDirPath);
		if (sourceDir.exists() && destDir.exists()) {
			// delete the directory if it already exists
			deleteDirectoryAndContentsRecursive(destDir);
			success = sourceDir.renameTo(destDir);
		}
		return success;
	}

	public static void deleteSelectedFilesFromDirectory(final String directoryPath, final List<String> filesList) {
		final File directory = new File(directoryPath);
		if (directory != null && directory.exists()) {
			for (final File file : directory.listFiles()) {
				if (filesList == null || filesList.isEmpty() || !filesList.contains(file.getName())) {
					file.delete();
				}
			}
		}
	}

	/**
	 * This API is creating file if not exists.
	 * 
	 * @param filePath {@link String}
	 * @return isFileCreated
	 */
	public static boolean createFile(final String filePath) {
		boolean isFileCreated = false;
		final File file = new File(filePath);
		if (file.exists()) {
			isFileCreated = true;
		} else {
			try {
				isFileCreated = file.createNewFile();
			} catch (final IOException e) {
				LOGGER.error("Unable to create file" + e.getMessage(), e);
			}
		}
		return isFileCreated;
	}

	/**
	 * This method append the src file to dest file.
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws Exception
	 */
	public static void appendFile(final File srcFile, final File destFile) throws Exception {
		InputStream in = null;
		in = new FileInputStream(srcFile);
		final FileOutputStream out = new FileOutputStream(destFile, true);
		final byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		if (in != null) {
			in.close();
		}
		if (out != null) {
			out.close();
		}

	}

	/**
	 * This API merging the input files into single output file.
	 * 
	 * @param srcFiles {@link String}
	 * @param destFile {@link String}
	 * @return
	 */
	public static boolean mergeFilesIntoSingleFile(final List<String> srcFiles, final String destFile) throws Exception {
		final boolean isFileMerged = false;
		final File outputFile = new File(destFile);
		for (final String string : srcFiles) {
			final File inputFile = new File(string);
			appendFile(inputFile, outputFile);
		}
		return isFileMerged;
	}

	public static OutputStream getOutputStreamFromZip(final String zipName, final String fileName) throws FileNotFoundException,
			IOException {
		ZipOutputStream stream = null;
		stream = new ZipOutputStream(new FileOutputStream(new File(zipName + ZIP_FILE_EXT)));
		final ZipEntry zipEntry = new ZipEntry(fileName);
		stream.putNextEntry(zipEntry);

		return stream;
	}

	/**
	 * Returns input stream from specified zip file.
	 * 
	 * @param zipName {@link String}
	 * @param fileName {@link String}
	 * 
	 * @return input stream from specified zip file.
	 */
	public static InputStream getInputStreamFromZip(final String zipName, final String fileName) throws FileNotFoundException,
			IOException {
		// Apache Utility is used in case of Unix operating system. In case of
		// all other operating system use Java utility.
		InputStream in = null;

		boolean status = false;
		int retryCount = 1;
		// introducing retry mechanism for zip exception
		while (!status && retryCount <= RETRY_COUNT) {
			try {
				if (OSUtil.isUnix()) {
					final org.apache.tools.zip.ZipFile zipFile = new org.apache.tools.zip.ZipFile(EphesoftStringUtil.concatenate(
							zipName, ZIP_FILE_EXT));
					in = zipFile.getInputStream(zipFile.getEntry(fileName));
				} else {
					final ZipFile zipFile = new ZipFile(EphesoftStringUtil.concatenate(zipName, ZIP_FILE_EXT));
					in = zipFile.getInputStream(zipFile.getEntry(fileName));
				}
				status = true;
			} catch (java.util.zip.ZipException zipException) {
				status = false;
				if (retryCount++ < RETRY_COUNT) {
					LOGGER.info("ZipException encountered during reading the file. Retrying again", zipException);
				} else {
					LOGGER.error(
							"ZipException encountered during reading the file. Even after retrying 3 times error is not rectified. Please restart batch again if it go into error",
							zipException);
				}
				try {
					Thread.sleep(SLEEP_PERIOD);
				} catch (InterruptedException e) {
					LOGGER.error("Thread interrupted while calling sleep method in fileUtil(getInputStreamFromZip)");
				}
			}
		}
		return in;
	}

	public static boolean isZipFileExists(final String zipFilePath) {
		final File f = new File(zipFilePath + ZIP_FILE_EXT);
		return f.exists();
	}

	/**
	 * This method returns the updated file name if file with same already exists in the parent folder.
	 * 
	 * @param fileName {@link String}
	 * @param parentFolder {@link File}
	 * 
	 * 
	 * @return updated file name.
	 */
	public static String getUpdatedFileNameForDuplicateFile(final String fileName, final File parentFolder) {
		String updatedFileName = fileName;
		int fileCount = -1;
		StringBuilder stringBuilder = new StringBuilder();
		boolean fileExists = new File(parentFolder, updatedFileName).exists();
		final int indexOfSeperator = fileName.lastIndexOf(IUtilCommonConstants.DOT);
		final String onlyFileName = fileName.substring(0, indexOfSeperator);
		final String onlyExtension = fileName.substring(indexOfSeperator);
		while (fileExists) {
			fileCount++;
			stringBuilder = new StringBuilder();
			stringBuilder.append(onlyFileName);
			stringBuilder.append(IUtilCommonConstants.UNDER_SCORE);
			stringBuilder.append(fileCount);
			stringBuilder.append(onlyExtension);
			updatedFileName = stringBuilder.toString();
			fileExists = new File(parentFolder, updatedFileName).exists();
		}
		return updatedFileName;
	}

	/**
	 * This method returns the updated file name if file with same already exists in the parent folder.
	 * 
	 * @param fileName {@link String}
	 * @param parentFolder {@link File}
	 * @param fileCount {@link Integer} Specifies the count to be appended in the file name if file with same name already exists.
	 *            (Initially -1 is passed when method is called first time for a file)
	 * @return
	 */
	public static String getUpdatedFileNameForDuplicateFile(final String fileName, final File parentFolder, final int fileCount,
			final String extension) {
		String updatedFileName = null;
		if (fileCount < 0) {
			updatedFileName = fileName;
		} else {
			updatedFileName = fileName + '_' + fileCount;
		}
		if (new File(parentFolder, updatedFileName + extension).exists()) {
			updatedFileName = getUpdatedFileNameForDuplicateFile(fileName, parentFolder, fileCount + 1, extension);
		} else {
			return updatedFileName;
		}
		return updatedFileName;
	}

	/**
	 * This method checks if the file with specific filer name already exists in the the parent folder.
	 * 
	 * @param fileName {@link String} name of the file to be searched. If null returns false.
	 * @param parentFolder {@link File} folder in which file is to be searched. If null or if is not a directory,returns false.
	 * @return true if the file exists, else false.
	 */
	public static boolean isFileExists(final String fileName, final File parentFolder) {
		LOGGER.info("checking if file with name " + fileName + " exists in folder " + parentFolder);
		boolean fileExists = false;
		if (parentFolder != null && parentFolder.isDirectory()) {
			LOGGER.info("Parent folder is a directory.");
			final File[] files = parentFolder.listFiles(new FileExistenceFilter(fileName));
			fileExists = null != files && files.length > 0;
		}
		return fileExists;
	}

	/**
	 * This method checks if the file with specific filer name already exists in the the parent folder.
	 * 
	 * @param fileName {@link String}
	 * @param parentFolder {@link File}
	 * @return
	 */
	// public static boolean isFileExists(String fileName, File parentFolder) {
	// return isFileExists(fileName, parentFolder, false);
	// }

	public static boolean cleanUpDirectory(final File srcPath) {
		boolean isDeleted = true;
		if (srcPath.exists()) {
			final File[] files = srcPath.listFiles();
			if (files != null) {
				for (int index = 0; index < files.length; index++) {
					if (files[index].isDirectory()) {
						isDeleted &= cleanUpDirectory(files[index]);
					}
					isDeleted &= files[index].delete();
				}
			}
		}
		isDeleted &= srcPath.delete();
		return isDeleted;
	}

	public static String createOSIndependentPath(final String path) {

		final StringTokenizer t = new StringTokenizer(path, "/\\");
		final StringBuffer OSIndependentfilePath = new StringBuffer();
		boolean isFirst = true;
		while (t.hasMoreTokens()) {
			if (!isFirst) {
				OSIndependentfilePath.append(File.separator);
			}
			OSIndependentfilePath.append(t.nextToken());
			isFirst = false;
		}
		return OSIndependentfilePath.toString();
	}

	/**
	 * This API moves file from source path to destination path by creating destination path if it does not exists.
	 * 
	 * @param sourcePath source path of file to be moved
	 * @param destinationPath destination path where file has to be moved
	 * @return operation success
	 * @throws Exception error occurred while copying file from source to destination path
	 */
	public static boolean moveFile(final String sourcePath, final String destinationPath) throws Exception {
		boolean success = false;
		if (null != sourcePath && null != destinationPath) {
			final File sourceFile = new File(sourcePath);
			final File destinationFile = new File(destinationPath);

			// Delete the file if already exists
			if (destinationFile.exists()) {
				deleteDirectoryAndContentsRecursive(destinationFile, true);
			}

			// Create directories for destination path
			if (destinationFile.getParentFile() != null) {
				destinationFile.getParentFile().mkdirs();
			}

			// Moving file from source path to destination path
			// REMOVED file.renameTo() implementation as it was environment dependent. And at times does not succeed.
			/*
			 * if (sourceFile.exists() && sourceFile.canWrite()) { success = sourceFile.renameTo(destinationFile); if (success) {
			 * LOGGER.info("Successfully changed the source file at " + sourceFile.getCanonicalPath() + " to " +
			 * destinationFile.getCanonicalPath()); } }
			 */

			// Copy file from source to destination path when the source file cannot be deleted
			if (!success && sourceFile.exists() && sourceFile.canWrite()) {
				LOGGER.info("Copying the folder from " + sourceFile.getCanonicalPath() + " to " + destinationFile.getCanonicalPath());
				if (sourceFile.isDirectory()) {
					copyDirectoryWithContents(sourceFile, destinationFile);
				} else {
					copyFile(sourceFile, destinationFile);
				}

				// Upon successful copy of the file to destination, now deleting it.
				forceDelete(sourceFile);
				success = true;
			}
		}
		return success;
	}

	/**
	 * This method checks whether the given file is a directory or file.
	 * 
	 * @param folderDetail - file to be checked
	 * @return
	 */
	public static boolean checkForFile(final String filePath) {
		boolean isFile = false;
		if (null != filePath) {
			final File file = new File(filePath);
			isFile = !file.isDirectory();
		}
		return isFile;
	}

	/**
	 * Method to replace invalid characters from file name {fileName} by the replace character specified by admin.
	 * 
	 * @param fileName name of the file from which invalid characters are to be replaced.
	 * @param invalidChars array of invalid characters which are to be replaced.
	 * @return
	 */
	public static String replaceInvalidFileChars(final String fileName) {
		String finalReplaceChar = IUtilCommonConstants.UNDER_SCORE;
		final String[] invalidChars = IUtilCommonConstants.INVALID_FILE_EXTENSIONS.split(IUtilCommonConstants.INVALID_CHAR_SEPARATOR);

		LOGGER.info("Entering removeInvalidFileChars method");
		String updatedFileName = fileName;
		if (fileName != null && !fileName.isEmpty() && invalidChars != null && invalidChars.length > 0) {
			if (finalReplaceChar == null || finalReplaceChar.isEmpty()) {
				LOGGER.info("Replace character not specified. Using default character '-' as a replace character.");
				finalReplaceChar = DEFAULT_REPLACE_CHAR;
			}
			for (final String invalidChar : invalidChars) {
				if (finalReplaceChar.equals(invalidChar)) {
					LOGGER.info("Replace character not specified or an invalid character. Using default character '-' as a replace character.");
					finalReplaceChar = DEFAULT_REPLACE_CHAR;
				}
				updatedFileName = updatedFileName.replace(invalidChar, finalReplaceChar);
			}
		}
		LOGGER.info("Exiting removeInvalidFileChars method");
		return updatedFileName;
	}

	/**
	 * API to check whether two file names are equal or not. File extensions are ignored while comparing the file names.
	 * 
	 * @param fileName1 {@link String} file name to be compared
	 * @param fileName2 {@link String} file name to be compared
	 * @return return true if file names are equal, false otherwise;
	 */
	public static boolean compareFileNames(final String fileName1, final String fileName2) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Comaring file names fileName1 = ", fileName1, " fileName2 = ", fileName2));
		boolean isEqual = false;
		if (fileName1 != null || fileName2 != null) {
			int indexOf = fileName1.lastIndexOf(IUtilCommonConstants.DOT);
			indexOf = indexOf == -1 ? fileName1.length() : indexOf;
			final String localOldFileName = fileName1.substring(0, indexOf);
			indexOf = fileName2.lastIndexOf(IUtilCommonConstants.DOT);
			indexOf = indexOf == -1 ? fileName1.length() : indexOf;
			final String localImageName = fileName2.substring(0, indexOf);
			isEqual = localImageName.equalsIgnoreCase(localOldFileName);
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Are file names equal = ", isEqual));
		return isEqual;
	}

	/**
	 * This API is used to validate a file's entension against a set of valid extensions.
	 * 
	 * @param fileName {@link String}
	 * @param supportedExtensions {@link String}[]
	 * @return
	 */
	public static boolean isFileExtensionValid(final String fileName, final String... supportedExtensions) {
		LOGGER.info("Checking validation on file with name " + fileName);
		final String attachmentExtension = fileName.substring(fileName.lastIndexOf(IUtilCommonConstants.DOT) + 1);
		boolean isFileValid = false;
		for (int i = 0; i < supportedExtensions.length; i++) {
			if (attachmentExtension.equalsIgnoreCase(supportedExtensions[i])) {
				isFileValid = true;
				break;
			}
		}
		if (isFileValid) {
			LOGGER.info("File " + fileName + " does not belong to " + supportedExtensions + " file type.");
		} else {
			LOGGER.info("File " + fileName + " is valid ");

		}
		return isFileValid;
	}

	/**
	 * This API is used write the content of an Input Stream to a given file. Method will not close the input stream object. That must
	 * be closed by the calling method.
	 * 
	 * @param saveFile {@link File}
	 * @param inputStream {@link InputStream}
	 * @return Size of buffer
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int writeFileViaStream(final File saveFile, final InputStream inputStream) throws FileNotFoundException, IOException {
		BufferedOutputStream bos = null;
		int ret = 0, count = 0;
		if (saveFile != null && inputStream != null) {
			try {
				bos = new BufferedOutputStream(new FileOutputStream(saveFile));
				final byte[] buff = new byte[2048];
				ret = inputStream.read(buff);
				while (ret > 0) {
					bos.write(buff, 0, ret);
					count += ret;
					ret = inputStream.read(buff);
				}
			} finally {
				try {
					if (bos != null) {
						bos.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (final IOException ioe) {
					LOGGER.error("Error while closing the stream.", ioe);
				}
			}
		}
		return count;
	}

	/**
	 * This API unzips files in specified destination directory. Destination directory is created if it doesn't exist. If destination
	 * directory passed is null, zip will be extracted in its parent directory.
	 * 
	 * @param zipFile {@link File} Zip File to be unzipped.
	 * @param destinationDir {@link String} Directory where file is to be unzipped.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void unzipFiles(final File fSourceZip, final String destinationDir) throws FileNotFoundException, IOException {
		String finalDestinationDir = destinationDir;
		LOGGER.info("Extracting zip file = " + fSourceZip.getAbsolutePath());
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipFile zipFile = null;
		if (null == fSourceZip || !isFileExtensionValid(fSourceZip.getName().toLowerCase(DEFAULT_LOCALE), "zip")) {
			LOGGER.info("File is either null or not a valid zip file. File passed = " + fSourceZip);
		} else {
			// Create destination directory if it doesn't exists
			try {
				if (null == finalDestinationDir) {
					finalDestinationDir = fSourceZip.getParent();
					LOGGER.info("destination Directory null, extracting files to directory = " + finalDestinationDir);
				}
				final File destDir = new File(finalDestinationDir);
				if (!destDir.exists()) {
					destDir.mkdir();
					LOGGER.info("destination directory " + finalDestinationDir + " created");
				}
				zipFile = new ZipFile(fSourceZip);

				// Extract entries from zip file
				final Enumeration<? extends ZipEntry> zipEntry = zipFile.entries();
				while (zipEntry.hasMoreElements()) {

					final ZipEntry entry = (ZipEntry) zipEntry.nextElement();
					if (entry != null) {
						final String outputFileName = entry.getName();

						// if the entry is directory, leave it.
						if (entry.isDirectory() || isFileExtensionValid(outputFileName.toLowerCase(DEFAULT_LOCALE), "zip")) {
							LOGGER.info("Cann't import entry within zip file. Entry is either a directory or a zip file. Entry : "
									+ outputFileName);
							continue;

						} else {
							File destinationFilePath = new File(finalDestinationDir, outputFileName);
							final String newFileName = updateFileName(destinationFilePath.getName(), finalDestinationDir);
							destinationFilePath = new File(finalDestinationDir, newFileName);

							LOGGER.info("Extracting file = " + outputFileName);

							// Get the InputStream for current entry of the zip file using getInputStream(Entry entry) method.
							bis = new BufferedInputStream(zipFile.getInputStream(entry));

							// read the current entry from the zip file, extract it and write the extracted file.
							fos = new FileOutputStream(destinationFilePath);
							bos = new BufferedOutputStream(fos, 1024);

							int data;
							final byte buffer[] = new byte[1024];
							boolean endOfFile = (data = bis.read(buffer, 0, 1024)) != -1;
							while (endOfFile) {
								bos.write(buffer, 0, data);
								endOfFile = (data = bis.read(buffer, 0, 1024)) != -1;
							}
							bos.flush();
						}
					}
				}
			} finally {
				try {
					LOGGER.info("Closing input and ouput streams...");
					// flush the output stream and close it.
					if (bos != null) {
						bos.flush();
						bos.close();
					}
					// close the input stream.
					if (bis != null) {
						bis.close();
					}
					if (null != zipFile) {
						zipFile.close();
					}
				} catch (final Exception e) {
					LOGGER.info("Error occurred while closing the stream for extracting zip file....", e);
				}
			}
		}
	}

	private static String updateFileName(final String fileName, final String folderPath) {
		String finalFileName = fileName;
		if (fileName != null) {
			int extensionIndex = fileName.indexOf(IUtilCommonConstants.DOT);
			extensionIndex = extensionIndex == -1 ? fileName.length() : extensionIndex;
			final File parentFile = new File(folderPath);
			LOGGER.info("Updating file name if any file with the same name exists. File : " + fileName);
			finalFileName = getUpdatedFileNameForDuplicateFile(fileName.substring(0, extensionIndex), parentFile, -1,
					fileName.substring(extensionIndex))
					+ fileName.substring(extensionIndex);
			LOGGER.info("Updated file name : " + finalFileName);
		}
		return finalFileName;
	}

	/**
	 * This method copies the contents of one folder into another.
	 * 
	 * @param originalFolder the folder to copy
	 * @param copiedFolder the folder to store the copied content
	 * @throws IOException
	 */
	public static void copyFolder(final File originalFolder, final File copiedFolder) throws IOException {
		if (copiedFolder.exists()) {
			copiedFolder.delete();
		}
		copiedFolder.mkdirs();

		if (originalFolder.isDirectory()) {
			final String[] folderList = originalFolder.list();
			Arrays.sort(folderList);

			for (final String folderName : folderList) {
				FileUtils.copyDirectoryWithContents(new File(originalFolder, folderName), new File(copiedFolder, folderName));
			}
		}
	}

	/**
	 * This method zips the contents of Directory specified into a zip file whose name is provided.
	 * 
	 * @param dir2zip {@link String} the directory to zip
	 * @param zout {@link ZipOutputStream} the zip stream
	 * @throws IOException
	 */
	public static void zipDirectoryWithFullName(final String dir2zip, final ZipOutputStream zout) throws IOException {
		final File srcDir = new File(dir2zip);
		final List<String> fileList = FileUtils.listDirectory(srcDir);
		for (final String fileName : fileList) {
			final File file = new File(srcDir.getParent(), fileName);
			String zipName = fileName;
			if (File.separatorChar != FORWARD_SLASH) {
				zipName = fileName.replace(File.separatorChar, FORWARD_SLASH);
			}

			ZipEntry zipEntry;
			if (file.isFile()) {
				zipEntry = new ZipEntry(zipName);
				zipEntry.setTime(file.lastModified());
				zout.putNextEntry(zipEntry);
				final FileInputStream fin = new FileInputStream(file);
				final byte[] buffer = new byte[BUFFER_SIZE];
				for (int n; (n = fin.read(buffer)) > 0;) {
					zout.write(buffer, 0, n);
				}
				if (fin != null) {
					fin.close();
				}
			} else {
				zipEntry = new ZipEntry(zipName + FORWARD_SLASH);
				zipEntry.setTime(file.lastModified());
				zout.putNextEntry(zipEntry);
			}
		}
		if (zout != null) {
			zout.close();
		}
	}

	/**
	 * This method zip the multiple files.
	 * 
	 * @param dir2zip {@link String} The directory in which file need to zip.
	 * @param zout {@link ZipOutputStream} The zipoutput stream.
	 * @param fileExtension {@link String} The extension of file which need to zip.
	 * @throws {@link IOException}
	 */
	public static void zipMultipleFiles(final String dir2zip, final ZipOutputStream zout, final String fileExtension)
			throws IOException {
		final File srcDir = new File(dir2zip);
		if (srcDir.exists()) {
			final List<String> fileList = listDirectory(srcDir);
			for (final String fileName : fileList) {
				final File file = new File(srcDir.getParent(), fileName);
				String zipName = fileName;
				if (File.separatorChar != FORWARD_SLASH) {
					zipName = fileName.replace(File.separatorChar, FORWARD_SLASH);
				}

				ZipEntry zipEntry;
				if (file.isFile()) {
					zipEntry = new ZipEntry(zipName);
					zipEntry.setTime(file.lastModified());
					zout.putNextEntry(zipEntry);
					final FileInputStream fin = new FileInputStream(file);
					final byte[] buffer = new byte[INITIAL_SIZE];
					int bytesRead;
					bytesRead = fin.read(buffer);
					while (bytesRead != -1) {
						zout.write(buffer, 0, bytesRead);
						bytesRead = fin.read(buffer);
					}
					if (fin != null) {
						fin.close();
					}
				} else {
					zipEntry = new ZipEntry(zipName + FORWARD_SLASH);
					zipEntry.setTime(file.lastModified());
					zout.putNextEntry(zipEntry);
				}
			}
			if (zout != null) {
				zout.close();
			}
		} else {
			LOGGER.error("Source folder doesn't exist.");
		}

	}

	/**
	 * Deletes a file. If file is a directory, deletes it and all sub-directories.
	 * <p>
	 * The difference between File.delete() and this method are:
	 * <ul>
	 * <li>A directory to be deleted does not have to be empty.</li>
	 * <li><code>{@link IUtilCommonConstants}.DELETE_RETRY_INDEX</code> count of maximum attempts will be made to delete the file</li>
	 * <li>An error log will be logged for each unsuccessful attempt to delete the file, but no exception will be thrown.</li>
	 * </ul>
	 * 
	 * Must only be used if the files to be deleted can be left behind in case the deletion is unsuccessful.
	 * 
	 * @param fileToBeDeleted file or directory to be deleted.
	 * @return boolean, true if the file deletion was successful, false otherwise.
	 */
	public static boolean forceDelete(final File fileToBeDeleted) {
		boolean isFolderDeleted = false;
		if (fileToBeDeleted != null) {
			final String fileAbsolutePath = fileToBeDeleted.getAbsolutePath();
			if (fileToBeDeleted.exists()) {
				LOGGER.info(EphesoftStringUtil.concatenate("Trying to delete ", fileAbsolutePath));
				for (int retryIndex = 0; retryIndex < IUtilCommonConstants.DELETE_RETRY_MAX_COUNT; retryIndex++) {
					try {
						org.apache.commons.io.FileUtils.forceDelete(fileToBeDeleted);
						isFolderDeleted = true;
						break;
					} catch (final IOException e) {
						LOGGER.error(EphesoftStringUtil.concatenate(fileAbsolutePath, " folder could not be deleted in ",
								(retryIndex + 1), " attempt(s). Max ", IUtilCommonConstants.DELETE_RETRY_MAX_COUNT, " tries."));
					}
				}
			} else {
				LOGGER.error(EphesoftStringUtil.concatenate("The file/folder ", fileAbsolutePath,
						" does not exist. So cannot be deleted."));
			}
		} else {
			LOGGER.error("File object is null. Escaping the delete functionality.");
		}
		return isFolderDeleted;
	}

	/**
	 * Waits for a folder to be modified completely.
	 * 
	 * @param folderToCheck directory to be monitored for modification.
	 * @param timeoutLimit for folder-modification.
	 */
	public static void waitForFolderModification(final File folderToCheck, final long timeoutLimit) {
		if (null != folderToCheck && folderToCheck.isDirectory() && timeoutLimit > 0) {
			final long startTimeWaitCopyOperation = System.currentTimeMillis();
			// Wait for folder to be copied completely
			while (isFolderModification(folderToCheck, ONE_MINUTE)
					&& (System.currentTimeMillis() - startTimeWaitCopyOperation) < timeoutLimit) {
				try {
					Thread.sleep(TEN_SECONDS);
				} catch (final InterruptedException exception) {
					LOGGER.error(EphesoftStringUtil.concatenate("interrupted exception occured. ", exception.getMessage()));
				}
			}
		}
	}

	/**
	 * Checks whether folder is modified or not within the specified duration.
	 * 
	 * @param folderToCheck {@link File}- folder to be checked
	 * @param waitingDuration {@link Long}- time duration for folder modification
	 * @return {@link Boolean}- <code>true</code> if folder has been modified within the specified duration, else <code>false</code>
	 */
	public static boolean isFolderModification(final File folderToCheck, final long waitingDuration) {
		boolean isFolderModified = false;
		if (null != folderToCheck && waitingDuration >= 0) {
			if (folderToCheck.exists() && ((System.currentTimeMillis() - folderToCheck.lastModified()) < waitingDuration)) {
				isFolderModified = true;
				LOGGER.debug(EphesoftStringUtil.concatenate(folderToCheck.getAbsolutePath(),
						" is not modified since last minute. So now this folder will be sent for batch processing."));
			}
		}
		return isFolderModified;
	}

	/**
	 * This method checks whether the given directory is empty or not.
	 * 
	 * @param directoryPath - dir to be checked
	 * @return
	 */
	public static boolean checkForEmptyDirectory(final String directoryPath) {
		boolean isEmpty = false;
		if (null != directoryPath) {
			final File file = new File(directoryPath);
			if (!file.exists()) {
				file.mkdirs();
				isEmpty = true;
			} else {
				final File[] listOfFiles = file.listFiles();
				if ((listOfFiles != null && listOfFiles.length <= 0)) {
					isEmpty = true;
				}
			}
		}
		return isEmpty;
	}

	/**
	 * Creates backup of oldFile on the backUpFilePath and replaces oldFile with newFile.
	 * 
	 * @param {@link String} oldFilePath whose backup is to be created.
	 * @param {@link String} backUpFilePath backup file path.
	 * @param {@link String} newFilePath file path of new file to be copied in place.
	 * @return boolean true if operation is successful false otherwise.
	 */
	public static boolean backupAndCopyFile(final String oldFilePath, final String backUpFilePath, final String newFilePath) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Inside backupAndCopyFile method. oldFilePath is ", oldFilePath,
				" ,backUpFilePath is ", backUpFilePath, " and newFilePath is ", newFilePath));
		boolean isBackUpCreated = false;
		if (!(EphesoftStringUtil.isNullOrEmpty(oldFilePath) || EphesoftStringUtil.isNullOrEmpty(backUpFilePath) || EphesoftStringUtil
				.isNullOrEmpty(newFilePath))) {
			try {
				final File newFile = new File(newFilePath);
				if (newFile.exists()) {
					final File oldFile = new File(oldFilePath);
					final boolean backupSuccess = FileUtils.moveFile(oldFilePath, backUpFilePath);
					if (backupSuccess) {
						LOGGER.debug(EphesoftStringUtil.concatenate(oldFilePath, " backed up successfully to ", backUpFilePath));
					} else {
						LOGGER.debug(EphesoftStringUtil.concatenate("Unable to create backup of file ", oldFilePath, " to ",
								backUpFilePath, " as file ", oldFilePath, "does not exist. Copying new file without creating backup."));
					}
					FileUtils.copyFile(newFile, oldFile);
					isBackUpCreated = true;
				} else {
					LOGGER.error(EphesoftStringUtil.concatenate("File is not present at path: ", newFilePath));
				}
			} catch (final Exception exception) {
				LOGGER.error(EphesoftStringUtil.concatenate("Unable to create backup of file ", oldFilePath, "backUpFilePath is ",
						backUpFilePath, " and new file path is ", newFilePath, exception.getMessage()), exception);
			}
		}
		return isBackUpCreated;
	}

	/**
	 * Writes text to specified file.
	 * 
	 * @param {@link File} fileName name of file where write operation is to be perform.
	 * @param {@link String} textToWrite text to be written in specified file.
	 * @param {@link boolean} appendText flag to decide whether overwrite or append specified text.
	 * 
	 */
	public static void writeTextToFile(final File gsArgumentFile, final String textToWrite, final boolean appendText) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Writing text : ", textToWrite, " to file ", gsArgumentFile.getAbsolutePath()));
		if (null != gsArgumentFile && !EphesoftStringUtil.isNullOrEmpty(textToWrite)) {
			BufferedWriter bufferedWriter = null;
			try {
				bufferedWriter = new BufferedWriter(new FileWriter(gsArgumentFile, appendText));
				bufferedWriter.write(textToWrite);
				LOGGER.debug(EphesoftStringUtil.concatenate("Text : ", textToWrite, " is written to file ",
						gsArgumentFile.getAbsolutePath(), " successfully."));
			} catch (final IOException exception) {
				LOGGER.error(
						EphesoftStringUtil.concatenate("Exception occured while writing content to file ",
								gsArgumentFile.getAbsolutePath()), exception);
			} finally {
				try {
					if (null != bufferedWriter) {
						bufferedWriter.close();
					}
				} catch (final IOException exception) {
					LOGGER.error("Exception occured while closing stream. Exception is ", exception);
				}
			}
		} else {
			LOGGER.error(EphesoftStringUtil
					.concatenate("Invalid parameter are specified for operation. Unable to perform any processing."));
		}
	}

	/**
	 * Checks if the file whose path is passed as a parameter is used by another process or not. In case parameter is passed as NULL or
	 * empty <code>true</code> is returned.
	 * 
	 * <p>
	 * It internally uses apache {@link org.apache.commons.io.FileUtils} <code>touch</code>> method. This method will wait for 1 minute
	 * for the file to be released by another process otherwise it return <code>true</code>.
	 * 
	 * @param file {@link File} File to be checked for locking.
	 * @return true if file is locked otherwise false.
	 */
	public static boolean isFileLocked(final File file) {
		boolean isFileLocked = true;
		String filePath = null;

		// changes with respect to ticket 2692: invalid batch xml creation in report-data folder for import multipage plugin.
		if (null != file && file.exists()) {
			filePath = file.getAbsolutePath();
			LOGGER.debug(EphesoftStringUtil.concatenate("Checking for file lock on: ", filePath));
			long startTime = System.currentTimeMillis();

			// Check and wait for 1 minute for the release of file if it is
			// locked
			while (isFileLocked && (System.currentTimeMillis() - startTime) < WAITING_TIME) {
				try {
					org.apache.commons.io.FileUtils.touch(file);
					isFileLocked = false;
				} catch (final IOException ioException) {

					// Wait for 1 second and check again if the file is released
					try {
						Thread.sleep(ONE_SECOND);
					} catch (final InterruptedException interruptedException) {
						// Do nothing
					}
					isFileLocked = true;
				}
			}
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Status of file: ", filePath, " locked is: ", isFileLocked));
		return isFileLocked;
	}

	/**
	 * This method renames a folder given its original path and new path as parameter.
	 * 
	 * @param originalPath {@link String} value containing path of original folder.
	 * @param newPath {@link String} value containing path of new folder which will replace old folder.
	 * @return returns the status of operation as boolean value.
	 */
	public static boolean renameFolder(final String originalPath, final String newPath) {
		boolean renameSuccessful = false;
		LOGGER.debug(EphesoftStringUtil.concatenate("Original path is : ", originalPath, " New Path is : ", newPath));
		if (originalPath == null) {
			LOGGER.error("Path of Original folder is null");
		} else if (newPath == null) {
			LOGGER.error("Path of new folder is null");
		} else {
			final File originalFolder = new File(originalPath);
			renameSuccessful = originalFolder.renameTo(new File(newPath));

			// Update the UNC folder name in the file system.
			if (renameSuccessful) {
				LOGGER.info("Moving of Directory and its contents of  original path folder is successful");
			} else {
				LOGGER.error("Moving of Directory and its contents of original path folder is failed");
			}
		}
		return renameSuccessful;
	}

	/**
	 * Checks if any file exists with the file path passed.
	 * 
	 * @param inputFilePath {@link String} Absolute Path of file whose existence is to be checked.
	 * @return Returns true if file exists otherwise false.
	 */
	public static boolean isFileExists(final String inputFilePath) {
		boolean isFileExists = false;
		if (!EphesoftStringUtil.isNullOrEmpty(inputFilePath)) {
			File file = new File(inputFilePath);
			isFileExists = file.exists();
		}
		return isFileExists;
	}

	/**
	 * Gets the file extension for the file.
	 * 
	 * @param imagePath {@link String} absolute file path
	 */
	public static String getFileExtension(final String imagePath) {
		String fileExtension = null;
		if (!EphesoftStringUtil.isNullOrEmpty(imagePath)) {
			int extensionIndex = imagePath.lastIndexOf(IUtilCommonConstants.DOT);
			if (extensionIndex != -1) {
				fileExtension = imagePath.substring(extensionIndex + 1);
			}
		}
		return fileExtension;
	}

	/**
	 * Closes the com.itextpdf.text.pdf.RandomAccessFileOrArray.
	 * 
	 * @param randomAccessFile {@link com.itextpdf.text.pdf.RandomAccessFileOrArray} com.itextpdf.text.pdf.RandomAccessFileOrArray to
	 *            be closed
	 */
	public static void closeStream(final RandomAccessFileOrArray randomAccessFile) {
		try {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		} catch (IOException ioException) {
			LOGGER.error("Error occurred while closing RandomAccessFileOrArray.", ioException);
		}
	}

	/**
	 * Closes the reader stream.
	 * 
	 * @param reader {@link java.io.Reader} to be closed
	 */
	public static void closeStream(final Reader reader) {
		IOUtils.closeQuietly(reader);
	}

	/**
	 * Creates the key store file at the path <code> keyStoreFilePath </code> which is secured by the <code>password</code> and stores
	 * the Key store in to the file.
	 * 
	 * @param keyStoreFilePath {@link String} absolute/Relative path to the key store file
	 * @param password char[] password by which the file is secured
	 * @param store {@link KeyStore} that protects the key store file
	 * @return boolean true if file is created , otherwise false.
	 * @throws GeneralSecurityException when key store key could not be stored
	 * @throws IOException when could not update the file / store the contents of key store at the path
	 */
	public static boolean createKeyStoreFile(final String keyStoreFilePath, final char[] password, final KeyStore store)
			throws GeneralSecurityException, IOException {
		FileOutputStream keyStoreStream = null;
		boolean fileCreated = false;
		try {
			if (store != null && !EphesoftStringUtil.isNullOrEmpty(keyStoreFilePath) && password != null) {
				File keyStoreFile = new File(keyStoreFilePath);
				File parentFile = keyStoreFile.getParentFile();
				parentFile.mkdirs();
				keyStoreStream = new FileOutputStream(keyStoreFilePath);
				store.store(keyStoreStream, password);
				fileCreated = true;
			}
		} finally {
			IOUtils.closeQuietly(keyStoreStream);
		}
		return fileCreated;
	}

	/**
	 * Checks whether the fileToSearch exists under the parent Directory. If the parent file is a directory and the file to search
	 * exist under the directory then it returns true else returns false.
	 * 
	 * @param parentDirectoryPath {@link String} Relative or absolute path to the parent directory
	 * @param fileToSearchPath {@link String} file to locate if is under the parent directory or not.
	 * @return true if the file exists under the parent directory else false.
	 */
	public static boolean isUnderDirectory(final String parentDirectoryPath, final String fileToSearchPath) {
		boolean isUnderDirectory = false;
		if (!EphesoftStringUtil.isNullOrEmpty(parentDirectoryPath) && !EphesoftStringUtil.isNullOrEmpty(fileToSearchPath)) {
			File parentDirectory = new File(parentDirectoryPath);
			File subFolderFile = new File(fileToSearchPath);
			if (parentDirectory.isDirectory() && subFolderFile.exists()) {
				String parentFileAbsolutePath = parentDirectory.getAbsolutePath();
				String subFolderFileAbsolutePath = parentDirectory.getAbsolutePath();
				isUnderDirectory = subFolderFileAbsolutePath.startsWith(parentFileAbsolutePath);
			}
		}
		return isUnderDirectory;
	}

	/**
	 * Creates JDOM document from a given xml.
	 * 
	 * @param xmlFilePath the xml file path
	 * @return JDOM document for given xml file
	 */
	public static org.jdom.Document createJDOMDocumentFromXML(final String xmlFilePath) {
		LOGGER.info(EphesoftStringUtil.concatenate("Entering create document from xml ", xmlFilePath));
		org.jdom.Document document = null;

		try {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(xmlFilePath);
		} catch (FileNotFoundException fileNotFoundException) {
			LOGGER.error(EphesoftStringUtil.concatenate("File not found = ", xmlFilePath, fileNotFoundException.getMessage()));
		} catch (Exception exception) {
			LOGGER.error(EphesoftStringUtil.concatenate("Exception occured while creating document from xml = ", xmlFilePath,
					exception.getMessage()));
		}
		LOGGER.info("Exiting create document from xml");
		return document;
	}

	private FileUtils() {
		// private constructor added to make the class non-instantiable.
	}

	/**
	 * This method deletes a given directory with its content without throwing exceptions. Implementation of apache FileUtils method
	 * deleteQuietly.
	 * 
	 * @param srcFile {@link File} the file to be deleted along with its contents.
	 * @return boolean true if deletion successful, false otherwise.
	 */
	public static boolean deleteQuietly(final File srcFile) {
		boolean deletionSuccessful = false;
		if (srcFile == null) {
			deletionSuccessful = false;
		}
		try {
			if (srcFile.isDirectory()) {
				deleteDirectoryAndContentsRecursive(srcFile);
			} else {
				deletionSuccessful = srcFile.delete();
			}
		} catch (Exception ignored) {
			deletionSuccessful = false;
		}
		return deletionSuccessful;
	}

	/**
	 * Closes the Closeable resource.
	 * 
	 * @param closeable {@link Closeable} to be closed.
	 */
	public static void closeResource(final Closeable closeable) {
		IOUtils.closeQuietly(closeable);
	}

	/**
	 * Closes the FileChannelRandomAccessSource.
	 * 
	 * @param fileChannelRandomAccessSource {@link com.itextpdf.text.io.FileChannelRandomAccessSource} to be closed.
	 */
	public static void closeFileChannelRandomAccessSource(final FileChannelRandomAccessSource fileChannelRandomAccessSource) {
		try {
			if (null != fileChannelRandomAccessSource) {
				fileChannelRandomAccessSource.close();
			}
		} catch (final IOException e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Exception occured while closing fileChannelRandomAccessSource ",
					e.getMessage(), e));
		}
	}

	/**
	 * Deletes files of specified file type from folder.
	 * 
	 * @param folderPath Absolute path of the folder
	 * @param fileExtension extension for files to be deleted
	 */
	public static void deleteFileOfType(final String folderPath, final String fileExtension) {
		if (!EphesoftStringUtil.isNullOrEmpty(folderPath)) {
			File directory = new File(folderPath);
			if (directory.isDirectory() && directory.exists()) {
				File[] fileList = directory.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(fileExtension.toLowerCase());
					}
				});
				for (File file : fileList) {
					file.delete();
				}
			}
		}
	}

	/**
	 * Returns a list of all the valid file names in a directory that match the regex pattern
	 * 
	 * @param filePath path of the folder location from where the files will be picked
	 * 
	 * @return list of all file names that match the regex
	 */
	public static List<String> getFileNamesWithRegexMatch(final String filePath, final String regexPattern) {
		List<String> fileNameList = new ArrayList<String>();
		if (EphesoftStringUtil.isNullOrEmpty(regexPattern) || EphesoftStringUtil.isNullOrEmpty(filePath)) {
			LOGGER.info("File path or regex pattern is null or blank");
		} else {
			File folderPath = new File(filePath);
			File[] listOfFiles = folderPath.listFiles();
			for (File file : listOfFiles) {
				if (file.getName().matches(regexPattern)) {
					fileNameList.add(file.getName());
				}
			}
		}
		return fileNameList;
	}

	/**
	 * returs a list of file names without extension from a directory that match a particular pattern
	 * 
	 * @param filePath
	 * @param regexPattern
	 * @return
	 */
	public static List<String> getFileNamesWithRegexMatchWithoutExtension(final String filePath, final String regexPattern) {
		List<String> fileNameList = new ArrayList<String>();
		if (EphesoftStringUtil.isNullOrEmpty(regexPattern) || EphesoftStringUtil.isNullOrEmpty(filePath)) {
			LOGGER.info("File path or regex pattern is null or blank");
		} else {
			File folderPath = new File(filePath);
			File[] listOfFiles = folderPath.listFiles();
			for (File file : listOfFiles) {
				if (file.getName().matches(regexPattern)) {
					fileNameList.add(org.apache.commons.io.FilenameUtils.removeExtension(file.getName()));
				}
			}
		}
		return fileNameList;
	}

	/**
	 * Checks if is directory has all valid extension files.
	 * 
	 * @param tempUnZipDir the temp un zip dir
	 * @param serializationExt the serialization ext
	 * @return true, if is directory has all valid extension files
	 */
	public static boolean isDirectoryHasAllValidExtensionFiles(String tempUnZipDir, String serializationExt) {
		boolean isValid = true;
		File folderPath = new File(tempUnZipDir);
		if (folderPath.isDirectory()) {
			File[] listOfFiles = folderPath.listFiles();
			if (listOfFiles.length > 0) {
				for (File file : listOfFiles) {
					if (file.isDirectory() || !isFileExtensionValid(file.getAbsolutePath(), serializationExt)) {
						isValid = false;
						break;
					}
				}
			} else {
				isValid = false;
			}
		} else {
			isValid = false;
		}
		return isValid;
	}
	
	/**
	 * 
	 * Deletes files from the directory comparing modified time with cutoff in milliseconds
	 * 
	 * @param file
	 * @param cutoff
	 * @param acceptOlder  
	 * @return
	 */
	public static void deleteAgedFile(File file, long cutoff, boolean acceptOlder)
	{
		deleteFiles(file, new AgeFileFilter(cutoff, acceptOlder ));
	}
	
	
	/**
	 * Deletes files from the folder with the specified filter
	 * 
	 * @param directory
	 * @param fileFilter
	 */
	public static void deleteFiles(File directory, FileFilter fileFilter) {
		File[] files = directory.listFiles(fileFilter);
		List<String> deletedFiles = new ArrayList<String>(files.length);
		for (File file : files) {
			if (file.isDirectory()) {
				FileUtils.deleteDirectoryAndContentsRecursive(file);
			} else {
				file.delete();
			}
			deletedFiles.add(file.getName());
		}
		LOGGER.info(EphesoftStringUtil.concatenate(deletedFiles," files deleted from directory " , directory));
	}

	/**
	 * Returns recursive count of specified file extensions present inside specified directory on the basis of recursive flag.
	 * 
	 * @param directoryPath {@link String} to be searched for file count.
	 * @param {@link String[]} extensions has file extensions to be searched in directory.
	 * @param recursive flag to perform file search recursively.
	 * @return count of specified file extensions
	 */
	public static int getFileCountOfTypeFromDirectory(final String directoryPath, final String[] extensions, final boolean recursive) {
		LOGGER.info("Executing getFileCountOfTypeFromDirectory API.");
		int filesCount = 0;
		if (!EphesoftStringUtil.isNullOrEmpty(directoryPath) && null != extensions) {
			LOGGER.info(EphesoftStringUtil.concatenate("Executing getFileCountOfTypeFromDirectory for directory : ", directoryPath,
					". Files extension to be searched are : ", Arrays.toString(extensions),
					" and recursive flag is : ", recursive));
			filesCount = org.apache.commons.io.FileUtils.listFiles(new File(directoryPath), extensions, recursive).size();
			LOGGER.info(EphesoftStringUtil.concatenate("Total ", filesCount,
					" are present in directory that matches specified extentions."));
		} else {
			LOGGER.error("Either specified directory or extension is invalid. Please make sure correct arguments are specified.");
		}
		LOGGER.info("Executed getFileCountOfTypeFromDirectory API.");
		return filesCount;
	}
}
