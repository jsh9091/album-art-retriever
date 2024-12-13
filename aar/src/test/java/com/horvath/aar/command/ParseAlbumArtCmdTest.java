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

import org.junit.Assert;
import org.junit.Test;

import com.horvath.aar.exception.AarException;

/**
 * Tests operations of ParseAlbumArtCmd. 
 * @author jhorvath
 */
public class ParseAlbumArtCmdTest {
	
	public static final String RESOURCES_DIRECTORY = "src" + File.separator + "test" 
			+ File.separator + "resources"+ File.separator;
	
	@Test
	public void perform_nullFile_exception() {
		boolean caughtException = false;
		try {
			// file is null
			ParseAlbumArtCmd cmd = new ParseAlbumArtCmd(null);
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParseAlbumArtCmd.ERROR_NULL_FILE));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileDoesNotExist_exception() {
		boolean caughtException = false;
		try {
			// file does not exist
			File fakeFile = new File("fake.mp3");
			ParseAlbumArtCmd cmd = new ParseAlbumArtCmd(fakeFile);
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParseAlbumArtCmd.ERROR_FILE_DOES_NOT_EXIST));
		}
		Assert.assertTrue(caughtException);
	}
	
	@Test
	public void perform_fileDoesNotHaveArt_messageReturned() {
		try {
			File noArtMp3File = new File(RESOURCES_DIRECTORY + "No-Art" + File.separator + "snap-no-art.mp3");
			
			Assert.assertTrue(noArtMp3File.exists());
			
			ParseAlbumArtCmd cmd = new ParseAlbumArtCmd(noArtMp3File);
			cmd.perform();
			
			// a file without art does not mean command failed
			Assert.assertTrue(cmd.isSuccess());
			// check message to be sure of what happened
			Assert.assertEquals(ParseAlbumArtCmd.MESSAGE_NO_ARTWORK_FOUND, cmd.getMessage());
			// expect the image to be null, since no art was in the MP3
			Assert.assertNull(cmd.getBufferedImage());
			
		} catch (AarException ex) {
			Assert.fail();
		}
	}

	@Test
	public void perform_mp3File_imageParsed() {
		try {
			File mp3File = new File(RESOURCES_DIRECTORY + "MP3-with-art" + File.separator + "snap.mp3");
			
			Assert.assertTrue(mp3File.exists());
			
			ParseAlbumArtCmd cmd = new ParseAlbumArtCmd(mp3File);
			cmd.perform();
			
			Assert.assertTrue(cmd.isSuccess());
			Assert.assertEquals(ParseAlbumArtCmd.MESSAGE_ARTWORK_PARSED, cmd.getMessage());
			Assert.assertNotNull(cmd.getBufferedImage());

		} catch (AarException ex) {
			Assert.fail();
		}
	}
	
}
