package com.datastax.enterprise.home;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.enterprise.docapi.banking.BankingTransactionRepository;
import com.datastax.enterprise.docapi.iot.CSV;
import com.datastax.enterprise.docapi.iot.IoTRepository;
import com.datastax.enterprise.docapi.iot.Power;
import com.datastax.enterprise.docapi.person.Person;
import com.datastax.enterprise.docapi.person.PersonModel;
import com.datastax.enterprise.docapi.person.PersonRepository;
import com.datastax.stargate.sdk.doc.ApiDocument;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class IoTController {

	@Autowired
	private BankingTransactionRepository repository;
	
	@Autowired
	private PersonRepository homeRepository;
	
	@Autowired
	private IoTRepository iotRepository;
	
	
	
	 @GetMapping("/simulateIoT")
	  public String simulateIoT(Model model) throws FileNotFoundException, IOException {
	    
		 System.out.println("Simulating IoT Data ....");
		 List<Power> iotsensors = convertCSV2JSON("household-power-consumption-QueryResult.csv");
		 int i=0;
		 for(Power power : iotsensors) {
			 iotRepository.createIoTDocument(power);
			 i++;
			 if(i==10) break;
		 }
		 
		 System.out.println("IoT Simulation Complete with  "+i+" documents");
		 
		 model.addAttribute("iotsensor", iotsensors);
		  
	      return "iot";
	    
	  }
	 
	 public List<Power> convertCSV2JSON(String csvFile) throws FileNotFoundException, IOException{
	    	
		 List<Power> iotList = new ArrayList<Power>();
		 ObjectMapper mapper = new ObjectMapper();
	    	try (InputStream in = new FileInputStream(csvFile);) {
	    	    CSV csv = new CSV(true, ',', in );
	    	    List < String > fieldNames = null;
	    	    if (csv.hasNext()) fieldNames = new ArrayList < > (csv.next());
	    	    List < Map < String, String >> list = new ArrayList < > ();
	    	    while (csv.hasNext()) {
	    	        List < String > x = csv.next();
	    	        Map < String, String > obj = new LinkedHashMap < > ();
	    	        for (int i = 0; i < fieldNames.size(); i++) {
	    	            obj.put(fieldNames.get(i), x.get(i));
	    	        }
//	    	        list.add(obj);
	    	        Power power = mapper.convertValue(obj, Power.class);
	    	        iotList.add(power);
	    	    }
	    	    
	    	}
	    	return iotList;

	    }
	
}
