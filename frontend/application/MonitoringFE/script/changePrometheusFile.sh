#!/bin/bash

LOGFILE=/etc/prometheus/logScript.txt

DIR_PROMETHEUS=/etc/prometheus
PATH_TEMP_JOB=$DIR_PROMETHEUS/gestfile
FILE_PROMETHEUS=$PATH_TEMP_JOB/prometheus.yml
FILE_TEMPLATE=$PATH_TEMP_JOB/template.yml

if [ ! -d $PATH_TEMP_JOB ]; then 
      mkdir $PATH_TEMP_JOB
      chmod 755 $PATH_TEMP_JOB
else
      echo $PATH_TEMP_JOB
      rm -f $PATH_TEMP_JOB/*
fi

rm -f $LOGFILE
cp $DIR_PROMETHEUS/prometheus.yml.base $FILE_PROMETHEUS
cp $DIR_PROMETHEUS/template.yml $FILE_TEMPLATE
chmod 777 $DIR_PROMETHEUS/*.json 

doRestart=$1
echo $doRestart >> $LOGFILE

i=2

echo $# >> $LOGFILE

while [ $i -le $# ]
do
        service=$(echo ${!i} | awk -F, '{print $1}')
        interval=$(echo ${!i} | awk -F, '{print $2}')
        echo "SERVIZIO $service" >> $LOGFILE
        echo "INTERVALLO $interval" >> $LOGFILE
        fileTemp=$PATH_TEMP_JOB/$service.json
	cp $FILE_TEMPLATE $fileTemp
        sed -i s/__SERVICE__/$service/g $fileTemp
        sed -i s/__INTERVAL__/$interval/g $fileTemp
        cat $fileTemp >> $FILE_PROMETHEUS

        i=$(($i+1))
done

cp $FILE_PROMETHEUS $DIR_PROMETHEUS/prometheus.yml
if [ "$doRestart" == "restart" ]; then
curl -k -X POST -H "Content-Type: application/json" -d "" http://$HOST_MON:39090/-/reload
fi
exit 0

