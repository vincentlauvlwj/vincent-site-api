#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2021 Vincent Lau.
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
JAVA_OPTS="-XX:-UseGCOverheadLimit -XX:NewRatio=1 -XX:SurvivorRatio=8 -XX:+UseSerialGC"
APP_MAINCLASS="com.tudaxia.test.TestMain"

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
BASEDIR=`cd "$PRGDIR/.." >/dev/null; pwd`


# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
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
        JAVA_HOME=`/usr/libexec/java_home`
      else
        JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
      fi
    fi
    ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
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
    JAVACMD=`which java`
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." 1>&2
  echo "  We cannot execute $JAVACMD" 1>&2
  exit 1
fi

for i in "$BASEDIR"/lib/*.jar; do
    CLASSPATH="$CLASSPATH":"$i"
done

if [ -n "$ENDORSED_DIR" ] ; then
  CLASSPATH=$BASEDIR/$ENDORSED_DIR/*:$CLASSPATH
fi

if [ -n "$CLASSPATH_PREFIX" ] ; then
  CLASSPATH=$CLASSPATH_PREFIX:$CLASSPATH
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$BASEDIR" ] && BASEDIR=`cygpath --path --windows "$BASEDIR"`
fi

run() {
  cd $BASEDIR
  exec $JAVACMD $JAVA_OPTS -classpath $CLASSPATH $APP_MAINCLASS
}

start() {
  pid=`cat $PID_FILE`
  if [ pid -gt 0 ]; then
    echo "================================"
    echo "Warn: $APP_MAINCLASS is already started! (pid=$pid)"
    echo "================================"
  else
    echo -n "Starting $APP_MAINCLASS ..."
    STDOUT=$BASEDIR/logs/service_stdout.log
    [ -e $BASEDIR/logs ] || mkdir $BASEDIR/logs -p

    cd $BASEDIR
    nohup $JAVACMD $JAVA_OPTS -classpath $CLASSPATH $APP_MAINCLASS > $STDOUT 2>&1 &
    echo $! > $PID_FILE

    pid=`cat $PID_FILE`
    if [ pid -gt 0 ]; then
      echo "(pid=$pid) [OK]"
    else
      echo "[Failed]"
    fi
  fi
}

stop() {
  pid=`cat $PID_FILE`
  if [ pid -gt 0 ]; then
    echo -n "Stopping $APP_MAINCLASS ...(pid=$pid) "
    kill $pid

    if [ $? -eq 0 ]; then
      echo "[OK]"
      rm -f $PID_FILE
    else
      echo "[Failed]"
    fi
  else
    echo "================================"
    echo "Warn: $APP_MAINCLASS is not running"
    echo "================================"
  fi
}

status() {
  pid=`cat $PID_FILE`
  if [ pid -gt 0 ]; then
    echo "$APP_MAINCLASS is running! (pid=$pid)"
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
 