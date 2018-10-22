package com.italtel.monitoring.fe.common.service;

public interface ConfigurationService<T extends OMEntity> {

	public T create(T obj) throws Exception;
}
