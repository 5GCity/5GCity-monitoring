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
- JOSS_HOME=<(where you have installed wildfly-14.0.1.Final)>
- JAVA_HOME=<(where there is jdk or jre 1.8)>
	
c. Run the build with Ant to produce the file _`<pathBuild>`/5GCity-monitoring/target/tar/MONITORING-<DATE>.tar_ 
    	
- _cd `<pathBuild>`/5GCity-monitoring_
- _ant -buildfile build.xml targetMon_



## Deployment

##### Prerequisites

- docker 1.13.0
- docker-compose 1.22.0

##### Dependencies

Linux node exporter installed and run on target node 

- _cd `<pathRun>`_
- _cp `<pathBuild>`/5GCity-monitoring/backend/prometheus/exporters/node_exporter-0\.16\.0\.linux-amd64\.tar\.gz \._
- _tar xvfz node_exporter-\*\.\*-amd64\.tar\.gz_
- _cd node_exporter-\*\.\*-amd64_
- _./node_exporter &_

NOTE:  Linux node exporter must be installed and running also on each node that must be monitored (you can also get it from _https://github.com/prometheus/node_exporter/releases/download/v0.16.0/node_exporter-0.16.0.linux-amd64.tar.gz_)

##### Deployment Setup

a. Untar file _MONITORING-`<DATE>`.tar_ generated in Build phase in a new directory  (i.e. _`<pathRun>`/mon_)
    
- _cd `<pathRun>`_
- _mkdir mon_
- _chmod 777 mon_
- _cd mon_
- _tar xvfz `<pathBuild>`/5GCity-monitoring/target/tar/MONITORING-`<DATE>`.tar_
	
b. (Optional) The file _`<pathRun>`/config\.properties_ contains the default values for the ports used to run grafana(33000), prometheus(39090) and frontend application(38080)

If you want to use ports'values different from default, please edit this file before to proceed with step c.

c. Run the install.sh script with parameter _`<IPAddressTarget>`_ = Management IP address of the your's test-bed target
	
- _./install.sh `<IPAddressTarget>`_
	
d. Run command  docker-compose up  in background

- _docker-compose up -d_

		
## Usage

Once the Monitoring manager is running, please open in your browser the Monitoring WebGui from
_http://`<IPAddressTarget>`:`<FrontEndPort>`/FrontEnd_
(for example ->  http://138.132.116.146:38080/FrontEnd)

From Dashboard you will connect to Grafana Tool by user admin/monitoring, using `<GrafanaPort>`:  you can see SummaryNODE dashboard for the node 
_`<IPAddressTarget>`_

You can add more inventory services and their relative inventory nodes / inventory metrics from the Monitoring WebGUI application.


## License

The 5GCity monitoing is published under Apache 2.0 license.
Please see the LICENSE file for more details.


## Feedback-Channel

Please use the GitHub issues for feedback.



