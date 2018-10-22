package com.italtel.monitoring.fe.common.service.conf.wrte;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.italtel.monitoring.fe.common.service.ConfigurationDispatcherService;
import com.italtel.monitoring.fe.common.service.ConfigurationService;
import com.italtel.monitoring.fe.common.service.OMEntity;

public abstract class CommonConfService<T extends OMEntity> implements
		ConfigurationService<T> {
	protected final ConfigurationDispatcherService configurator;
	private final static Logger log = LoggerFactory
			.getLogger(CommonConfService.class);

	public CommonConfService(ConfigurationDispatcherService configurator) {
		this.configurator = configurator;
	}

	@Override
	public T create(T entity) throws Exception {
		persistEntityOnCreate(entity);
		return entity;

	}

	protected void persistEntityOnCreate(T obj) throws Exception {
		EntityManager em = configurator.getEntityManager();
		try {
			em.persist(obj);
			em.flush();
		} catch (Exception e) {
			String errMsg = "Error persisting " + obj + ": " + e.getMessage();
			log.error(errMsg, e);
			throw new Exception(errMsg, e);
		}

	}

	protected void persistEntityOnDelete(T obj) throws Exception {
		log.debug("Removing: {}", obj);
		EntityManager em = configurator.getEntityManager();
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
}
