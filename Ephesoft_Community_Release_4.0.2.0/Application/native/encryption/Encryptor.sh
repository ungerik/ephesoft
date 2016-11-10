export JAVA_HOME=@@INSTALL_DIR@@/Dependencies/jdk1.7.0_71
export EPHESOFT_LIBRARY_PATH=@@INSTALL_DIR@@/Application/WEB-INF/lib
export JAR_NAME=ephesoft.jar
export CLASS_NAME=com.ephesoft.dcma.encryption.core.PasswordEncryptor

java -cp %EPHESOFT_LIBRARY_PATH%/%JAR_NAME% %CLASS_NAME% %EPHESOFT_LIBRARY_PATH%