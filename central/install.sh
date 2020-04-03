#!/bin/bash

LOGFILE=/tmp/logScript.txt

rm -f $LOGFILE

if [ $# -lt 1 ];
then echo "Missing Ip Address"
exit 1
fi

if [ ! -e ./config.properties ]; then
echo "Missing file config.properties"
exit 1
fi

source config.properties

prot="false";
HOST_MON=$1
HOST_MON=${HOST_MON,,}
if [ "x$HOST_MON" == "xhttp" ] || [ "x$HOST_MON" == "xhttps" ]; then
 echo "First parameter is wrong:  it must be an Ip Address"
 exit 1
fi

EXT_HOST_MON=$2
if [ "x$EXT_HOST_MON" == "x" ]; then
 EXT_HOST_MON=$HOST_MON
 PROTOCOL="http"
else 
	EXT_HOST_MON=${EXT_HOST_MON,,}
fi

if [ "x$EXT_HOST_MON" == "xhttp" ] || [ "x$EXT_HOST_MON" == "xhttps" ]; then
 PROTOCOL=$EXT_HOST_MON
 EXT_HOST_MON=$HOST_MON
 prot="true";
fi
echo $HOST_MON >> $LOGFILE
echo $PROTOCOL >> $LOGFILE

if [ "$prot" == "false" ]; then
	PROTOCOL=$3
	[[ "z$PROTOCOL" == "z" ]] && PROTOCOL="http"
	#echo $PROTOCOL
	PROTOCOL=${PROTOCOL,,}
	echo $PROTOCOL >> $LOGFILE
	if [ ! "$PROTOCOL" == "http" ] && [ ! "$PROTOCOL" == "https" ]; then
		echo "Third parameter is wrong:  it must be https or http"
		exit 1
	fi
fi


cp ./prometheus/prometheus.yml.base.orig ./prometheus/prometheus.yml.base
if [ $PROTOCOL == "https" ]; then
 output=$(grep __DOMAIN_MON__  config.properties)
 echo $output
 if [ ! "x$output" == "x" ]; then
 echo "you must set DOMAIN_MON in config.properties"
 exit 1
 fi
 output=$(grep __FILE_KEY__  config.properties)
 echo $output
 if [ ! "x$output" == "x" ]; then
 echo "you must set FILE_KEY in config.properties"
 exit 1
 fi
 output=$(grep __FILE_CRT__  config.properties)
 echo $output
 if [ ! "x$output" == "x" ]; then
 echo "you must set FILE_CRT  in config.properties"
 exit 1
 fi
 cp ./grafana/provisioning/datasources/all.yml.https.orig ./grafana/provisioning/datasources/all.yml
 cp ./grafana/config.https.ini ./grafana/config.ini
 cp ./docker-compose.yml.https.orig ./docker-compose.yml
 sed -i s/__DOMAIN_MON__/$DOMAIN_MON/g grafana/config.ini
 sed -i s/__DOMAIN_MON__/$DOMAIN_MON/g docker-compose.yml
 sed -i s/__FILE_KEY__/$FILE_KEY/g grafana/config.ini
 sed -i s/__FILE_CRT__/$FILE_CRT/g grafana/config.ini
else 
 cp ./grafana/provisioning/datasources/all.yml.orig ./grafana/provisioning/datasources/all.yml
 cp ./grafana/config.http.ini ./grafana/config.ini
 cp ./docker-compose.yml.orig ./docker-compose.yml
fi

sed -i s/__HOST_MON__/$HOST_MON/g prometheus/prometheus.yml.base
sed -i s/__AM_PORT_MON__/$AM_PORT_MON/g prometheus/prometheus.yml.base
sed -i s/__HOST_MON__/$EXT_HOST_MON/g grafana/provisioning/datasources/all.yml
sed -i s/__PM_PORT_MON__/$PM_PORT_MON/g grafana/provisioning/datasources/all.yml
sed -i s/__HOST_MON__/$HOST_MON/g docker-compose.yml
sed -i s/__PM_PORT_MON__/$PM_PORT_MON/g docker-compose.yml
sed -i s/__FE_PORT_MON__/$FE_PORT_MON/g docker-compose.yml
sed -i s/__GF_PORT_MON__/$GF_PORT_MON/g docker-compose.yml
sed -i s/__AM_PORT_MON__/$AM_PORT_MON/g docker-compose.yml
sed -i s#__DASHBOARD_URL__#$DASHBOARD_URL#g alertmanager/config/alertmanager.yml.base
			
if [ ! -d ./frontend/database ]; then
  echo "not exist database" >> $LOGFILE
  mkdir ./frontend/database
  chmod 777 ./frontend/database
fi

if [ ! -e ./prometheus/prometheus.yml ]; then
  echo "not exist prometheus.yml" >> $LOGFILE
  cp ./prometheus/prometheus.yml.base ./prometheus/prometheus.yml
  chmod 777 ./prometheus/prometheus.yml
fi

if [ ! -e ./frontend/deployments/FRONTEND.ear.dodeploy ]; then
  echo "not exist FRONTEND.ear.dodeploy" >> $LOGFILE
  touch ./frontend/deployments/FRONTEND.ear.dodeploy
  chmod 777 ./frontend/deployments/FRONTEND.ear.dodeploy
fi

if [ ! -e ./alertmanager/config/alertmanager.yml ]; then
  echo "not exist prometheus.yml" >> $LOGFILE
  cp ./alertmanager/config/alertmanager.yml.base ./alertmanager/config/alertmanager.yml
  chmod 777 ./alertmanager/config/alertmanager.yml
fi 
