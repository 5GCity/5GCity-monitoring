package com.italtel.monitoring.fe.ws.inventory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.italtel.monitoring.fe.common.service.ConfigurationDispatcherService;
import com.italtel.monitoring.fe.common.service.conf.wrte.CommonConfService;
import com.italtel.monitoring.fe.entity.DashboardType;
import com.italtel.monitoring.fe.entity.DashboardTypes;
import com.italtel.monitoring.fe.entity.InventoryNode;
import com.italtel.monitoring.fe.entity.InventoryNodes;
import com.italtel.monitoring.fe.entity.InventoryService;
import com.italtel.monitoring.fe.entity.InventoryServiceJob;
import com.italtel.monitoring.fe.entity.InventoryServices;
import com.italtel.monitoring.fe.entity.Job;
import com.italtel.monitoring.fe.entity.Jobs;
import com.italtel.monitoring.fe.entity.Slice;
import com.italtel.monitoring.fe.entity.Slices;
import com.italtel.monitoring.fe.entity.um.RoleType;
import com.italtel.monitoring.fe.entity.um.User;
import com.italtel.monitoring.fe.utils.DBUtility;
import com.italtel.monitoring.fe.utils.OSUtils;
import com.italtel.monitoring.fe.ws.BaseWS;
import com.italtel.monitoring.fe.ws.entity.Result;

@Stateless
//@SecurityDomain("omDomain")
//@PermitAll
@WebService(
		endpointInterface = "com.italtel.monitoring.fe.ws.inventory.InventoryWSInterface")
public class InventoryWS extends BaseWS implements InventoryWSInterface {
	@EJB(
			lookup = "java:global/FRONTEND/MonitoringFE/ConfigurationDispatcherBean!com.italtel.monitoring.fe.common.service.ConfigurationDispatcherService")
	private ConfigurationDispatcherService confService;

	private final static Logger log = LoggerFactory
			.getLogger(CommonConfService.class);
	
	@Override
	public String test() {
		
		Principal myUser = getPrincipal();
		String user = myUser.getName();
		log.error("The user is :"+user);
		return "user is " + user;
	}

	/********************
	 * InventoryService *
	 ********************/
	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<InventoryService> createInventoryService(
			InventoryService inventoryService) {
		Result<InventoryService> result;

		log.info("Request received: createInventoryService {}",
				inventoryService);

		try {
			if (inventoryService.getName() != null) {
				log.debug("InventoryService created (name={})",
						inventoryService.getName());
				InventoryService service = getSingleService(inventoryService
						.getName(),true);
				if (service != null) {
					throw new Exception("service " + inventoryService.getName()
							+ " already exists");
				}
			}
			User tmpuser = getUser();
			if(tmpuser != null)
				inventoryService.setOrg(tmpuser.getOrg());
			else 
				inventoryService.setOrg(User.DEF_SYSTEM_ORG);
			InventoryService inventoryServicePers = confService
					.create(inventoryService);
			// Success result
			result = new Result<InventoryService>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(inventoryServicePers);
		} catch (Exception e) {
			log.error("Error creating a {}: {}", inventoryService,
					e.getMessage());
			// Error result
			result = new Result<InventoryService>(e);
		}
		return result;
	}

	@Override
	public Result<InventoryService> getInventoryService(String name) {
		log.info("Request received: getInventoryService name {}", name);
		Result<InventoryService> result;
		try {
			// Read
			InventoryService inventoryService = getSingleService(name,false);
			if (inventoryService == null) {
				throw new Exception("service " + name
						+ " not exists");
			}
			// Success result
			result = new Result<InventoryService>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(inventoryService);
		} catch (Exception e) {
			log.error("Error reading InventoryService with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<InventoryService>(e);
		}
		return result;
	}

	public InventoryService getSingleService(String name,Boolean toSkip) {
		InventoryService inventoryService = DBUtility
				.getNamedQuerySingleResultByName(
						confService.getEntityManager(),
						InventoryService.QUERY_READ_SERVICE,
						InventoryService.class, name);
		User tmpuser = getUser();
		if(tmpuser != null) {
			if((!toSkip) && (!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN))) {			
				if (inventoryService != null) {
					if (!(inventoryService.getOrg().equals(tmpuser.getOrg()))) {
						return null;
					}
				} else return null;
			}
		}
		return inventoryService;
	}

	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<?> deleteInventoryService(String name) {
		Result<?> result;

		log.info("Request received: deleteInventoryService name={}", name);

		try {
			// get + delete
			InventoryService inventoryService = getSingleService(name,false);
			if (inventoryService != null) {
				confService.delete(inventoryService);
			}
			// Success result
			result = new Result<Object>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("Error deleting InventoryService with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<Object>(e);
		}
		return result;
	}

	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<InventoryService> updateInventoryService(
			InventoryService inventoryService) {
		Result<InventoryService> result;

		log.info("Request received: updateInventoryService {}",
				inventoryService);
		try {

			if (inventoryService.getName() != null) {
				log.debug("InventoryService update (name={})",
						inventoryService.getName());
				InventoryService service = getSingleService(inventoryService
						.getName(),false);
				if (service == null) {
					throw new Exception("service " + inventoryService.getName()
							+ " not exists");
				}
			} else
				throw new Exception("service name is null");

			User tmpuser = getUser();
			if(tmpuser != null)
				inventoryService.setOrg(tmpuser.getOrg());
			else 
				inventoryService.setOrg(User.DEF_SYSTEM_ORG);
			InventoryService inventoryServicePers = confService
					.update(inventoryService);
			// Success result
			result = new Result<InventoryService>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(inventoryServicePers);
		} catch (Exception e) {
			log.error("Error update a {}: {}", inventoryService, e.getMessage());
			// Error result
			result = new Result<InventoryService>(e);
		}
		return result;
	}

	@Override
	public Result<InventoryServices> listInventoryServices() {

		log.info("Request received: listInventoryServices");
		try {
			// Read
			List<InventoryService> inventoryServices = getAllServices();
			return new Result<InventoryServices>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE, new InventoryServices(
							inventoryServices));

		} catch (Exception e) {
			log.error("Error reading listInventoryServices {}", e.getMessage());

			// Error result
			return new Result<InventoryServices>(e);
		}
	}

	public List<InventoryService> getAllServices() {
		List<InventoryService> inventoryServicesTemp = DBUtility
				.getNamedQueryResultList(confService.getEntityManager(),
						InventoryService.QUERY_READ_ALL_SERVICES,
						InventoryService.class);
		List<InventoryService> inventoryServices = new ArrayList<InventoryService>();
		
		User tmpuser = getUser();
		if(tmpuser != null) {
			if(!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN)) {
				for (InventoryService invServ: inventoryServicesTemp) {
					if (invServ.getOrg().equals(tmpuser.getOrg()))
						inventoryServices.add(invServ);
				}
				return inventoryServices;
			}
		} 
		return inventoryServicesTemp;		
	}

	@Override
	public Result<InventoryService> getInventoryServiceDefault() {

		log.info("Request received: getInventoryServiceDefault");
		try {
			// Success result
			return new Result<InventoryService>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE,
					confService.getDefault(InventoryService.class));
		} catch (Exception e) {
			log.error("Error getting default InventoryService: {}",
					e.getMessage());
			// Error result
			return new Result<InventoryService>(e);
		}
	}

	/********************
	 * Slice *
	 ********************/
	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<Slice> createSlice(Slice slice) {
		Result<Slice> result;

		log.info("Request received: createSlice {}",
				slice);

		try {
			if (slice.getName() != null) {
				log.debug("Slice created (name={})",
						slice.getName());
				Slice tmpslice = getSingleSlice(slice
						.getName(),true);
				if (tmpslice != null) {
					throw new Exception("slice " + tmpslice.getName()
							+ " already exists");
				}
			}
			User tmpuser = getUser();
			if(tmpuser != null)
				slice.setOrg(tmpuser.getOrg());
			else 
				slice.setOrg(User.DEF_SYSTEM_ORG);
			Slice slicePers = confService
					.create(slice);
			// Success result
			result = new Result<Slice>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(slicePers);
		} catch (Exception e) {
			log.error("Error creating a {}: {}", slice,
					e.getMessage());
			// Error result
			result = new Result<Slice>(e);
		}
		return result;
	}

	@Override
	public Result<Slice> getSlice(String name) {
		log.info("Request received: getSlice name {}", name);
		Result<Slice> result;
		try {
			// Read
			Slice slice = getSingleSlice(name,false);
			if (slice == null) {
				throw new Exception("slice " + name
						+ " not exists");
			}
			// Success result
			result = new Result<Slice>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(slice);
		} catch (Exception e) {
			log.error("Error reading Slice with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<Slice>(e);
		}
		return result;
	}

	public Slice getSingleSlice(String name,Boolean toSkip) {
		Slice slice = DBUtility
				.getNamedQuerySingleResultByName(
						confService.getEntityManager(),
						Slice.QUERY_READ_SLICE,
						Slice.class, name);
		User tmpuser = getUser();
		if(tmpuser != null) {
			if((!toSkip) && (!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN))) {			
				if (slice != null) {
					if (!(slice.getOrg().equals(tmpuser.getOrg()))) {
						return null;
					}
				} else return null;
			}
		}
		return slice;
	}

	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<?> deleteSlice(String name) {
		Result<?> result;

		log.info("Request received: deleteSlice name={}", name);

		try {
			// get + delete
			Slice slice = getSingleSlice(name,false);
			if (slice != null) {
				confService.delete(slice);
			}
			// Success result
			result = new Result<Object>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("Error deleting Slice with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<Object>(e);
		}
		return result;
	}

	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<Slice> updateSlice(Slice slice) {
		Result<Slice> result;

		log.info("Request received: updateSlice {}",
				slice);
		try {

			if (slice.getName() != null) {
				log.debug("Slice update (name={})",
						slice.getName());
				Slice tmpslice = getSingleSlice(slice
						.getName(),false);
				if (tmpslice == null) {
					throw new Exception("slice " + slice.getName()
							+ " not exists");
				}
			} else
				throw new Exception("slice name is null");

			User tmpuser = getUser();
			if(tmpuser != null)
				slice.setOrg(tmpuser.getOrg());
			else
				slice.setOrg(User.DEF_SYSTEM_ORG);
			Slice slicePers = confService
					.update(slice);
			// Success result
			result = new Result<Slice>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(slicePers);
		} catch (Exception e) {
			log.error("Error update a {}: {}", slice, e.getMessage());
			// Error result
			result = new Result<Slice>(e);
		}
		return result;
	}

	@Override
	public Result<Slices> listSlices() {

		log.info("Request received: listSlices");
		try {
			// Read
			List<Slice> slices = getAllSlices();
			return new Result<Slices>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE, new Slices(
							slices));

		} catch (Exception e) {
			log.error("Error reading listSlices {}", e.getMessage());

			// Error result
			return new Result<Slices>(e);
		}
	}

	public List<Slice> getAllSlices() {
		List<Slice> slicesTemp = DBUtility
				.getNamedQueryResultList(confService.getEntityManager(),
						Slice.QUERY_READ_ALL_SLICES,
						Slice.class);
		List<Slice> slices = new ArrayList<Slice>();
		User tmpuser = getUser();
		if(tmpuser != null) {
			if(!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN)) {
				for (Slice invServ: slicesTemp) {
					if (invServ.getOrg().equals(tmpuser.getOrg()))
						slices.add(invServ);
				}
				return slices;
			}
		} 
		return slicesTemp;
	}

	public Result<Slice> getSliceDefault() {

		log.info("Request received: getSliceDefault");
		try {
			// Success result
			return new Result<Slice>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE,
					confService.getDefault(Slice.class));
		} catch (Exception e) {
			log.error("Error getting default Slice: {}",
					e.getMessage());
			// Error result
			return new Result<Slice>(e);
		}
	}


	/********************
	 * InventoryNode *
	 ********************/
	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<InventoryNode> createInventoryNode(InventoryNode inventoryNode) {
		Result<InventoryNode> result;

		log.debug("Request received: createNode {}", inventoryNode);

		try {
			if (inventoryNode.getName() != null) {
				log.debug("InventoryNode created (name={})",
						inventoryNode.getName());
				InventoryNode node = getSingleNode(inventoryNode.getName(),true);
				if (node != null) {
					throw new Exception("node " + inventoryNode.getName()
							+ " already exists");
				}
			}
			User tmpuser = getUser();
			if(tmpuser != null)
				inventoryNode.setOrg(tmpuser.getOrg());
			else
				inventoryNode.setOrg(User.DEF_SYSTEM_ORG);
			log.info("createInventoryNode create");
			InventoryNode inventoryNodePers = confService.create(inventoryNode);
			// Success result
			result = new Result<InventoryNode>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(inventoryNodePers);
		} catch (Exception e) {
			log.error("Error creating a {}: {}", inventoryNode, e.getMessage());

			// Error result
			result = new Result<InventoryNode>(e);
		}
		return result;
	}

	@Override
	public Result<InventoryNode> getInventoryNode(String name) {
		log.info("Request received: getInventoryNode name {}", name);
		Result<InventoryNode> result;
		try {
			// get
			InventoryNode inventoryNode = getSingleNode(name,false);
			if (inventoryNode == null) {
				throw new Exception("node " + name
						+ " not exists");
			}
			// Success result
			result = new Result<InventoryNode>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(inventoryNode);
		} catch (Exception e) {
			log.error("Error reading InventoryNode with name={}: {}", name,
					e.getMessage());
			// Error result
			result = new Result<InventoryNode>(e);
		}
		return result;
	}

	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<?> deleteInventoryNode(String name) {
		Result<?> result;

		log.info("Request received: deleteInventoryNode name={}", name);

		try {
			// get + delete
			InventoryNode inventoryNode = getSingleNode(name,false);
			if (inventoryNode != null) {
				confService.delete(inventoryNode);
			}
			// Success result
			result = new Result<Object>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("Error deleting InventoryNode with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<Object>(e);
		}
		return result;
	}

	public InventoryNode getSingleNode(String name,Boolean toSkip) {
		InventoryNode inventoryNode = DBUtility
				.getNamedQuerySingleResultByName(
						confService.getEntityManager(),
						InventoryNode.QUERY_READ_NODE, InventoryNode.class,
						name);
		User tmpuser = getUser();
		if(tmpuser != null) {
			if((!toSkip) && (!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN))) {			
				if (inventoryNode != null) {
					if (!(inventoryNode.getOrg().equals(tmpuser.getOrg()))) {
						return null;
					}
				} else return null;
			}
		}
		return inventoryNode;
	}

	@Override
	public Result<InventoryNodes> listInventoryNodes() {

		log.info("Request received: listInventoryNodes");

		try {
			// Read
			List<InventoryNode> inventoryNodes = getAllNodes();
			return new Result<InventoryNodes>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE, new InventoryNodes(
							inventoryNodes));

		} catch (Exception e) {
			log.error("Error reading listInventoryNodes {}", e.getMessage());

			// Error result
			return new Result<InventoryNodes>(e);
		}
	}

	public List<InventoryNode> getAllNodes() {
		
		log.debug("Request received: getAllNodes");
		List<InventoryNode> inventoryNodesTemp= DBUtility.getNamedQueryResultList(
				confService.getEntityManager(),
				InventoryNode.QUERY_READ_ALL_NODES, InventoryNode.class);
		List<InventoryNode> inventoryNodes = new ArrayList<InventoryNode>();

		User tmpuser = getUser();
		if(tmpuser != null) {
			if(!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN)) {
				for (InventoryNode invServ: inventoryNodesTemp) {
					if (invServ.getOrg().equals(tmpuser.getOrg()))
						inventoryNodes.add(invServ);
				}
				return inventoryNodes;
			}
		} 
		return inventoryNodesTemp;
	}

	@Override
	public Result<InventoryNode> getInventoryNodeDefault() {

		log.info("Request received: getInventoryNodeDefault");
		try {
			// Success result
			return new Result<InventoryNode>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE,
					confService.getDefault(InventoryNode.class));
		} catch (Exception e) {
			log.error("Error getting default InventoryNode: {}", e.getMessage());
			// Error result
			return new Result<InventoryNode>(e);
		}
	}
	
	@Override
	public Result<InventoryService> getServiceNameByJob(String name) {
		
		log.error("Request received: getServiceNameByJob name {}", name);

		Result<InventoryService> result = new Result<InventoryService>(Result.OK_SUCCESS_CODE,
				Result.OK_SUCCESS_MESSAGE,
				null);

		boolean serviceFind = false;
		try {

			List<InventoryService> inventoryServices = getAllServices();
			for (InventoryService service : inventoryServices) {				
				if (service != null) {
					log.error("Service Name is  " + service.getName());

					List<InventoryServiceJob> serviceJobs = service.getJobs();

					for (InventoryServiceJob sn : serviceJobs) {
						Job jobData = sn.getJob();
						log.error("Job Name is  " + jobData.getName());
						if(jobData.getName().equals(name)) {
							log.error("Find Service Name " + service.getName());
							// Success result
							serviceFind = true;
							result.setData(service);
							break;
						}
					}
				}
			}
			if (serviceFind == false) {
				result.setData(null);
			}

		} catch (Exception e) {
			log.error("Error reading InventoryService with name={}: {}", name,
					e.getMessage());
			// Error result
			result = new Result<InventoryService>(e);
		}
		return result;
	}
	

	/********************
	 * Job *
	 ********************/
	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<Job> createJob(Job job) {
		Result<Job> result;

		log.info("Request received: createJob {}",
				job);

		try {
			if (job.getName() != null) {
				log.debug("Job created (name={})",
						job.getName());
				Job tmpjob = getSingleJob(job
						.getName(),true);
				if (tmpjob != null) {
					throw new Exception("job " + tmpjob.getName()
							+ " already exists");
				}
			}
			User tmpuser = getUser();
			if(tmpuser != null)
				job.setOrg(tmpuser.getOrg());
			else
				job.setOrg(User.DEF_SYSTEM_ORG);
			Job jobPers = confService
					.create(job);
			// Success result
			result = new Result<Job>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(jobPers);
		} catch (Exception e) {
			log.error("Error creating a {}: {}", job,
					e.getMessage());
			// Error result
			result = new Result<Job>(e);
		}
		return result;
	}

	@Override
	public Result<Job> getJob(String name) {
		log.info("Request received: getJob name {}", name);
		Result<Job> result;
		try {
			// Read
			Job job = getSingleJob(name,false);
			if (job == null) {
				throw new Exception("job " + name
						+ " not exists");
			}
			// Success result
			result = new Result<Job>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(job);
		} catch (Exception e) {
			log.error("Error reading Job with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<Job>(e);
		}
		return result;
	}

	public Job getSingleJob(String name,Boolean toSkip) {
		Job job = DBUtility
				.getNamedQuerySingleResultByName(
						confService.getEntityManager(),
						Job.QUERY_READ_JOB,
						Job.class, name);
		User tmpuser = getUser();
		if(tmpuser != null) {
			if((!toSkip) && (!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN))) {			
				if (job != null) {
					if (!(job.getOrg().equals(tmpuser.getOrg()))) {
						return null;
					}
				} else return null;
			}
		}
		return job;
	}

	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<?> deleteJob(String name) {
		Result<?> result;

		log.info("Request received: deleteJob name={}", name);

		try {
			// get + delete
			Job job = getSingleJob(name,false);
			if (job != null) {
				confService.delete(job);
			}
			// Success result
			result = new Result<Object>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("Error deleting Job with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<Object>(e);
		}
		return result;
	}

	@Override
//	@RolesAllowed({ "SYSTEM_ADMIN", "ADMIN" })
	public Result<Job> updateJob(Job job) {
		Result<Job> result;

		log.info("Request received: updateJob{}",
				job);
		try {

			if (job.getName() != null) {
				log.debug("Job update (name={})",
						job.getName());
				Job tmpjob = getSingleJob(job
						.getName(),false);
				if (tmpjob == null) {
					throw new Exception("job " + job.getName()
							+ " not exists");
				}
			} else
				throw new Exception("job name is null");
			User tmpuser = getUser();
			if(tmpuser != null)
				job.setOrg(tmpuser.getOrg());
			else
				job.setOrg(User.DEF_SYSTEM_ORG);
			Job jobPers = confService
					.update(job);
			// Success result
			result = new Result<Job>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(jobPers);
		} catch (Exception e) {
			log.error("Error update a {}: {}", job, e.getMessage());
			// Error result
			result = new Result<Job>(e);
		}
		return result;
	}

	@Override
	public Result<Jobs> listJobs() {

		log.info("Request received: listJobs");
		try {
			// Read
			List<Job> jobs = getAllJobs();
			return new Result<Jobs>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE, new Jobs(
							jobs));

		} catch (Exception e) {
			log.error("Error reading listJobs {}", e.getMessage());

			// Error result
			return new Result<Jobs>(e);
		}
	}

	public List<Job> getAllJobs() {
		List<Job> jobsTemp = DBUtility
				.getNamedQueryResultList(confService.getEntityManager(),
						Job.QUERY_READ_ALL_JOBS,
						Job.class);
		List<Job> jobs = new ArrayList<Job>();

		User tmpuser = getUser();
		if(tmpuser != null) {
			if(!tmpuser.getRole().equals(RoleType.SYSTEM_ADMIN)) {
				for (Job invServ: jobsTemp) {
					if (invServ.getOrg().equals(tmpuser.getOrg()))
						jobs.add(invServ);
				}
				return jobs;
			}
		} 
		return jobsTemp;
	}
	
	@Override
	public Result<Job> getJobDefault() {

		log.info("Request received: getJobDefault");
		try {
			// Success result
			return new Result<Job>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE,
					confService.getDefault(Job.class));
		} catch (Exception e) {
			log.error("Error getting default Job: {}",
					e.getMessage());
			// Error result
			return new Result<Job>(e);
		}
	}
	
	public User getUser() {
		
		Principal myuser = getPrincipal();
//		log.debug("getUser myuser :"+myuser.getName());
		User user = new User();
		if(myuser != null) {

			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("username", myuser.getName());

			user = DBUtility
					.getNamedQuerySingleResultByNamedParams(confService.getEntityManager(),
							User.QUERY_READ_USER_BY_USERNAME, User.class,
							queryParams);
		}
//		log.debug("getUser user :"+user);
		return user;
	}

	@Override
	public Result<DashboardTypes> listDashboardType() {

		log.info("Request received: listDashboardType");
		try {
			// Read
			DashboardTypes dtypes= new DashboardTypes();
			BufferedReader reader = null;
			String pathDashFile = "";
			if (OSUtils.isUnix()) {
				 pathDashFile = System.getProperty("jboss.server.config.dir") + "/../conf/fileDashboardType.txt";
			} else {
				log.debug("Windows");
				 pathDashFile = "/tmp/fileDashboardType.txt";
			}
			
			reader = new BufferedReader(new FileReader(pathDashFile));

			String s = "";
			List<String> listString = new ArrayList<String>();
			while ((s = reader.readLine()) != null) {
				if (!s.equals("")) {
					listString.add(s);
				}
			}
			reader.close();
			log.info("listDashboardType listString = "+listString);
			
			List<DashboardType> listDash = new ArrayList<DashboardType>();
			for (String str: listString) {
				log.info("into for");
				DashboardType dt = new DashboardType();
				dt.setType(str);
				log.info("DashboardType dt = "+ dt.toString());
				listDash.add(dt);
			}
			log.info("listDashboardType listDash = "+listDash);
			dtypes.setDashboardTypes(listDash);
			
			log.info("listDashboardType dtypes = "+ dtypes.toString());
			
			return new Result<DashboardTypes>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE,dtypes );

		} catch (Exception e) {
			log.error("Error reading listDashboardType {}", e.getMessage());

			// Error result
			return new Result<DashboardTypes>(e);
		}
	}
}
