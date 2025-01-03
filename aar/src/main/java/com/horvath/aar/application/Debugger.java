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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple debugger class. 
 * @author jhorvath
 */
public class Debugger {

	private static boolean debugging = false; 
	private static Logger logger = Logger.getLogger(Debugger.class.getName());
	
	/**
	 * Outputs debugging data to the console, if debugging mode is on.
	 * @param text String 
	 * @param className String
	 */
	public static void printLog(String text, String className) {
		printLog(text, className, Level.INFO);
	}

	/**
	 * Outputs debugging data to the console, if debugging mode is on.
	 * @param text Sting 
	 * @param className String
	 * @param level Level
	 */
	public static void printLog(String text, String className, Level level) {
		if (isDebugging()) {
			logger.log(level, text + " - " + className);
		}
	}

	/**
	 * Returns a boolean indicating if the application is in debugging mode or not.
	 * @return boolean 
	 */
	public static boolean isDebugging() {
		return Debugger.debugging;
	}
	
	/**
	 * Activate or deactivates debugging functionality for the application. 
	 * @param debugging boolean 
	 */
	public static void setDebugging(boolean debugging) {
		Debugger.debugging = debugging;
	}
}
