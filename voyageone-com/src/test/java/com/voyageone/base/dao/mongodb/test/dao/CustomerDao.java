package com.voyageone.base.dao.mongodb.test.dao;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface CustomerDao extends CrudRepository<Customer, String> {

    public Customer findOneByFirstName(String firstName);

    public Customer findOneByLastName(String firstName);

    public Iterable<Customer> findByFirstName(String firstName);


    public Iterable<Customer> findByFirstNameRegex(String firstName);


    public Integer countByFirstName(String firstName);


    public Iterable<Customer> findByFirstNameAndLastName(String firstName,String lastName);

    @Query(value="{ 'firstName' : ?0 ,'lastName' : ?1}", fields="{ 'firstName' : 1, 'lastName' : 1}")
    public Iterable<Customer> findByThePersonsFirstName(String firstName,String lastName);
}
