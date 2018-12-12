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

//	@GET
//	@Path("/test")
//	@WebMethod(operationName = "test")
//	@WebResult(name = "return")
//	public String test();

	/********************
	 * InventoryService *
	 ********************/

	@PUT
	@Path("/service")
	@WebMethod(operationName = "createService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> createInventoryService(@WebParam(
			name = "service") InventoryService inventoryService);

	@GET
	@Path("/service/{name}")
	@WebMethod(operationName = "getService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> getInventoryService(
			@PathParam("name") @WebParam(name = "name") String name);

	@DELETE
	@Path("/service/{name}")
	@WebMethod(operationName = "deleteService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteInventoryService(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/service")
	@WebMethod(operationName = "listServices")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryServices> listInventoryServices();

	@GET
	@Path("/service/template")
	@WebMethod(operationName = "getServiceDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> getInventoryServiceDefault();

	@POST
	@Path("/service")
	@WebMethod(operationName = "updateService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> updateInventoryService(@WebParam(
			name = "service") InventoryService inventoryService);

	/********************
	 * InventoryNode *
	 ********************/
	@PUT
	@Path("/node")
	@WebMethod(operationName = "createNode")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNode> createInventoryNode(@WebParam(
			name = "node") InventoryNode inventoryNode);

	@GET
	@Path("/node/{name}")
	@WebMethod(operationName = "getNode")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNode> getInventoryNode(@PathParam("name") @WebParam(
			name = "name") String name);

	@DELETE
	@Path("/node/{name}")
	@WebMethod(operationName = "deleteNode")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteInventoryNode(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/node")
	@WebMethod(operationName = "listNodes")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNodes> listInventoryNodes();

	@GET
	@Path("/node/template")
	@WebMethod(operationName = "getNodeDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryNode> getInventoryNodeDefault();

	/********************
	 * InventoryMetric *
	 ********************/
	@PUT
	@Path("/metric")
	@WebMethod(operationName = "createMetric")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetric> createInventoryMetric(@WebParam(
			name = "metric") InventoryMetric inventoryMetric);

	@GET
	@Path("/metric/{name}")
	@WebMethod(operationName = "getMetric")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetric> getInventoryMetric(
			@PathParam("name") @WebParam(name = "name") String name);

	@DELETE
	@Path("/metric/{name}")
	@WebMethod(operationName = "deleteMetric")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteInventoryMetric(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/metric")
	@WebMethod(operationName = "listMetrics")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetrics> listInventoryMetrics();

	@GET
	@Path("/metric/template")
	@WebMethod(operationName = "getMetricDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryMetric> getInventoryMetricDefault();

}
