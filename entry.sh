
#!/bin/bash
if [ -d "/var/log/message" ]; then
	java -jar API-0.0.1-SNAPSHOT.jar  > /var/log/messages
else
	java -jar API-0.0.1-SNAPSHOT.jar
fi