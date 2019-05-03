package com.italtel.monitoring.fe.utils;

import java.util.UUID;

import org.slf4j.MDC;

import com.italtel.monitoring.fe.log.ServiceType;

public class MDCHelper {

	private static final String TX_KEY = "tx";

	public static void startTx(String prefix) {
		startTx(prefix, false);
	}

	public static void startTx() {
		startTx(null, false);
	}

	public static void startTx(String prefix, boolean overwrite) {
		String pre = (prefix != null) ? prefix + "-" : "";
		if (overwrite || MDC.get(TX_KEY) == null) {
			MDC.put(TX_KEY, pre + UUID.randomUUID().toString());
		}
	}

	public static void endTx() {
		MDC.remove(TX_KEY);
	}

	public static String getTx() {
		return MDC.get(TX_KEY);
	}

	public static void setTx(String tx) {
		MDC.put(TX_KEY, tx);
	}

	public static void setServiceType(ServiceType serviceType) {
		if (MDC.get("serviceType") == null) {
			MDC.put("serviceType", serviceType.name());
		}
	}

	public static void setDestType(String destType) {
		MDC.put("DEST_TYPE", destType);
	}

	public static void unsetDestType() {
		MDC.remove("DEST_TYPE");
	}

	public static void setDest(String dest) {
		MDC.put("DEST", dest);
	}

	public static void unsetDest() {
		MDC.remove("DEST");
	}

	public static String getDest() {
		return MDC.get("DEST");
	}

}
