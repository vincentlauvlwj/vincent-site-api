#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2001-2006 The Apache Software Foundation.
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
#
#   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
#   reserved.


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

# Reset the REPO variable. If you need to influence this use the environment setup file.
REPO=


# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
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

if [ -z "$REPO" ]
then
  REPO="$BASEDIR"/repo
fi

CLASSPATH="$BASEDIR"/etc:"$REPO"/org/springframework/boot/spring-boot-starter-web/1.3.8.RELEASE/spring-boot-starter-web-1.3.8.RELEASE.jar:"$REPO"/org/springframework/boot/spring-boot-starter/1.3.8.RELEASE/spring-boot-starter-1.3.8.RELEASE.jar:"$REPO"/org/springframework/boot/spring-boot-starter-logging/1.3.8.RELEASE/spring-boot-starter-logging-1.3.8.RELEASE.jar:"$REPO"/ch/qos/logback/logback-classic/1.1.7/logback-classic-1.1.7.jar:"$REPO"/ch/qos/logback/logback-core/1.1.7/logback-core-1.1.7.jar:"$REPO"/org/slf4j/jcl-over-slf4j/1.7.21/jcl-over-slf4j-1.7.21.jar:"$REPO"/org/slf4j/jul-to-slf4j/1.7.21/jul-to-slf4j-1.7.21.jar:"$REPO"/org/slf4j/log4j-over-slf4j/1.7.21/log4j-over-slf4j-1.7.21.jar:"$REPO"/org/yaml/snakeyaml/1.16/snakeyaml-1.16.jar:"$REPO"/org/springframework/boot/spring-boot-starter-tomcat/1.3.8.RELEASE/spring-boot-starter-tomcat-1.3.8.RELEASE.jar:"$REPO"/org/apache/tomcat/embed/tomcat-embed-core/8.0.37/tomcat-embed-core-8.0.37.jar:"$REPO"/org/apache/tomcat/embed/tomcat-embed-el/8.0.37/tomcat-embed-el-8.0.37.jar:"$REPO"/org/apache/tomcat/embed/tomcat-embed-logging-juli/8.0.37/tomcat-embed-logging-juli-8.0.37.jar:"$REPO"/org/apache/tomcat/embed/tomcat-embed-websocket/8.0.37/tomcat-embed-websocket-8.0.37.jar:"$REPO"/org/springframework/boot/spring-boot-starter-validation/1.3.8.RELEASE/spring-boot-starter-validation-1.3.8.RELEASE.jar:"$REPO"/org/hibernate/hibernate-validator/5.2.4.Final/hibernate-validator-5.2.4.Final.jar:"$REPO"/javax/validation/validation-api/1.1.0.Final/validation-api-1.1.0.Final.jar:"$REPO"/org/jboss/logging/jboss-logging/3.3.0.Final/jboss-logging-3.3.0.Final.jar:"$REPO"/com/fasterxml/classmate/1.1.0/classmate-1.1.0.jar:"$REPO"/com/fasterxml/jackson/core/jackson-databind/2.6.7/jackson-databind-2.6.7.jar:"$REPO"/com/fasterxml/jackson/core/jackson-annotations/2.6.7/jackson-annotations-2.6.7.jar:"$REPO"/com/fasterxml/jackson/core/jackson-core/2.6.7/jackson-core-2.6.7.jar:"$REPO"/org/springframework/spring-web/4.2.8.RELEASE/spring-web-4.2.8.RELEASE.jar:"$REPO"/org/springframework/spring-beans/4.2.8.RELEASE/spring-beans-4.2.8.RELEASE.jar:"$REPO"/org/springframework/spring-webmvc/4.2.8.RELEASE/spring-webmvc-4.2.8.RELEASE.jar:"$REPO"/org/springframework/spring-expression/4.2.8.RELEASE/spring-expression-4.2.8.RELEASE.jar:"$REPO"/org/springframework/boot/spring-boot-autoconfigure/1.3.8.RELEASE/spring-boot-autoconfigure-1.3.8.RELEASE.jar:"$REPO"/org/springframework/boot/spring-boot/1.3.8.RELEASE/spring-boot-1.3.8.RELEASE.jar:"$REPO"/org/springframework/boot/spring-boot-configuration-processor/1.3.8.RELEASE/spring-boot-configuration-processor-1.3.8.RELEASE.jar:"$REPO"/org/json/json/20140107/json-20140107.jar:"$REPO"/org/springframework/spring-core/4.2.8.RELEASE/spring-core-4.2.8.RELEASE.jar:"$REPO"/org/springframework/boot/spring-boot-starter-aop/1.3.8.RELEASE/spring-boot-starter-aop-1.3.8.RELEASE.jar:"$REPO"/org/springframework/spring-aop/4.2.8.RELEASE/spring-aop-4.2.8.RELEASE.jar:"$REPO"/aopalliance/aopalliance/1.0/aopalliance-1.0.jar:"$REPO"/org/aspectj/aspectjweaver/1.8.9/aspectjweaver-1.8.9.jar:"$REPO"/org/springframework/boot/spring-boot-starter-mail/1.3.8.RELEASE/spring-boot-starter-mail-1.3.8.RELEASE.jar:"$REPO"/org/springframework/spring-context/4.2.8.RELEASE/spring-context-4.2.8.RELEASE.jar:"$REPO"/org/springframework/spring-context-support/4.2.8.RELEASE/spring-context-support-4.2.8.RELEASE.jar:"$REPO"/com/sun/mail/javax.mail/1.5.6/javax.mail-1.5.6.jar:"$REPO"/javax/activation/activation/1.1.1/activation-1.1.1.jar:"$REPO"/org/mybatis/spring/boot/mybatis-spring-boot-starter/1.1.1/mybatis-spring-boot-starter-1.1.1.jar:"$REPO"/org/mybatis/spring/boot/mybatis-spring-boot-autoconfigure/1.1.1/mybatis-spring-boot-autoconfigure-1.1.1.jar:"$REPO"/org/mybatis/mybatis/3.4.0/mybatis-3.4.0.jar:"$REPO"/org/mybatis/mybatis-spring/1.3.0/mybatis-spring-1.3.0.jar:"$REPO"/org/springframework/boot/spring-boot-starter-jdbc/1.3.8.RELEASE/spring-boot-starter-jdbc-1.3.8.RELEASE.jar:"$REPO"/org/apache/tomcat/tomcat-jdbc/8.0.37/tomcat-jdbc-8.0.37.jar:"$REPO"/org/apache/tomcat/tomcat-juli/8.0.37/tomcat-juli-8.0.37.jar:"$REPO"/org/springframework/spring-jdbc/4.2.8.RELEASE/spring-jdbc-4.2.8.RELEASE.jar:"$REPO"/org/springframework/spring-tx/4.2.8.RELEASE/spring-tx-4.2.8.RELEASE.jar:"$REPO"/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar:"$REPO"/org/apache/commons/commons-lang3/3.3.2/commons-lang3-3.3.2.jar:"$REPO"/org/slf4j/slf4j-api/1.7.21/slf4j-api-1.7.21.jar:"$REPO"/me/liuwj/site/site-api/1.0-SNAPSHOT/site-api-1.0-SNAPSHOT.jar

ENDORSED_DIR=
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
  [ -n "$HOME" ] && HOME=`cygpath --path --windows "$HOME"`
  [ -n "$BASEDIR" ] && BASEDIR=`cygpath --path --windows "$BASEDIR"`
  [ -n "$REPO" ] && REPO=`cygpath --path --windows "$REPO"`
fi

echo $JAVACMD
echo $JAVA_OPTS
echo $CLASSPATH
echo $REPO
echo $BASEDIR
echo $PRG
echo $PRGDIR

exec "$JAVACMD" $JAVA_OPTS  \
  -classpath "$CLASSPATH" \
  -Dapp.name="app" \
  -Dapp.pid="$$" \
  -Dapp.repo="$REPO" \
  -Dapp.home="$BASEDIR" \
  -Dbasedir="$BASEDIR" \
  me.liuwj.site.Application \
  "$@"
