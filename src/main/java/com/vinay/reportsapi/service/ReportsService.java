package com.vinay.reportsapi.service;

import java.util.List;

import com.vinay.reportsapi.dto.SearchRequestDto;
import com.vinay.reportsapi.dto.SearchResponceDto;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportsService {
	
	public List<String> getUniquePlanNames();
	public List<String> getUniquePlanStatus();
	public List<SearchResponceDto> getPlans(SearchRequestDto searchRequestDto);
	public void generateExcel(HttpServletResponse responseExcel) throws Exception;
	public void generatePdf(HttpServletResponse responsePdf) throws Exception;

	

}
