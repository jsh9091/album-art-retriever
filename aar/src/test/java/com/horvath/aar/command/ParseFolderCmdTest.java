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

public class ParseFolderCmdTest {
	
	public static final String RESOURCES_DIRECTORY = "src" + File.separator + "test" 
			+ File.separator + "resources"+ File.separator;
	
	public static final String MP3_WITH_ART = "MP3-with-art";
	public static final String MP3 = "snap.mp3";


	@Test
	public void perform_nullFolder_exception() {
		boolean caughtException = false;
		try {
			// file is null
			ParseFolderCmd cmd = new ParseFolderCmd(null);
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParseFolderCmd.ERROR_NULL_FOLDER));
		}
		Assert.assertTrue(caughtException);

	}

	@Test
	public void perform_fakeFolder_exception() {
		boolean caughtException = false;
		try {
			// file fake
			ParseFolderCmd cmd = new ParseFolderCmd(new File("fake"));
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParseFolderCmd.ERROR_FOLDER_DOES_NOT_EXIST));
		}
		Assert.assertTrue(caughtException);
	}

	@Test
	public void perform_fileIsNotAFolder_exception() {
		boolean caughtException = false;
		try {
			final String path = RESOURCES_DIRECTORY + MP3_WITH_ART + File.separator + MP3;
			
			// file is not a folder
			ParseFolderCmd cmd = new ParseFolderCmd(new File(path));
			cmd.perform();
			
			// should not get here
			Assert.fail();
			
		} catch (AarException ex) {
			caughtException = true;
			Assert.assertTrue(ex.getMessage().contains(ParseFolderCmd.ERROR_FILE_IS_NOT_FOLDER));
		}
		Assert.assertTrue(caughtException);

	}
	
	
}
