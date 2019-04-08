package com.italtel.monitoring.fe.ws.um;

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

import com.italtel.monitoring.fe.entity.um.User;
import com.italtel.monitoring.fe.ws.entity.Result;
import com.italtel.monitoring.fe.ws.um.entity.PasswordInfo;
import com.italtel.monitoring.fe.ws.um.entity.Users;


@Path("/um")
@Local
@WebService
public interface UMWSInterface {

	/*******************
	 * User Management *
	 *******************/

	@PUT
	@WebMethod(operationName = "createUser")
	@WebResult(name = "return")
	public Result<User> createUser(@WebParam(name = "user") User user);

	@POST
	@WebMethod(operationName = "updateUser")
	@WebResult(name = "return")
	public Result<User> updateUser(@WebParam(name = "user") User user);

	@POST
	@Path("/changePwd")
	@WebMethod(operationName = "changePassword")
	@WebResult(name = "return")
	public Result<?> changePassword(
			@WebParam(name = "passwordInfo") PasswordInfo passwordInfo);

	@DELETE
	@Path("/{username}")
	@WebMethod(operationName = "deleteUser")
	@WebResult(name = "return")
	public Result<?> deleteUser(@PathParam("username") @WebParam(
			name = "username") String username);

	@GET
	@WebMethod(operationName = "listUsers")
	@WebResult(name = "return")
	public Result<Users> listUsers();

	@GET
	@Path("/{username}")
	@WebMethod(operationName = "getUser")
	@WebResult(name = "return")
	public Result<User> getUser(@PathParam("username") @WebParam(
			name = "username") String username);

	@GET
	@Path("/template")
	@WebMethod(operationName = "getDefaultUser")
	@WebResult(name = "return")
	public Result<User> getDefaultUser();	

}
