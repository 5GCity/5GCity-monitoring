README.md

# 5GCity-monitoring

## Scope

Monitoring manager module for
5GCity distributed cloud and radio platform.


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

- _cd <pathBuild>_
- _wget http://download.jboss.org/wildfly/14.0.1.Final/wildfly-14.0.1.Final.tar.gz_
- _tar xvfz wildfly-14.0.1.Final.tar.gz_
- _chmod -R 777 wildfly-14.0.1.Final_
- _cd wildfly-14.0.1.Final_
- _cp -R modules modules.orig_
- _cp -R standalone standalone.orig_
- _cp -R domain domain.orig_

##### Build Setup

a. Get from github all 5GCity-monitoring projects directories into _<pathBuild>/MONITORING_

b. Edit the file _<pathBuild>/MONITORING/config/build/env.build.properties_ to adapt it to your test-bed setup:
- JOSS_HOME=<(where you have installed wildfly-14.0.1.Final)>
- JAVA_HOME=<(where there is jdk or jre 1.8)>
	
c. Run the build with Ant to produce the file _<pathBuild>/MONITORING/target/tar/MONITORING-<DATE>.tar_ 
    	
- _cd <pathBuild>/MONITORING_
- _ant -buildfile build.xml targetMon_



## Deployment

##### Prerequisites

- docker 1.13.0
- docker-compose 1.22.0

##### Dependencies

Linux node exporter  installed and run on target node and on each node that must be monitored

- _cd <pathRun>_
- _wget https://github.com/prometheus/node_exporter/releases/download/v*/node_exporter-*.*-amd64.tar.gz_
- _tar xvfz node_exporter-*.*-amd64.tar.gz_
- _cd <pathRun>/node_exporter-*.*-amd64_
- _./node_exporter &_

##### Deployment Setup

a. Untar file _MONITORING-<DATE>.tar_ generated in Build phase in a new directory  (i.e. _<pathRun>/monitoring_) 
    
- _cd <pathRun>_
- _mkdir monitoring_
- _chmod 777 monitoring_
- _cd monitoring_
- _tar xvfz <pathBuild>/MONITORING/target/tar/MONITORING-<DATE>.tar_
	
b. Run the install.sh script with parameter _<IPAddressTarget>_ = Management IP address of the your's test-bed target
	
- _./install.sh <IPAddressTarget>_
	
c. Run command  docker-compose up  in background

- _docker-compose up -d_

		
## Usage

Once the Monitoring manager is running, please open in your browser the Monitoring WebGui from
_http://<IPAddressTarget>:38080/FrontEnd_

From Dashboard you will connect to Grafana Tool by user admin/monitoring:  you can see SummaryNODE dashboard for the node _<IPAddressTarget>_
You can add more inventory services and their relative inventory nodes / inventory metrics from the Monitoring WebGUI application.


## License

The 5GCity monitoing is published under Apache 2.0 license.
Please see the LICENSE file for more details.


## Feedback-Channel

Please use the GitHub issues for feedback.



