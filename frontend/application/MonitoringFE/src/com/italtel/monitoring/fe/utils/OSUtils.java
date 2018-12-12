package com.italtel.monitoring.fe.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSUtils {

	private static final Logger log = LoggerFactory.getLogger(OSUtils.class);

	public static final String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	public static ArrayList<String> executeShellCommand(String cmd)
			throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder buffer = new StringBuilder();
		if (isWindows()) {
			buffer.append("cmd /C ");
		}
		buffer.append(cmd);

		String line = null;
		Process pr = null;
		BufferedReader stdErr = null, stdOut = null;

		try {
			log.debug("Executing command: {}", buffer);
			pr = Runtime.getRuntime().exec(buffer.toString());
			int exitVal = pr.waitFor();

			stdOut = new BufferedReader(new InputStreamReader(
					pr.getInputStream(), "UTF-8"));
			stdErr = new BufferedReader(new InputStreamReader(
					pr.getErrorStream(), "UTF-8"));

			while ((line = stdOut.readLine()) != null) {
				result.add(line);
			}

			if (exitVal != 0) {
				while ((line = stdErr.readLine()) != null) {
					result.add(line);
				}
				log.error("Error on command '{}': {}", cmd, result);
				throw new Exception(result.toString());
			} else {
				log.debug("Executed command '{}' with result: {}", buffer,
						result);
			}

			return result;
		} finally {
			if (stdOut != null) {
				try {
					stdOut.close();
				} catch (Exception e) {
				}
			}
			if (stdErr != null) {
				try {
					stdErr.close();
				} catch (Exception e) {
				}
			}
			if (pr != null) {
				pr.destroy();
			}
		}
	}

	public static ArrayList<String> executeShellCommandUnix(String cmd)
			throws Exception {
		ArrayList<String> result = new ArrayList<String>();

		String line = null;
		Process pr = null;
		BufferedReader stdErr = null, stdOut = null;

		try {
			// log.debug("Executing command unix: {}", cmd);
			String[] commands = { "/bin/sh", "-c", cmd };
			pr = Runtime.getRuntime().exec(commands);
			int exitVal = pr.waitFor();

			stdOut = new BufferedReader(new InputStreamReader(
					pr.getInputStream(), "UTF-8"));
			stdErr = new BufferedReader(new InputStreamReader(
					pr.getErrorStream(), "UTF-8"));

			while ((line = stdOut.readLine()) != null) {
				result.add(line);
			}

			if (exitVal != 0) {
				while ((line = stdErr.readLine()) != null) {
					result.add(line);
				}
				log.error("Error on command '{}': {}", cmd, result);
				throw new Exception(result.toString());
			} else {
				log.debug("Executed sh command with result: {}", result);
			}

			return result;
		} finally {
			if (stdOut != null) {
				try {
					stdOut.close();
				} catch (Exception e) {
				}
			}
			if (stdErr != null) {
				try {
					stdErr.close();
				} catch (Exception e) {
				}
			}
			if (pr != null) {
				pr.destroy();
			}
		}
	}
	
	public static String getGrafanaPort() {
	  String grPort=null;
	  grPort = System.getenv("GF_PORT_MON");
	
	  if(grPort == null)
		 grPort = "3000";

	  log.debug("grPort " + grPort);
      return grPort;
	}

}