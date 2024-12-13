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

}
