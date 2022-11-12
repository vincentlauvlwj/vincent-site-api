#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2022 Vincent Lau.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
# ----------------------------------------------------------------------------

# custom settings...
APP_MAINCLASS="me.liuwj.site.Application"
JAVA_OPTS="-XX:+UseSerialGC -XX:-UseGCOverheadLimit -XX:NewRatio=1 -XX:SurvivorRatio=8"

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=$(ls -ld "$PRG")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=$(dirname "$PRG")/"$link"
  fi
done

PRGDIR=$(dirname "$PRG")
BASEDIR=$(cd "$PRGDIR/.." > /dev/null; pwd)
PID_FILE="$BASEDIR/pid"


# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "$(uname)" in
  CYGWIN*)
    cygwin=true
    ;;
  Darwin*)
    darwin=true
    if [ -z "$JAVA_VERSION" ] ; then
     JAVA_VERSION="CurrentJDK"
    else
     echo "Using Java version: $JAVA_VERSION"
    fi
    if [ -z "$JAVA_HOME" ]; then
      if [ -x "/usr/libexec/java_home" ]; then
        JAVA_HOME=$(/usr/libexec/java_home)
      else
        JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
      fi
    fi
    ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=$(java-config --jre-home)
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=$(cygpath --unix "$JAVA_HOME")
  [ -n "$CLASSPATH" ] && CLASSPATH=$(cygpath --path --unix "$CLASSPATH")
fi

# If a specific java binary isn't specified search for the standard 'java' binary
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=$(which java)
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute $JAVACMD" 1>&2
  exit 1
fi

for i in "$BASEDIR"/lib/*.jar; do
  if [ -n "$CLASSPATH" ]; then
    CLASSPATH="$CLASSPATH":"$i"
  else
    CLASSPATH="$i"
  fi
done

if [ -n "$ENDORSED_DIR" ] ; then
  CLASSPATH=$BASEDIR/$ENDORSED_DIR:$CLASSPATH
fi

if [ -n "$CLASSPATH_PREFIX" ] ; then
  CLASSPATH=$CLASSPATH_PREFIX:$CLASSPATH
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$BASEDIR" ] && BASEDIR=$(cygpath --path --windows "$BASEDIR")
  [ -n "$JAVACMD" ] && JAVACMD=$(cygpath --path --windows "$JAVACMD")
  [ -n "$CLASSPATH" ] && CLASSPATH=$(cygpath --path --windows "$CLASSPATH")
  [ -n "$PID_FILE" ] && PID_FILE=$(cygpath --path --windows "$PID_FILE")
fi

run() {
  cd "$BASEDIR"
  exec "$JAVACMD" $JAVA_OPTS -classpath "$CLASSPATH" "$APP_MAINCLASS"
}

do_start() {
  echo "Starting application $APP_MAINCLASS..."
  [ -e "$BASEDIR"/logs ] || mkdir -p "$BASEDIR"/logs
  STDOUT=$BASEDIR/logs/service_stdout.log
  [ -e "$STDOUT" ] && mv "$STDOUT" "$STDOUT.$(date "+%Y%m%d%H%M%S")"

  cd "$BASEDIR"
  nohup "$JAVACMD" $JAVA_OPTS -classpath "$CLASSPATH" "$APP_MAINCLASS" > "$STDOUT" 2>&1 &

  if [ $? -eq 0 ]; then
    echo $! > "$PID_FILE"
    echo "[OK] (pid=$(cat "$PID_FILE"))"
    echo "You can check the application's standard output by:"
    echo "  tail -f $STDOUT"
  else
    echo "[Failed]"
  fi
}

do_stop() {
  echo "Stopping application $APP_MAINCLASS... (pid=$(cat "$PID_FILE"))"
  echo "[kill -15]\c"
  kill -15 "$(cat "$PID_FILE")"

  if wait_for_stop; then
    echo "[OK]"
    rm -f "$PID_FILE"
  else
    echo "[kill -9]\c"
    kill -9 "$(cat "$PID_FILE")"

    if wait_for_stop; then
      echo "[OK]"
      rm -f "$PID_FILE"
    else
      echo "[Failed]"
    fi
  fi
}

wait_for_stop() {
  wait_seconds=10

  while [ $wait_seconds -ge 0 ]; do
    if ! ps -p "$(cat "$PID_FILE")" > /dev/null; then
      return 0
    fi

    if [ $wait_seconds -gt 0 ]; then
      echo ".\c"
      sleep 1
    fi

    wait_seconds=$(expr $wait_seconds - 1)
  done

  # returning non-zero value means the function fails.
  return 1
}

start() {
  if [ -s "$PID_FILE" ] && ps -p "$(cat "$PID_FILE")" > /dev/null; then
    echo "================================"
    echo "Warn: Application $APP_MAINCLASS is already started. (pid=$(cat "$PID_FILE"))"
    echo "================================"
  else
    do_start
  fi
}

restart() {
  if [ -s "$PID_FILE" ] && ps -p "$(cat "$PID_FILE")" > /dev/null; then
    do_stop
  fi

  do_start
}

stop() {
  if [ ! -s "$PID_FILE" ]; then
    echo "================================"
    echo "Warn: Application $APP_MAINCLASS is not running."
    echo "================================"
  elif ! ps -p "$(cat "$PID_FILE")" > /dev/null; then
    echo "================================"
    echo "Warn: Application $APP_MAINCLASS is already DOWN!!! (pid=$(cat "$PID_FILE"))"
    echo "================================"
    rm -f "$PID_FILE"
  else
    do_stop
  fi
}

status() {
  if [ ! -s "$PID_FILE" ]; then
    echo "Status: Application $APP_MAINCLASS is not running."
  elif ! ps -p "$(cat "$PID_FILE")" > /dev/null; then
    echo "Status: Application $APP_MAINCLASS is DOWN!!! (pid=$(cat "$PID_FILE"))"
  else
    echo "Status: Application $APP_MAINCLASS is running. (pid=$(cat "$PID_FILE"))"
  fi
}

info() {
  echo "System Information:"
  echo "****************************"
  echo "$(uname -a)"
  echo
  echo "JAVA_HOME=$JAVA_HOME"
  echo "$("$JAVACMD" -version)"
  echo
  echo "BASEDIR=$BASEDIR"
  echo "JAVA_OPTS=$JAVA_OPTS"
  echo "APP_MAINCLASS=$APP_MAINCLASS"
  echo "CLASSPATH=$CLASSPATH"
  echo "****************************"
}

case "$1" in
  'run')
    run
    ;;
  'start')
    start
    ;;
  'stop')
    stop
    ;;
  'restart')
    restart
    ;;
  'status')
    status
    ;;
  'info')
    info
    ;;
  *)
    echo "Usage: $0 {run|start|stop|restart|status|info}"
    exit 1
esac
