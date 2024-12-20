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

import javax.activation.MimetypesFileTypeMap;

import org.junit.Assert;
import org.junit.Test;

import com.horvath.aar.exception.AarException;

/**
 * Tests operations of WriteBufferedImageCmd. 
 * @author jhorvath
 */
public class WriteBufferedImageCmdTest {
	
	public static final String RESOURCES_DIRECTORY = "src" + File.separator + "test" 
			+ File.separator + "resources"+ File.separator;
	
	public static final String MP3_WITH_ART = "MP3-with-art";
	public static final String MP3 = "snap.mp3";
	public static final String MP3_NO_ART = "snap-no-art.mp3";
	
	@Test
	public void perform_nullFile_exception() {
		boolean caughtException = false;
		try {
			// file is null
			WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(null, new BufferedImage(1, 1, 1));
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(WriteBufferedImageCmd.ERROR_NULL_FILE));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_folderNotReal_exception() {
		boolean caughtException = false;
		try {
			WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(new File("fakefolder"), new BufferedImage(1, 1, 1));
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(WriteBufferedImageCmd.ERROR_PARENT_FOLDER_DOES_NOT_EXIST));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileIsNotFolder_exception() {
		boolean caughtException = false;
		try {
			File mp3File = new File(RESOURCES_DIRECTORY + MP3_WITH_ART + File.separator + MP3);
			
			WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(mp3File, new BufferedImage(1, 1, 1));
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(WriteBufferedImageCmd.ERROR_FILE_IS_NOT_FOLDER));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_imageNull_exception() {
		boolean caughtException = false;
		try {
			File mp3Folder= new File(RESOURCES_DIRECTORY + MP3_WITH_ART );
			
			WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(mp3Folder, null);
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(WriteBufferedImageCmd.ERROR_IMAGE_NULL));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test 
	public void perform_nameNull_defaulNameSet() {
		File mp3Folder= new File(RESOURCES_DIRECTORY + MP3_WITH_ART );
		
		WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(mp3Folder, new BufferedImage(1, 1, 1), null);
		
		try {
			cmd.perform();			
		} catch (AarException ex) {
			Assert.fail();
		}
		
		Assert.assertEquals(WriteBufferedImageCmd.DEFAULT_NAME, cmd.getName());
	}
	
	@Test
	public void perform_nameEmpty_defaulNameSet() {
		File mp3Folder= new File(RESOURCES_DIRECTORY + MP3_WITH_ART );
		
		WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(mp3Folder, new BufferedImage(1, 1, 1), " ");
		
		try {
			cmd.perform();			
		} catch (AarException ex) {
			Assert.fail();
		}
		
		Assert.assertEquals(WriteBufferedImageCmd.DEFAULT_NAME, cmd.getName());
	}
	
	@Test
	public void perform_nameGiven_nameSet() {
		File mp3Folder= new File(RESOURCES_DIRECTORY + MP3_WITH_ART );
		final String name = "My file name.jpg";
		
		WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(mp3Folder, new BufferedImage(1, 1, 1), name);
		
		Assert.assertEquals(name, cmd.getName());
	}
	
	@Test
	public void perform_goodFileGoodImage_fileGenerated() {
		
		File imageFile = new File(RESOURCES_DIRECTORY + MP3_WITH_ART + File.separator +WriteBufferedImageCmd.DEFAULT_NAME);
		
		// perform cleanups after a failed run
		if (imageFile.exists()) {
			Assert.assertTrue(imageFile.delete());
		}
		Assert.assertFalse(imageFile.exists());
		
		try {
			File mp3File = new File(RESOURCES_DIRECTORY + MP3_WITH_ART + File.separator + MP3);
			ParseAlbumArtCmd parseCmd = new ParseAlbumArtCmd(mp3File);
			parseCmd.perform();
			
			BufferedImage image = parseCmd.getBufferedImage();
			
			WriteBufferedImageCmd cmd = new WriteBufferedImageCmd(mp3File.getParentFile(), image);
			cmd.perform();
			
			Assert.assertTrue(imageFile.exists());
			Assert.assertTrue(isFileImage(imageFile));
			
			// cleanup 
			Assert.assertTrue(imageFile.delete());
			
		} catch (AarException ex) {
			Assert.fail();
		}
		
	}
	
	/**
	 * Checks if a given file is an image or not. 
	 * @param file File
	 * @return boolean 
	 */
	private boolean isFileImage(File file) {
		String mimetype= new MimetypesFileTypeMap().getContentType(file);
        String type = mimetype.split("/")[0];
        return type.equals("image");
	}

}
