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

package com.ephesoft.gxt.core.shared.dto;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FileWrapper implements IsSerializable {

	private String name;
	private String path;
	private FileType kind;
	private Date modifiedAt;
	private Long modifiedTimeInSeconds;
	private boolean subFolderContained = true;
	private String noOfFiles;
	private Float fileSize;

	public FileWrapper(String path, String name, Date modifiedAt) {
		this.name = name;
		this.path = path;
		this.modifiedAt = modifiedAt;
		this.kind = this.getFileType(extractFileExtention(name));

	}

	public FileWrapper(String path, String name) {
		this.name = name;
		this.path = path;
		this.kind = this.getFileType(extractFileExtention(name));
	}

	public FileWrapper(String path) {
		this.name = "";
		this.path = path;
		this.kind = FileType.DIR;
	}

	public FileWrapper(String path, String name, Date modifiedAt, boolean subFolderContained, Long modifiedTimeInSeconds) {
		this.name = name;
		this.path = path;
		this.modifiedAt = modifiedAt;
		this.kind = this.getFileType(extractFileExtention(name));
		this.subFolderContained = subFolderContained;
		this.setModifiedTimeInSeconds(modifiedTimeInSeconds);
	}

	public FileWrapper() {
	}

	public FileWrapper(String path, String name, Date modifiedAt, long modifiedTimeInSeconds) {
		this.name = name;
		this.path = path;
		this.modifiedAt = modifiedAt;
		this.kind = this.getFileType(extractFileExtention(name));
		this.setModifiedTimeInSeconds(modifiedTimeInSeconds);
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public Date getModified() {
		return this.modifiedAt;
	}

	public FileType getKind() {
		return kind;
	}

	public void setIsDirectory() {
		this.kind = FileType.DIR;
	}

	private static String extractFileExtention(String file) {
		int dot = file.lastIndexOf('.');
		return file.substring(dot + 1).toLowerCase();
	}

	private FileType getFileType(String ext) {
		if (ext.equalsIgnoreCase("doc") || ext.equalsIgnoreCase("pdf") || ext.equalsIgnoreCase("ppt") || ext.equalsIgnoreCase("docx"))
			return FileType.DOC;
		if (ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("wnv") || ext.equalsIgnoreCase("mpeg") || ext.equalsIgnoreCase("mov")
				|| ext.equalsIgnoreCase("mp3"))
			return FileType.MM;
		if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("tif")
				|| ext.equalsIgnoreCase("psd") || ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("jpeg"))
			return FileType.IMG;
		return FileType.OTHER;

	}

	public void setSubFolderContained(boolean subFolderContained) {
		this.subFolderContained = subFolderContained;
	}

	public boolean isSubFolderContained() {
		return subFolderContained;
	}

	public void setModifiedTimeInSeconds(Long modifiedTimeInSeconds) {
		this.modifiedTimeInSeconds = modifiedTimeInSeconds;
	}

	public Long getModifiedTimeInSeconds() {
		return modifiedTimeInSeconds;
	}

	public String getNoOfFiles() {
		return noOfFiles;
	}

	public void setNoOfFiles(String noOfFiles) {
		this.noOfFiles = noOfFiles;
	}

	public Float getFileSize() {
		return fileSize;
	}

	public void setFileSize(Float fileSize) {
		this.fileSize = fileSize;
	}

}
