/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoir.service;

import java.sql.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import memoir.Person;

/**
 *
 * @author Roger
 */
@Stateless
@Path("memoir.person")
public class PersonFacadeREST extends AbstractFacade<Person> {

    @PersistenceContext(unitName = "MemoirRESTPU")
    private EntityManager em;

    public PersonFacadeREST() {
        super(Person.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Person entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Person entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Person find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("findByAddress/{address}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findAddress(@PathParam("address") String address) {
        Query q = em.createNamedQuery("Person.findByPAddress");
        q.setParameter("pAddress", address);
        return q.getResultList();
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findAll() {
        return super.findAll();
    }

    @GET
    @Path("findByDateOfBirth/{dob}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByDateOfBirth(@PathParam("dob") Date dob) {
        Query q = em.createNamedQuery("Person.findByPDob");
        q.setParameter("pDob", dob);
        return q.getResultList();
    }

    @GET
    @Path("findByFirstName/{firstName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByFirstName(@PathParam("firstName") String fName) {
        Query q = em.createNamedQuery("Person.findByPFirstName");
        q.setParameter("pFirstName", fName);
        return q.getResultList();
    }

    @GET
    @Path("findByGender/{gender}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByGender(@PathParam("gender") String gender) {
        Query q = em.createNamedQuery("Person.findByPGender");
        q.setParameter("pGender", gender);
        return q.getResultList();
    }

    //Task 3b
    @GET
    @Path("findByGenderStatePostcode/{gender}/{state}/{postcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByGenderStatePostcode(
            @PathParam("gender") String gender,
            @PathParam("state") String state, 
            @PathParam("postcode") String postcode) {
        TypedQuery<Person> q = em.createQuery(
                "SELECT p FROM Person p "
                + "WHERE p.pGender = :gender "
                    + "AND p.pState = :state "
                    + "AND p.pPostcode = :postcode",Person.class);
        q.setParameter("gender", gender);
        q.setParameter("state", state);
        q.setParameter("postcode", postcode);
        return q.getResultList();
    }

    @GET
    @Path("findByPostcode/{postcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByPostcode(@PathParam("postcode") String postcode) {
        Query q = em.createNamedQuery("Person.findByPPostcode");
        q.setParameter("pPostcode", postcode);
        return q.getResultList();
    }

    @GET
    @Path("findByState/{state}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findByState(@PathParam("state") String state) {
        Query q = em.createNamedQuery("Person.findByPState");
        q.setParameter("pState", state);
        return q.getResultList();
    }

    @GET
    @Path("findBySurname/{surname}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> findBySurname(@PathParam("surname") String sName) {
        Query q = em.createNamedQuery("Person.findByPSurname");
        q.setParameter("pSurname", sName);
        return q.getResultList();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
