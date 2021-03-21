#!/bin/sh
JAVA_HOME="/usr/java/jdk"
JAVA_OPTS="-XX:-UseGCOverheadLimit -XX:NewRatio=1 -XX:SurvivorRatio=8 -XX:+UseSerialGC"
CONSOLE_LOG="/dev/null"
RUNNING_USER=root
APP_HOME=/opt/tudaxia/test/WEB-INF
APP_MAINCLASS=com.tudaxia.test.TestMain

CLASSPATH=$APP_HOME/classes
for i in "$APP_HOME"/lib/*.jar; do
    CLASSPATH="$CLASSPATH":"$i"
done

psid=0

checkpid() {
    javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAINCLASS`

    if [ -n "$javaps" ]; then
        psid=`echo $javaps | awk '{print $1}'`
    else
        psid=0
    fi
}

start() {
    checkpid

    if [ $psid -ne 0 ]; then
        echo "================================"
        echo "warn: $APP_MAINCLASS already started! (pid=$psid)"
        echo "================================"
    else
        echo -n "Starting $APP_MAINCLASS ..."
        JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH $APP_MAINCLASS > $CONSOLE_LOG 2>&1 &"
        su - $RUNNING_USER -c "$JAVA_CMD"
        checkpid
        if [ $psid -ne 0 ]; then
            echo "(pid=$psid) [OK]"
        else
            echo "[Failed]"
        fi
    fi
}

stop() {
    checkpid

    if [ $psid -ne 0 ]; then
        echo -n "Stopping $APP_MAINCLASS ...(pid=$psid) "
        su - $RUNNING_USER -c "kill $psid"
        if [ $? -eq 0 ]; then
            echo "[OK]"
        else
            echo "[Failed]"
        fi

        checkpid
        if [ $psid -ne 0 ]; then
            stop
        fi
    else
        echo "================================"
        echo "warn: $APP_MAINCLASS is not running"
        echo "================================"
    fi
}

status() {
    checkpid

    if [ $psid -ne 0 ];  then
        echo "$APP_MAINCLASS is running! (pid=$psid)"
    else
        echo "$APP_MAINCLASS is not running"
    fi
}

info() {
    echo "System Information:"
    echo "****************************"
    echo `head -n 1 /etc/issue`
    echo `uname -a`
    echo
    echo "JAVA_HOME=$JAVA_HOME"
    echo `$JAVA_HOME/bin/java -version`
    echo
    echo "APP_HOME=$APP_HOME"
    echo "APP_MAINCLASS=$APP_MAINCLASS"
    echo "****************************"
}

case "$1" in
    'start')
        start
        ;;
    'stop')
       stop
       ;;
    'restart')
       stop
       start
       ;;
    'status')
       status
       ;;
    'info')
       info
       ;;
    *)
       echo "Usage: $0 {start|stop|restart|status|info}"
       exit 1
 