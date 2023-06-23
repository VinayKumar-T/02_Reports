package com.vinay.reportsapi.service.impl;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vinay.reportsapi.dto.SearchRequestDto;
import com.vinay.reportsapi.dto.SearchResponceDto;
import com.vinay.reportsapi.entity.EligibilityDetails;
import com.vinay.reportsapi.repository.EligibilityRepository;
import com.vinay.reportsapi.service.ReportsService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

	private final EligibilityRepository eligibilityRepository;

	@Override
	public List<String> getUniquePlanNames() {
		return eligibilityRepository.getPlanNames();

		/*
		 * 2nd type not recommended List<EligibilityDetails> findAll =
		 * eligibilityRepository.findAll(); List<String> planNames=new ArrayList<>();
		 * findAll.stream().map(e->e.getPlanName()).distinct().forEach(e->planNames.add(
		 * e)); return planNames;
		 */
	}

	@Override
	public List<String> getUniquePlanStatus() {

		return eligibilityRepository.getPlanStatus();
		/*
		 * 2st Type not recommended List<EligibilityDetails> findAll =
		 * eligibilityRepository.findAll(); List<String> planStatus=new ArrayList<>();
		 * findAll.stream().map(e->e.getPlanStatus()).distinct().forEach(e->planStatus.
		 * add(e)); return planStatus;
		 */

	}

	@Override
	public List<SearchResponceDto> getPlans(SearchRequestDto dto) {

		EligibilityDetails ed = new EligibilityDetails();
			String name=dto.getPlanName();
		if ( name!= null && !dto.getPlanName().equals("")) {
			ed.setName(name);
		}
		String status=dto.getPlanStatus();

		if (status!= null && !dto.getPlanStatus().equals("")) {
			ed.setPlanStatus(status);
		}
		LocalDate startDate = dto.getStartDate();
		if (startDate!= null  ) {
			ed.setPlanStartDate(startDate);
		}
		LocalDate endDate = dto.getEndDate();
		if (endDate != null ) {
			ed.setPlanEndDate(endDate);
		}
		// Example is a Interface which uses to generate filter querry
		// used to create Dynamic where conditions
		Example<EligibilityDetails> exampleQueryBuilder = Example.of(ed);
		List<EligibilityDetails> eligibilityDetails = eligibilityRepository.findAll(exampleQueryBuilder);

		Function<EligibilityDetails, SearchResponceDto> function = new Function<EligibilityDetails, SearchResponceDto>() {

			@Override
			public SearchResponceDto apply(EligibilityDetails t) {
				SearchResponceDto response = new SearchResponceDto();
				BeanUtils.copyProperties(t, response);
				return response;
			}
		};
		List<SearchResponceDto> searchResponse = eligibilityDetails.stream().map(function).collect(Collectors.toList());

		return searchResponse;

		/*
		 * Type 2 NOT RECOMMENDED List<EligibilityDetails> findAll =
		 * eligibilityRepository.findAll(); List<EligibilityDetails> collect =
		 * findAll.stream().filter(e->e.getName().equals(dto.getPlanName()))
		 * .filter(e->e.getPlanStatus().equals(dto.getPlanStatus()))
		 * .filter(e->e.getPlanStartDate().equals(dto.getStartDate()))
		 * .filter(e->e.getPlanEndDate().equals(dto.getEndDate()))
		 * .collect(Collectors.toList());
		 * 
		 * List<SearchResponceDto> response=new ArrayList<>();
		 * 
		 * for (EligibilityDetails eligibleDetails : collect) {
		 * 
		 * SearchResponceDto responseDto=new SearchResponceDto();
		 * 
		 * BeanUtils.copyProperties(eligibleDetails, responseDto);
		 * 
		 * response.add(responseDto);
		 * 
		 * }
		 */

	}

	@Override
	public void generateExcel(HttpServletResponse responseExcel) throws Exception {
		List<EligibilityDetails> entities = eligibilityRepository.findAll();

		HSSFWorkbook workbook = HSSFWorkbookFactory.createWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow headerRow = sheet.createRow(0);
		headerRow.createCell(1).setCellValue("Name");
		headerRow.createCell(2).setCellValue("email");
		headerRow.createCell(2).setCellValue("mobile");
		headerRow.createCell(3).setCellValue("sex");
		headerRow.createCell(4).setCellValue("ssn");

		entities.forEach(entity -> {
			int r = 1;

			HSSFRow row = sheet.createRow(r);
			row.createCell(0).setCellValue(entity.getName());
			row.createCell(1).setCellValue(entity.getEMail());
			row.createCell(2).setCellValue(entity.getMobileNum());
			row.createCell(3).setCellValue(entity.getSex());
			row.createCell(4).setCellValue(entity.getSSN());
			r++;
		});

		ServletOutputStream outputStream = responseExcel.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

	@Override
	public void generatePdf(HttpServletResponse responsePdf) throws Exception {
		List<EligibilityDetails> entities = eligibilityRepository.findAll();
		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, responsePdf.getOutputStream());
		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Search Reports", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 2.0f, 3.5f, 3.0f, 1.0f, 2.5f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		cell.setPhrase(new Phrase("Name"));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Email"));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Mobile"));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Gender"));
		table.addCell(cell);

		cell.setPhrase(new Phrase("SSN"));
		table.addCell(cell);

		for (EligibilityDetails eligibilityInfo : entities) {
			table.addCell(eligibilityInfo.getName());
			table.addCell(eligibilityInfo.getEMail());
			table.addCell(String.valueOf(eligibilityInfo.getMobileNum()));
			table.addCell(String.valueOf(eligibilityInfo.getSex()));
			table.addCell(String.valueOf(eligibilityInfo.getSSN()));
		}
		document.add(table);
		document.close();

	}

}
