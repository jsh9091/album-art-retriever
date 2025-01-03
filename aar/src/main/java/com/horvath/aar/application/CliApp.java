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
package com.horvath.aar.application;

import java.awt.image.BufferedImage;
import java.io.File;

import com.horvath.aar.command.ParseAlbumArtCmd;
import com.horvath.aar.command.WriteBufferedImageCmd;
import com.horvath.aar.exception.AarException;

/**
 * Processes CLI inputs and controls operations. 
 * @author jhorvath
 */
public class CliApp {

	private File file = null; 
	
	/**
	 * Constructor. 
	 * @param path String 
	 */
	public CliApp(String path) {
		this.file = new File(path);
	}
	
	public void run() {
		
		if (file.isDirectory()) {
			// TODO call folder command 
			
		} else {
			parsefile(file);
		}
	}
	
	/**
	 * Parses an individual MP3 file. 
	 * @param mp3File File 
	 */
	private void parsefile(File mp3File) {
		try {
			ParseAlbumArtCmd parseCmd = new ParseAlbumArtCmd(file);
			parseCmd.perform();
			
			if (parseCmd.isSuccess()) {
				BufferedImage image = parseCmd.getBufferedImage();
				
				WriteBufferedImageCmd writeCmd = new WriteBufferedImageCmd(file.getParentFile(), image);
				writeCmd.perform();
				
				if (writeCmd.isSuccess()) {
					System.out.println("Successfully wrote MP3 art file to " + file.getParent());
					
				} else {
					System.err.println("Somethig went wrong with writing the jpeg art file.");
				}
			} else {
				System.err.println("Somethig went wrong with reading the MP3 file.");
			}
			
		} catch (AarException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
