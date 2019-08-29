package com.techgig.dictionary.sampledictionary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleDictionaryTests {
	private static ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<String, String>();
	public static final Pattern pt = Pattern.compile("[^A-Za-z0-9]");

	@Test
	public void importtest() {

		File file;

		try {
			file = ResourceUtils.getFile("classpath:xsb");
			try (Stream<String> stream = Files.lines(Paths.get("" + file.getPath()))) {
				stream.forEach(s -> execute(s));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		//System.out.println(dictionary.size());
		assertNotNull(dictionary);
		assertTrue(dictionary.containsKey("wordbook"));
		assertFalse(dictionary.containsKey("techgig"));

	}

	public static void execute(String c) {

		Matcher match = pt.matcher(c);
		while (match.find()) {
			String s = match.group();
			c = c.replaceAll("\\" + s, "\t");
		}
		String[] words = c.split("\t");
		for (String word : words) {
			if (!word.isEmpty() && word.length() > 1) {
				dictionary.put(word.toLowerCase(), "");
				//System.out.println(word.toLowerCase());
			}

		}

	}

}
