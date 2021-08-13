package com.datastax.enterprise.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.datastax.astra.sdk.AstraClient;
import com.datastax.enterprise.banking.PendingTransaction;
import com.datastax.enterprise.banking.TransactionMapper;
import com.datastax.enterprise.docapi.person.Person;
import com.datastax.enterprise.docapi.person.PersonRepository;
import com.datastax.stargate.sdk.doc.ApiDocument;
import com.datastax.stargate.sdk.rest.ApiRestClient;
import com.datastax.stargate.sdk.rest.TableClient;
import com.datastax.stargate.sdk.rest.domain.SearchTableQuery;


/**
 * Home Controller, we want to show the gate.
 *
 * @author Ram Palagummi 
 */
@Controller
public class HomeController {

    /** View name. */
    private static final String HOME_VIEW = "home";
    
    /** Data Access. */
    private final PersonRepository homeRepository;
    
    @Autowired
	private AstraClient astraClient;
    
    private static ApiRestClient clientApiRest;
    
    private static final String WORKING_KEYSPACE = "enterprise";
    private static final String WORKING_TABLE    = "pendingtransactions_by_correlationid";
    
    
    /**
     * Injection with Constructor.
     */
    public HomeController(PersonRepository homeRepository) {
		this.homeRepository = homeRepository;
	}
    
	
	@GetMapping("/")
    public String show(Model model) 
    throws Exception {
		
		List<ApiDocument<Person>> persons =  homeRepository.findAll().collect(Collectors.toList());
//		List<ApiDocument<Person>> persons =  homeRepository.findPersonByLastName("Palagummi").getResults();
        model.addAttribute("persons", persons);
        
        return HOME_VIEW;
    }
	
	
	@GetMapping("/hackathon")
    public String hackathon(Model model) 
    throws Exception {
		
//		clientApiRest = astraClient.apiStargateData();
// 		 TableClient transactionTable = clientApiRest.keyspace(WORKING_KEYSPACE).table(WORKING_TABLE);
//		 List<PendingTransaction> list = transactionTable.search(SearchTableQuery.builder()
//	             .where("status").isEqualsTo("Pending")
//	             .build(), new TransactionMapper()).getResults();
//		 
//		 model.addAttribute("transactions", list);
		
        return "hackathon";
    }
}
