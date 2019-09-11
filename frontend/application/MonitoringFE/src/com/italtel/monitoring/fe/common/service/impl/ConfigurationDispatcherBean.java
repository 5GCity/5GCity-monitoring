package com.italtel.monitoring.fe.common.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.Principal;

import com.italtel.monitoring.fe.ws.BaseWS;
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
import com.italtel.monitoring.fe.entity.AlertRule;
import com.italtel.monitoring.fe.entity.InventoryNode;
import com.italtel.monitoring.fe.entity.InventoryService;
import com.italtel.monitoring.fe.entity.InventoryServiceJob;
import com.italtel.monitoring.fe.entity.Job;
import com.italtel.monitoring.fe.entity.JobInventoryNode;
import com.italtel.monitoring.fe.entity.MetricTypeEnum;
import com.italtel.monitoring.fe.entity.PrometheusTarget;
import com.italtel.monitoring.fe.entity.PrometheusTargetLabel;
import com.italtel.monitoring.fe.entity.Slice;
import com.italtel.monitoring.fe.entity.SliceInventoryNode;
import com.italtel.monitoring.fe.entity.Source;
import com.italtel.monitoring.fe.entity.SourceTypeEnum;
import com.italtel.monitoring.fe.entity.um.User;
import com.italtel.monitoring.fe.entity.um.UserSlice;
import com.italtel.monitoring.fe.utils.DBUtility;
import com.italtel.monitoring.fe.utils.OSUtils;

@Singleton
@Startup
@Local({ ConfigurationDispatcherService.class })
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ConfigurationDispatcherBean implements ConfigurationDispatcherService {

	private static final String QUERY_CHECK_NODE = "select d from InventoryNode d";
	private static final String QUERY_CHECK_SERVICE = "select d from InventoryService d";
	private static final String QUERY_CHECK_JOB = "select d from Job d";
	private static final String QUERY_CHECK_SLICE = "select d from Slice d";

	@PersistenceContext(unitName = "MONITORING-DB")
	EntityManager em;

	private final static Logger log = LoggerFactory.getLogger(ConfigurationDispatcherBean.class);

	Map<Class<? extends OMEntity>, CommonConfService<? extends OMEntity>> confServices;

	@PostConstruct
	public void init() {
		log.debug("Init ConfigurationDispatcherBean");

		InventoryNode node = new InventoryNode();
		Job job = new Job();
		boolean nodeB = false;
		boolean sliceB = false;
		boolean jobB = false;
		if (em.createQuery(QUERY_CHECK_NODE).getResultList().isEmpty()) {
			log.info("%%% initialize node table %%%");

			node.setName(InventoryNode.DEF_NODE_MONITORING);
			String host = System.getenv("HOST_MON");
			if (host == null)
				node.setIp("localhost");
			else
				node.setIp(host);
			nodeB = true;
			node.setOrg(User.DEF_SYSTEM_ORG);
			em.persist(node);
		}

		// slice
		if (em.createQuery(QUERY_CHECK_SLICE).getResultList().isEmpty()) {
			log.info("%%% initialize slice table %%%");

			if (nodeB) {
				Slice sl = new Slice();
				List<SliceInventoryNode> slnodes = new ArrayList<SliceInventoryNode>();
				sl.setName(Slice.DEF_SLICE_MONITORING);
				SliceInventoryNode slnode = new SliceInventoryNode();
				slnode.setNode(node);
				slnode.setInventoryNodeName(node.getName());
				slnodes.add(slnode);
				sl.setSliceNodes(slnodes);
				sliceB = true;
				sl.setOrg(User.DEF_SYSTEM_ORG);
				em.persist(sl);

			} else
				log.error("%%% initialize slice table not done%%%");
		}

		// job
		if (em.createQuery(QUERY_CHECK_JOB).getResultList().isEmpty()) {
			log.info("%%% initialize job table %%%");

			if ((nodeB) && (sliceB)) {

				List<JobInventoryNode> jobnodes = new ArrayList<JobInventoryNode>();
				job.setName(Job.DEF_JOB_MONITORING);
				job.setDescription(Job.DEF_JOB_DESCRIPTION);
				JobInventoryNode slnode = new JobInventoryNode();
				slnode.setNode(node);
				slnode.setInventoryNodeName(node.getName());
				jobnodes.add(slnode);
				job.setJobNodes(jobnodes);
				Source source = new Source();
				source.setInterval(Source.DEF_SOURCE_INTERVAL);
				source.setPort(Source.DEF_SOURCE_PORT_MONITORING);
				source.setMetricPath(Source.DEF_SOURCE_METRICPATH);
				source.setSourceType(SourceTypeEnum.EXPORTER);
				source.setDashboardType("NODE");
				job.setJobSource(source);
				job.setOrg(User.DEF_SYSTEM_ORG);
				jobB = true;

				em.persist(job);

			} else
				log.error("%%% initialize job table not done%%%");
		}
		// service
		if (em.createQuery(QUERY_CHECK_SERVICE).getResultList().isEmpty()) {
			log.info("%%% initialize service table %%%");

			if (jobB) {
				List<InventoryServiceJob> srvjobs = new ArrayList<InventoryServiceJob>();
				InventoryService srv = new InventoryService();
				srv.setName(InventoryService.DEF_SERVICE_MONITORING);
				srv.setDescription(InventoryService.DEF_SERVICE_DESCRIPTION);
				InventoryServiceJob srvjob = new InventoryServiceJob();
				srvjob.setJob(job);
				srvjob.setJobName(job.getName());
				srvjobs.add(srvjob);
				srv.setJobs(srvjobs);
				srv.setOrg(User.DEF_SYSTEM_ORG);
				em.persist(srv);

			} else
				log.error("%%% initialize service table not done%%%");
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
		if (InventoryService.class.getName().toString().equals(clazz.getName().toString())) {
			InventoryService invService = new InventoryService();
			return (T) invService;
		}
		if (Slice.class.getName().toString().equals(clazz.getName().toString())) {
			return (T) new Slice();
		}
		if (InventoryNode.class.getName().toString().equals(clazz.getName().toString())) {
			return (T) new InventoryNode();
		}
		if (Job.class.getName().toString().equals(clazz.getName().toString())) {
			Job job = new Job();
			Source source = new Source();
			source.setPort(Source.DEF_SOURCE_PORT_MONITORING);
			source.setInterval(Source.DEF_SOURCE_INTERVAL);
			source.setMetricPath(Source.DEF_SOURCE_METRICPATH);
			source.setDashboardType("NODE");
			source.setSourceType(SourceTypeEnum.EXPORTER);
			log.debug("getDefault JOB source =  " + source.toString());
			job.setJobSource(source);

			return (T) job;
		}
		return null;
	}

	@Override
	public <T extends OMEntity> T create(T obj) throws ConfigException, Exception {
		log.debug("create ConfigurationDispatcherBean");

		// For Service
		// some validations check
		if (InventoryService.class.isInstance(obj)) {
			InventoryService service = (InventoryService) obj;

			if (service != null) {

				String serviceName = service.getName();
				if (serviceName == null || serviceName.isEmpty()) {
					throw new ConfigException("Service name is null or empty");
				}
				if (serviceName.equals(InventoryService.DEF_SERVICE_MONITORING)) {
					throw new ConfigException("Service name ServiceMonitoring cannot be created");
				}

				manageJobsInService(service);
			}
		}

		// For Slice
		// some validations check
		if (Slice.class.isInstance(obj)) {
			Slice slice = (Slice) obj;

			if (slice != null) {

				String sliceName = slice.getName();
				if (sliceName == null || sliceName.isEmpty()) {
					throw new ConfigException("Slice name is null or empty");
				}
				if (sliceName.equals(Slice.DEF_SLICE_MONITORING)) {
					throw new ConfigException("Slice name Monitoring cannot be created");
				}
				manageNodesInSlice(slice);
			}
		}

		// For Job
		// some validations check
		if (Job.class.isInstance(obj)) {
			Job job = (Job) obj;

			if (job != null) {

				String jobName = job.getName();
				if (jobName == null || jobName.isEmpty()) {
					throw new ConfigException("Job name is null or empty");
				}
				if (jobName.equals(Job.DEF_JOB_MONITORING)) {
					throw new ConfigException("Job name Monitoring cannot be created");
				}
				manageNodesInJob(job);
				Source jobSource = job.getJobSource();
				if (jobSource == null) {
					jobSource = new Source();
					job.setJobSource(jobSource);
				}
				if (jobSource.getSourceType() == null) {
					jobSource.setSourceType(SourceTypeEnum.EXPORTER);
				}
				log.debug("create ConfigurationDispatcherBean Source Type " + jobSource.getSourceType().toString());
				if (jobSource.getDashboardType() == null) {
//					throw new ConfigException(
//							"DashboardType cannot be null");
					jobSource.setDashboardType("NODE");
				}
				if (jobSource.getPort() != null) {
					if ((jobSource.getPort() < 0) || (jobSource.getPort() > Source.DEF_SOURCE_MAX_PORT)) {
						throw new ConfigException(
								"Job port is out of range (" + 0 + "-" + Source.DEF_SOURCE_MAX_PORT + ")");
					}
				} else {
					if (jobSource.getDashboardType().equals("APACHE"))
						jobSource.setPort(9117);
					else
						jobSource.setPort(9100);
				}
				if (jobSource.getInterval() != null) {
					if ((jobSource.getInterval() < 0) || (jobSource.getInterval() > Source.DEF_SOURCE_MAX_INTERVAL)) {
						throw new ConfigException(
								"Job interval is out of range (" + 0 + "-" + Source.DEF_SOURCE_MAX_INTERVAL + ")");
					}
				} else {
					throw new ConfigException("Job interval cannot be null");
				}
				// check source data
				if (jobSource.getMetricPath() == null) {
					jobSource.setMetricPath("/metrics");
				}
			}
		}

		// For Node
		// Check value for InventoryNode
		if (InventoryNode.class.isInstance(obj)) {
			InventoryNode node = (InventoryNode) obj;

			log.info("create ConfigurationDispatcherBean obj " + obj.toString());

			if ((node.getName() == null) || (node.getName().isEmpty())) {
				throw new ConfigException("Node name is null or empty");
			}

			if (node.getName().equals(InventoryNode.DEF_NODE_MONITORING)) {
				throw new ConfigException("Node name Monitoring cannot be created");
			}

		}

		// For Node
		// Check value for InventoryNode
		if (AlertRule.class.isInstance(obj)) {
			AlertRule alertrule = (AlertRule) obj;

			log.info("create ConfigurationDispatcherBean obj " + obj.toString());

			if ((alertrule.getName() == null) || (alertrule.getName().isEmpty())) {
				throw new ConfigException("AlertRule name is null or empty");
			}

			if ((alertrule.getExpression() == null) || (alertrule.getExpression().isEmpty())) {
				throw new ConfigException("AlertRule expression is null or empty");
			}

			if ((alertrule.getDuration() == null) || (alertrule.getDuration().isEmpty())) {
				alertrule.setDuration("1m");
			}

			if ((alertrule.getSeverity() == null) || (alertrule.getSeverity().isEmpty())) {
				alertrule.setDuration("critical");
			}

		}
		// Persist obj
		persistEntityOnCreate(obj);

		if (InventoryService.class.isInstance(obj)) {
			regeneratePrometheusConfig(true);
		}

		if (AlertRule.class.isInstance(obj)) {
			addRule((AlertRule) obj);
		}

		return obj;

	}

	public void manageNodesInSlice(Slice slice) throws ConfigException {
		List<SliceInventoryNode> sliceNodes = slice.getSliceNodes();

		log.error("manageNodesInSlice sliceNodes: " + sliceNodes);

		Set<String> inventoryNodeNames = new HashSet<>();

		if ((sliceNodes != null) && (!sliceNodes.isEmpty())) {
			validateNodesInSlice(sliceNodes, inventoryNodeNames);
		} else {
			log.error("At least one node must be present");
			throw new ConfigException("At least one node must be present");
		}
	}

	public void manageJobsInService(InventoryService service) throws ConfigException {
		List<InventoryServiceJob> serviceJobs = service.getJobs();

		Set<String> inventoryJobNames = new HashSet<>();

		if ((serviceJobs != null) && (!serviceJobs.isEmpty())) {
			validateJobsInService(serviceJobs, inventoryJobNames, service.getName());
		} else {
			log.error("At least one job must be present");
			throw new ConfigException("At least one job must be present");
		}
	}

	public void manageNodesInJob(Job job) throws ConfigException {
		List<JobInventoryNode> jobNodes = job.getJobNodes();

		Set<String> inventoryNodeNames = new HashSet<>();

		if ((jobNodes != null) && (!jobNodes.isEmpty())) {
			validateNodesInJob(jobNodes, inventoryNodeNames);
		} else {
			log.error("At least one node must be present");
			throw new ConfigException("At least one node must be present");
		}
	}

	public void validateNodesInJob(List<JobInventoryNode> jobNodes, Set<String> inventoryNodeNames)
			throws ConfigException {
		for (JobInventoryNode sn : jobNodes) {
			String nodeName = sn.getInventoryNodeName();
			if (nodeName == null || nodeName.isEmpty()) {
				throw new ConfigException("Node name is null or empty");
			}
			if (nodeName.equals(Job.DEF_JOB_MONITORING)) {
				throw new ConfigException(
						"Node with name '" + nodeName + "'" + "cannot be associated to job different to Monitoring");
			}
			Map<String, Object> queryParams2 = new HashMap<String, Object>();
			queryParams2.put("name", nodeName);

			InventoryNode result = DBUtility.getNamedQuerySingleResultByNamedParams(em, InventoryNode.QUERY_READ_NODE,
					InventoryNode.class, queryParams2);

			if (result == null) {
				log.error("Node not found, name:{}", nodeName);
				throw new ConfigException("Node not found");
			} else {
				sn.setNode(result);
			}

			if (!inventoryNodeNames.add(nodeName)) {
				throw new ConfigException("duplicated node with name '" + nodeName + "'");
			}
		}
	}

	public void validateNodesInSlice(List<SliceInventoryNode> sliceNodes, Set<String> inventoryNodeNames)
			throws ConfigException {
		for (SliceInventoryNode sn : sliceNodes) {
			String nodeName = sn.getInventoryNodeName();
			if (nodeName == null || nodeName.isEmpty()) {
				throw new ConfigException("Node name is null or empty");
			}
			if (nodeName.equals(Job.DEF_JOB_MONITORING)) {
				throw new ConfigException(
						"Node with name '" + nodeName + "'" + "cannot be associated to slice different to Monitoring");
			}
			Map<String, Object> queryParams2 = new HashMap<String, Object>();
			queryParams2.put("name", nodeName);

			InventoryNode result = DBUtility.getNamedQuerySingleResultByNamedParams(em, InventoryNode.QUERY_READ_NODE,
					InventoryNode.class, queryParams2);

			if (result == null) {
				log.error("Node not found, name:{}", nodeName);
				throw new ConfigException("Node not found");
			} else {
				sn.setNode(result);
			}

			if (!inventoryNodeNames.add(nodeName)) {
				throw new ConfigException("duplicated node with name '" + nodeName + "'");
			}
		}
	}

	public void validateJobsInService(List<InventoryServiceJob> serviceJobs, Set<String> jobNames, String nameService)
			throws ConfigException {
		for (InventoryServiceJob sn : serviceJobs) {
			String jobName = sn.getJobName();
			if (jobName == null || jobName.isEmpty()) {
				throw new ConfigException("Job name is null or empty");
			}
			if (jobName.equals(Job.DEF_JOB_MONITORING)) {
				throw new ConfigException("Job with name '" + jobName + "'"
						+ "cannot be associated to service different to ServiceMonitoring");
			}

			Map<String, Object> queryParams2 = new HashMap<String, Object>();
			queryParams2.put("name", jobName);

			Job result = DBUtility.getNamedQuerySingleResultByNamedParams(em, Job.QUERY_READ_JOB, Job.class,
					queryParams2);
			if (result == null) {
				log.error("Job not found, name:{}", jobName);
				throw new ConfigException("Job not found");
			} else {
				sn.setJob(result);
			}

			if (!jobNames.add(jobName)) {
				throw new ConfigException("duplicated job with name '" + jobName + "'");
			}

			// cerco in tutti i servivi se il mio nodo � gia' associato
			List<InventoryService> sl = DBUtility.getNamedQueryResultList(em, InventoryService.QUERY_READ_ALL_SERVICES,
					InventoryService.class);

			for (InventoryService serv : sl) {
				log.debug("Serv {} myservice :{}", serv.getName(), nameService);
				if (!serv.getName().equals(nameService)) {
					List<InventoryServiceJob> jobsList = serv.getJobs();
					for (InventoryServiceJob job : jobsList) {
						if (job.getJobName().equals(jobName)) {
							log.debug("Job {} service association to another service : ", jobName, serv.getName());
							throw new ConfigException("Job already associated to another service");
						}
					}
				}

			}
		}
	}

	protected void addRule(AlertRule rule) throws Exception {
		
		StringBuilder cmd = new StringBuilder();
		
		cmd.append(System.getProperty("jboss.server.config.dir"));
		cmd.append("/../../../bin/addAlert.sh ");
		cmd.append(rule.getName());
		cmd.append(" \"").append(rule.getExpression()).append("\" ");
		cmd.append((rule.getDuration() != null) ? rule.getDuration() : "1m").append(" ");
		cmd.append((rule.getSeverity() != null) ? rule.getSeverity() : "critical").append(" ");
		cmd.append((rule.getSummary() != null) ? rule.getSummary() : "").append(" ");
		cmd.append((rule.getDescription() != null) ? rule.getDescription() : "");
		
		String cmdString = cmd.toString();
		
		log.debug("cmd :" + cmdString);
		if (OSUtils.isUnix()) {
			OSUtils.executeShellCommandUnix(cmdString);
		} else {
			log.debug("Windows");
		}

	}
	
	protected void removeRule(AlertRule rule) throws Exception {
		
		StringBuilder cmd = new StringBuilder();
		
		cmd.append(System.getProperty("jboss.server.config.dir"));
		cmd.append("/../../../bin/removeAlert.sh ");
		cmd.append(rule.getName());
		
		String cmdString = cmd.toString();
		
		log.debug("cmd :" + cmdString);
		if (OSUtils.isUnix()) {
			OSUtils.executeShellCommandUnix(cmdString);
		} else {
			log.debug("Windows");
		}

	}	

	protected <T extends OMEntity> void regeneratePrometheusConfig(boolean restart)
			throws JsonProcessingException, Exception {

		String paramsScript = "";

		// se sourceType == EXPORTER resta uguale, da aggiungere oltre all'intervallo
		// anche
		// metrics_path: /metrics (esempio)
		// la porta diventa quella del source
		//
		// se sourceType == GATEWAY � uguale ad exporter, l'indirizzo ip siamo noi
		// stessi, la porta le
		// per impostiamo a 10000, bisogna fare un ws per farci mandare i contatori
		// e recuperare l'indirizzo IP di chi ci manda i contatori
		//
		// se sourceType == CLIENT to do

		List<InventoryService> listAllServices = DBUtility.getNamedQueryResultList(em,
				InventoryService.QUERY_READ_ALL_SERVICES, InventoryService.class);

		for (InventoryService service : listAllServices) {

			ObjectMapper mapper = new ObjectMapper();

			if (service != null) {
				log.info("Service Name is  " + service.getName());
				if (!service.getName().equals(InventoryService.DEF_SERVICE_MONITORING)) {
					log.info("into if  != ServiceMonitoring");

					List<InventoryServiceJob> serviceJobs = service.getJobs();
					if ((serviceJobs != null) && (!serviceJobs.isEmpty())) {

						for (InventoryServiceJob sj : serviceJobs) {
							List<PrometheusTarget> listPrometheustargets = new ArrayList<>();
							Job myjob = sj.getJob();
							if (myjob != null) {
								if ((myjob.getJobSource().getSourceType().equals(SourceTypeEnum.CLIENT))
										|| (myjob.getJobSource().getSourceType().equals(SourceTypeEnum.GATEWAY)))
									continue;
								if (!myjob.getName().equals(Job.DEF_JOB_MONITORING)) {
									log.info("into if  != Monitoring");
									log.info("Job Name is  " + myjob.getName());
									List<JobInventoryNode> serviceNodes = myjob.getJobNodes();

									if ((serviceNodes != null) && (!serviceNodes.isEmpty())) {

										for (JobInventoryNode sn : serviceNodes) {

											InventoryNode nodeData = sn.getNode();
											PrometheusTarget pt = new PrometheusTarget();
											List<String> list = new ArrayList<String>();
											list.add(nodeData.getIp() + ":" + myjob.getJobSource().getPort());
											PrometheusTargetLabel ptl = new PrometheusTargetLabel();
											ptl.setInstance(nodeData.getName());
											ptl.setService(service.getName());
											ptl.setOrg(service.getOrg());
											ptl.setType(myjob.getJobSource().getDashboardType());
											pt.setLabels(ptl);
											pt.setTargets(list);
											listPrometheustargets.add(pt);
										}
									}

									String jsonS = mapper.writeValueAsString(listPrometheustargets);

									String jsonPretty = mapper.writerWithDefaultPrettyPrinter()
											.writeValueAsString(listPrometheustargets);

									log.info("create mapper node : " + jsonS);
									log.info("create mapper node : " + jsonPretty);

									// String filenameTarget = ConfigProperty.PROMETHEUS_PATH
									// .getStringValue() + service.getName() + ".json";
									String filenameTarget = "/etc/prometheus/" + myjob.getName() + ".json";

									File fileJson = new File(filenameTarget);

									log.info("filenameTarget : " + filenameTarget);

									try {
										if (fileJson.exists())
											fileJson.delete();
										BufferedWriter out = new BufferedWriter(new FileWriter(fileJson));
										out.write(jsonPretty);
										out.close();

									} catch (Exception e) {
										log.error("Exception " + e.getMessage());
									} finally {
										log.error(" finally ");
									}
									paramsScript = paramsScript + myjob.getName() + ","
											+ myjob.getJobSource().getInterval() + ","
											+ myjob.getJobSource().getMetricPath() + " ";
								}
							}
						}
					}
				}
			}
		}
		log.debug("paramsScript :" + paramsScript);
		if (paramsScript != null) {
			String cmd = null;
			if (restart)
				cmd = System.getProperty("jboss.server.config.dir") + "/../../../bin/changePrometheusFile.sh"
						+ " restart " + paramsScript;
			else
				cmd = System.getProperty("jboss.server.config.dir") + "/../../../bin/changePrometheusFile.sh"
						+ " norestart " + paramsScript;

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

			if (nodeName.equals(InventoryNode.DEF_NODE_MONITORING)) {
				throw new ConfigException("Node Monitoring cannot be deleted");
			}
			Long counter = DBUtility.getNamedQuerySingleResultByName(em,
					SliceInventoryNode.QUERY_COUNT_SLICENODE_WITH_NODE, Long.class, nodeName);
			if (counter > 0) {
				throw new ConfigException("Node referenced by a slice cannot be deleted!");
			}
			Long counterJob = DBUtility.getNamedQuerySingleResultByName(em,
					JobInventoryNode.QUERY_COUNT_JOBNODE_WITH_NODE, Long.class, nodeName);
			if (counterJob > 0) {
				throw new ConfigException("Node referenced by a job cannot be deleted!");
			}
		}
		if (Slice.class.isInstance(obj)) {
			Slice slice = (Slice) obj;
			String sliceName = slice.getName();

			if (sliceName.equals(Slice.DEF_SLICE_MONITORING)) {
				throw new ConfigException("Slice Monitoring cannot be deleted");
			}
			Long counter = DBUtility.getNamedQuerySingleResultByName(em, UserSlice.QUERY_COUNT_USERSLICE_WITH_SLICE,
					Long.class, sliceName);
			if (counter > 0) {
				throw new ConfigException("Slice referenced by a user cannot be deleted!");
			}
		}

		if (Job.class.isInstance(obj)) {
			Job job = (Job) obj;
			String jobName = job.getName();

			if (jobName.equals(Job.DEF_JOB_MONITORING)) {
				throw new ConfigException("Job Monitoring cannot be deleted");
			}
			Long counter = DBUtility.getNamedQuerySingleResultByName(em,
					InventoryServiceJob.QUERY_COUNT_SERVICEJOB_WITH_JOB, Long.class, jobName);
			if (counter > 0) {
				throw new ConfigException("Job referenced by a service cannot be deleted!");
			}
		}

		persistEntityOnDelete(obj);

		if (InventoryService.class.isInstance(obj)) {
			regeneratePrometheusConfig(true);
		}
		
		if (AlertRule.class.isInstance(obj)) {
			removeRule((AlertRule) obj);
		}		

		return obj;
	}

	@Override
	public <T extends OMEntity> T update(T obj) throws Exception {

		log.debug("updateConfigurationDispatcherBean obj:" + obj.getClass().getName());

		boolean differentServiceList = false;

		// For Slice only
		if (Slice.class.isInstance(obj)) {
			log.debug("updateConfigurationDispatcherBean in slice");
			Slice slice = (Slice) obj;
			log.debug("updateConfigurationDispatcherBean slice : " + slice);
			if (slice != null) {
				if (slice.getName() != null) {

					if (slice.getName().equals(Slice.DEF_SLICE_MONITORING)) {
						log.error("Slice identified by name {} cannot be updated!", slice.getName());
						throw new ConfigException("Slice Monitoring cannot be updated");
					}
					Map<String, Object> queryParams1 = new HashMap<String, Object>();
					queryParams1.put("name", slice.getName());

					Slice oldSl = DBUtility.getNamedQuerySingleResultByNamedParams(em, Slice.QUERY_READ_SLICE,
							Slice.class, queryParams1);

					if (oldSl == null) {
						log.error("Slice identified by name {} not found!", slice.getName());
						throw new ConfigException("Slice not found");
					}

					// list new nodes
					manageNodesInSlice(slice);
				}

			} else {
				log.error("Slice Name cannot be null");
				throw new ConfigException("Slice Name cannot be null");
			}
		} else if (InventoryService.class.isInstance(obj)) {
			// For Service only
			log.debug("updateConfigurationDispatcherBean in service");
			InventoryService service = (InventoryService) obj;
			if (service != null) {
				if (service.getName() != null) {

					if (service.getName().equals(InventoryService.DEF_SERVICE_MONITORING)) {
						log.error("Service identified by name {} cannot be updated!", service.getName());
						throw new ConfigException("ServiceMonitoring cannot be updated");
					}
					Map<String, Object> queryParams1 = new HashMap<String, Object>();
					queryParams1.put("name", service.getName());

					InventoryService oldSrv = DBUtility.getNamedQuerySingleResultByNamedParams(em,
							InventoryService.QUERY_READ_SERVICE, InventoryService.class, queryParams1);

					if (oldSrv == null) {
						log.error("Service identified by name {} not found!", service.getName());
						throw new ConfigException("Service not found");
					}
					// list new jobs
					manageJobsInService(service);

					List<InventoryServiceJob> oldJobs = oldSrv.getJobs();
					List<String> oldJobNames = new ArrayList<String>();
					for (InventoryServiceJob ino : oldJobs) {
						oldJobNames.add(ino.getJobName());
					}

					List<InventoryServiceJob> newJobs = service.getJobs();
					List<String> srvJobNames = new ArrayList<String>();
					for (InventoryServiceJob ins : newJobs) {
						oldJobNames.add(ins.getJobName());
					}

					if (!(srvJobNames.containsAll(oldJobNames) && oldJobNames.containsAll(srvJobNames))) {
						differentServiceList = true;
						log.debug("jobs are different");
					} else {
						for (InventoryServiceJob ins : newJobs) {
							for (InventoryServiceJob ino : oldJobs) {
								if (ins.getJobName().equals(ino.getJobName())) {
									if (!ins.equals(ino)) {
										differentServiceList = true;
										log.debug("Object jobs are different");
										break;
									}
								}
							}
							if (differentServiceList == true)
								break;
						}
					}
				}

			} else {
				log.error("Service Name cannot be null");
				throw new ConfigException("Service Name cannot be null");
			}
		} else if (Job.class.isInstance(obj)) {
			// For Job only
			log.debug("updateConfigurationDispatcherBean in job");
			Job job = (Job) obj;
			if (job != null) {
				if (job.getName() != null) {

					if (job.getName().equals(Job.DEF_JOB_MONITORING)) {
						log.error("Job identified by name {} cannot be updated!", job.getName());
						throw new ConfigException("Job Monitoring cannot be updated");
					}
					Map<String, Object> queryParams1 = new HashMap<String, Object>();
					queryParams1.put("name", job.getName());

					Job oldJ = DBUtility.getNamedQuerySingleResultByNamedParams(em, Job.QUERY_READ_JOB, Job.class,
							queryParams1);

					if (oldJ == null) {
						log.error("Job identified by name {} not found!", job.getName());
						throw new ConfigException("Job not found");
					}

					// list new nodes
					manageNodesInJob(job);

				}

			} else {
				log.error("Job Name cannot be null");
				throw new ConfigException("Job Name cannot be null");
			}
		} else {
			log.error("Object not recognized");
			throw new ConfigException("Object not recognized");
		}

		persistEntityOnUpdate(obj);

		if (InventoryService.class.isInstance(obj) && differentServiceList) {
			log.debug("regeneratePrometheusConfig update service");
			regeneratePrometheusConfig(true);
		}

		return obj;
	}

	<T> T detachAndResetReturnedValue(T entity) {
		if (entity != null) {
			em.detach(entity);
		}
		return entity;
	}

	protected <T extends OMEntity> void persistEntityOnCreate(T obj) throws Exception {
		try {
			em.persist(obj);
			em.flush();
		} catch (Exception e) {
			String errMsg = "Error persisting " + obj + ": " + e.getMessage();
			log.error(errMsg, e);
			throw new Exception(errMsg, e);
		}

	}

	protected <T extends OMEntity> void persistEntityOnUpdate(T obj) throws Exception {
		try {
			em.merge(obj);
			em.flush();
		} catch (Exception e) {
			String errMsg = "Error merging " + obj + ": " + e.getMessage();
			log.error(errMsg, e);
			throw new Exception(errMsg, e);
		}

	}

	protected <T extends OMEntity> void persistEntityOnDelete(T obj) throws Exception {
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
