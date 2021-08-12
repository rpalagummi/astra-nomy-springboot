package com.datastax.yasa.docapi.person;

import java.io.Serializable;
import java.util.List;

import com.datastax.stargate.sdk.doc.Collection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Collection("person")
public class Person implements Serializable {
    
	/** Serial. */
    private static final long serialVersionUID = 2798538288964412234L;

    private long id = 0;
    
    private String content;
    
    private String firstname;
    
    private String lastname;
    
    private int age = 0;
    
    private List<String> countries;
    
    private Address address;

    /**
     * Defaut Constructor.
     */
    public Person() {}
    
    /**
     * Defaut Constructor.
     */
    public Person(String first, String last) {
        this.firstname = first;
        this.lastname  = last;
    }
    
    
    /**
     * Defaut Constructor.
     */
    public Person(String first, String last, int age, Address a) {
        this.firstname = first;
        this.lastname  = last;
        this.age = age;
        this.address = a;
    }
    
    
    
    public static class Address {
        private String street;
    	private String city;
        private int zipCode;
        public Address() {}
        public Address(String street, String city, int zip) {
            this.street = street;
        	this.city = city;
            this.zipCode = zip;
                    
        }
        
        
        public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		/**
         * Getter accessor for attribute 'city'.
         *
         * @return
         *       current value of 'city'
         */
        public String getCity() {
            return city;
        }
        /**
         * Setter accessor for attribute 'city'.
         * @param city
         * 		new value for 'city '
         */
        public void setCity(String city) {
            this.city = city;
        }
        /**
         * Getter accessor for attribute 'zipCode'.
         *
         * @return
         *       current value of 'zipCode'
         */
        public int getZipCode() {
            return zipCode;
        }
        /**
         * Setter accessor for attribute 'zipCode'.
         * @param zipCode
         * 		new value for 'zipCode '
         */
        public void setZipCode(int zipCode) {
            this.zipCode = zipCode;
        }
    }


    /**
     * Getter accessor for attribute 'age'.
     *
     * @return
     *       current value of 'age'
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter accessor for attribute 'age'.
     * @param age
     * 		new value for 'age '
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Getter accessor for attribute 'address'.
     *
     * @return
     *       current value of 'address'
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Setter accessor for attribute 'address'.
     * @param address
     * 		new value for 'address '
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Getter accessor for attribute 'firstname'.
     *
     * @return
     *       current value of 'firstname'
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Setter accessor for attribute 'firstname'.
     * @param firstname
     * 		new value for 'firstname '
     * @return 
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Getter accessor for attribute 'lastname'.
     *
     * @return
     *       current value of 'lastname'
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Setter accessor for attribute 'lastname'.
     * @param lastname
     * 		new value for 'lastname '
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Getter accessor for attribute 'contries'.
     *
     * @return
     *       current value of 'contries'
     */
    public List<String> getCountries() {
        return countries;
    }

    /**
     * Setter accessor for attribute 'contries'.
     * @param contries
     * 		new value for 'contries '
     */
    public void setConutries(List<String> contries) {
        this.countries = contries;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
