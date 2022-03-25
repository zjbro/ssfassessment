package sg.edu.nus.iss.ssfassessment;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import sg.edu.nus.iss.ssfassessment.Model.Quotation;
import sg.edu.nus.iss.ssfassessment.Service.QuotationService;

@SpringBootTest
class SsfassessmentApplicationTests {

	@Autowired
	private QuotationService qService;

	@Test
	void returnGetQuotations(){
		List<String> cart = new ArrayList<>();
		cart.add("durian");
		cart.add("plum");
		cart.add("pear");

		Quotation quotations = new Quotation();
		quotations = qService.getQuotations(cart).get();
		Assertions.assertNotNull(quotations);
		
	}

}
