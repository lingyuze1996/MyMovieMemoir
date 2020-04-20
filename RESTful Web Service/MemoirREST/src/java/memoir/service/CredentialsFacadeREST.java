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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import memoir.Credentials;

/**
 *
 * @author Roger
 */
@Stateless
@Path("memoir.credentials")
public class CredentialsFacadeREST extends AbstractFacade<Credentials> {

    @PersistenceContext(unitName = "MemoirRESTPU")
    private EntityManager em;

    public CredentialsFacadeREST() {
        super(Credentials.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Credentials entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, Credentials entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credentials find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findAll() {
        return super.findAll();
    }

    @GET
    @Path("findByPasswordHash/{passwordHash}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Credentials> findByPasswordHash(@PathParam("passwordHash") String pw) {
        Query q = em.createNamedQuery("Credentials.findByPasswordHash");
        q.setParameter("passwordHash", pw);
        return q.getResultList();
    }

    @GET
    @Path("findByPersonId/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Credentials> findByPId(@PathParam("pId") Integer pId) {
        Query q = em.createNamedQuery("Credentials.findByPId");
        q.setParameter("pId", pId);
        return q.getResultList();
    }
        
    @GET
    @Path("findBySignUpDate/{signUpDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Credentials> findBySignUpDate(@PathParam("signUpDate") Date signDate) {
        Query q = em.createNamedQuery("Credentials.findBySignUpDate");
        q.setParameter("signUpDate", signDate);
        return q.getResultList();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
