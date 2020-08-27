
#!/bin/bash
java --version
for i in {1..40};
do
	OUTPUT=$(./gradlew com.ibm.ws.request.timing_fat:buildandrun)
	echo "${OUTPUT}"
	if [[ "$OUTPUT" =~ "FAILED" ]]; 
	then
	  echo "FAILURE!!"
	  break
	fi
	OUTPUT=$(./gradlew --stop)
	echo "${OUTPUT}"
done
