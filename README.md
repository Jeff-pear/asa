# ASA
front-end deployment:
1、npm run build
2、for nginx config:

server{
        listen 9527;       
        index index.html;
        root D://git//asa//vue//dist;    
        location /api {
            # proxy request to java server
            proxy_pass http://localhost:8080/;
        }
    }
    
back-end deployment:
1、mvn package
2、
java -jar  -server -Xms4g -Xmx4g -Xmn2g -Xss1024k -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=60 -XX:+UseCMSInitiatingOccupancyOnly -XX:AutoBoxCacheMax=20000 -XX:
-OmitStackTraceInFastThrow --spring.profiles.active=prod --server.port=8080 asa-1.0.0-SNAPSHOT.jar
