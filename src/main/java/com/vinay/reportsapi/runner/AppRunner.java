package com.vinay.reportsapi.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.vinay.reportsapi.entity.EligibilityDetails;
import com.vinay.reportsapi.repository.EligibilityRepository;

import lombok.RequiredArgsConstructor;

@Component
public class AppRunner implements ApplicationRunner {
	@Autowired
	private EligibilityRepository eligibilityRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		EligibilityDetails e1=new EligibilityDetails();
		
		e1.setEligibilityId(1);
		e1.setName("vinay");
		e1.setEMail("t.v@gmail.com");
		e1.setSex('M');
		e1.setSSN(12345672222l);
		e1.setPlanName("health");
		e1.setPlanStatus("Approved");;
		eligibilityRepository.save(e1);
		

		EligibilityDetails e2=new EligibilityDetails();
		e2.setEligibilityId(2);
		e2.setName("pranay");
		e2.setEMail("p.v@gmail.com");
		e2.setSex('M');
		e2.setSSN(0000005672222l);
		e2.setPlanName("bike");
		e2.setPlanStatus("denied");;
		eligibilityRepository.save(e2);

		
		EligibilityDetails e3=new EligibilityDetails();
		e3.setEligibilityId(3);
		e3.setName("jessy");
		e3.setEMail("j.v@gmail.com");
		e3.setSex('F');
		e3.setSSN(00000004444444l);
		e3.setPlanName("car");
		e3.setPlanStatus("Approved");;
		
		eligibilityRepository.save(e3);
	}

	
}
