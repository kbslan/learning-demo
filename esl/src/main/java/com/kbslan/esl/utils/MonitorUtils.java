package com.kbslan.esl.utils;

import com.sun.jmx.mbeanserver.JmxMBeanServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * nothing to say
 * </p>
 *
 * @author chao.lan
 * @version 1.0.0
 * @since 2023/9/1 16:28
 */
@SuppressWarnings("restriction")
public final class MonitorUtils {
    private final static Logger logger = LoggerFactory.getLogger(MonitorUtils.class);

    /**
     * 返回本机IP
     */
    private static final String HOST_NAME = NetUtils.getInet4Address();
    /**
     * 返回本机实例编号
     */
    private static String INSTANCE_CODE = null;
    /**
     * 时间模板
     */
    private static final String dateTemple = "yyyyMMddHHmmss";

    /**
     * 时间模板
     */
    private static final String dateTimeTemple = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取本机IP地址
     *
     * @return
     */
    public static String getLocalIP() {
        return HOST_NAME;
    }



    /**
     * 格式化指定时间
     *
     * @param date
     * @return
     */
    public static String formatDate(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateTemple);
        return formatter.format(new Date(date));
    }

    /**
     * 返回格式化后的当前时间
     *
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(dateTemple);
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 返回格式化后的当前时间
     *
     * @return
     */
    public static String getNowDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeTemple);
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    public static String getDateTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeTemple);
        return formatter.format(new Date(time));
    }

    /**
     * 获取当前进程的PID
     */
    public static int getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        try {
            return Integer.parseInt(name.substring(0, name.indexOf('@')));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 输出异常信息
     *
     * @param t
     * @return
     */
    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    /**
     * 从MBean获取tomcat运行端口
     *
     * @return
     */
    public static Integer getTomcatPortByMBean() {
        // final String PROTOCOL = "HTTP/1.1";

        MBeanServer mBeanServer = null;
        try {
            ArrayList<MBeanServer> mBeanServers = MBeanServerFactory.findMBeanServer(null);
            if (mBeanServers.size() > 0) {
                for (MBeanServer _mBeanServer : mBeanServers) {
                    if (_mBeanServer instanceof JmxMBeanServer) {
                        mBeanServer = _mBeanServer;
                        break;
                    }
                }
            }
            if (mBeanServer == null) {
                return null;
            }
            //标准容器采用这种
            Integer port = getTomcatPort("Catalina:type=Connector,*",mBeanServer);
            if (port != null){
                return port;
            }else{
                //spring boot采用这种
                return  getTomcatPort("Tomcat:type=Connector,*",mBeanServer);
            }

        } catch (Throwable e) {
            logger.warn("dmc get app web port fail:{}",e.getMessage());
        }
        return null;
    }

    private static Integer getTomcatPort(String name,MBeanServer mBeanServer)throws Exception{
        final String schema = "http";
        Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName(name), null);
        if (objectNames != null && objectNames.size() > 0) {
            for (ObjectName objectName : objectNames) {
                String  protocol = (String) mBeanServer.getAttribute(objectName, "protocol");
                if (protocol != null){
                    protocol = protocol.toLowerCase();
                    if (protocol.startsWith(schema)) {
                        return (Integer) mBeanServer.getAttribute(objectName, "port");
                    }
                }

            }
        }
        return null;
    }
    /**
     * 返回JVM实例编号
     *
     * @return
     */
    public static String getJvmInstanceCode() {
        if (INSTANCE_CODE == null) {
            INSTANCE_CODE = getInstanceCode();
            System.setProperty("appInstanceCode",INSTANCE_CODE);
        }
        return INSTANCE_CODE;

    }

    private static String getInstanceCode() {
        // 先去检测当前应用是否是tomcat，如果是返回端口号
        String os = System.getProperty("os.name");
        String separator = ":";
        if (os.startsWith("Windows")){
            separator = "@";
        }
        Integer port = getTomcatPortByMBean();
        if (port != null) {
            return getLocalIP() + separator + port.toString();
        }
        int instanceValue = (getStartPath() + getApplicationPath()).hashCode();
        String instanceCode;
        if (instanceValue < 0) {
            instanceValue = Math.abs(instanceValue);
        }
        instanceCode = String.valueOf(instanceValue);
        return getLocalIP() + separator + instanceCode;
    }

    /**
     * 启动路径
     */
    private static String getStartPath() {
        String startPath = System.getProperties().get("user.dir").toString();
        startPath = startPath.replaceAll("\\\\", "/");
        return startPath;
    }

    /**
     * 工程路径（web） classes路径 （worker）
     */
    private static String getApplicationPath() {
        String classPath = Thread.currentThread().getContextClassLoader().getResource(MonitorUtils.class.getName().replaceAll("\\.","/") + ".class").getPath();
        return classPath;
    }

    public static void main(String[] args) {
        System.out.println(getInstanceCode());
        System.out.println(MonitorUtils.class.getClassLoader().getResource("").getPath());
        System.out.println(MonitorUtils.class.getClassLoader().getResource("").getFile());
        String startPath = System.getProperties().get("user.dir").toString();
        System.out.println(startPath);
        startPath = System.getProperty("java.class.path").toString();
        System.out.println(startPath);
        String classPath = Thread.currentThread().getContextClassLoader().getResource(MonitorUtils.class.getName().replaceAll("\\.","/") + ".class").getPath();
        System.out.println(classPath);
        classPath = classPath.replaceAll("\\\\", "/");
        System.out.println(classPath);
        System.out.println(getStartPath());
    }
}

