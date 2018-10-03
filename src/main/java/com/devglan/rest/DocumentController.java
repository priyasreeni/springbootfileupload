package com.devglan.rest;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.devglan.service.Document;
import com.devglan.service.DocumentService;
import com.devglan.service.ResponseMetadata;

@Controller
@RequestMapping(value = "/doc")
public class DocumentController {

	private static final Logger LOG = Logger.getLogger(DocumentController.class);

	@Autowired
	DocumentService documentService;

    @Bean
    RestTemplate restTemplate() {
          return new RestTemplate();
    }

    @Autowired
    RestTemplate rt;
    
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody ResponseMetadata handleFileUpload(@RequestParam(value = "file") MultipartFile file)
			throws IOException {
		ResponseMetadata rd = null;
		String url = "http://eventhandler-microservice:8084/event/save";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		try {
			System.out.println("DocumentController.handleFileUpload() Bipin before start");

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("eventname", "EVENT_FILE_UPLOAD_BEGINS");
			map.add("eventdescrption", "TIME: " + new Timestamp((new Date()).getTime()) + " - File Name: '"
					+ file.getOriginalFilename() + "' - File Size: " + file.getSize());

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			System.out.println("DocumentController.handleFileUpload() Bipin before here ");
			rt.postForEntity(url, request, String.class);
			System.out.println("DocumentController.handleFileUpload() Bipin before ends ");
		} catch (Throwable e) {
			System.out.println("DocumentController.handleFileUpload():Bipin before: " + e);
		}

		rd = documentService.save(file);

		try {
			System.out.println("DocumentController.handleFileUpload() Bipin after starts");
			RestTemplate rt1 = new RestTemplate();
			MultiValueMap<String, String> map1 = new LinkedMultiValueMap<String, String>();
			map1.add("eventname", "EVENT_FILE_UPLOAD_COMPLETED");
			map1.add("eventdescrption", "TIME: " + new Timestamp((new Date()).getTime()) + " - File Name: '"
					+ file.getOriginalFilename() + "' - File Size: " + file.getSize());

			HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<MultiValueMap<String, String>>(map1,
					headers);
			System.out.println("DocumentController.handleFileUpload() Bipin after here ");
			rt1.postForEntity(url, request1, String.class);
			System.out.println("DocumentController.handleFileUpload() Bipin after ends ");
		} catch (Throwable e) {
			System.out.println("DocumentController.handleFileUpload(): Bipin After " + e);
		}
		return rd;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public HttpEntity<byte[]> getDocument(@PathVariable Long id) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(documentService.getDocumentFile(id), httpHeaders, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody List<Document> getDocument() {
		return documentService.findAll();
	}

}
