/*
 * MIT License
 * 
 * Copyright (c) 2025 Joshua Horvath
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import com.horvath.aar.application.Debugger;
import com.horvath.aar.exception.AarException;

/**
 * Writes a BufferedImage to disk. 
 * @author jhorvath 
 */
public class WriteBufferedImageCmd extends AarCommand {
	
	private File folder;
	private String name;
	private BufferedImage image;
	
	public static final String DEFAULT_NAME = "album.jpg";
	
	public static final String ERROR_NULL_FILE = "The file cannot be null";
	public static final String ERROR_PARENT_FOLDER_DOES_NOT_EXIST = "The parent folder does not exist";
	public static final String ERROR_FILE_IS_NOT_FOLDER = "The file is not a folder";
	public static final String ERROR_IMAGE_NULL= "The image is null";
	
	/**
	 * Constructor. 
	 * @param folder File 
	 * @param image BufferedImage
	 */
	public WriteBufferedImageCmd(File folder, BufferedImage image) {
		this(folder, image, DEFAULT_NAME);
	}

	/**
	 * Constructor. 
	 * @param folder File
	 * @param image BufferedImage
	 * @param name String 
	 */
	public WriteBufferedImageCmd(File folder, BufferedImage image, String name) {
		this.folder = folder;
		this.image = image;
		this.name = name;
	}
	
	@Override
	public void perform() throws AarException {
		Debugger.printLog("Write image to disk", this.getClass().getName());
		
		success = false;
		
		validate();
		
		try {
			File outputfile = new File(folder.getAbsolutePath() + File.separator + name);
			ImageIO.write(image, "jpg", outputfile);
			
			success = true;
			
		} catch (IOException ex) {
			final String message = "Unexpected Exception: " + ex.getMessage();
			Debugger.printLog(message, this.getClass().getName(), Level.SEVERE);
			this.message = message;
			throw new AarException(message, ex);
		}
	}

	/**
	 * Validates the constructor inputs. 
	 * @throws AarException
	 */
	private void validate() throws AarException {
		
		if (folder == null) {
			throw new AarException(ERROR_NULL_FILE);
		}

		if (!folder.exists()) {
			throw new AarException(ERROR_PARENT_FOLDER_DOES_NOT_EXIST);
		}
		
		if (!folder.isDirectory()) {
			throw new AarException(ERROR_FILE_IS_NOT_FOLDER);
		}
		
		if (image == null) {
			throw new AarException(ERROR_IMAGE_NULL);
		}

		if (name == null || name.trim().isEmpty()) {
			name = DEFAULT_NAME;
		}
	}

	public File getFolder() {
		return folder;
	}

	public String getName() {
		return name;
	}

	public BufferedImage getImage() {
		return image;
	}
	
}
