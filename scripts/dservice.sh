#!/bin/sh
# edit by zzh

# ./
APP_BIN=`dirname $0`
APP_HOME=$APP_BIN/..
export APP_HOME
echo $APP_HOME

JARS="${APP_HOME}/conf:${APP_HOME}/devConf:"
LIB=$APP_HOME/libs
if [ -d $LIB ]; then
  for i in $LIB/*.jar; do
    JARS="$JARS":$i
  done
fi
PLUGIN=$APP_HOME/plugins
if [ -d $PLUGIN ]; then
  for i in $PLUGIN/*.jar; do
    JARS="$JARS":$i
  done
fi
#echo $JARS
IP=`hostname -i`
HOST=`hostname`

###COMMON OPTS
JAVA_OPTS="$JAVA_OPTS -Dlog.home=${APP_HOME}/log -Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="$JAVA_OPTS -server -XX:+DisableExplicitGC"
###CMS GC
#JAVA_OPTS="$JAVA_OPTS -XX:NewRatio=4 -XX:SurvivorRatio=2 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:-CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:+CMSClassUnloadingEnabled"
###G1 GC
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC -XX:MaxGCPauseMillis=1000"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails -XX:+PrintGCDateStamps "
###APP
JAVA_OPTS="$JAVA_OPTS -Xms2g -Xmx2g -XX:MetaspaceSize=128M -server -Xnoclassgc -Djava.net.preferIPv4Stack=true"
JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.hostname=${IP} -Dcom.sun.management.jmxremote.port=6395"
JAVA_OPTS="$JAVA_OPTS -Xloggc:"${APP_HOME}"/bin/gc-stat.vgc "
#JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8321"

JAVA_OPTS="-Dprogram.name=deploy.service $JAVA_OPTS"
#echo $JAVA_OPTS

java $JAVA_OPTS -cp $JARS com.boco.deploy.Application $* >> /dev/null &
echo $! >> pidfile
echo "start finished"
