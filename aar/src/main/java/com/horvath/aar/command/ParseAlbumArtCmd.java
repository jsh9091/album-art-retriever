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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

import com.horvath.aar.application.Debugger;
import com.horvath.aar.exception.AarException;

/**
 * Command for parsing album artwork from audio file. 
 * 
 * @author jhorvath
 */
public class ParseAlbumArtCmd extends AarCommand {
	
	private File file;
	
	private BufferedImage bufferedImage = null;
	
	public static final String ERROR_NULL_FILE = "The file cannot be null";
	public static final String ERROR_FILE_DOES_NOT_EXIST = "The file was not found.";
	
	public static final String MESSAGE_ARTWORK_PARSED = "Art was found and retrieved from file.";
	public static final String MESSAGE_NO_ARTWORK_FOUND = "The audiofile did not contain album artwork.";
	
	/**
	 * Constructor. 
	 * @param file File 
	 */
	public ParseAlbumArtCmd(File file) {
		this.file = file;
	}

	@Override
	public void perform() throws AarException {
		Debugger.printLog("Parse art from audio file", this.getClass().getName());
		
		this.success = false;
		
		if (file == null) {
			throw new AarException(ERROR_NULL_FILE);
		} else if (!file.exists()) {
			throw new AarException(ERROR_FILE_DOES_NOT_EXIST);
		}
		
		processImage();
		
		this.success = true;
	}
	
	/**
	 * Performs image extraction from audio file. 
	 * @throws AarException
	 */
	private void processImage() throws AarException {
		try {
			AudioFile audioFile = AudioFileIO.read(file);

			Tag tag = audioFile.getTag();

			List<Artwork> existingArtworkList = tag.getArtworkList();

			if (existingArtworkList.isEmpty()) {
				message = MESSAGE_NO_ARTWORK_FOUND;

			} else {
				Artwork art = existingArtworkList.get(0);
				bufferedImage = art.getImage();

				message = MESSAGE_ARTWORK_PARSED;
			}

		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException
				| InvalidAudioFrameException ex) {
			final String message = "Unexpected Exception: " + ex.getMessage();
			Debugger.printLog(message, this.getClass().getName(), Level.SEVERE);
			this.message = message;
			throw new AarException(message, ex);
		}
	}
	
	public File getParentDirectory() {
		return this.file.getParentFile();
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

}
