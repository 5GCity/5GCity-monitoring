#!/bin/bash

LOGFILE=/tmp/logScript.txt

rm -f $LOGFILE

if [ $# -ne 1 ];
then echo "Missing Ip Address"
exit 1
fi

HOST_MON=$1
echo $HOST_MON >> $LOGFILE

sed -i s/__HOST_MON__/$HOST_MON/g prometheus/prometheus.yml.base
sed -i s/__HOST_MON__/$HOST_MON/g grafana/provisioning/datasources/all.yml
sed -i s/__HOST_MON__/$HOST_MON/g docker-compose.yml
			
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
