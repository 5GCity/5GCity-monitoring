package com.italtel.monitoring.fe.ws.inventory;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.italtel.monitoring.fe.common.service.ConfigurationDispatcherService;
import com.italtel.monitoring.fe.common.service.conf.wrte.CommonConfService;
import com.italtel.monitoring.fe.entity.InventoryMetric;
import com.italtel.monitoring.fe.entity.InventoryMetrics;
import com.italtel.monitoring.fe.entity.InventoryNode;
import com.italtel.monitoring.fe.entity.InventoryNodes;
import com.italtel.monitoring.fe.entity.InventoryService;
import com.italtel.monitoring.fe.entity.InventoryServices;
import com.italtel.monitoring.fe.utils.DBUtility;
import com.italtel.monitoring.fe.ws.entity.Result;

@Stateless
@WebService(
		endpointInterface = "com.italtel.monitoring.fe.ws.inventory.InventoryWSInterface")
public class InventoryWS implements InventoryWSInterface {
	@EJB(
			lookup = "java:global/FRONTEND/MonitoringFE/ConfigurationDispatcherBean!com.italtel.monitoring.fe.common.service.ConfigurationDispatcherService")
	private ConfigurationDispatcherService confService;

	private final static Logger log = LoggerFactory
			.getLogger(CommonConfService.class);

//	@Override
//	public String test() {
//		return "OK";
//	}

	/********************
	 * InventoryService *
	 ********************/
	@Override
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
						.getName());
				if (service != null) {
					throw new Exception("service " + inventoryService.getName()
							+ " already exists");
				}
			}
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
			InventoryService inventoryService = getSingleService(name);
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

	public InventoryService getSingleService(String name) {
		InventoryService inventoryService = DBUtility
				.getNamedQuerySingleResultByName(
						confService.getEntityManager(),
						InventoryService.QUERY_READ_SERVICE,
						InventoryService.class, name);
		return inventoryService;
	}

	@Override
	public Result<?> deleteInventoryService(String name) {
		Result<?> result;

		log.info("Request received: deleteInventoryService name={}", name);

		try {
			// get + delete
			InventoryService inventoryService = getSingleService(name);
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
						.getName());
				if (service == null) {
					throw new Exception("service " + inventoryService.getName()
							+ " not exists");
				}
			} else
				throw new Exception("service name is null");

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
		List<InventoryService> inventoryServices = DBUtility
				.getNamedQueryResultList(confService.getEntityManager(),
						InventoryService.QUERY_READ_ALL_SERVICES,
						InventoryService.class);
		return inventoryServices;
	}

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
	 * InventoryNode *
	 ********************/
	@Override
	public Result<InventoryNode> createInventoryNode(InventoryNode inventoryNode) {
		Result<InventoryNode> result;

		log.debug("Request received: createNode {}", inventoryNode);

		try {
			if (inventoryNode.getName() != null) {
				log.debug("InventoryNode created (name={})",
						inventoryNode.getName());
				InventoryNode node = getSingleNode(inventoryNode.getName());
				if (node != null) {
					throw new Exception("node " + inventoryNode.getName()
							+ " already exists");
				}
			}
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
			InventoryNode inventoryNode = getSingleNode(name);
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
	public Result<?> deleteInventoryNode(String name) {
		Result<?> result;

		log.info("Request received: deleteInventoryNode name={}", name);

		try {
			// get + delete
			InventoryNode inventoryNode = getSingleNode(name);
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

	public InventoryNode getSingleNode(String name) {
		InventoryNode inventoryNode = DBUtility
				.getNamedQuerySingleResultByName(
						confService.getEntityManager(),
						InventoryNode.QUERY_READ_NODE, InventoryNode.class,
						name);
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
		List<InventoryNode> inventoryNodes = DBUtility.getNamedQueryResultList(
				confService.getEntityManager(),
				InventoryNode.QUERY_READ_ALL_NODES, InventoryNode.class);
		return inventoryNodes;
	}

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

	/********************
	 * InventoryMetric *
	 ********************/
	@Override
	public Result<InventoryMetric> createInventoryMetric(
			InventoryMetric inventoryMetric) {
		Result<InventoryMetric> result;

		log.debug("Request received: createNode {}", inventoryMetric);

		try {
			if (inventoryMetric.getName() != null) {
				log.debug("inventoryMetric created (name={})",
						inventoryMetric.getName());
				InventoryMetric metric = getSingleMetric(inventoryMetric
						.getName());
				if (metric != null) {
					throw new Exception("metric " + inventoryMetric.getName()
							+ " already exists");
				}
			}
			InventoryMetric inventoryMetricPers = confService
					.create(inventoryMetric);
			// Success result
			result = new Result<InventoryMetric>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(inventoryMetricPers);
		} catch (Exception e) {
			log.error("Error creating a {}: {}", inventoryMetric,
					e.getMessage());

			// Error result
			result = new Result<InventoryMetric>(e);
		}
		return result;
	}

	@Override
	public Result<InventoryMetric> getInventoryMetric(String name) {
		log.info("Request received: getInventoryMetric name {}", name);
		Result<InventoryMetric> result;
		try {
			// get
			InventoryMetric inventoryMetric = getSingleMetric(name);
			if (inventoryMetric == null) {
				throw new Exception("metric " + name
						+ " not exists");
			}
			// Success result
			result = new Result<InventoryMetric>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
			result.setData(inventoryMetric);
		} catch (Exception e) {
			log.error("Error reading InventoryMetric with name={}: {}", name,
					e.getMessage());
			// Error result
			result = new Result<InventoryMetric>(e);
		}
		return result;
	}

	@Override
	public Result<?> deleteInventoryMetric(String name) {
		Result<?> result;

		log.info("Request received: deleteInventoryMetric name={}", name);

		try {
			// get + delete
			InventoryMetric inventoryMetric = getSingleMetric(name);
			if (inventoryMetric != null) {
				confService.delete(inventoryMetric);
			}
			// Success result
			result = new Result<Object>();
			result.setCode(Result.OK_SUCCESS_CODE);
			result.setMessage(Result.OK_SUCCESS_MESSAGE);
		} catch (Exception e) {
			log.error("Error deleting inventoryMetric with name={}: {}", name,
					e.getMessage());

			// Error result
			result = new Result<Object>(e);
		}
		return result;
	}

	public InventoryMetric getSingleMetric(String name) {
		InventoryMetric inventoryMetric = DBUtility
				.getNamedQuerySingleResultByName(
						confService.getEntityManager(),
						InventoryMetric.QUERY_READ_METRIC,
						InventoryMetric.class, name);
		return inventoryMetric;
	}

	@Override
	public Result<InventoryMetrics> listInventoryMetrics() {

		log.info("Request received: listInventoryMetrics");

		try {
			// Read
			List<InventoryMetric> inventoryMetrics = getAllMetrics();
			return new Result<InventoryMetrics>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE, new InventoryMetrics(
							inventoryMetrics));

		} catch (Exception e) {
			log.error("Error reading listInventoryMetrics {}", e.getMessage());

			// Error result
			return new Result<InventoryMetrics>(e);
		}
	}

	public List<InventoryMetric> getAllMetrics() {
		List<InventoryMetric> inventoryMetrics = DBUtility
				.getNamedQueryResultList(confService.getEntityManager(),
						InventoryMetric.QUERY_READ_ALL_METRICS,
						InventoryMetric.class);
		return inventoryMetrics;
	}

	public Result<InventoryMetric> getInventoryMetricDefault() {

		log.info("Request received: getInventoryMetricDefault");
		try {
			// Success result
			return new Result<InventoryMetric>(Result.OK_SUCCESS_CODE,
					Result.OK_SUCCESS_MESSAGE,
					confService.getDefault(InventoryMetric.class));
		} catch (Exception e) {
			log.error("Error getting default InventoryMetric: {}",
					e.getMessage());
			// Error result
			return new Result<InventoryMetric>(e);
		}
	}
}
