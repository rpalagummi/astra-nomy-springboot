package com.datastax.enterprise.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.datastax.enterprise.iot.CSV;
import com.datastax.enterprise.iot.IoTDocumentAPIRepository;
import com.datastax.enterprise.iot.Power;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class IoTController {

	
	@Autowired
	private IoTDocumentAPIRepository iotRepository;
	
	
	String IOT_DATA_FILEPATH = "household-power-consumption-QueryResult.csv";
	
	 @GetMapping("/simulateIoT")
	  public String simulateIoT(Model model) throws FileNotFoundException, IOException {
	    
		 System.out.println("Simulating IoT Data ....");
		 List<Power> iotsensors = convertCSV2JSON(IOT_DATA_FILEPATH);
		 int i=0;
		 for(Power power : iotsensors) {
			 iotRepository.createIoTDocument(power);
			 i++;
			 if(i==100) break; // if its a large file, upload is restricted to first 100 records 
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
	    	        Power power = mapper.convertValue(obj, Power.class);
	    	        iotList.add(power);
	    	    }
	    	    
	    	}
	    	return iotList;

	    }
	
}
