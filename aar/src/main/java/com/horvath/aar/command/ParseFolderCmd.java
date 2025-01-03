/*
 * MIT License
 * 
 * Copyright (c) 2024 Joshua Horvath
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.horvath.aar.command;

import java.io.File;
import java.util.logging.Level;

import com.horvath.aar.application.Debugger;
import com.horvath.aar.exception.AarException;

/**
 * Recursively processes all sub-folder for given folder and extracts album art from first MP3 file found. 
 * @author jhorvath
 */
public class ParseFolderCmd extends AarCommand {
	
	private File rootFolder;
	
	public static final String ERROR_NULL_FOLDER = "The folder cannot be null";
	public static final String ERROR_FOLDER_DOES_NOT_EXIST = "The folder was not found.";
	public static final String ERROR_FILE_IS_NOT_FOLDER = "The file is not a folder.";

	/**
	 * Constructor. 
	 * @param folder File
	 */
	public ParseFolderCmd(File folder) {
		this.rootFolder = folder;
	}

	@Override
	public void perform() throws AarException {
		Debugger.printLog("Parse art from folder structure", this.getClass().getName());
		
		this.success = false;
		
		validate();

		processSubFolders(rootFolder);
		
		this.success = true;
	}
	
	/**
	 * Recursive method to explore sub-folders and call processing operations. 
	 * @param folder File 
	 * @throws AarException
	 */
	private void processSubFolders(File folder) throws AarException {
	    File[] files = folder.listFiles();
	    
        for (File file: files) {
            if (file.isDirectory()) {
            	// recurse, and go down another folder level 
                processSubFolders(file);
            }
            else {
                if (findMp3File(file)) {
                	return;
                }
            }
        }
	}
	
	/**
	 * Checks if given file is an MP3, and attempts to process the image from it.
	 * Returns true only if file is an MP3, and album art image written to disk.
	 * 
	 * @param file File 
	 * @return boolean 
	 */
	private boolean findMp3File(File file) {
		
		boolean result = false;
		
		if (!file.exists() || file.isDirectory()) {
			return result;
		}
		
		if (file.getName().toLowerCase().endsWith(".mp3")) {
			result = processMp3File(file);
		}
		
		return result;
	}
	
	/**
	 * Parses the album art from MP3 file. 
	 * Returns true if image file written to disk. 
	 * 
	 * @param mp3 File 
	 * @return boolean 
	 */
	private boolean processMp3File(File mp3) {
		boolean result = false; 
		
		try {
			// process buffered image 
			ParseAlbumArtCmd parseArtCmd = new ParseAlbumArtCmd(mp3);
			parseArtCmd.perform();
			
			if (parseArtCmd.isSuccess()) {
				
				WriteBufferedImageCmd writeCmd = new WriteBufferedImageCmd(mp3.getParentFile(), parseArtCmd.getBufferedImage());
				writeCmd.perform();
				
				result = writeCmd.isSuccess();
			}

		} catch (AarException ex) {
			Debugger.printLog("Error: " + ex.getMessage(), this.getClass().getName(), Level.WARNING);
			return false;
		}
		
		return result;
	}
	
	
	/**
	 * Validates the given root folder. 
	 * @throws AarException
	 */
	private void validate() throws AarException {
		
		if (rootFolder == null) {
			throw new AarException(ERROR_NULL_FOLDER);
		}
		
		if (!rootFolder.exists()) {
			throw new AarException(ERROR_FOLDER_DOES_NOT_EXIST);
		}
		
		if (!rootFolder.isDirectory()) {
			throw new AarException(ERROR_FILE_IS_NOT_FOLDER);
		}		
	}

}
