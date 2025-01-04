# Album Art Retriever
The Album Art Retriever (AAR) is a small tool for extracting album cover art from MP3 files and writing those images to disk as a JPEG file. The AAR tool can process one MP3 file at a time, or it can batch process a folder and all of its sub-folders. This is folder processing may be useful for extracting album cover art for an entire MP3 library. When processing a folder, the AAR will look for the first MP3 file that contains album art, extract the album cover art, and writes it to disk. In folder mode processing, the AAR stops looking for MP3 files in a folder once a MP3 file with album cover art is found.  

**To extract album cover artwork for an individual MP3 file:**
1. In a terminal, navigate to location of the aar.jar file. 
2. Enter the following command:
3. `java -jar aar.jar <path to MP3 file>`

**To process a folder:**
1. In a terminal, navigate to location of the aar.jar file. 
2. Enter the following command:
3. `java -jar aar.jar <path to root folder to be processed>`
