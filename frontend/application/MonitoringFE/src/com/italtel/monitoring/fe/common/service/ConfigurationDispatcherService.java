package com.italtel.monitoring.fe.common.service;

import javax.persistence.EntityManager;

public interface ConfigurationDispatcherService {

	public <T extends OMEntity> T create(T obj) throws Exception;

	public <T extends OMEntity> T update(T obj) throws Exception;

	public <T extends OMEntity> T delete(T obj) throws Exception;

	public <T extends OMEntity> T read(T obj, Class<T> clazz) throws Exception;

	public <T extends OMEntity> T getDefault(Class<T> clazz) throws Exception;

	public EntityManager getEntityManager();
}
