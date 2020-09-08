
#!/bin/bash
java --version
for i in {1..40};
do
	OUTPUT=$(./gradlew com.ibm.ws.microprofile.opentracing.1.3_fat_tck:buildandrun)
	echo "${OUTPUT}"
	if [[ "$OUTPUT" =~ "FAILED" ]]; 
	then
	  echo "FAILURE!!"
	  break
	fi
	OUTPUT=$(./gradlew --stop)
	echo "${OUTPUT}"
done
