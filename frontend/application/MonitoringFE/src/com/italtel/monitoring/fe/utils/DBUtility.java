package com.italtel.monitoring.fe.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class DBUtility {

	/* Count */

	public static <T> long countEntities(EntityManager em, Class<T> clazz) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		cq.select(qb.count(cq.from(clazz)));

		TypedQuery<Long> typedQuery = em.createQuery(cq);
		return typedQuery.getSingleResult();
	}

	/* Native Query */

	public static <T> T getNativeQuerySingleResultByNamedParams(
			EntityManager em, String nativeQueryString, Class<T> classType,
			Map<String, Object> queryParams) {
		return _getSingleResultByNamedParams(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				queryParams);
	}

	public static <T> T getNativeQuerySingleResultByPositionalParams(
			EntityManager em, String nativeQueryString, Class<T> classType,
			Map<Integer, Object> queryParams) {
		return _getSingleResultByPositionalParams(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				queryParams);
	}

	public static <T> T getNativeQuerySingleResultByName(EntityManager em,
			String nativeQueryString, Class<T> classType, String name) {
		return _getSingleResultByName(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				name);
	}

	public static <T> T getNativeQuerySingleResultById(EntityManager em,
			String nativeQueryString, Class<T> classType, int id) {
		return _getSingleResultById(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				id);
	}

	public static <T> T getNativeQuerySingleResultById(EntityManager em,
			String nativeQueryString, Class<T> classType, long id) {
		return _getSingleResultById(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				id);
	}

	public static <T> T getNativeQuerySingleResultById(EntityManager em,
			String nativeQueryString, Class<T> classType, Object id) {
		return _getSingleResultById(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				id);
	}

	public static <T> T getNativeQuerySingleResult(EntityManager em,
			String nativeQueryString, Class<T> classType) {
		return getNativeQuerySingleResultByNamedParams(em, nativeQueryString,
				classType, null);
	}

	public static <T> List<T> getNativeQueryResultListByNamedParams(
			EntityManager em, String nativeQueryString, Class<T> classType,
			Map<String, Object> queryParams) {
		return _getResultListByNamedParams(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				queryParams);
	}

	public static <T> List<T> getNativeQueryResultListByPositionalParams(
			EntityManager em, String nativeQueryString, Class<T> classType,
			Map<Integer, Object> queryParams) {
		return _getResultListByPositionalParams(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				queryParams);
	}

	public static <T> List<T> getNativeQueryResultListByName(EntityManager em,
			String nativeQueryString, Class<T> classType, String name) {
		return _getResultListByName(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				name);
	}

	public static <T> List<T> getNativeQueryResultListById(EntityManager em,
			String nativeQueryString, Class<T> classType, int id) {
		return _getResultListById(em,
				_getNativeQuery(em, nativeQueryString, classType), classType,
				id);
	}

	public static <T> List<T> getNativeQueryResultList(EntityManager em,
			String nativeQueryString, Class<T> classType) {
		return getNativeQueryResultListByNamedParams(em, nativeQueryString,
				classType, null);
	}

	static <T> Query _getNativeQuery(EntityManager em,
			String nativeQueryString, Class<T> classType) {
		Query query = null;
		if (classType == null) {
			query = em.createNativeQuery(nativeQueryString);
		} else {
			query = em.createNativeQuery(nativeQueryString, classType);
		}
		return query;
	}

	/* Query */

	public static <T> T getQuerySingleResultByNamedParams(EntityManager em,
			String queryString, Class<T> classType,
			Map<String, Object> queryParams) {
		return _getSingleResultByNamedParams(em,
				_getQuery(em, queryString, classType), classType, queryParams);
	}

	public static <T> T getQuerySingleResultByPositionalParams(
			EntityManager em, String queryString, Class<T> classType,
			Map<Integer, Object> queryParams) {
		return _getSingleResultByPositionalParams(em,
				_getQuery(em, queryString, classType), classType, queryParams);
	}

	public static <T> T getQuerySingleResultByName(EntityManager em,
			String queryString, Class<T> classType, String name) {
		return _getSingleResultByName(em,
				_getQuery(em, queryString, classType), classType, name);
	}

	public static <T> T getQuerySingleResultById(EntityManager em,
			String queryString, Class<T> classType, int id) {
		return _getSingleResultById(em, _getQuery(em, queryString, classType),
				classType, id);
	}

	public static <T> T getQuerySingleResultById(EntityManager em,
			String queryString, Class<T> classType, long id) {
		return _getSingleResultById(em, _getQuery(em, queryString, classType),
				classType, id);
	}

	public static <T> T getQuerySingleResultById(EntityManager em,
			String queryString, Class<T> classType, Object id) {
		return _getSingleResultById(em, _getQuery(em, queryString, classType),
				classType, id);
	}

	public static <T> T getQuerySingleResult(EntityManager em,
			String queryString, Class<T> classType) {
		return getQuerySingleResultByNamedParams(em, queryString, classType,
				null);
	}

	public static <T> List<T> getQueryResultListByNamedParams(EntityManager em,
			String queryString, Class<T> classType,
			Map<String, Object> queryParams) {
		return _getResultListByNamedParams(em,
				_getQuery(em, queryString, classType), classType, queryParams);
	}

	public static <T> List<T> getQueryResultListByPositionalParams(
			EntityManager em, String queryString, Class<T> classType,
			Map<Integer, Object> queryParams) {
		return _getResultListByPositionalParams(em,
				_getQuery(em, queryString, classType), classType, queryParams);
	}

	public static <T> List<T> getQueryResultListByName(EntityManager em,
			String queryString, Class<T> classType, String name) {
		return _getResultListByName(em, _getQuery(em, queryString, classType),
				classType, name);
	}

	public static <T> List<T> getQueryResultListById(EntityManager em,
			String queryString, Class<T> classType, int id) {
		return _getResultListById(em, _getQuery(em, queryString, classType),
				classType, id);
	}

	public static <T> List<T> getQueryResultList(EntityManager em,
			String queryString, Class<T> classType) {
		return getQueryResultListByNamedParams(em, queryString, classType, null);
	}

	static <T> TypedQuery<T> _getQuery(EntityManager em, String queryString,
			Class<T> classType) {
		TypedQuery<T> typedQuery = em.createQuery(queryString, classType);
		return typedQuery;
	}

	/* Named Query */

	public static <T> T getNamedQuerySingleResultByNamedParams(
			EntityManager em, String namedQueryName, Class<T> classType,
			Map<String, Object> queryParams) {
		return _getSingleResultByNamedParams(em,
				_getNamedQuery(em, namedQueryName, classType), classType,
				queryParams);
	}

	public static <T> T getNamedQuerySingleResultByPositionalParams(
			EntityManager em, String namedQueryName, Class<T> classType,
			Map<Integer, Object> queryParams) {
		return _getSingleResultByPositionalParams(em,
				_getNamedQuery(em, namedQueryName, classType), classType,
				queryParams);
	}

	public static <T> T getNamedQuerySingleResultByName(EntityManager em,
			String namedQueryName, Class<T> classType, String name) {
		return _getSingleResultByName(em,
				_getNamedQuery(em, namedQueryName, classType), classType, name);
	}

	public static <T> T getNamedQuerySingleResultById(EntityManager em,
			String namedQueryName, Class<T> classType, int id) {
		return _getSingleResultById(em,
				_getNamedQuery(em, namedQueryName, classType), classType, id);
	}

	public static <T> T getNamedQuerySingleResultById(EntityManager em,
			String namedQueryName, Class<T> classType, long id) {
		return _getSingleResultById(em,
				_getNamedQuery(em, namedQueryName, classType), classType, id);
	}

	public static <T> T getNamedQuerySingleResultById(EntityManager em,
			String namedQueryName, Class<T> classType, Object id) {
		return _getSingleResultById(em,
				_getNamedQuery(em, namedQueryName, classType), classType, id);
	}

	public static <T> T getNamedQuerySingleResult(EntityManager em,
			String queryString, Class<T> classType) {
		return getNamedQuerySingleResultByNamedParams(em, queryString,
				classType, null);
	}

	public static <T> List<T> getNamedQueryResultListByNamedParams(
			EntityManager em, String namedQueryName, Class<T> classType,
			Map<String, Object> queryParams) {
		return _getResultListByNamedParams(em,
				_getNamedQuery(em, namedQueryName, classType), classType,
				queryParams);
	}

	public static <T> List<T> getNamedQueryResultListByNamedParams(
			EntityManager em, String namedQueryName, Class<T> classType,
			Map<String, Object> queryParams, int limit) {
		TypedQuery<T> _namedQuery = _getNamedQuery(em, namedQueryName,
				classType);
		if (limit > 0) {
			_namedQuery.setMaxResults(limit);
		}
		return _getResultListByNamedParams(em, _namedQuery, classType,
				queryParams);
	}

	public static <T> List<T> getNamedQueryResultListByPositionalParams(
			EntityManager em, String namedQueryName, Class<T> classType,
			Map<Integer, Object> queryParams) {
		return _getResultListByPositionalParams(em,
				_getNamedQuery(em, namedQueryName, classType), classType,
				queryParams);
	}

	public static <T> List<T> getNamedQueryResultListByName(EntityManager em,
			String namedQueryName, Class<T> classType, String name) {
		return _getResultListByName(em,
				_getNamedQuery(em, namedQueryName, classType), classType, name);
	}

	public static <T> List<T> getNamedQueryResultListById(EntityManager em,
			String namedQueryName, Class<T> classType, int id) {
		return _getResultListById(em,
				_getNamedQuery(em, namedQueryName, classType), classType, id);
	}

	public static <T> List<T> getNamedQueryResultListById(EntityManager em,
			String namedQueryName, Class<T> classType, long id) {
		return _getResultListById(em,
				_getNamedQuery(em, namedQueryName, classType), classType, id);
	}

	public static <T> List<T> getNamedQueryResultList(EntityManager em,
			String namedQueryName, Class<T> classType) {
		return getNamedQueryResultListByNamedParams(em, namedQueryName,
				classType, null, 0);
	}

	public static <T> List<T> getNamedQueryResultList(EntityManager em,
			String namedQueryName, Class<T> classType, int limit) {
		return getNamedQueryResultListByNamedParams(em, namedQueryName,
				classType, null, limit);
	}

	static <T> TypedQuery<T> _getNamedQuery(EntityManager em,
			String namedQueryName, Class<T> classType) {
		TypedQuery<T> typedQuery = em.createNamedQuery(namedQueryName,
				classType);
		return typedQuery;
	}

	/* Common */

	@SuppressWarnings("unchecked")
	static <T> T _getSingleResultByNamedParams(EntityManager em, Query query,
			Class<T> classType, Map<String, Object> queryParams) {
		try {
			if (queryParams != null) {
				for (Entry<String, Object> param : queryParams.entrySet()) {
					query.setParameter(param.getKey(), param.getValue());
				}
			}
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T _getSingleResultByPositionalParams(EntityManager em,
			Query query, Class<T> classType, Map<Integer, Object> queryParams) {
		try {
			if (queryParams != null) {
				for (Entry<Integer, Object> param : queryParams.entrySet()) {
					query.setParameter(param.getKey(), param.getValue());
				}
			}
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T _getSingleResultByName(EntityManager em, Query query,
			Class<T> classType, String name) {
		try {
			query.setParameter("name", name);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T _getSingleResultById(EntityManager em, Query query,
			Class<T> classType, int id) {
		try {
			query.setParameter("id", id);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T _getSingleResultById(EntityManager em, Query query,
			Class<T> classType, long id) {
		try {
			query.setParameter("id", id);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> T _getSingleResultById(EntityManager em, Query query,
			Class<T> classType, Object id) {
		try {
			query.setParameter("id", id);
			return (T) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static <T> List<T> _getResultListByNamedParams(EntityManager em,
			Query query, Class<T> classType, Map<String, Object> queryParams) {
		if (queryParams != null) {
			for (Entry<String, Object> param : queryParams.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	static <T> List<T> _getResultListByPositionalParams(EntityManager em,
			Query query, Class<T> classType, Map<Integer, Object> queryParams) {
		if (queryParams != null) {
			for (Entry<Integer, Object> param : queryParams.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	static <T> List<T> _getResultListByName(EntityManager em, Query query,
			Class<T> classType, String name) {
		query.setParameter("name", name);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	static <T> List<T> _getResultListById(EntityManager em, Query query,
			Class<T> classType, int id) {
		query.setParameter("id", id);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	static <T> List<T> _getResultListById(EntityManager em, Query query,
			Class<T> classType, long id) {
		query.setParameter("id", id);
		return query.getResultList();
	}
}
