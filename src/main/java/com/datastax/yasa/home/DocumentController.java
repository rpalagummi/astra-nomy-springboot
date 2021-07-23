package com.datastax.yasa.home;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.datastax.stargate.sdk.doc.ApiDocument;
import com.datastax.yasa.docapi.banking.PendingTransaction;
import com.datastax.yasa.docapi.person.Person;
import com.datastax.yasa.docapi.person.Person.Address;
import com.datastax.yasa.docapi.person.PersonModel;
import com.datastax.yasa.docapi.person.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DocumentController {
	
	@Autowired
	private PersonRepository homeRepository;
	
	ObjectMapper mapper = new ObjectMapper();

	
	/**
	 * Injection with Constructor.
	 */
	public DocumentController(PersonRepository homeRepository) {
		this.homeRepository = homeRepository;
	}
	
	@GetMapping("/createnew")
	  public String greetingForm(@RequestParam(required = false) String id, Model model) {
		
		PersonModel personModel = new PersonModel();
		if(id != null && !id.isEmpty()) {
			
			try {
				Person person = homeRepository.find(id).get();
				String jsonString = mapper.writeValueAsString(person);//mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
	        	personModel.setDocId(id);
	        	personModel.setContent(jsonString);
	        	System.out.println(jsonString);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        } 
		}
	    model.addAttribute("personModel", personModel);
	    return "greeting";
	  }

	
	  @PostMapping("/createdoc")
	  public String greetingSubmit(@ModelAttribute PersonModel personModel, Model model) throws JsonMappingException, JsonProcessingException {
	    
	   
	    Person person = mapper.readValue(personModel.getContent(), Person.class);
	    String id = homeRepository.create(person);
	    personModel.setDocId(id);
	    
	    model.addAttribute("personModel", personModel);
	    
	    return "result";
	  }
	  
	  
	  @GetMapping("/update")
	  public String updateDocument(@RequestParam(required = false) String id, Model model) {
		
		PersonModel personModel = new PersonModel();
		if(id != null && !id.isEmpty()) {
			
			try {
				Person person = homeRepository.find(id).get();
				String jsonString = mapper.writeValueAsString(person);//mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
	        	personModel.setDocId(id);
	        	personModel.setContent(jsonString);
	        	System.out.println(jsonString);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        } 
		}
	    model.addAttribute("personModel", personModel);
	    return "update";
	  }
	  
	  @PostMapping("/updatedoc")
	  public String updateDoc(@ModelAttribute PersonModel personModel, Model model) throws JsonMappingException, JsonProcessingException {
		  System.out.println("Document ID = "+personModel.getDocId());
		  
		  if(personModel.getDocId() != null && !personModel.getDocId().isEmpty()){
			  
//			  if(!personModel.getSubDocumentPath().isEmpty()) {
//				if(personModel.getSubDocumentPath().equals("address")){
				  
					System.out.println("Updating the Sub Document \"address\" ");
					Address address = mapper.readValue(personModel.getContent(), Address.class);
					System.out.println(" Updating to City : "+address.getCity());
					homeRepository.updateSubDocument(personModel.getDocId(),"address", address); 
/*
					
				}else if(personModel.getSubDocumentPath().equals("firstname")){
					System.out.println("Updating the Sub Document \"firstname\" with "+personModel.getContent());
					homeRepository.updateSubDocument(personModel.getDocId(),"firstname", personModel.getContent()); 
					
				}else if(personModel.getSubDocumentPath().equals("lastname")){
					
					System.out.println("Updating the Sub Document \"lastname\" ");
					homeRepository.updateSubDocument(personModel.getDocId(),"lastname", personModel.getContent()); 
					
				}else if(personModel.getSubDocumentPath().equals("age")){
					
					System.out.println("Updating the Sub Document \"age\" ");
					homeRepository.updateSubDocument(personModel.getDocId(),"age", Integer.getInteger(personModel.getContent())); 
				}
			  }else {
				  System.out.println("Updating the ENTIRE DOCUMENT ");
				  Person person = mapper.readValue(personModel.getContent(), Person.class);
				  homeRepository.updateDocument(personModel.getDocId(), person);
			  }
*/
 
			  
		  }else {
			  System.out.println("Oooops Document ID can't be EMPTY or NULL");
		  }
		  
		  
		  List<ApiDocument<Person>> persons =  homeRepository.findAll().collect(Collectors.toList());
			
//			List<ApiDocument<Person>> persons =  homeRepository.findPersonByLastName("Palagummi").getResults();

	        model.addAttribute("persons", persons);
	      return "home";
	  }

	  @GetMapping("/updateSubDoc")
	  public String updateSubDocument(@RequestParam String id, @ModelAttribute PersonModel personModel, Model model) throws JsonMappingException, JsonProcessingException {
		  
		  System.out.println("Document ID = "+id);
		  
		  Person person = mapper.readValue(personModel.getContent(), Person.class);
		  
		  
		  if(person.getAddress() != null) {
			  System.out.println("Updating Address ........ ");
//			  homeRepository.updateSubDocument(id,"address", Address.class);
		  }
		  
		  
		  List<ApiDocument<Person>> persons =  homeRepository.findAll().collect(Collectors.toList());
			
//			List<ApiDocument<Person>> persons =  homeRepository.findPersonByLastName("Palagummi").getResults();

	        model.addAttribute("persons", persons);
	      return "home";
	  }
	  
	  @GetMapping("/delete")
	  public String deleteDoc(@RequestParam String id, Model model) {
		  System.out.println("String ID = "+id);
		  
		  homeRepository.deleteDocument(id);
		  
		  List<ApiDocument<Person>> persons =  homeRepository.findAll().collect(Collectors.toList());
			
//			List<ApiDocument<Person>> persons =  homeRepository.findPersonByLastName("Palagummi").getResults();

	        model.addAttribute("persons", persons);
	      return "home";
	  }
	  
}