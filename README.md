README.md

# 5GCity-monitoring

## Scope

Monitoring manager module for
5GCity distributed cloud and radio platform.


The Monitoring manager module is composed by three main submodules:
- a java Frontend application - Monitoring WebGui -
- a customized Prometheus tool
- a customized Grafana Tool


## Development

To contribute to the development of the 5GCity monitoring manager,
you may use the very same development workflow
as for any other 5GCity Github project.
That is, you have to fork the repository and create pull requests.


## Build

##### Prerequisites

- Ant 1.9
- Java 1.8 

##### Dependencies

Wildfly-14.0.1.Final installed and customized

- _cd `<pathBuild>`_
- _wget http://download.jboss.org/wildfly/14.0.1.Final/wildfly-14.0.1.Final.tar.gz_
- _tar xvfz wildfly-14.0.1.Final.tar.gz_
- _chmod -R 777 wildfly-14.0.1.Final_
- _cd wildfly-14.0.1.Final_
- _cp -R modules modules.orig_
- _cp -R standalone standalone.orig_
- _cp -R domain domain.orig_

##### Build Setup

a. Get from github all 5GCity-monitoring projects directories into a directory to do make the build (i.e. _`<pathBuild>`/5GCity-monitoring_)

b. Edit the file _`<pathBuild>`/5GCity-monitoring/conf/build/env.build.properties_ to adapt it to your test-bed setup:
- JBOSS_HOME=<(where you have installed wildfly-14.0.1.Final)>
- JAVA_HOME=<(where there is jdk or jre 1.8)>
	
c. Run the build with Ant to produce the file _`<pathBuild>`/5GCity-monitoring/target/tar/MONITORING-<DATE>.tgz_ 
    	
- _cd `<pathBuild>`/5GCity-monitoring_
- _ant -buildfile build.xml targetMon_




## Deployment

##### Prerequisites

- docker 1.13.0
- docker-compose 1.22.0

##### Dependencies

Last generated deliverable file must be available: MONITORING-<DATE>.tgz 
(i.e. MONITORING-07-03-2019.tgz)


##### Deployment Setup

a. Untar file _MONITORING-`<DATE>`.tgz_  in a directory  (i.e. _/opt/monitoring_)
    
- _cd /opt/monitoring_
- put in this directory the last deliverable file _MONITORING-`<DATE>`.tgz_
- _tar xvfz `MONITORING-`<DATE>`.tgz_

b. Install Linux node exporter and run it on test-bed target node

- _mkdir /opt/exporters_
- _cp /opt/monitoring/exporters/* /opt/exporters_
- _cd /opt/exporters_
- _tar xvfz node_exporter-\*\.\*-amd64\.tar\.gz_
- _cd node_exporter-\*\.\*-amd64_
- _./node_exporter &_


c. (Mandatory only on first deployment) The file _/opt/monitoring/config\.properties_ must contain the values for the ports used to run grafana(default=3000), prometheus(default=9090) and frontend application(default=8888)

- _cp /opt/monitoring/config\.properties\.sample /opt/monitoring/config\.properties_

d. (Optional) If you want to use ports'values different from default, please edit this file (_/opt/monitoring/config\.properties_) before to proceed with step e.

e. Run the install.sh script with parameter _`<IPAddressTarget>`_ = Management IP address of the your's test-bed target

 ___Please specify the natted ip address for _`<IPAddressTarget>`_, if any___
	
- _./install.sh `<IPAddressTarget>`_
	
f. Run command  docker-compose up  in background to startup 5G monitoring application

- _docker-compose up -d_

NOTE: whenever you have an error in startup 5G monitoring application (i.e port already in use), first shutdown the application and then execute steps d., e. and f. again.  
 To shutdown the 5G monitoring application run the command: *docker-compose down*


##### Deploy exporters on remote nodes

Exporters must be installed and running on each remote node

To deploy Linux node exporters and get system metrics:

. You can get it from _https://github.com/prometheus/node_exporter/releases/download/v0.16.0/node_exporter-0.16.0.linux-amd64.tar.gz_) or you can find it on _/opt/monitoring/exporters/node_exporter-0.16.0.linux-amd64.tar.gz_ so you can put this file on remote node on any directory

b. Install on each remote node the Linux node exporter and run it 

- Access to remote node and put the file _node_exporter-0.16.0.linux-amd64.tar.gz_ on any directory (e.g. `<anydir>`)
- _cd `<anydir>`_
- _tar xvfz node_exporter-\*\.\*-amd64\.tar\.gz_
- _cd node_exporter-\*\.\*-amd64_
- _./node_exporter &_



To deploy apache exporters and get apache metrics:

a. You can find it on _/opt/monitoring/exporters/apache_exporter.tar.gz_ so you can put this file on remote node on any directory

b. Install on each remote node the apache exporter and run it 

- Access to remote node and put the file _apache_exporter.tar.gz_ on any directory (e.g. `<anydir>`)
- _cd `<anydir>`_
- _tar xvfz apache_exporter.tar.gz_
- _cd apache_exporter_
- _./apache_exporter &_

Otherwise, valid ONLY for monitoring one only remote node from another "local node"

a2. You can find apache exporter on _/opt/monitoring/exporters/apache_exporter.tar.gz_ so you can put this file on local node on any directory

b2. Install on the local node the apache exporter and run it to scrape only one remote node:

- Access to local node and put the file _apache_exporter.tar.gz_ on any directory (e.g. `<anydir>`)
- _cd `<anydir>`_
- _tar xvfz apache_exporter.tar.gz_
- _cd apache_exporter_
- _./apache_exporter -scrape_uri "http://<remoteNodeIPAddress>/server-status/?auto" &_

		
## Usage

Once the Monitoring manager is running, please open in your browser the Monitoring WebGui from
_http://`<IPAddressTarget>`:`<FrontEndPort>`/FrontEnd_
(for example ->  http://10.10.10.10:8888/FrontEnd)

The monitoring system is self-monitored through "monitoring" service.

You can add more services to monitor other remote nodes from the Monitoring WebGUI application or from available REST APIs.


## License

The 5GCity monitoring is published under Apache 2.0 license.
Please see the LICENSE file for more details.


## Feedback-Channel

Please use the GitHub issues for feedback.



