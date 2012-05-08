package at.subera.memento.examples;

import java.io.File;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.*;
import com.drew.metadata.exif.ExifIFD0Directory;

public class MetadataExample {
	public static void main(String[] args) throws Exception {
		File jpegFile = new File("D:\\bilder\\test.JPG");
		Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
		
		for (Directory directory : metadata.getDirectories()) {
		    for (Tag tag : directory.getTags()) {
		        System.out.println(tag);
		    }
		}
		
		Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
		int TAG_RATING = 0x4746;
		if (directory.containsTag(TAG_RATING)) {
			int r = directory.getInt(TAG_RATING);
			System.out.println("Rating is: " + r);
			//0x4746
		}
	}
}
