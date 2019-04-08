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
        job=$(echo ${!i} | awk -F, '{print $1}')
        interval=$(echo ${!i} | awk -F, '{print $2}')
        path=$(echo ${!i} | awk -F, '{print $3}')
        echo "JOB $job" >> $LOGFILE
        echo "INTERVAL $interval" >> $LOGFILE
        echo "PATH $path" >> $LOGFILE
        fileTemp=$PATH_TEMP_JOB/$job.json
	cp $FILE_TEMPLATE $fileTemp
        sed -i s#__JOB__#$job#g $fileTemp
        sed -i s#__INTERVAL__#$interval#g $fileTemp
        sed -i s#__PATH__#$path#g $fileTemp
        cat $fileTemp >> $FILE_PROMETHEUS

        i=$(($i+1))
done

cp $FILE_PROMETHEUS $DIR_PROMETHEUS/prometheus.yml
if [ "$doRestart" == "restart" ]; then
curl -k -X POST -H "Content-Type: application/json" -d "" http://$HOST_MON:$PM_PORT_MON/-/reload
fi
exit 0

