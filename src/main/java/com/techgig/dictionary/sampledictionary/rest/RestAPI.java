package com.techgig.dictionary.sampledictionary.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class RestAPI {
	private static final Logger log4j = LogManager.getLogger(RestAPI.class);
	private static ConcurrentHashMap<String, String> dictionary = new ConcurrentHashMap<String, String>();
	public static final Pattern pt = Pattern.compile("[^A-Za-z0-9]");

	@PostMapping("/upload")
	public ResponseEntity<String> loadapp(@RequestParam("file") @NotNull MultipartFile file)
			throws SizeLimitExceededException {
		if (file != null) {
			try (Stream<String> stream = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))
					.lines()) {
				stream.forEach(s -> execute(s));
				return ResponseEntity.status(HttpStatus.CREATED)
						.body("Imported " + dictionary.size() + " words successfully");
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occured");

			}

		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body("File upload is mandatory");
		}

	}

	@GetMapping("/search")
	public String loadapp(@RequestParam String word) {

		if (dictionary.containsKey(word.toLowerCase())) {
			return "Word is available in the dictionary";
		} else {
			return "Word doesn't exist in the dictionary";
		}

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
				log4j.info(word);
				dictionary.put(word.toLowerCase(), "");
			}

		}

	}
}
