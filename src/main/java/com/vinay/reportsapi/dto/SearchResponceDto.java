package com.vinay.reportsapi.dto;

import lombok.Data;

@Data
public class SearchResponceDto {
	private String name;
	private Long mobileNum;
	private String eMail;
	private Character sex;
	private Long SSN;
}
