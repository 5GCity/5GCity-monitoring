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

HOST_MON=$1
EXT_HOST_MON=$2
[[ "x$EXT_HOST_MON" == "x" ]] && EXT_HOST_MON=$HOST_MON
echo $HOST_MON >> $LOGFILE

cp ./prometheus/prometheus.yml.base.orig ./prometheus/prometheus.yml.base
cp ./grafana/provisioning/datasources/all.yml.orig ./grafana/provisioning/datasources/all.yml
cp ./docker-compose.yml.orig ./docker-compose.yml

sed -i s/__HOST_MON__/$HOST_MON/g prometheus/prometheus.yml.base
sed -i s/__HOST_MON__/$EXT_HOST_MON/g grafana/provisioning/datasources/all.yml
sed -i s/__PM_PORT_MON__/$PM_PORT_MON/g grafana/provisioning/datasources/all.yml
sed -i s/__HOST_MON__/$HOST_MON/g docker-compose.yml
sed -i s/__PM_PORT_MON__/$PM_PORT_MON/g docker-compose.yml
sed -i s/__FE_PORT_MON__/$FE_PORT_MON/g docker-compose.yml
sed -i s/__GF_PORT_MON__/$GF_PORT_MON/g docker-compose.yml
			
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
