#!/usr/bin/env bash

REMOTE_DEBUG="-agentlib:jdwp=transport=dt_socket,address=14025,server=y,suspend=n"
MEMORY="-Xms512m -Xmx512m -XX:MaxMetaspaceSize=256m"
GC_LOG=" -verbose:gc -Xloggc:./logs/gc_pid_%p.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
JMX="-Dcom.sun.management.jmxremote.port=15025 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"

case $1 in
    GC_ParNew_CMS)
        GC="-XX:+UseParNewGC -XX:+UseConcMarkSweepGC"
        CMS_OPTS="-XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark"
        GC="$GC $CMS_OPTS"
        ;;
    GC_Serial)
        GC="-XX:+UseSerialGC"
        ;;
    GC_ScavengeAdapt_PSMS)
        GC="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+UseAdaptiveSizePolicy"
        ;;
    GC_Scavenge_PSMS)
        GC="-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:-UseAdaptiveSizePolicy"
        ;;
    GC_G1)
        GC="-XX:+UseG1GC"
        ;;
    *)
        echo "Usage: $0 GC_ParNew_CMS|GC_Serial|GC_ScavengeAdapt_PSMS|GC_Scavenge_PSMS|GC_G1"
        exit
        ;;
esac

if [ ! -d "./logs" ]; then
    mkdir "./logs"
fi

if [ ! -d "./dumps" ]; then
    mkdir "./dumps"
fi

java $REMOTE_DEBUG $MEMORY $GC $GC_LOG $JMX $DUMP -XX:OnOutOfMemoryError="kill -3 %p" -jar target/lesson4.jar
