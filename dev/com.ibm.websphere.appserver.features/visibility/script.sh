#!/bin/bash

features="com.ibm.websphere.appserver.appLifecycle-1.0, com.ibm.websphere.appserver.cdi1.2-servlet3.1, com.ibm.websphere.appserver.javax.jsp-2.3, com.ibm.websphere.appserver.javaeeddSchema-1.0, jndi-1.0, com.ibm.websphere.appserver.javax.ejb-3.2, com.ibm.websphere.appserver.transaction-1.2, com.ibm.websphere.appserver.requestProbes-1.0, com.ibm.websphere.appserver.dynamicBundle-1.0, com.ibm.websphere.appserver.builtinAuthorization-1.0, com.ibm.websphere.appserver.injection-1.0, com.ibm.websphere.appserver.javaeePlatform-7.0, cdi-1.2, com.ibm.websphere.appserver.javax.el-3.0, com.ibm.websphere.appserver.javax.jsf-2.2, jsp-2.3, com.ibm.wsspi.appserver.webBundle-1.0, com.ibm.websphere.appserver.cdi1.2-transaction1.2, com.ibm.websphere.appserver.securityInfrastructure-1.0, com.ibm.websphere.appserver.restHandler-1.0, com.ibm.websphere.appserver.javaeePlatform7.0-jndi1.0, com.ibm.websphere.appserver.javaeedd-1.0, com.ibm.websphere.appserver.org.eclipse.microprofile.metrics-1.0, com.ibm.websphere.appserver.mpMetrics1.0-cdi1.2, com.ibm.websphere.appserver.javax.annotation-1.2, com.ibm.websphere.appserver.internal.slf4j-1.7.7, com.ibm.websphere.appserver.artifact-1.0, com.ibm.websphere.appserver.javax.validation-1.1, servlet-3.1, com.ibm.websphere.appserver.javax.servlet-3.1, ssl-1.0, com.ibm.websphere.appserver.containerServices-1.0, com.ibm.websphere.appserver.classloading-1.0, com.ibm.websphere.appserver.cdi1.2-jndi1.0, com.ibm.websphere.appserver.javaeeCompatible-7.0, com.ibm.websphere.appserver.servlet-servletSpi1.0, com.ibm.websphere.appserver.javax.connector.internal-1.7, mpMetrics-1.0, com.ibm.websphere.appserver.javaeePlatform-6.0, com.ibm.websphere.appserver.anno-1.0, com.ibm.websphere.appserver.ltpa-1.0, com.ibm.websphere.appserver.basicRegistry-1.0, com.ibm.websphere.appserver.jta-1.2, com.ibm.wsspi.appserver.webBundleSecurity-1.0, com.ibm.websphere.appserver.javax.cdi-1.2, com.ibm.websphere.appserver.adminSecurity-1.0, com.ibm.websphere.appserver.security-1.0, com.ibm.websphere.appserver.javax.interceptor-1.2, com.ibm.websphere.appserver.httptransport-1.0, el-3.0, com.ibm.websphere.appserver.builtinAuthentication-1.0, com.ibm.websphere.appserver.channelfw-1.0, com.ibm.websphere.appserver.appmanager-1.0, com.ibm.websphere.appserver.javax.persistence.base-2.1, json-1.0, distributedMap-1.0, com.ibm.websphere.appserver.javax.persistence-2.1"

features="${features//,}"

addActivationType() {
  if grep -q WLP-Activation-Type "$1"; then
    echo "  PRESENT  WLP-Activation-Type already present in $1"
  else
    echo "  ADDED    WLP-Activation-Type added to           $1"
    echo "WLP-Activation-Type: parallel" >> $1
  fi
}

# Iterate the string variable using for loop
for val in $features; do
  if [ -f auto/$val.feature ]; then
    echo "AUTO $val"
    addActivationType auto/$val.feature
  else
    if [ -f private/$val.feature ]; then
      echo "PRIVATE $val"
      addActivationType private/$val.feature
    else
      if [ -f protected/$val.feature ]; then
        echo "PROTECTED $val"
        addActivationType protected/$val.feature
      else
        if [ -d public/$val ]; then
          echo "PUBLIC $val"
          for file in public/$val/*.feature
          do
            if [[ -f $file ]]; then
              addActivationType $file
            fi
          done
        else
          echo "UNKNOWN $val"
        fi
      fi
    fi
  fi
done
