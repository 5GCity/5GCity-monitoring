package com.italtel.monitoring.fe.common.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.italtel.monitoring.fe.common.service.ConfigurationDispatcherService;
import com.italtel.monitoring.fe.common.service.OMEntity;
import com.italtel.monitoring.fe.common.service.conf.wrte.CommonConfService;
import com.italtel.monitoring.fe.common.service.exception.ConfigException;
import com.italtel.monitoring.fe.entity.InventoryMetric;
import com.italtel.monitoring.fe.entity.InventoryNode;
import com.italtel.monitoring.fe.entity.InventoryService;
import com.italtel.monitoring.fe.entity.InventoryServiceInventoryMetric;
import com.italtel.monitoring.fe.entity.InventoryServiceInventoryNode;
import com.italtel.monitoring.fe.entity.MetricTypeEnum;
import com.italtel.monitoring.fe.entity.PrometheusTarget;
import com.italtel.monitoring.fe.entity.PrometheusTargetLabel;
import com.italtel.monitoring.fe.utils.DBUtility;
import com.italtel.monitoring.fe.utils.OSUtils;

@Singleton
@Startup
@Local({ ConfigurationDispatcherService.class })
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ConfigurationDispatcherBean implements
		ConfigurationDispatcherService {
	
	private static final String QUERY_CHECK_METRIC = "select d from InventoryMetric d";
	private static final String QUERY_CHECK_NODE = "select d from InventoryNode d";
	private static final String QUERY_CHECK_SERVICE = "select d from InventoryService d";
	private static final Integer PORT = 9100;
	private static final Integer INTERVAL= 30;
	private static final String  MONITORING = "Monitoring";

	@PersistenceContext(unitName = "MONITORING-DB")
	EntityManager em;

	private final static Logger log = LoggerFactory
			.getLogger(ConfigurationDispatcherBean.class);

	Map<Class<? extends OMEntity>, CommonConfService<? extends OMEntity>> confServices;

	@PostConstruct
	public void init() {
		log.debug("Init ConfigurationDispatcherBean");
		
		InventoryMetric metric = new InventoryMetric();
		InventoryNode node = new InventoryNode();
		boolean nodeB=false;
		boolean metricB=false;
		if (em.createQuery(QUERY_CHECK_METRIC).getResultList().isEmpty()) {
			log.info("%%% initialize metric table %%%");
			
			metric.setName("NODE");
			metric.setDesc("Metric Group NODE");
			metricB=true;
			em.persist(metric);
		}
	
		if (em.createQuery(QUERY_CHECK_NODE).getResultList().isEmpty()) {
			log.info("%%% initialize node table %%%");
			
			node.setName(MONITORING);
			String host=System.getenv("HOST_MON");
	        if (host==null)
			  node.setIp("localhost");
	        else 
	        	node.setIp(host);
			node.setPort(PORT);
			nodeB=true;
			em.persist(node);
			
		}
	
		
		if (em.createQuery(QUERY_CHECK_SERVICE).getResultList().isEmpty()) {
			log.info("%%% initialize service table %%%");
			if (nodeB && metricB) {
			InventoryService srv = new InventoryService();
			List<InventoryServiceInventoryMetric> metrics=new ArrayList<InventoryServiceInventoryMetric>();
			List<InventoryServiceInventoryNode> nodes=new ArrayList<InventoryServiceInventoryNode>();
			srv.setName(MONITORING);
			srv.setDescription("Service that monitors the node of 5G-Monitoring for metric group NODE");
			srv.setInterval(INTERVAL);
			InventoryServiceInventoryMetric srvmetric=new InventoryServiceInventoryMetric();
			srvmetric.setMetric(metric);
			srvmetric.setInventoryMetricName(metric.getName());
			metrics.add(srvmetric);
			InventoryServiceInventoryNode srvnode=new InventoryServiceInventoryNode();
			srvnode.setNode(node);
			srvnode.setInventoryNodeName(node.getName());
			nodes.add(srvnode);
			srv.setInventoryMetrics(metrics);
			srv.setInventoryNodes(nodes);
			em.persist(srv);
			
			} else log.error("%%% initialize service table not done%%%");
				
		}
		
	}

	@PreDestroy
	public void destroy() {
		log.debug("Destroy ConfigurationDispatcherBean");
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@SuppressWarnings("unchecked")
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public <T extends OMEntity> T getDefault(Class<T> clazz) throws Exception {
		log.debug("clazz GetName is " + clazz.getName().toString());
		if (InventoryService.class.getName().toString()
				.equals(clazz.getName().toString())) {
			InventoryService invService = new InventoryService();
			invService.setInterval(10);
			return (T) invService;
		}
		if (InventoryMetric.class.getName().toString()
				.equals(clazz.getName().toString())) {
			return (T) new InventoryMetric();
		}
		if (InventoryNode.class.getName().toString()
				.equals(clazz.getName().toString())) {
			InventoryNode invNode = new InventoryNode();
			invNode.setPort(9100);
			return (T) invNode;
		}
		return null;
	}

	@Override
	public <T extends OMEntity> T create(T obj) throws ConfigException,
			Exception {
		log.debug("create ConfigurationDispatcherBean");

		// For Service
		// some validations check
		if (InventoryService.class.isInstance(obj)) {
			InventoryService service = (InventoryService) obj;

			if (service != null) {

				String serviceName = service.getName();
				if (serviceName == null || serviceName.isEmpty()) {
					throw new ConfigException(
							"Service name is null or empty");
				}
				
				
				if ((service.getInterval() < 0) || (service.getInterval() > 60)) {
					throw new ConfigException(
							"Service interval is out of range (" + 0
									+ "-" + 60 + ")");
				}

				manageNodesInService(service);

				manageMetricsInService(service);

			}
		}

		// For Metric
		// check value is present in MetricTypeEnum
		if (InventoryMetric.class.isInstance(obj)) {
			InventoryMetric metric = (InventoryMetric) obj;

			if ((metric.getName() == null) || (metric.getName().isEmpty())) {
				throw new ConfigException(
						"Metric name is null or empty");
			}
			
			if (metric.getName().equals(MetricTypeEnum.NODE.toString())) {
				throw new ConfigException(
						"Metric name NODE cannot be created");
			}

			boolean found = false;
			for (MetricTypeEnum mte : MetricTypeEnum.values()) {
				log.debug("Metric value is  metric "
						+ metric.getName() + ";  mte is " + mte);
				if (mte.toString().equals(metric.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				log.error("Metric value is not a valid metric");
				throw new ConfigException(
						"Metric value is not a valid metric");
			}

		}

		// For Node
		// Check value for InventoryNode
		if (InventoryNode.class.isInstance(obj)) {
			InventoryNode node = (InventoryNode) obj;

			if ((node.getName() == null) || (node.getName().isEmpty())) {
				throw new ConfigException("Node name is null or empty");
			}
			
			if (node.getName().equals(MONITORING)) {
				throw new ConfigException(
						"Node name Monitoring cannot be created");
			}

			if ((node.getPort() < 0) || (node.getPort() > 65535)) {
				throw new ConfigException(
						"Node port is out of range (" + 0 + "-"
								+ 65536 + ")");
			}
		}

		// Persist obj
		persistEntityOnCreate(obj);

		if (InventoryService.class.isInstance(obj))
			regeneratePrometheusConfig(true);

		return obj;

	}

	public void manageMetricsInService(InventoryService service)
			throws ConfigException {
		List<InventoryServiceInventoryMetric> serviceMetrics = service
				.getInventoryMetrics();

		Set<String> inventoryMetricNames = new HashSet<>();

		if ((serviceMetrics != null) && (!serviceMetrics.isEmpty())) {
			validateMetricsInService(serviceMetrics, inventoryMetricNames);
		} else {
			log.error("At least one metric must be present");
			throw new ConfigException(
					"At least one metric must be present");
		}
	}

	public void manageNodesInService(InventoryService service)
			throws ConfigException {
		List<InventoryServiceInventoryNode> serviceNodes = service
				.getInventoryNodes();

		Set<String> inventoryNodeNames = new HashSet<>();

		if ((serviceNodes != null) && (!serviceNodes.isEmpty())) {
			validateNodesInService(serviceNodes, inventoryNodeNames);
		} else {
			log.error("At least one node must be present");
			throw new ConfigException("At least one node must be present");
		}
	}

	public void validateNodesInService(
			List<InventoryServiceInventoryNode> serviceNodes,
			Set<String> inventoryNodeNames) throws ConfigException {
		for (InventoryServiceInventoryNode sn : serviceNodes) {
			String nodeName = sn.getInventoryNodeName();
			if (nodeName == null || nodeName.isEmpty()) {
				throw new ConfigException("Node name is null or empty");
			}

			Map<String, Object> queryParams2 = new HashMap<String, Object>();
			queryParams2.put("name", nodeName);

			InventoryNode result = DBUtility
					.getNamedQuerySingleResultByNamedParams(em,
							InventoryNode.QUERY_READ_NODE, InventoryNode.class,
							queryParams2);

			if (result == null) {
				log.error("Node not found, name:{}", nodeName);
				throw new ConfigException("Node not found");
			} else {
				sn.setNode(result);
			}

			if (!inventoryNodeNames.add(nodeName)) {
				throw new ConfigException(
						"duplicated node with name '" + nodeName + "'");
			}
		}
	}

	protected <T extends OMEntity> void regeneratePrometheusConfig(boolean restart)
			throws JsonProcessingException, Exception {

		String paramsScript = "";

		List<InventoryService> listAllServices = DBUtility
				.getNamedQueryResultList(em,
						InventoryService.QUERY_READ_ALL_SERVICES,
						InventoryService.class);
		

		for (InventoryService service : listAllServices) {

			List<PrometheusTarget> listPrometheustargets = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();

			if (service != null) {
				log.info("Service Name is  " + service.getName());
				if (!service.getName().equals(MONITORING))  {
				log.info("into if  != Monitoring");
				
				List<InventoryServiceInventoryNode> serviceNodes = service
						.getInventoryNodes();

				List<InventoryServiceInventoryMetric> serviceMetrics = service
						.getInventoryMetrics();

				if ((serviceNodes != null) && (!serviceNodes.isEmpty())) {
					for (InventoryServiceInventoryMetric sm : serviceMetrics) {

						for (InventoryServiceInventoryNode sn : serviceNodes) {
							InventoryNode nodeData = sn.getNode();
							PrometheusTarget pt = new PrometheusTarget();
							List<String> list = new ArrayList<String>();
							list.add(nodeData.getIp() + ":"
									+ nodeData.getPort());
							PrometheusTargetLabel ptl = new PrometheusTargetLabel();
							ptl.setInstance(nodeData.getName());
							ptl.setMetricGroup(sm.getInventoryMetricName());
							pt.setLabels(ptl);
							pt.setTargets(list);
							listPrometheustargets.add(pt);

						}
					}

					String jsonS = mapper
							.writeValueAsString(listPrometheustargets);

					String jsonPretty = mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(listPrometheustargets);

					log.info("create mapper node : " + jsonS);
					log.info("create mapper node : " + jsonPretty);

					//String filenameTarget = ConfigProperty.PROMETHEUS_PATH
					//		.getStringValue() + service.getName() + ".json";
					String filenameTarget = "/etc/prometheus/" + service.getName() + ".json";

					File fileJson = new File(filenameTarget);

					log.info("filenameTarget : " + filenameTarget);

					try {
						if (fileJson.exists())
							fileJson.delete();
						BufferedWriter out = new BufferedWriter(new FileWriter(
								fileJson));
						out.write(jsonPretty);
						out.close();

					} catch (Exception e) {
						log.error("Exception " + e.getMessage());
					} finally {
						log.error(" finally ");
					}
				}
			  
			paramsScript = paramsScript + service.getName() + ","
					+ service.getInterval() + " ";
			
				}
			}
		}
		log.debug("paramsScript :" + paramsScript);
		if (paramsScript!=null) {
			String cmd=null;
			if (restart)
				cmd = System.getProperty("jboss.server.config.dir") + "/../../../bin/changePrometheusFile.sh" + 
						" restart " + paramsScript;
			else
				cmd = System.getProperty("jboss.server.config.dir") + "/../../../bin/changePrometheusFile.sh" + 
						" norestart " + paramsScript;

			log.debug("cmd :" + cmd);
			if (OSUtils.isUnix()) {
				OSUtils.executeShellCommandUnix(cmd);
			} else {
				log.debug("Windows");
				OSUtils.executeShellCommand("/tmp/changePrometheus.bat");
			}
		}
	}

	@Override
	public <T extends OMEntity> T delete(T obj) throws Exception {
		log.debug("deleteConfigurationDispatcherBean");

		if (InventoryNode.class.isInstance(obj)) {
			InventoryNode node = (InventoryNode) obj;
			String nodeName = node.getName();
			
			if (nodeName.equals(MONITORING)) {
				throw new ConfigException(
						"Node Monitoring cannot be deleted");
			}
			Long counter = DBUtility
					.getNamedQuerySingleResultByName(
							em,
							InventoryServiceInventoryNode.QUERY_COUNT_SERVICENODE_WITH_NODE,
							Long.class, nodeName);
			if (counter > 0) {
				throw new ConfigException(
						"Node referenced by a service cannot be deleted!");
			}
		}
		if (InventoryMetric.class.isInstance(obj)) {
			InventoryMetric metric = (InventoryMetric) obj;
			String metricName = metric.getName();
			
			if (metricName.equals(MetricTypeEnum.NODE.toString())) {
				throw new ConfigException(
						"Metric NODE cannot be deleted");
			}
			Long counter = DBUtility
					.getNamedQuerySingleResultByName(
							em,
							InventoryServiceInventoryMetric.QUERY_COUNT_SERVICENODE_WITH_METRIC,
							Long.class, metricName);
			if (counter > 0) {
				throw new ConfigException(
						"Metric referenced by a service cannot be deleted!");
			}
		}

		if (InventoryService.class.isInstance(obj)) {
			InventoryService service = (InventoryService) obj;
			String serviceName = service.getName();
		
			if (serviceName.equals(MONITORING)) {
			throw new ConfigException(
					"Service Monitoring cannot be deleted");
			}
		}
		persistEntityOnDelete(obj);

		if (InventoryService.class.isInstance(obj)) {
			regeneratePrometheusConfig(true);
		}
		
		return obj;
	}

	@Override
	public <T extends OMEntity> T update(T obj) throws Exception {
		log.debug("updateConfigurationDispatcherBean");

		boolean differentServiceList = false;

		// For Service only
		if (InventoryService.class.isInstance(obj)) {
			InventoryService service = (InventoryService) obj;
			if (service != null) {
				if (service.getName() != null) {
					
					if (service.getName().equals(MONITORING)) {
						log.error("Service identified by name {} cannot be updated!",
								service.getName());
						throw new ConfigException("Service Monitoring cannot be updated");
					}
					Map<String, Object> queryParams1 = new HashMap<String, Object>();
					queryParams1.put("name", service.getName());

					InventoryService oldSrv = DBUtility
							.getNamedQuerySingleResultByNamedParams(em,
									InventoryService.QUERY_READ_SERVICE,
									InventoryService.class, queryParams1);

					if (oldSrv == null) {
						log.error("Service identified by name {} not found!",
								service.getName());
						throw new ConfigException("Service not found");
					}

					if (service.getDescription() == null)
						service.setDescription(oldSrv.getDescription());
					if (service.getInterval() == 0)
						service.setInterval(oldSrv.getInterval());

					if (oldSrv.getInterval() != service.getInterval()) {
						differentServiceList = true;
						log.debug(" interval is different: old is "
								+ oldSrv.getInterval() + " new is "
								+ service.getInterval());
					}

					// list new nodes
					manageNodesInService(service);

					// list of new Metric
					manageMetricsInService(service);

					List<InventoryServiceInventoryNode> oldNodes = oldSrv
							.getInventoryNodes();
					List<InventoryServiceInventoryMetric> oldMetrics = oldSrv
							.getInventoryMetrics();
					List<String> oldNodeNames = new ArrayList<String>();
					for (InventoryServiceInventoryNode ino : oldNodes) {
						oldNodeNames.add(ino.getInventoryNodeName());
					}

					List<String> oldMetricNames = new ArrayList<String>();
					for (InventoryServiceInventoryMetric imo : oldMetrics) {
						oldMetricNames.add(imo.getInventoryMetricName());
					}

					List<InventoryServiceInventoryNode> newNodes = service
							.getInventoryNodes();
					List<InventoryServiceInventoryMetric> newMetrics = service
							.getInventoryMetrics();

					List<String> srvNodeNames = new ArrayList<String>();
					for (InventoryServiceInventoryNode ins : newNodes) {
						srvNodeNames.add(ins.getInventoryNodeName());
					}

					List<String> srvMetricNames = new ArrayList<String>();
					for (InventoryServiceInventoryMetric ims : newMetrics) {
						srvMetricNames.add(ims.getInventoryMetricName());
					}

					if (!(srvNodeNames.containsAll(oldNodeNames) && oldNodeNames
							.containsAll(srvNodeNames))) {
						differentServiceList = true;
						log.debug("nodes are different");
					}

					if (!(srvMetricNames.containsAll(oldMetricNames) && oldMetricNames
							.containsAll(srvMetricNames))) {
						differentServiceList = true;
						log.debug("metric are different");
					}

				}

			} else {
				log.error("Service Name cannot be null");
				throw new ConfigException("Service Name cannot be null");
			}
		} else {
			log.error("Service cannot be null");
			throw new ConfigException("Service cannot be null");
		}
		persistEntityOnUpdate(obj);

		if (InventoryService.class.isInstance(obj) && differentServiceList)
			regeneratePrometheusConfig(false);

		return obj;
	}

	public void validateMetricsInService(
			List<InventoryServiceInventoryMetric> serviceMetrics,
			Set<String> inventoryMetricNames) throws ConfigException {
		for (InventoryServiceInventoryMetric sm : serviceMetrics) {
			String metricName = sm.getInventoryMetricName();
			if (metricName == null || metricName.isEmpty()) {
				throw new ConfigException("metricName is null or empty");
			}

			Map<String, Object> queryParams2 = new HashMap<String, Object>();
			queryParams2.put("name", metricName);

			InventoryMetric result = DBUtility
					.getNamedQuerySingleResultByNamedParams(em,
							InventoryMetric.QUERY_READ_METRIC,
							InventoryMetric.class, queryParams2);

			if (result == null) {
				log.error("Metric not found, name:{}", metricName);
				throw new ConfigException("Metric not found");
			} else {
				sm.setMetric(result);
			}

			if (!inventoryMetricNames.add(metricName)) {
				throw new ConfigException(
						"duplicated InventoryMetric with name '" + metricName
								+ "'");
			}
		}
	}

	<T> T detachAndResetReturnedValue(T entity) {
		if (entity != null) {
			em.detach(entity);
		}
		return entity;
	}

	protected <T extends OMEntity> void persistEntityOnCreate(T obj)
			throws Exception {
		try {
			em.persist(obj);
			em.flush();
		} catch (Exception e) {
			String errMsg = "Error persisting " + obj + ": " + e.getMessage();
			log.error(errMsg, e);
			throw new Exception(errMsg, e);
		}

	}

	protected <T extends OMEntity> void persistEntityOnUpdate(T obj)
			throws Exception {
		try {
			em.merge(obj);
			em.flush();
		} catch (Exception e) {
			String errMsg = "Error merging " + obj + ": " + e.getMessage();
			log.error(errMsg, e);
			throw new Exception(errMsg, e);
		}

	}

	protected <T extends OMEntity> void persistEntityOnDelete(T obj)
			throws Exception {
		try {
			T objPers = em.merge(obj);
			em.remove(objPers);
			em.flush();
		} catch (Exception e) {
			String errMsg = "Error removing " + obj + ": " + e.getMessage();
			log.error(errMsg, e);
			throw new Exception(errMsg, e);
		}
	}

	@Override
	public <T extends OMEntity> T read(T obj, Class<T> clazz) throws Exception {
		log.debug("read ConfigurationDispatcherBean");
		return null;
	}
}
