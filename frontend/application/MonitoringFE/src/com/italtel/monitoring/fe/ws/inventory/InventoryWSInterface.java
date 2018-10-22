package com.italtel.monitoring.fe.ws.inventory;

import javax.ejb.Local;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.italtel.monitoring.fe.entity.InventoryMetric;
import com.italtel.monitoring.fe.entity.InventoryMetrics;
import com.italtel.monitoring.fe.entity.InventoryNode;
import com.italtel.monitoring.fe.entity.InventoryNodes;
import com.italtel.monitoring.fe.entity.InventoryService;
import com.italtel.monitoring.fe.entity.InventoryServices;
import com.italtel.monitoring.fe.ws.entity.Result;

@Path("/fe")
@Local
@WebService
public interface InventoryWSInterface {

	@GET
	@Path("/test")
	@WebMethod(operationName = "test")
	@WebResult(name = "return")
	public String test();

	/********************
	 * InventoryService *
	 ********************/

	@PUT
	@Path("/inventoryService")
	@WebMethod(operationName = "createInventoryService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> createInventoryService(@WebParam(
			name = "inventoryService") InventoryService inventoryService);

	@GET
	@Path("/inventoryService/{name}")
	@WebMethod(operationName = "getInventoryService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> getInventoryService(
			@PathParam("name") @WebParam(name = "name") String name);

	@DELETE
	@Path("/inventoryService/{name}")
	@WebMethod(operationName = "deleteInventoryService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteInventoryService(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/inventoryService")
	@WebMethod(operationName = "listInventoryServices")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryServices> listInventoryServices();

	@GET
	@Path("/inventoryService/template")
	@WebMethod(operationName = "getInventoryServiceDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> getInventoryServiceDefault();

	@POST
	@Path("/inventoryService")
	@WebMethod(operationName = "updateInventoryService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> updateInventoryService(@WebParam(
			name = "inventoryService") InventoryService inventoryService);

	/********************
	 * InventoryNode *
	 ********************/
	@PUT
	@Path("/inventoryNode")
	@WebMethod(operationName = "createInventoryNode")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNode> createInventoryNode(@WebParam(
			name = "inventoryNode") InventoryNode inventoryNode);

	@GET
	@Path("/inventoryNode/{name}")
	@WebMethod(operationName = "getInventoryNode")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNode> getInventoryNode(@PathParam("name") @WebParam(
			name = "name") String name);

	@DELETE
	@Path("/inventoryNode/{name}")
	@WebMethod(operationName = "deleteInventoryNode")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteInventoryNode(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/inventoryNode")
	@WebMethod(operationName = "listInventoryNodes")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNodes> listInventoryNodes();

	@GET
	@Path("/inventoryNode/template")
	@WebMethod(operationName = "getInventoryNodeDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNode> getInventoryNodeDefault();

	/********************
	 * InventoryMetric *
	 ********************/
	@PUT
	@Path("/inventoryMetric")
	@WebMethod(operationName = "createInventoryMetric")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetric> createInventoryMetric(@WebParam(
			name = "inventoryMetric") InventoryMetric inventoryMetric);

	@GET
	@Path("/inventoryMetric/{name}")
	@WebMethod(operationName = "getInventoryMetric")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetric> getInventoryMetric(
			@PathParam("name") @WebParam(name = "name") String name);

	@DELETE
	@Path("/inventoryMetric/{name}")
	@WebMethod(operationName = "deleteInventoryMetric")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteInventoryMetric(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/inventoryMetric")
	@WebMethod(operationName = "listInventoryMetrics")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetrics> listInventoryMetrics();

	@GET
	@Path("/inventoryMetric/template")
	@WebMethod(operationName = "getInventoryMetricDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetric> getInventoryMetricDefault();

}
