package controllers;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtos.CustomerCardRequestDTO;
import exceptions.ServiceException;
import sevices.CustomerCardServiceRemote;

@Path("/customers/cards")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerCardController {

    private CustomerCardServiceRemote customerCardService;

    @PostConstruct
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            customerCardService = (CustomerCardServiceRemote) ctx.lookup(
                "java:global/BusinessLogicEAR/BusinessLogic/CustomerCardServiceBean!sevices.CustomerCardServiceRemote"
            );
        } catch (NamingException e) {
            throw new ServiceException("Failed to lookup EJB: " + e.getMessage(), e);
        }
    }

    @GET
    public Response getAllCustomersCards() {
        try {
            if (customerCardService == null) {
                return Response.serverError().entity("EJB Injection failed!").build();
            }
            return Response.ok(customerCardService.getAllCustomersCards()).build();
        } catch (Exception e) {
            throw new ServiceException("Error fetching customer cards", e);
        }
    }

    @GET
    @Path("/{cardNumber}")
    public Response getCustomerByCardNumber(@PathParam("cardNumber") String cardNumber) {
        try {
            return Response.ok(customerCardService.getCustomerByCardNumber(cardNumber)).build();
        } catch (Exception e) {
            throw new ServiceException("Error fetching customer with card number: " + cardNumber, e);
        }
    }

    @POST
    public Response addNewCustomerCard(CustomerCardRequestDTO request) {
        try {
            String result = customerCardService.addNewCustomerCard(request);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (Exception e) {
            throw new ServiceException("Error adding new customer card", e);
        }
    }

    @PUT
    @Path("/{cardNumber}")
    public Response updateCustomerCard(CustomerCardRequestDTO request,
                                       @PathParam("cardNumber") String cardNumber) {
        try {
            String result = customerCardService.updateCustomerCard(request, cardNumber);
            return Response.ok(result).build();
        } catch (Exception e) {
            throw new ServiceException("Error updating customer card with number: " + cardNumber, e);
        }
    }

    @DELETE
    @Path("/{cardNumber}")
    public Response deleteCustomerCard(@PathParam("cardNumber") String cardNumber) {
        try {
            String result = customerCardService.deleteCustomerCard(cardNumber);
            return Response.ok(result).build();
        } catch (Exception e) {
            throw new ServiceException("Error deleting customer card with number: " + cardNumber, e);
        }
    }
}
