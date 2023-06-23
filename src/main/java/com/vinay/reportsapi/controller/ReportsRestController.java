package com.vinay.reportsapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vinay.reportsapi.dto.SearchRequestDto;
import com.vinay.reportsapi.dto.SearchResponceDto;
import com.vinay.reportsapi.service.ReportsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReportsRestController {

	private final ReportsService reportsService;

	@GetMapping("/names")
	public ResponseEntity<List<String>> getPlanNames() {

		List<String> uniquePlanNames = reportsService.getUniquePlanNames();

		return new ResponseEntity<List<String>>(uniquePlanNames, HttpStatus.OK);
	}

	@GetMapping("/statuses")
	public ResponseEntity<List<String>> getPlanStatus() {

		List<String> uniquePlanStatus = reportsService.getUniquePlanStatus();

		return new ResponseEntity<List<String>>(uniquePlanStatus, HttpStatus.OK);

	}
	//To get body we have choosen POSTMAPPING
	//GETMAPPING we can not send body we can send only as path variable or query params
	@PostMapping("/searchresult")
	public ResponseEntity<List<SearchResponceDto>> getSearchResults(@RequestBody SearchRequestDto searchRequestDto) {
		List<SearchResponceDto> plans = reportsService.getPlans(searchRequestDto);
		return new ResponseEntity<List<SearchResponceDto>>(plans, HttpStatus.OK);
	}
	
	@GetMapping("/excel")
	public void getExcel(HttpServletResponse response) throws Exception {
	response.setContentType("application/octet-stream");
	String headerkey="Content_Disposition";
	String headervalue="attachment;filename=data.xlsx";
	response.setHeader(headerkey, headervalue);
	reportsService.generateExcel(response);
	}
	
	public void getPdf(HttpServletResponse response) throws Exception {
		
		response.setContentType("application/pdf");
		String headerKey="Content_Disposition";
		String headerValue="attachment;filename=data.pdf";
		response.setHeader(headerKey, headerValue);
		reportsService.generatePdf(response);
		
		
	}
}
