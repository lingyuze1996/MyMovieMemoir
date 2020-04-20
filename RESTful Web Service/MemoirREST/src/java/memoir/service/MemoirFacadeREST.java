/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memoir.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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
import memoir.Memoir;

/**
 *
 * @author Roger
 */
@Stateless
@Path("memoir.memoir")
public class MemoirFacadeREST extends AbstractFacade<Memoir> {

    @PersistenceContext(unitName = "MemoirRESTPU")
    private EntityManager em;

    public MemoirFacadeREST() {
        super(Memoir.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Memoir entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Memoir entity) {
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
    public Memoir find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findAll() {
        return super.findAll();
    }

    @GET
    @Path("findByCinemaId/{cId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByCinemaId(@PathParam("cId") Integer cId) {
        Query q = em.createNamedQuery("Memoir.findByCId");
        q.setParameter("cId", cId);
        return q.getResultList();
    }

    @GET
    @Path("findByComment/{comment}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByComment(@PathParam("comment") String comment) {
        Query q = em.createNamedQuery("Memoir.findByMComment");
        q.setParameter("mComment", comment);
        return q.getResultList();
    }

    @GET
    @Path("findByMovieName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByMovieName(@PathParam("name") String name) {
        Query q = em.createNamedQuery("Memoir.findByMMovieName");
        q.setParameter("mMovieName", name);
        return q.getResultList();
    }

    //Task 3c
    @GET
    @Path("findByMovieNameAndCinemaName/{movieName}/{cinemaName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByMovieNameAndCinemaName(
            @PathParam("movieName") String mName,
            @PathParam("cinemaName") String cName) {
        TypedQuery<Memoir> q = em.createQuery(
                "SELECT m FROM Memoir m "
                + "WHERE m.mMovieName = :mName "
                    + "AND m.cId.cName = :cName", Memoir.class);
        q.setParameter("mName", mName);
        q.setParameter("cName", cName);
        return q.getResultList();
    }

    @GET
    @Path("findByMovieReleaseDate/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByMovieReleaseDate(@PathParam("date") Date date) {
        Query q = em.createNamedQuery("Memoir.findByMMovieReleaseDate");
        q.setParameter("mMovieReleaseDate", date);
        return q.getResultList();
    }

    @GET
    @Path("findByPersonId/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByPersonId(@PathParam("pId") Integer pId) {
        Query q = em.createNamedQuery("Memoir.findByPId");
        q.setParameter("pId", pId);
        return q.getResultList();
    }

    @GET
    @Path("findByRating/{rating}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByRating(@PathParam("rating") BigDecimal rating) {
        Query q = em.createNamedQuery("Memoir.findByMRating");
        q.setParameter("mRating", rating);
        return q.getResultList();
    }

    //Task 3d
    @GET
    @Path("findByRatingAndLocationPostcode/{rating}/{postcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByRatingAndLocationPostcode(
            @PathParam("rating") BigDecimal rating,
            @PathParam("postcode") String postcode) {
        Query q = em.createNamedQuery("Memoir.findByRatingAndLocationPostcode");
        q.setParameter("mRating", rating);
        q.setParameter("cPostcode", postcode);
        return q.getResultList();
    }

    @GET
    @Path("findByWatchingDatetime/{datetime}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Memoir> findByWatchingDatetime(@PathParam("datetime") Timestamp datetime) {
        Query q = em.createNamedQuery("Memoir.findByMWatchingDatetime");
        q.setParameter("mWatchingDatetime", datetime);
        return q.getResultList();
    }

    //Task 4c
    @GET
    @Path("findHighestRatingMovies/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object findHighestRatingMovies(@PathParam("pId") Integer pId) {
        Query q = em.createQuery(
                "SELECT m.mMovieName, OPERATOR('DateToString', m.mMovieReleaseDate), m.mRating "
                + "FROM Memoir m "
                + "WHERE m.mRating = (SELECT MAX(m.mRating) FROM Memoir m) "
                    + "AND m.pId.pId = :pId ", Object[].class);
        q.setParameter("pId", pId);
        List<Object[]> queryList = q.getResultList();
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject jsonObject = Json.createObjectBuilder().
                    add("MovieName", (String) row[0])
                    .add("Rating", (BigDecimal) row[2])
                    .add("ReleaseDate", (String) row[1]).build();
            arrayBuilder.add(jsonObject);
        }
        JsonArray jsonArray = arrayBuilder.build();
        return jsonArray;
    }

    //Task 4b
    @GET
    @Path("findMonthTotalMovies/{pId}/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object findMonthTotalMovies(
            @PathParam("pId") Integer pId,
            @PathParam("year") Integer year) {
        Query q = em.createQuery(
                "SELECT EXTRACT(MONTH FROM m.mWatchingDatetime), COUNT(m.mId) "
                + "FROM Memoir m "
                + "WHERE EXTRACT(YEAR FROM m.mWatchingDatetime) = :year "
                    + "AND m.pId.pId = :pId "
                + "GROUP BY EXTRACT(MONTH FROM m.mWatchingDatetime)", Object[].class);
        q.setParameter("pId", pId);
        q.setParameter("year", year);
        List<Object[]> queryList = q.getResultList();
        
        List<Integer[]> outcomeList = new ArrayList<Integer[]>();
        for (int mm = 1; mm <= 12; mm++) {
            Integer[] singleMonth = new Integer[2];
            singleMonth[0] = mm;
            singleMonth[1] = 0;
            outcomeList.add(singleMonth);
            for (Object[] row : queryList) {
                if ((int) row[0] == mm) {
                    singleMonth[1] = ((Long) row[1]).intValue();
                    break;
                }
            }
        }       
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Integer[] row : outcomeList) {
            JsonObject jsonObject = Json.createObjectBuilder().
                    add("Month", intToMonth((int) row[0]))
                    .add("TotalMoviesWatched", (int) row[1]).build();
            arrayBuilder.add(jsonObject);
        }
        JsonArray jsonArray = arrayBuilder.build();
        return jsonArray;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memoir> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    //Task 4e
    @GET
    @Path("findRemakeMovies/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object findRemakeMovies(@PathParam("pId") Integer pId) {     
        Query q = em.createQuery(
                "SELECT m1.mMovieName, EXTRACT(YEAR FROM m1.mMovieReleaseDate), EXTRACT(YEAR FROM m2.mMovieReleaseDate) "
                + "FROM Memoir m1 join Memoir m2 "
                    + "ON m1.mMovieName = m2.mMovieName AND m1.mMovieReleaseDate < m2.mMovieReleaseDate "
                + "WHERE m1.pId.pId = :pId", Object[].class);
        q.setParameter("pId", pId);
        List<Object[]> queryList = q.getResultList();
    
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();      
        for (Object[] row : queryList) {
            JsonObject jsonObject = Json.createObjectBuilder().
                    add("movieName", (String) row[0])
                    .add("FirstRelease", (int) row[1])
                    .add("Remake", (int) row[2]).build();
            arrayBuilder.add(jsonObject);                              
        }
        JsonArray jsonArray = arrayBuilder.build();
        return jsonArray;
    }
    
    //Task 4d
    @GET
    @Path("findSameYearMovies/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object findSameYearMovies(@PathParam("pId") Integer pId) {
        Query q = em.createQuery(
                "SELECT m.mMovieName, EXTRACT(YEAR FROM m.mMovieReleaseDate) "
                + "FROM Memoir m "
                + "WHERE EXTRACT(YEAR FROM m.mMovieReleaseDate) = EXTRACT(YEAR FROM m.mWatchingDatetime) "
                    + "AND m.pId.pId = :pId ", Object[].class);
        q.setParameter("pId", pId);
        List<Object[]> queryList = q.getResultList();

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject jsonObject = Json.createObjectBuilder().
                    add("MovieName", (String) row[0])
                    .add("ReleaseYear", (int) row[1]).build();
            arrayBuilder.add(jsonObject);
        }
        JsonArray jsonArray = arrayBuilder.build();
        return jsonArray;
    }

    //Task 4a
    @GET
    @Path("findSuburbTotalMovies/{pId}/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object findSuburbTotalMovies(
            @PathParam("pId") Integer pId,
            @PathParam("startDate") Date startDate,
            @PathParam("endDate") Date endDate) {
        Query q = em.createQuery(
                "SELECT m.cId.cLocationPostcode, COUNT(m.mId) "
                + "FROM Memoir m "
                + "WHERE m.mWatchingDatetime <= :endDate "
                    + "AND m.mWatchingDatetime >= :startDate "
                    + "AND m.pId.pId = :pId "
                + "GROUP BY m.cId.cLocationPostcode", Object[].class);
        q.setParameter("pId", pId);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        List<Object[]> queryList = q.getResultList();
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Object[] row : queryList) {
            JsonObject jsonObject = Json.createObjectBuilder().
                    add("SuburbPostcode", (String) row[0])
                    .add("TotalMovieWatched", (long) row[1]).build();
            arrayBuilder.add(jsonObject);
        }
        JsonArray jsonArray = arrayBuilder.build();
        return jsonArray;
    }   

    //Task 4f
    @GET
    @Path("findTopFiveRecentMovies/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object findTopFiveRecentMovies(@PathParam("pId") Integer pId) {     
        Query q = em.createQuery(
                "SELECT m.mMovieName, OPERATOR('DateToString', m.mMovieReleaseDate), m.mRating "
                        + "FROM Memoir m "
                        + "WHERE EXTRACT(YEAR FROM m.mMovieReleaseDate) = EXTRACT(YEAR FROM CURRENT_DATE) "
                            + "AND m.pId.pId = :pId "
                        + "ORDER BY m.mRating DESC ", Object[].class);
        q.setParameter("pId", pId);
        List<Object[]> queryList = q.getResultList();
    
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        int recordNumber = 0;
        for (Object[] row : queryList) {
            JsonObject jsonObject = Json.createObjectBuilder().
                    add("MovieName", (String) row[0])
                    .add("Rating",(BigDecimal) row[2])
                    .add("ReleaseDate", (String) row[1]).build();
            arrayBuilder.add(jsonObject);
            recordNumber ++;
            if (recordNumber >= 5) break;
        }
        JsonArray jsonArray = arrayBuilder.build();
        return jsonArray;
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

    public String intToMonth(int i) {
        String month = "";
        switch(i) {
            case 1: month = "January"; break;
            case 2: month = "February"; break;
            case 3: month = "March"; break;
            case 4: month = "April"; break;
            case 5: month = "May"; break;
            case 6: month = "June"; break;
            case 7: month = "July"; break;
            case 8: month = "August"; break;
            case 9: month = "September"; break;
            case 10: month = "October"; break;
            case 11: month = "November"; break;
            case 12: month = "December"; break;
            default: break;
        }
        return month;
    }

}
