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

import com.italtel.monitoring.fe.entity.AlertRule;
import com.italtel.monitoring.fe.entity.AlertRules;
import com.italtel.monitoring.fe.entity.DashboardTypes;
import com.italtel.monitoring.fe.entity.InventoryNode;
import com.italtel.monitoring.fe.entity.InventoryNodes;
import com.italtel.monitoring.fe.entity.InventoryService;
import com.italtel.monitoring.fe.entity.InventoryServices;
import com.italtel.monitoring.fe.entity.Job;
import com.italtel.monitoring.fe.entity.Jobs;
import com.italtel.monitoring.fe.entity.Slice;
import com.italtel.monitoring.fe.entity.Slices;
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

	@POST
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

	@PUT
	@Path("/service")
	@WebMethod(operationName = "updateService")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> updateInventoryService(@WebParam(
			name = "service") InventoryService inventoryService);

	/********************
	 * Slice *
	 ********************/

	@POST
	@Path("/slice")
	@WebMethod(operationName = "createSlice")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Slice> createSlice(@WebParam(
			name = "slice") Slice slice);

	@GET
	@Path("/slice/{name}")
	@WebMethod(operationName = "getSlice")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Slice> getSlice(
			@PathParam("name") @WebParam(name = "name") String name);

	@DELETE
	@Path("/slice/{name}")
	@WebMethod(operationName = "deleteSlice")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteSlice(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/slice")
	@WebMethod(operationName = "listSlices")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Slices> listSlices();

	@GET
	@Path("/slice/template")
	@WebMethod(operationName = "getSliceDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Slice> getSliceDefault();

	@PUT
	@Path("/slice")
	@WebMethod(operationName = "updateSlice")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Slice> updateSlice(@WebParam(
			name = "slice") Slice slice);


	/********************
	 * InventoryNode *
	 ********************/
	@POST
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

	@GET
	@Path("/service/job/{name}")
	@WebMethod(operationName = "getServiceNameByJob")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<InventoryService> getServiceNameByJob(@PathParam("name") @WebParam(
			name = "name") String name);
	
	/********************
	 * JOB              *
	 ********************/
	@POST
	@Path("/job")
	@WebMethod(operationName = "createJob")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Job> createJob(@WebParam(
			name = "job") Job job);

	@GET
	@Path("/job/{name}")
	@WebMethod(operationName = "getJob")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Job> getJob(
			@PathParam("name") @WebParam(name = "name") String name);

	@DELETE
	@Path("/job/{name}")
	@WebMethod(operationName = "deleteJob")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteJob(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/job")
	@WebMethod(operationName = "listJobs")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Jobs> listJobs();

	@GET
	@Path("/job/template")
	@WebMethod(operationName = "getJobDefault")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Job> getJobDefault();

	@PUT
	@Path("/job")
	@WebMethod(operationName = "updateJob")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<Job> updateJob(@WebParam(
			name = "job") Job job);
	
	@GET
	@Path("/job/dashboardType")
	@WebMethod(operationName = "listDashboardType")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<DashboardTypes> listDashboardType();
	
	/********************
	 * ALERT            *
	 ********************/
	@POST
	@Path("/alertrule")
	@WebMethod(operationName = "createAlertRule")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<AlertRule> createAlertRule(@WebParam(
			name = "alert") AlertRule alertRule);

	@GET
	@Path("/alertrule/{name}")
	@WebMethod(operationName = "getAlertRule")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<AlertRule> getAlertRule(
			@PathParam("name") @WebParam(name = "name") String name);

	@DELETE
	@Path("/alertrule/{name}")
	@WebMethod(operationName = "deleteAlertRule")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<?> deleteAlertRule(@PathParam("name") @WebParam(
			name = "name") String name);

	@GET
	@Path("/alertrule")
	@WebMethod(operationName = "listAlertRules")
	@WebResult(name = "return")
	@Produces(MediaType.APPLICATION_JSON)
	public Result<AlertRules> listAlertRules();

	
}
