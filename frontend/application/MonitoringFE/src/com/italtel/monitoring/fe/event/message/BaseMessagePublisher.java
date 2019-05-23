package com.italtel.monitoring.fe.event.message;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.italtel.monitoring.fe.utils.MDCHelper;

public abstract class BaseMessagePublisher {

	private final static Logger log = LoggerFactory
			.getLogger(BaseMessagePublisher.class);

	private static final String JMSConnectionFactoryJNDIName = "java:/JmsConnectionFactory";

	public static final String JMSMessagePropertyName_ServiceUnitInstance = "SUI";
	public static final String JMSMessagePropertyName_LogTx = "LogTx";
	public static final String JMSMessagePropertyName_Timestamp = "Timestamp";

	private final String jmsDestinationJNDIName;
	private final String publisherName;

	private Connection connection;
	private Session session;
	private MessageProducer producer;

	public BaseMessagePublisher(String publisherName,
			String jmsDestinationJNDIName) {
		this.publisherName = publisherName;
		this.jmsDestinationJNDIName = jmsDestinationJNDIName;
	}

	abstract protected String getDefaultJMSMessageType();

	public void start() throws Exception {
		log.trace("Starting {}", publisherName);

		try {
			InitialContext iniCtx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) iniCtx
					.lookup(JMSConnectionFactoryJNDIName);
			Destination destination = (Destination) iniCtx
					.lookup(jmsDestinationJNDIName);

			// Create connection
			connection = cf.createConnection();
			connection.setClientID(publisherName);
			// Create session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// Start connection
			connection.start();
			// Create producer
			producer = session.createProducer(destination);

			log.trace("Started {}", publisherName);
		} catch (Exception e) {
			stop();

			throw new Exception("Error starting event publisher: "
					+ e.getMessage(), e);
		}
	}

	public void stop() {
		log.trace("Stopping {}", publisherName);

		if (connection != null) {
			try {
				connection.stop();
			} catch (JMSException e) {
				log.error("Error stopping connection: {}", e.getMessage(), e);
			}

			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					log.error("Error closing session: {}", e.getMessage(), e);
				}
				session = null;
			}

			try {
				connection.close();
			} catch (JMSException e) {
				log.error("Error closing connection: {}", e.getMessage(), e);
			}
			connection = null;
		}

		if (producer != null) {
			try {
				producer.close();
			} catch (JMSException e) {
				log.error("Error closing producer: {}", e.getMessage(), e);
			}
			producer = null;
		}

		log.trace("Stopped {}", publisherName);
	}

	public void sendMessage(Serializable message, Map<String, Object> properties)
			throws JMSException {
		sendMessage(message, properties, getDefaultJMSMessageType());
	}

	public void sendMessage(Serializable message,
			Map<String, Object> properties, String jmsMsgType)
			throws JMSException {
		if (producer == null) {
			throw new JMSException("Message publisher is not started!");
		}

		synchronized (session) {
			ObjectMessage om = session.createObjectMessage();

			if (jmsMsgType != null) {
				om.setJMSType(jmsMsgType);
			}

			if (properties != null) {
				for (Entry<String, Object> property : properties.entrySet()) {
					om.setObjectProperty(property.getKey(), property.getValue());
				}
			}
			om.setStringProperty(JMSMessagePropertyName_LogTx,
					MDCHelper.getTx());

			if (message != null) {
				om.setObject(message);
			}

			om.setStringProperty(JMSMessagePropertyName_Timestamp,
					Long.toString(Calendar.getInstance().getTimeInMillis()));

			producer.send(om);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName())
				.append(" [jmsDestinationJNDIName=")
				.append(jmsDestinationJNDIName).append(", publisherName=")
				.append(publisherName).append("]");
		return builder.toString();
	}

}
