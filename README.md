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
(i.e. MONITORING-03-04-2020.tgz)


##### Deployment Setup

a. Untar file _MONITORING-`<DATE>`.tgz_  in a directory  (i.e. _/opt/monitoring_)
    
- _cd /opt/monitoring_
- put in this directory the last deliverable file _MONITORING-`<DATE>`.tgz_
- _tar xvfz_  _MONITORING-`<DATE>`.tgz_

 b. Install Linux node exporter (and run it as service,  with COLLECTOR Facility)  on test-bed target node

- _mkdir /opt/exporters_
- _cp /opt/monitoring/exporters/* /opt/exporters_
- _cd /opt/exporters_
- _tar xvfz NODE_EXPORTER_FULL.tgz_
- _./install.sh_

Note:  in case of problem of install node_exporter as a service, you can simply start node_exporter with collector facility as follows (run this command after all comands above):
- _/opt/exporters/node/node_exporter --collector.textfile.directory /opt/exporters/collector &_

c. (Mandatory only on first deployment) The file _/opt/monitoring/config\.properties_ must contain the values for the ports used to run grafana(default=3000), prometheus(default=9090) , alert manager (default=9093) and frontend application(default=8888)

- _cp /opt/monitoring/config\.properties\.sample /opt/monitoring/config\.properties_

d1. (Optional for protocol=http) If you want to use ports'values different from default, please edit this file (_/opt/monitoring/config\.properties_) before to proceed with step e.

d2. (Mandatory for protocol=https when use Prometheus and Grafana behind an nginx reverse proxy https) You must change the value of \__DOMAIN_MON\__, \__FILE_KEY\__ and \__FILE_CRT\__  variable with the value of serverName (domain_name), the fileName.key and the fileName.crt used for ssl authorization https.

e. Run the install.sh script : 
	
- _./install.sh_  _`<ManagementIPAddress>` [`<NATIPAddress>`] [`<protocol>`]_

where

_`<ManagementIPAddress>`_ = Management IP address of the your's test-bed target

and _`<NATIPAddress>`_ = NAT Management IP address of the your's test-bed target, if any

and _`<protocol>`_ = _https_ (when use Prometheus and Grafana behind a nginx reverse proxy https,  mandatory in this case) or _http_ (it can be omitted in this case).


f. Run command  docker-compose up  in background to startup 5G monitoring application

- _docker-compose up -d_

NOTE: whenever you have an error in startup 5G monitoring application (i.e port already in use), first shutdown the application and then execute steps d1.(d2), e. and f. again.  
 To shutdown the 5G monitoring application run the command: *docker-compose down*
 
 
 ##### Use NGINX as proxy server (https) for prometheus and grafana in monitoring server
 Prerequisites: 
   
 - nginx version 1.16.1 :   already installed on monitoring server

 - ssl crt and key files generated for monitoring server  (i.e.  <monitoring_domain.crt>  and< monitoring_domain.key>)

-  the value of the monitoring server domain must be known :  <monitoring_domain>

As root (sudo -i) on monitoring system
create the directory  /root/certs/monitoring where you must put the ssl files former mentioned
- _mkdir -p /root/certs /root/certs/monitoring_

copy(sftp) the ssl files above mentioned in /root/certs/monitoring
- _cp <monitoring_domain.crt> /root/certs/monitoring/<monitoring_domain>.pem_
- _cp <monitoring_domain.key> /root/certs/monitoring/<monitoring_domain>.key_

 create a file nginx.conf  in /etc/nginx (see an example below *)

 restart nginx  
-  _systemctl restart nginx_


(*)  Here is the /etc/nginx/nginx.conf example  where <monitoring_domain>=5gcity-monitoring.i2cat.net

http {

server {
listen 443 ssl;
ssl on;
ssl_certificate /root/certs/monitoring/5gcity-monitoring.i2cat.net.pem;
ssl_certificate_key /root/certs/monitoring/5gcity-monitoring.i2cat.net.key;
server_name 5gcity-monitoring.i2cat.net;
location /prometheus/ {
proxy_pass http://localhost:9090/;
}
location / {
root /var/www;
index index.html index.htm;

proxy_pass http://localhost:3000/;
proxy_set_header Host $host;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header X-Forwarded-Server $host;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
proxy_set_header X-Forwarded-Proto $scheme;
}
}
}


##### Deploy exporters on remote nodes

Exporters must be installed and running on each remote node

To deploy Linux node exporters and get system metrics:

. You can get it from _https://github.com/prometheus/node_exporter/releases/download/NODE_EXPORTER_FULL.tgz_) or you can find it on _/opt/monitoring/exporters/NODE_EXPORTER_FULL.tgz_ so you can put this file on remote node on any directory

b. Install on each remote node the Linux node exporter and run it as a service with facility collector 

- Access to remote node and put the file NODE_EXPORTER_FULL.tgz_ on any directory (e.g. `<anydir>`)
- _cd `<anydir>`_
- _tar xvfz NODE_EXPORTER_FULL.tgz_
- _./install.sh_

Note:  in case of problem of install node_exporter as a service, you can simply start node_exporter with collector facility as follows (run this command after all comands above):
- _/opt/exporters/node/node_exporter --collector.textfile.directory /opt/exporters/collector &_


To deploy apache exporters and get apache metrics:

a. You can find it on _/opt/monitoring/exporters/apache_exporter.tar.gz_ so you can put this file on remote node on any directory

b. Install on each remote node the apache exporter and run it 

- Access to remote node and put the file _apache_exporter.tar.gz_ on any directory (e.g. `<anydir>`)
- _cd `<anydir>`_
- _tar xvfz apache_exporter.tar.gz_
- _cd apache_exporter_
- _./apache_exporter &_

Otherwise: valid ONLY for monitoring only one remote node from another "node"
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


About Alerting (Alert Rules and Alerts) please see "Monitoring System's Description (Overview)" document.


About Collector Faciltity of Linux node exporter,  to create a "custom" metric use the collect.sh script in /opt/exporters
- _collect.sh `<customMetricName>` `<value>`_     
to create a customMetric with effective name collector_`<customMetricName>`, its value and a standard help
or 
-  _collect.sh `<customMetricName>` `<value>` "`<customizedHelp>`"_     
to create a customMetric with effective name collector_`<customMetricName>`, its value  and a customezed help



## License

The 5GCity monitoring is published under Apache 2.0 license.
Please see the LICENSE file for more details.


## Feedback-Channel

Please use the GitHub issues for feedback.



